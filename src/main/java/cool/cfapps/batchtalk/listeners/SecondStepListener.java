package cool.cfapps.batchtalk.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecondStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        log.info("SecondStepListener beforeStep : {}", stepExecution.getStepName());
        log.info("SecondStepListener JobContext: {}", stepExecution.getJobExecution().getExecutionContext());
        //log.info("SecondStepListener Context: {}", stepExecution.getExecutionContext());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("SecondStepListener afterStep : {}", stepExecution.getStepName());
        log.info("SecondStepListener JobContext: {}", stepExecution.getJobExecution().getExecutionContext());
        //log.info("SecondStepListener Context: {}", stepExecution.getExecutionContext());

        return StepExecutionListener.super.afterStep(stepExecution);

    }
}
