package cool.cfapps.batchtalk.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlRootElement(name = "student")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public @Data class StudentModel {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
