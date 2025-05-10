package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.AnimalCageHistory;
import java.util.List;

@Component
public class AnimalCageHistoryDAO {
    private final JdbcTemplate jdbcTemplate;

    public AnimalCageHistoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AnimalCageHistory> findByAnimalId(int animalId) {
        return jdbcTemplate.query(
                "SELECT * FROM animal_cage_history WHERE animal_id = ? ORDER BY start_date",
                new BeanPropertyRowMapper<>(AnimalCageHistory.class),
                animalId
        );
    }
}
