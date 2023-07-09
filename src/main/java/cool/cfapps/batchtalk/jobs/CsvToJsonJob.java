package cool.cfapps.batchtalk.jobs;

import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.reader.FlatFileReader;
import cool.cfapps.batchtalk.writer.JsonFileWriter;
import cool.cfapps.batchtalk.writer.XmlFileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class CsvToJsonJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final JsonFileWriter<StudentModel> jsonFileWriter;
    private final FlatFileReader flatFileReader;

    @Value("${batch.file.input}")
    private String inputFiles;

    @Value("${batch.file.output}")
    private String outputFiles;

    @Value("${batch.file.name}")
    private String fileName;

    public CsvToJsonJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                        FlatFileReader flatFileReader, JsonFileWriter<StudentModel> jsonFileWriter) {
        this.jsonFileWriter = jsonFileWriter;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.flatFileReader = flatFileReader;
    }


    @Bean
    public Job processCsvToJsonJob() {
        return new JobBuilder("processCsvToJsonJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processJsonStep())
                .build();

    }

    private Step processJsonStep() {
        return new StepBuilder("processCsvToJsonStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(flatFileReader.flatFileItemReader(inputFiles + fileName + ".csv"))
                .writer(jsonFileWriter.jsonFileItemWriter(outputFiles + "from_csv.json"))
                .build();

    }
}
