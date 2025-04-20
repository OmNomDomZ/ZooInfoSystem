package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Position;

import java.util.List;

@Component
public class PositionDAO {
    private final JdbcTemplate jdbcTemplate;

    public PositionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Возвращает все должности из БД
    public List<Position> findAll() {
        return jdbcTemplate.query(
                "SELECT id, title FROM positions ORDER BY id",
                new BeanPropertyRowMapper<>(Position.class)
        );
    }
}
