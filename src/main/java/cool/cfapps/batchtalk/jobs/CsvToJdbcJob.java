package cool.cfapps.batchtalk.jobs;

import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.reader.FlatFileReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.sql.BatchUpdateException;

@Configuration
public class CsvToJdbcJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final FlatFileReader flatFileReader;
    private final JdbcBatchItemWriter<StudentModel> jdbcBatchItemWriter;

    @Value("${batch.file.input}")
    private String inputFiles;

    @Value("${batch.file.name}")
    private String fileName;

    public CsvToJdbcJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                        FlatFileReader flatFileReader, JdbcBatchItemWriter<StudentModel> jdbcBatchItemWriter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.flatFileReader = flatFileReader;
        this.jdbcBatchItemWriter = jdbcBatchItemWriter;
    }


    @Bean
    public Job processCsvToJdbcJob() {
        return new JobBuilder("processCsvToJdbcJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processCsvToJdbcStep())
                .build();

    }

    private Step processCsvToJdbcStep() {
        return new StepBuilder("processCsvToJdbcStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(flatFileReader.flatFileItemReader(inputFiles+fileName+".csv"))
                .writer(jdbcBatchItemWriter)
                .faultTolerant()
                .skip(DuplicateKeyException.class)
                .skip(Throwable.class)
                .skipLimit(1000)
                .build();

    }
}
