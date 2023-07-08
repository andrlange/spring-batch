package cool.cfapps.batchtalk.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FirstJobListener implements JobExecutionListener {


    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);
        log.info("FirstJobListener beforeJob : {}", jobExecution.getJobInstance().getJobName());
        log.info("FirstJobListener Params: {}", jobExecution.getJobParameters());
        log.info("FirstJobListener Context: {}", jobExecution.getExecutionContext());

        jobExecution.getExecutionContext().put("key1", "value1");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobExecutionListener.super.afterJob(jobExecution);
        log.info("FirstJobListener afterJob: {}", jobExecution.getJobInstance().getJobName());
        log.info("FirstJobListener Params: {}", jobExecution.getJobParameters());
        log.info("FirstJobListener Context: {}", jobExecution.getExecutionContext());
    }
}
