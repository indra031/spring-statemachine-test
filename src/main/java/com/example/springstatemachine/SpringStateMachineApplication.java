package com.example.springstatemachine;

import com.example.springstatemachine.model.CamelCallEvents;
import com.example.springstatemachine.model.CamelCallStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.Date;
import java.util.function.Consumer;

@SpringBootApplication(scanBasePackages = {"com.example"})
public class SpringStateMachineApplication implements CommandLineRunner {

    @Autowired
    StateMachineFactory<CamelCallStates, CamelCallEvents> stmFactory;

    @Autowired
    @Qualifier("schedulerTasks")
    ThreadPoolTaskScheduler scheduler;

    public static void main(String[] args) {
        SpringApplication.run(SpringStateMachineApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println( "state machine start");

        scheduler.schedule(  defaultTask(), new Date(System.currentTimeMillis() + 3000));
        scheduler.schedule(  defaultTask(), new Date(System.currentTimeMillis() + 3000));
        scheduler.schedule(  defaultTask(), new Date(System.currentTimeMillis() + 3000));


        StateMachine<CamelCallStates, CamelCallEvents> stateMachine = stmFactory.getStateMachine();
        StateMachine<CamelCallStates, CamelCallEvents> stateMachine1 = stmFactory.getStateMachine();
//
        stateMachine.start();
        stateMachine1.start();
//
//        System.out.println( stateMachine );
//        System.out.println( stateMachine1 );
//
//
        stateMachine.sendEvent( CamelCallEvents.SEND_INITIAL_DP );
        stateMachine1.sendEvent( CamelCallEvents.SEND_INITIAL_DP );
//
//        stateMachine.sendEvent( CamelCallEvents.IDP_ANSWER_TIMEOUT );
//        stateMachine.sendEvent( CamelCallEvents.SEND_CALL_START );
//        stateMachine.sendEvent( CamelCallEvents.CALL_START_ASNWER_TIMEOUT);
//
//        stateMachine1.sendEvent( CamelCallEvents.IDP_ANSWER_TIMEOUT );
//        stateMachine1.sendEvent( CamelCallEvents.SEND_CALL_START );
//        stateMachine1.sendEvent( CamelCallEvents.CALL_START_ASNWER_TIMEOUT);
//
        Thread.sleep( 5000 );
        stateMachine.stop();
        stateMachine1.stop();

        Thread.sleep( 10000 );
        scheduler.shutdown();
//        stateMachine = null;
//        stateMachine1 = null;
    }

    @Bean
    public Runnable defaultTask( )
    {
        System.out.println( "### Bean defaultTask()");
        return () -> {
            System.out.println( "Timer default task: " + Thread.currentThread().getName());
        };
    }
}
