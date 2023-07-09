package cool.cfapps.batchtalk.writer;

import cool.cfapps.batchtalk.models.StudentModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class FlatFileWriter {



    public FlatFileItemWriter<StudentModel> flatFileItemWriter(String outputFile) {
        FlatFileItemWriter<StudentModel> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource(outputFile));
        log.info("output file: " + outputFile);

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
