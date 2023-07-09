package cool.cfapps.batchtalk.reader;

import cool.cfapps.batchtalk.models.StudentModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlatFileReader {
    public FlatFileItemReader<StudentModel> flatFileItemReader(String inputFile) {
        log.info("input file {}", inputFile);
        FlatFileItemReader<StudentModel> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(inputFile));
        reader.setLineMapper(new DefaultLineMapper<>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("ID", "First Name", "Last Name", "Email");
                        setDelimiter(",");
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(StudentModel.class);
                    }
                });
            }
        });
        // Skip Header
        reader.setLinesToSkip(1);

        return reader;
    }
}
