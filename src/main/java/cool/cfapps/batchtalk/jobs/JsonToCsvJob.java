package cool.cfapps.batchtalk.jobs;

import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.reader.JsonFileReader;
import cool.cfapps.batchtalk.reader.XmlFileReader;
import cool.cfapps.batchtalk.writer.FlatFileWriter;
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
public class JsonToCsvJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final JsonFileReader<StudentModel> jsonFileReader;
    private final FlatFileWriter flatFileWriter;

    @Value("${batch.file.input}")
    private String inputFiles;

    @Value("${batch.file.output}")
    private String outputFiles;

    @Value("${batch.file.name}")
    private String fileName;

    public JsonToCsvJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                        FlatFileWriter flatFileWriter, JsonFileReader<StudentModel> jsonFileReader) {
        this.jsonFileReader = jsonFileReader;
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.flatFileWriter = flatFileWriter;
    }


    @Bean
    public Job processJsonToCsvJob() {
        return new JobBuilder("processJsonToCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processJsonStep())
                .build();

    }

    private Step processJsonStep() {
        return new StepBuilder("processJsonToCsvStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(jsonFileReader.jsonItemReader(inputFiles+fileName+".json",StudentModel.class))
                .writer(flatFileWriter.flatFileItemWriter(outputFiles+"from_json.csv"))
                .build();

    }
}
