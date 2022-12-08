package org.example.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.WikiParseException;
import org.example.service.PhoneCheckerService;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app", name = "allow-quartz")
public class UpdateDbJob implements Job {
    private final PhoneCheckerService phoneCheckerService;

    @Value("${app.wiki.cron-schedule}")
    private String cronSchedule;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            log.info("Updating db from wiki");
            phoneCheckerService.updateDbFromWiki();
        } catch (IOException e) {
            throw new WikiParseException(e);
        }
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Qrtz_Trigger_db")
                .withSchedule(cronSchedule(cronSchedule))
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
