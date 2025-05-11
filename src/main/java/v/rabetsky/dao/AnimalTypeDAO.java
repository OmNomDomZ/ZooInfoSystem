package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.dto.AnimalTypeDTO;
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
                "SELECT * FROM animal_types ORDER BY id",
                new BeanPropertyRowMapper<>(AnimalType.class)
        );
    }

    public List<AnimalTypeDTO> findAllAnimalTypes() {
        String sql = """
            SELECT at.id, at.type, dt.type AS dietTypeName
            FROM animal_types at
            JOIN diet_types dt ON at.diet_type_id = dt.id
            ORDER BY at.type
        """;
        return jdbcTemplate.query(sql,
                (rs, i) -> {
                    AnimalTypeDTO at = new AnimalTypeDTO();
                    at.setId(rs.getInt("id"));
                    at.setType(rs.getString("type"));
                    at.setDietTypeName(rs.getString("dietTypeName"));
                    return at;
                });
    }

    public void save(String type, int dietTypeId) {
        jdbcTemplate.update("INSERT INTO animal_types (type, diet_type_id) VALUES (?, ?)",
                type, dietTypeId);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM animal_types WHERE id = ?", id);
    }

    public boolean hasAnimals(int typeId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM animals WHERE animal_type_id = ?",
                Integer.class, typeId
        );
        return count > 0;
    }
}
