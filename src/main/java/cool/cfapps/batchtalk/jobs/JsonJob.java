package cool.cfapps.batchtalk.jobs;

import cool.cfapps.batchtalk.models.StudentModel;
import cool.cfapps.batchtalk.writer.StudentWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class JsonJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final StudentWriter studentWriter;

    @Value("${batch.file.input}")
    private String inputFile;
    @Value("${batch.file.output}")
    private String outputFile;

    public JsonJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                   StudentWriter studentWriter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.studentWriter = studentWriter;
    }


    @Bean
    public Job processJsonJob() {
        return new JobBuilder("processJsonJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processJsonStep())
                //.next(secondStep())
                .build();

    }

    private Step processJsonStep() {
        return new StepBuilder("processJsonStep", jobRepository)
                .<StudentModel, StudentModel>chunk(3, platformTransactionManager)
                .reader(jsonItemReader())
                //.writer(studentWriter)
                .writer(jsonFileItemWriter())
                .build();

    }

    public JsonItemReader<StudentModel> jsonItemReader() {
        return new JsonItemReaderBuilder<StudentModel>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(StudentModel.class))
                .resource(new FileSystemResource(inputFile+"student.json"))
                .name("StudentModelReader")
                .build();
    }

    public JsonFileItemWriter<StudentModel> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<StudentModel>()
              .resource(new FileSystemResource(outputFile+"student.json"))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
              .name("StudentModelWriter")
              .build();
    }
}
