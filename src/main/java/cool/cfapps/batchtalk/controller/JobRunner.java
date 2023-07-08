package cool.cfapps.batchtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobRunner {

    private final JobLauncher jobLauncher;

    private final JobOperator jobOperator;

    public JobRunner(JobLauncher jobLauncher, JobOperator jobOperator) {
        this.jobLauncher = jobLauncher;
        this.jobOperator = jobOperator;
    }

    @Async
    public void runJob(Job job, JobParameters jobParameters) {
        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, jobParameters);
            log.info("Job {} started", jobExecution.getJobId());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error(e.getMessage(), e);
        }
        if (jobExecution!= null) {
            log.info("Job {} finished with status {}", jobExecution.getJobId(), jobExecution.getStatus());
        }
    }

    @Async
    public void stopJob(Long executionId) {
        try {
            log.info("jobOperator {}", jobOperator.getJobNames());
            jobOperator.stop(executionId);
        } catch (Exception e) {
            log.error("error stopping job {}", executionId);
        }
    }
}
