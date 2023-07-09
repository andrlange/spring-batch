package cool.cfapps.batchtalk.reader;

import cool.cfapps.batchtalk.models.StudentModel;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class XmlFileReader {

    public StaxEventItemReader<StudentModel> xmlItemReader(String fileName, String elementName, Class modelClass) {

        StaxEventItemReader<StudentModel> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource(fileName));
        reader.setFragmentRootElementName(elementName);
        reader.setUnmarshaller(new Jaxb2Marshaller(){
            {
                setClassesToBeBound(modelClass);
            }
        });

        return reader;
    }
}
