package cool.cfapps.batchtalk.services;


import cool.cfapps.batchtalk.controller.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JobRunnerService {

    private final JobRunner jobRunner;

    private final Job csvJob;
    private final Job csvToJdbcJob;
    private final Job xmlToCsvJob;

    private final Job csvToXmlJob;

    private final Job jsonToCsvJob;
    private final Job csvToJsonJob;

    public JobRunnerService(JobRunner jobRunner,
                            @Qualifier("processCsvJob") Job csvJob,
                            @Qualifier("processCsvToJdbcJob") Job csvToJdbcJob,
                            @Qualifier("processXmlToCsvJob") Job xmlToCsvJob,
                            @Qualifier("processCsvToXmlJob") Job csvToXmlJob,
                            @Qualifier("processJsonToCsvJob") Job jsonToCsvJob,
                            @Qualifier("processCsvToJsonJob") Job csvToJsonJob
    ){
        this.jobRunner = jobRunner;
        this.csvJob = csvJob;
        this.csvToJdbcJob = csvToJdbcJob;
        this.xmlToCsvJob = xmlToCsvJob;
        this.csvToXmlJob = csvToXmlJob;
        this.jsonToCsvJob = jsonToCsvJob;
        this.csvToJsonJob = csvToJsonJob;
    }

    @Bean
    public void sequenceJobs() {

        // Simple CSV Job with exception
        log.info("CsvJob run at {}", LocalDateTime.now());
        Map<String, JobParameter<?>> jobParametersMap = new HashMap<>();

        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        JobParameters jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(csvJob, jobParameters);


        // CSV to JDBC Job
        log.info("CsvToJdbcJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(csvToJdbcJob, jobParameters);

        // XML to CSV Job
        log.info("XmlToCsvJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(xmlToCsvJob, jobParameters);

        // CSV to XML Job
        log.info("CsvToXmlJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(csvToXmlJob, jobParameters);


        // JSON to CSV Job
        log.info("JsonToCsvJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(jsonToCsvJob, jobParameters);

        // CSV to JSON Job
        log.info("CsvToJsonJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(csvToJsonJob, jobParameters);


    }
}
