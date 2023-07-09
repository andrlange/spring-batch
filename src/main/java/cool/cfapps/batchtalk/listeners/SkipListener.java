package cool.cfapps.batchtalk.listeners;

import cool.cfapps.batchtalk.models.StudentModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;

@Component
@Slf4j
public class SkipListener {

    @Value("${batch.file.output}")
    private String output;

    @OnSkipInRead
    public void onSkipRead(Throwable throwable) {
        if (throwable instanceof FlatFileParseException) {
            log.warn("FlatFileParseException");
            fileWriter(output+"chunks-jobs/csv-job/reader/read_exceptions.txt",
                    "Line:" + ((FlatFileParseException) throwable).getLineNumber() + " => " +
                    ((FlatFileParseException) throwable).getInput());
        }
    }

    @OnSkipInProcess
    public void onSkipProcess(StudentModel student, Throwable throwable) {
    if (throwable instanceof NullPointerException) {
            fileWriter(output+"chunks-jobs/csv-job/processor/process_exceptions.txt",
                    "Student => " + student + " => " + throwable.getMessage());
        }
    }

    private void fileWriter(String fileName, String data) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(data + "\n");
        } catch (Exception e) {
            log.error("file:{}", e.getMessage());
        }
    }
}
