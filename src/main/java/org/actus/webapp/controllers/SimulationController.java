package org.actus.webapp.controllers;

import org.actus.webapp.models.Event;
import org.actus.webapp.models.EventStream2;
import org.actus.webapp.models.ObservedData;
import org.actus.webapp.models.ScenarioData;
import org.actus.webapp.models.ScenarioSimulationInput;
import org.actus.webapp.models.TwoDimensionalPrepaymentModelData;
import org.actus.webapp.repositories.ScenarioRepository;
import org.actus.webapp.utils.TimeSeriesModel;
import org.actus.webapp.utils.TwoDimensionalPrepaymentModel;
import org.actus.webapp.utils.MultiDimensionalRiskFactorModel;

import org.actus.attributes.ContractModel;
import org.actus.attributes.ContractModelProvider;
import org.actus.contracts.ContractType;
import org.actus.events.ContractEvent;
import org.actus.states.StateSpace;
import org.actus.externals.RiskFactorModelProvider;
import org.actus.events.EventFactory;
import org.actus.events.ContractEvent;
import org.actus.time.ScheduleFactory;
import org.actus.events.EventFactory;
import org.actus.functions.pam.POF_PP_PAM;
import org.actus.functions.pam.STF_PP_PAM;
import org.actus.types.EventType;
import org.actus.types.ContractTypeEnum;
import org.actus.util.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Period;

@RestController
public class SimulationController {

    @Autowired
    ScenarioRepository scenarioRepository;

    // param:   Json Array of Json Objects
    // return:  ArrayList of ArrayList of ContractEvents
    @RequestMapping(method = RequestMethod.POST, value = "/simulations/runScenario")
    @ResponseBody
    @CrossOrigin(origins = "*")
    public List<EventStream2> runScenarioSimulation(@RequestBody ScenarioSimulationInput json) {
        
        // extract body parameters
        String scenarioId = json.getScenarioId();
        List<Map<String, Object>> contractData = json.getContracts();

        // fetch scenario data and create risk factor observer
        ScenarioData scenario = scenarioRepository.findByScenarioId(scenarioId);
        if(scenario == null) {
            throw new RuntimeException("Scenario with scenarioId='" + scenarioId + "' not found!");
        }
        RiskFactorModelProvider observer;
        try {
            observer = createObserver(scenario);
        } catch(Exception e){
            throw new RuntimeException("Could not create 'observer' for scenarioId='" + scenarioId + "'!");
        }

        // for each contract compute events
        ArrayList<EventStream2> output = new ArrayList<>();
        contractData.forEach(entry -> {
            // extract contract terms
            ContractModel terms;
            String contractID = (entry.get("contractID") == null)? "NA":entry.get("contractID").toString();
            try {
                terms = ContractModel.parse(entry); 
            } catch(Exception e){
                output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
                return; // skip this iteration and continue with next
            }
            // compute contract events
            try {
                output.add(new EventStream2(scenarioId, contractID, "Success", "", computeEvents(terms, observer)));
            }catch(Exception e){
                output.add(new EventStream2(scenarioId, contractID, "Failure", e.toString(), new ArrayList<Event>()));
            }
        });
        return output;
    }

    private RiskFactorModelProvider createObserver(ScenarioData json) {
        MultiDimensionalRiskFactorModel observer = new MultiDimensionalRiskFactorModel();
        List<ObservedData> timeSeriesData = json.getTimeSeriesData();
        List<TwoDimensionalPrepaymentModelData> prepModelData = json.getTwoDimensionalPrepaymentModelData();

        if(timeSeriesData.size()>0) {
            timeSeriesData.forEach(entry -> {
                observer.add(entry.getMarketObjectCode(),new TimeSeriesModel(entry));
            });
        }
        
        if(!prepModelData.isEmpty()) {
            prepModelData.forEach(entry -> {
                try {
                    
                    observer.add(entry.getRiskFactorId(),
                        new TwoDimensionalPrepaymentModel(entry.getRiskFactorId(),entry,observer));

                } catch(Exception e) {
                    throw new RuntimeException("riskFactorType for MultiDimensionalModelData with riskFactorId='" + entry.getRiskFactorId() + "' unsupported!");
                }
            });
        }

        return observer;
    }

    private List<Event> computeEvents(ContractModel model, RiskFactorModelProvider observer) {
        // define projection end-time
        LocalDateTime to = model.getAs("TerminationDate");
        if(to == null) to = model.getAs("MaturityDate");
        if(to == null) to = model.getAs("AmortizationDate");
        if(to == null) to = model.getAs("SettlementDate");
        if(to == null) to = LocalDateTime.now().plusYears(5);

        // compute actus schedule
        ArrayList<ContractEvent> schedule = ContractType.schedule(to, model);

        // add prepayment events if prepayment model referenced by contract
        if(!CommonUtils.isNull(model.getAs("ObjectCodeOfPrepaymentModel"))) {
            schedule.addAll(EventFactory.createEvents(
                        ScheduleFactory.createSchedule(
                                model.<LocalDateTime>getAs("InitialExchangeDate").plusMonths(12),
                                to,
                                "P1YL1",
                                model.getAs("EndOfMonthConvention"),
                                false
                        ),
                        EventType.PP,
                        model.getAs("Currency"),
                        new POF_PP_PAM(),
                        new STF_PP_PAM(),
                        model.getAs("BusinessDayConvention"),
                        model.getAs("ContractID")
                ));
        }

        // add deposit withdrawal events if withdrawal model referenced by UMP contract
        if(model.getAs("ContractType").equals(ContractTypeEnum.UMP) && !CommonUtils.isNull(model.getAs("MarketObjectCode"))) {
            model.addAttribute("ObjectCodeOfPrepaymentModel",model.getAs("MarketObjectCode"));
            schedule.addAll(EventFactory.createEvents(
                        ScheduleFactory.createSchedule(
                                model.<LocalDateTime>getAs("InitialExchangeDate").plusYears(1),
                                model.<LocalDateTime>getAs("InitialExchangeDate").plusYears(3),
                                "P1YL1",
                                model.getAs("EndOfMonthConvention"),
                                true
                        ),
                        EventType.PP,
                        model.getAs("Currency"),
                        new POF_PP_PAM(),
                        new STF_PP_PAM(),
                        model.getAs("BusinessDayConvention"),
                        model.getAs("ContractID")
                ));
        }

        // apply schedule to contract
        schedule = ContractType.apply(schedule, model, observer);
        
        // transform schedule to event list and return
        return schedule.stream().map(e -> new Event(e)).collect(Collectors.toList());
    }

}