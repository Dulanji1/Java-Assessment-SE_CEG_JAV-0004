package com.example.scheduling.system.scheduler;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.rmi.server.LogStream.log;

@Component
@DisallowConcurrentExecution
public class MotivationalEmailJob implements Job {


    private static final Logger log = LoggerFactory.getLogger(MotivationalEmailJob.class);

    private final MotivationalEmailService motivationalEmailService;

    public MotivationalEmailJob(MotivationalEmailService motivationalEmailService) {
        this.motivationalEmailService = motivationalEmailService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Call the service to send motivational emails
        motivationalEmailService.sendMotivationalEmails();
        log.info("Motivational Email Send.");
    }
}
