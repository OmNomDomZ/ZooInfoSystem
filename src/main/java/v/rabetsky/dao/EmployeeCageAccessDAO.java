package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.dto.CageDTO;

import java.util.List;

@Slf4j
@Component
public class EmployeeCageAccessDAO {
    private final JdbcTemplate jdbc;

    @Autowired
    public EmployeeCageAccessDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CageDTO> findCagesByEmployee(int employeeId) {
        List<CageDTO> cageDTOs = jdbc.query(
                """
                        SELECT\
                            c.id,\
                            c.animal_type_id,\
                            a_t.type as animalTypeName,\
                            c.capacity
                        FROM cages c
                            JOIN employees_cages_access eca ON eca.cage_id = c.id
                            JOIN animal_types a_t ON c.animal_type_id = a_t.id
                        WHERE eca.employee_id = ?""",
                new BeanPropertyRowMapper<>(CageDTO.class),
                employeeId
        );
        log.info("Cages={}", cageDTOs);
        return cageDTOs;
    }

    public void add(int employeeId, int cageId) {
        jdbc.update(
                "INSERT INTO employees_cages_access(employee_id, cage_id) VALUES (?, ?)",
                employeeId, cageId
        );
    }

    public void remove(int employeeId, int cageId) {
        jdbc.update(
                "DELETE FROM employees_cages_access WHERE employee_id = ? AND cage_id = ?",
                employeeId, cageId
        );
    }

    public List<CageDTO> findAllCages() {
        return jdbc.query(
                "SELECT c.*, at.type AS animalTypeName FROM cages c " +
                        "JOIN animal_types at ON c.animal_type_id = at.id",
                (rs, rowNum) -> {
                    CageDTO cage = new CageDTO();
                    cage.setId(rs.getInt("id"));
                    cage.setCapacity(rs.getInt("capacity"));
                    cage.setAnimalTypeId(rs.getInt("animal_type_id"));
                    cage.setAnimalTypeName(rs.getString("animalTypeName"));
                    return cage;
                }
        );
    }
}

