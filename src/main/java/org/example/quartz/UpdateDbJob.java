package org.example.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.PhoneCheckerService;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateDbJob implements Job {
    private final PhoneCheckerService phoneCheckerService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("Updating db from wiki");
            phoneCheckerService.updateDbFromWiki();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Qrtz_Trigger_db")
                .withSchedule(dailyAtHourAndMinute(11, 21))
                .build();
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(UpdateDbJob.class)
                .storeDurably()
                .withIdentity("Qrtz_Job_Detail_db")
                .build();
    }
}
