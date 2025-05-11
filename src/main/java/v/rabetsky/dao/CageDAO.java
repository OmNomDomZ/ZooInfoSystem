package v.rabetsky.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.dto.CageDTO;
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

    public Cage findById(int id) {
            return jdbc.queryForObject("SELECT * FROM cages WHERE id = ?",
                new BeanPropertyRowMapper<>(Cage.class), id);
    }

    public Integer numAnimalsInCage(int id) {
        return jdbc.queryForObject("SELECT COUNT(a.cage_id) FROM animals a WHERE a.cage_id = ?",
                Integer.class, id);
    }

    public List<CageDTO> findAllDTO() {
        return jdbc.query("""
                        SELECT c.id,
                               c.animal_type_id,
                               at.type AS animal_type_name,
                               c.capacity
                          FROM cages c
                          JOIN animal_types at ON c.animal_type_id = at.id
                        ORDER BY c.id
                        """,
                (rs, rowNum) -> new CageDTO(
                        rs.getInt("id"),
                        rs.getInt("animal_type_id"),
                        rs.getString("animal_type_name"),
                        rs.getInt("capacity")
                ));
    }

    public void save(int animalTypeId, int capacity) {
        jdbc.update(
                "INSERT INTO cages (animal_type_id, capacity) VALUES (?, ?)",
                animalTypeId, capacity
        );
    }

    public void delete(int cageId) {
        jdbc.update("DELETE FROM cages WHERE id = ?", cageId);
    }
}
