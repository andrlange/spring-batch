package cool.cfapps.batchtalk.processor;

import cool.cfapps.batchtalk.models.StudentModel;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentProcessor implements ItemProcessor<StudentModel, StudentModel> {
    @Override
    public StudentModel process(StudentModel item) throws NullPointerException {

        if(item.getId() == 3) throw new NullPointerException();
        return item;
    }
}
