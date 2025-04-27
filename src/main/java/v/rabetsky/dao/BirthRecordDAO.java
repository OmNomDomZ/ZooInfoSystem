package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.BirthRecord;
import java.util.List;

@Component
public class BirthRecordDAO {
    private final JdbcTemplate jdbcTemplate;

    public BirthRecordDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BirthRecord> findByParentId(int animalId) {
        return jdbcTemplate.query(
                "SELECT * FROM birth_records WHERE parent_id_1 = ? OR parent_id_2 = ? ORDER BY birth_date DESC",
                new BeanPropertyRowMapper<>(BirthRecord.class),
                animalId, animalId
        );
    }
}