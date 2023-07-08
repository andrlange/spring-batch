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
    //private final Job restJob;
    private final Job csvJob;

    //private final Job jsonJob;
    //private final Job xmlJob;
    //private final Job csvToJdbcJob;
    //private final Job csvToRestJob;
    //private final Job folderCaptureJob;


    public JobRunnerService(JobRunner jobRunner,
                            //@Qualifier("processRestJob") Job restJob,
                            @Qualifier("processCsvJob") Job csvJob//,
                            //@Qualifier("processJsonJob") Job jsonJob,
                            //@Qualifier("processXmlJob") Job xmlJob,
                            //@Qualifier("processCsvToJdbcJob") Job csvToJdbcJob,
                            //@Qualifier("processCsvToRestJob") Job csvToRestJob,
                            //@Qualifier("processFolderCaptureJob") Job folderCaptureJob)
    ){
        this.jobRunner = jobRunner;
        //this.restJob = restJob;
        this.csvJob = csvJob;
        //this.jsonJob = jsonJob;
        //this.xmlJob = xmlJob;
        //this.csvToJdbcJob = csvToJdbcJob;
        //this.csvToRestJob = csvToRestJob;
        //this.folderCaptureJob = folderCaptureJob;
    }

    @Bean
    public void sequenceJobs() {
        log.info("CsvJob run at {}", LocalDateTime.now());
        Map<String, JobParameter<?>> jobParametersMap = new HashMap<>();

        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        JobParameters jobParameters = new JobParameters(jobParametersMap);

        jobRunner.runJob(csvJob, jobParameters);
        /*
        log.info("JsonJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(jsonJob, jobParameters);

        log.info("RestJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(restJob, jobParameters);

        log.info("XmlJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(xmlJob, jobParameters);

        log.info("CsvToRestJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(csvToRestJob, jobParameters);

        /*
        log.info("CsvToJdbcJob run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(csvToJdbcJob, jobParameters);


        log.info("FolderCapture run at {}", LocalDateTime.now());
        jobParametersMap.clear();
        jobParametersMap.put("currentTime", new JobParameter<>(System.currentTimeMillis(), Long.class));
        jobParameters = new JobParameters(jobParametersMap);
        jobRunner.runJob(folderCaptureJob, jobParameters);

         */
    }
}
