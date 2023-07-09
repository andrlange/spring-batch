package cool.cfapps.batchtalk.reader;

import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class JsonFileReader<T> {

    // with Generics
    public JsonItemReader<T> jsonItemReader(String fileName, Class<T> clazz) {
        return new JsonItemReaderBuilder<T>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(clazz))
                .resource(new FileSystemResource(fileName))
                .name("JsonReader")
                .build();
    }
}
