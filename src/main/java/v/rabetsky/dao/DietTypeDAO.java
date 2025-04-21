package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.DietType;

import java.util.List;

@Component
public class DietTypeDAO {
    private final JdbcTemplate jdbcTemplate;

    public DietTypeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DietType> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM diet_types",
                new BeanPropertyRowMapper<>(DietType.class)
        );
    }
}
