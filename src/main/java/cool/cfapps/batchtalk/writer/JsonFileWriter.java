package cool.cfapps.batchtalk.writer;

import cool.cfapps.batchtalk.models.StudentModel;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class JsonFileWriter<T> {

    // with Generics
    public JsonFileItemWriter<T> jsonFileItemWriter(String fileName) {
        return new JsonFileItemWriterBuilder<T>()
                .resource(new FileSystemResource(fileName))
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .name("JsonWriter")
                .build();
    }
}
