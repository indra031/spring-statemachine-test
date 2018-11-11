package com.example.springstatemachine.configurations;

import com.example.springstatemachine.model.CamelCallEvents;
import com.example.springstatemachine.model.CamelCallStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class CamelBasicScenarioStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<CamelCallStates, CamelCallEvents> {
    @Override
    public void configure(StateMachineStateConfigurer<CamelCallStates, CamelCallEvents> states) throws Exception {
        states.withStates()
                .initial(CamelCallStates.INITIAL_STATE)
                .end(CamelCallStates.CALL_ENDED)
                .states(EnumSet.allOf(CamelCallStates.class));

    }



    @Override

    public void configure(StateMachineTransitionConfigurer<CamelCallStates, CamelCallEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(CamelCallStates.INITIAL_STATE)
                .target(CamelCallStates.WAIT_IDP_ANSWER)
                .event(CamelCallEvents.SEND_INITIAL_DP)
                .and().withExternal()
                .source(CamelCallStates.WAIT_IDP_ANSWER)
                .target(CamelCallStates.WAIT_CALL_START_ANSWER)
                .event(CamelCallEvents.SEND_CALL_START)
                .and().withExternal()
                .source(CamelCallStates.WAIT_IDP_ANSWER)
                .target(CamelCallStates.CALL_ENDED)
                .event(CamelCallEvents.IDP_ANSWER_TIMEOUT)
                .and().withInternal()
                .source(CamelCallStates.WAIT_IDP_ANSWER)
                .timerOnce(2000)
                .action(timerAction())
                .and().withExternal()
                .source(CamelCallStates.WAIT_CALL_START_ANSWER)
                .target(CamelCallStates.CALL_ENDED)
                .event(CamelCallEvents.END_CALL)
                .and().withExternal()
                .source(CamelCallStates.WAIT_CALL_START_ANSWER)
                .target(CamelCallStates.CALL_ENDED)
                .event(CamelCallEvents.CALL_START_ASNWER_TIMEOUT);
    }

    private Action<CamelCallStates, CamelCallEvents> timerAction() {
        return new Action<CamelCallStates, CamelCallEvents>() {
            @Override
            public void execute(StateContext<CamelCallStates, CamelCallEvents> context) {
                System.out.println("#####timer action");
                context.getStateMachine().sendEvent(CamelCallEvents.IDP_ANSWER_TIMEOUT);
            }
        };
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<CamelCallStates, CamelCallEvents> config) throws Exception {
        config.withConfiguration()
                .autoStartup(false)
                .listener(listener());
    }

    private StateMachineListener<CamelCallStates, CamelCallEvents> listener() {
        return new StateMachineListenerAdapter<CamelCallStates, CamelCallEvents>() {
            @Override
            public void stateChanged(State<CamelCallStates, CamelCallEvents> from, State<CamelCallStates, CamelCallEvents> to) {
                String fromStr = "";
                String toStr = "";

                if (from != null)
                    fromStr = from.getId().name();

                if (to != null)
                    toStr = to.getId().name();

                System.out.println("from = [" + fromStr + "], to = [" + toStr + "]");
            }
        };
    }
}
