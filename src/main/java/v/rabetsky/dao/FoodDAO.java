package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Food;
import java.util.List;

@Component
public class FoodDAO {
    private final JdbcTemplate jdbc;

    public FoodDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Food> findAll() {
        return jdbc.query("SELECT * FROM food", new BeanPropertyRowMapper<>(Food.class));
    }
}
