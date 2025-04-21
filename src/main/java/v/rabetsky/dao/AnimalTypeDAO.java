package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.AnimalType;

import java.util.List;

@Component
public class AnimalTypeDAO {
    private final JdbcTemplate jdbcTemplate;

    public AnimalTypeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AnimalType> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM animal_types",
                new BeanPropertyRowMapper<>(AnimalType.class)
        );
    }
}
