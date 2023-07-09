package cool.cfapps.batchtalk.writer;

import cool.cfapps.batchtalk.models.StudentModel;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcWriter {

    private final DataSource universityDataSource;

    public JdbcWriter(@Qualifier("universityDataSource") DataSource universityDataSource) {
        this.universityDataSource = universityDataSource;
    }

    @Bean
    public JdbcBatchItemWriter<StudentModel> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<StudentModel> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource((universityDataSource));

        writer.setSql("INSERT INTO university_students (id, first_name,last_name,email) VALUES (:id,:firstName," +
                      ":lastName," + ":email)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }
}
