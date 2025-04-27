package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Cage;

import java.util.List;

@Component
public class CageDAO {

    private final JdbcTemplate jdbc;

    public CageDAO(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Cage> findAll() {
        return jdbc.query("SELECT * FROM cages",
                new BeanPropertyRowMapper<>(Cage.class));
    }
}
