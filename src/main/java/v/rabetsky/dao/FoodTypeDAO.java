package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.FoodType;
import java.util.List;

@Component
public class FoodTypeDAO {
    private final JdbcTemplate jdbc;

    public FoodTypeDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<FoodType> findAll() {
        return jdbc.query("SELECT * FROM food_types", new BeanPropertyRowMapper<>(FoodType.class));
    }
}
