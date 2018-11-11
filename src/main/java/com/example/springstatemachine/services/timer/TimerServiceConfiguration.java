package com.example.springstatemachine.services.timer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TimerServiceConfiguration {


    @Bean("schedulerTasks")
    public ThreadPoolTaskScheduler  threadPoolTaskScheduler()
    {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize( 5 );
        threadPoolTaskScheduler.setThreadNamePrefix( "Scheduler-");

        return threadPoolTaskScheduler;
    }
}
