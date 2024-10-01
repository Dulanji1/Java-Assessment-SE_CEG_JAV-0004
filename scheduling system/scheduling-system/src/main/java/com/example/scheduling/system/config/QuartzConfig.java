package com.example.scheduling.system.config;

import com.example.scheduling.system.scheduler.MotivationalEmailJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    // Define the Quartz JobDetail for the MotivationalEmailJob
    @Bean
    @Qualifier("motivationalemail")
    public JobDetail motivationalEmailJobDetail() {
        return JobBuilder.newJob(MotivationalEmailJob.class)
                .withIdentity("motivationalEmailJob")
                .storeDurably() // Keep job details even if no triggers are associated with it
                .build();
    }

    // Define a trigger for the job, set to run every Monday at 8 AM
    @Bean
    public Trigger motivationalEmailTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(motivationalEmailJobDetail()) // Link this trigger to the job
                .withIdentity("motivationalEmailTrigger")
                .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(DateBuilder.MONDAY, 8, 0)) // Every Monday at 8 AM
                .build();
    }

    /*
Cron Expression Explanation:
- "0 * * * * ?":
  - 0: Execute at the start of every minute.
  - *: Every hour.
  - *: Every day of the month.
  - *: Every month.
  - ?: Any day of the week.
*/

//    @Bean
//    public Trigger motivationalEmailTrigger(@Qualifier("motivationalemail") JobDetail jobDetail) {
//        return TriggerBuilder.newTrigger()
//                .forJob(motivationalEmailJobDetail()) // Link this trigger to the job
//                .withIdentity("motivationalEmailTrigger")
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // Run every minute
//                .build();
//    }


}
