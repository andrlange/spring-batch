package cool.cfapps.batchtalk.jobs;


import cool.cfapps.batchtalk.listeners.FirstJobListener;
import cool.cfapps.batchtalk.listeners.FirstStepListener;
import cool.cfapps.batchtalk.listeners.SecondStepListener;
import cool.cfapps.batchtalk.listeners.SkipListener;
import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.processor.StudentProcessor;
import cool.cfapps.batchtalk.reader.FlatFileReader;
import cool.cfapps.batchtalk.writer.FlatFileWriter;
import cool.cfapps.batchtalk.writer.StudentWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

@Configuration
@Slf4j
public class CsvJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final StudentWriter studentWriter;

    private final FlatFileReader flatFileReader;

    private final FlatFileWriter flatFileWriter;

    private final StudentProcessor studentProcessor;

    private final SkipListener skipListener;

    private final FirstJobListener firstJobListener;

    private final FirstStepListener firstStepListener;

    private final SecondStepListener secondStepListener;

    @Value("${batch.file.input}")
    private String inputFiles;

    @Value("${batch.file.output}")
    private String outputFiles;

    @Value("${batch.file.name}")
    private String fileName;

    public CsvJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                  StudentWriter studentWriter, FlatFileReader flatFileReader, StudentProcessor studentProcessor,
                  SkipListener skipListener, FirstJobListener firstJobListener, FirstStepListener firstStepListener,
                  FlatFileWriter flatFileWriter, SecondStepListener secondStepListener) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentWriter = studentWriter;
        this.flatFileReader = flatFileReader;
        this.studentProcessor = studentProcessor;
        this.skipListener = skipListener;
        this.firstJobListener = firstJobListener;
        this.firstStepListener = firstStepListener;
        this.flatFileWriter = flatFileWriter;
        this.secondStepListener = secondStepListener;
    }


    @Bean
    public Job processCsvJob() {
        return new JobBuilder("processCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(firstJobListener)
                .start(processCsvStep())
                .next(outputCsvStep())
                .build();

    }

    private Step processCsvStep() {
        log.info("create CsvStep");
        return new StepBuilder("processCsvStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(flatFileReader.flatFileItemReader(inputFiles+fileName+".csv"))
                .processor(studentProcessor)
                //.writer(studentWriter)
                .writer(flatFileWriter.flatFileItemWriter(outputFiles+fileName+".csv"))
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(NullPointerException.class)
                .skip(Throwable.class)
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())
                .skipLimit(5)
                .retryLimit(3)
                .retry(Throwable.class)
                .listener(firstStepListener)
                .listener(skipListener)
                .build();

    }

    private Step outputCsvStep() {
        log.info("create output CsvStep");
        return new StepBuilder("outputCsvStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(flatFileReader.flatFileItemReader(inputFiles+fileName+".csv"))
                .processor(studentProcessor)
                .writer(studentWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(NullPointerException.class)
                .skip(Throwable.class)
                .skipLimit(5)
                .retryLimit(3)
                .retry(Throwable.class)
                .listener(secondStepListener)
                .listener(skipListener)
                .build();

    }

}
