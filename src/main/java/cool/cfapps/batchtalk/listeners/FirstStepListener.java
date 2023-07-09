package cool.cfapps.batchtalk.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FirstStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        log.info("FirstStepListener beforeStep : {}", stepExecution.getStepName());
        log.info("FirstStepListener JobContext: {}", stepExecution.getJobExecution().getExecutionContext());
        log.info("FirstStepListener Context: {}", stepExecution.getExecutionContext());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("FirstStepListener afterStep : {}", stepExecution.getStepName());
        log.info("FirstStepListener JobContext: {}", stepExecution.getJobExecution().getExecutionContext());
        log.info("FirstStepListener Context: {}", stepExecution.getExecutionContext());

        return StepExecutionListener.super.afterStep(stepExecution);

    }
}
