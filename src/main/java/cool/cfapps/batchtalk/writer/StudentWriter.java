package cool.cfapps.batchtalk.writer;

import cool.cfapps.batchtalk.models.StudentModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StudentWriter implements ItemWriter<StudentModel> {
    @Override
    public void write(Chunk<? extends StudentModel> chunk) throws Exception {
        log.info("writing Items: chuck size-> {}", chunk.size());
        chunk.forEach(item -> log.info("item -> {}", item));
    }
}
