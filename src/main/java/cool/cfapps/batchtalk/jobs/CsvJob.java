package cool.cfapps.batchtalk.jobs;


import cool.cfapps.batchtalk.listeners.SkipListener;
import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.processor.StudentProcessor;
import cool.cfapps.batchtalk.reader.FlatFileReader;
import cool.cfapps.batchtalk.writer.StudentWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;

@Configuration
@Slf4j
public class CsvJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final StudentWriter studentWriter;

    private final FlatFileReader flatFileReader;

    private final StudentProcessor studentProcessor;

    private final SkipListener skipListener;

    @Value("${batch.file.input}")
    private String inputFiles;
    @Value("${batch.file.output}")
    private String outputFiles;
    @Value("${batch.file.name}")
    private String fileName;

    public CsvJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, StudentWriter studentWriter, FlatFileReader flatFileReader, StudentProcessor studentProcessor, SkipListener skipListener) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentWriter = studentWriter;
        this.flatFileReader = flatFileReader;
        this.studentProcessor = studentProcessor;
        this.skipListener = skipListener;
    }


    @Bean
    public Job processCsvJob() {
        return new JobBuilder("processCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processCsvStep())
                //.next(secondStep())
                .build();

    }

    private Step processCsvStep() {
        log.info("processCsvStep...");
        return new StepBuilder("processCsvStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(flatFileReader.flatFileItemReader(inputFiles+fileName+".csv"))
                .processor(studentProcessor)
                //.writer(studentWriter)
                .writer(flatFileItemWriter())
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(NullPointerException.class)
                .skip(Throwable.class)
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())
                .skipLimit(500)
                .retryLimit(100)
                .retry(Throwable.class)
                .listener(skipListener)
                .build();

    }


    private FlatFileItemWriter<StudentModel> flatFileItemWriter() {
        FlatFileItemWriter<StudentModel> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFiles + fileName+".csv"));
        log.info("writing to file: " + outputFiles + fileName+".csv");

        writer.setHeaderCallback(header -> header.write("ID,First Name, Last Name, Email"));

        writer.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(new String[]{"id", "firstName", "lastName", "email"});
                    }
                });
                setDelimiter(",");
            }
        });

        writer.setFooterCallback(footer -> footer.write("created @" + new Date()));

        return writer;
    }


}
