package cool.cfapps.batchtalk.writer;

import cool.cfapps.batchtalk.models.StudentModel;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class XmlFileWriter {

    public StaxEventItemWriter<StudentModel> xmlItemWriter(String fileName, String rootElementName, Class modelName) {
        StaxEventItemWriter<StudentModel> writer = new StaxEventItemWriter<>();
        writer.setResource(new FileSystemResource(fileName));
        writer.setRootTagName(rootElementName);
        writer.setMarshaller(new Jaxb2Marshaller(){
            {
                setClassesToBeBound(modelName);
            }
        });

        return writer;
    }
}
