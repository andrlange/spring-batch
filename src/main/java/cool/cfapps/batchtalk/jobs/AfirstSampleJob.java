package cool.cfapps.batchtalk.jobs;


import cool.cfapps.batchtalk.listeners.FirstJobListener;
import cool.cfapps.batchtalk.listeners.FirstStepListener;
import cool.cfapps.batchtalk.tasklet.ThirdTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class AfirstSampleJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ThirdTasklet thirdTasklet;

    private final FirstJobListener firstJobListener;

    private final FirstStepListener firstStepListener;

    public AfirstSampleJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, ThirdTasklet thirdTasklet, FirstJobListener firstJobListener, FirstStepListener firstStepListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.thirdTasklet = thirdTasklet;
        this.firstJobListener = firstJobListener;
        this.firstStepListener = firstStepListener;
    }

    @Bean
    public Job firstJob() {
        return new JobBuilder("firstJob", jobRepository)
                //.incrementer(new RunIdIncrementer())
                .start(firstStep())
                .next(secondStep())
                .next(thirdStep())
                .listener(firstJobListener)
                .build();
    }

    private Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .tasklet(firstTasklet(), transactionManager) // or .chunk(chunkSize, transactionManager)
                .listener(firstStepListener)
                .build();
    }

    private Tasklet firstTasklet() {
        return (contribution, chunkContext) -> {
            log.info("EXECUTING FIRST TASKLET");
            contribution.getStepExecution().getExecutionContext().put("foo", "bar");
            contribution.getStepExecution().getJobExecution().getExecutionContext().put("foo2", "bar2");
            return RepeatStatus.FINISHED;
        };
    }

    private Step secondStep() {
        return new StepBuilder("secondStep", jobRepository)
                .tasklet(secondTasklet(), transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }
    private Tasklet secondTasklet() {
        return (contribution, chunkContext) -> {
            log.info("EXECUTING SECOND TASKLET");
            String value = (String)contribution.getStepExecution().getJobExecution().getExecutionContext().get("foo2");
            log.info("kv pair from step1 -> foo2: " + value);
            return RepeatStatus.FINISHED;
        };
    }

    private Step thirdStep() {
        return new StepBuilder("thirdStep", jobRepository)
                .tasklet(thirdTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }
}
