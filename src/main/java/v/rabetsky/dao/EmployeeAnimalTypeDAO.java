package v.rabetsky.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.AnimalType;

import java.util.List;

@Component
public class EmployeeAnimalTypeDAO {
    private final JdbcTemplate jdbc;

    @Autowired
    public EmployeeAnimalTypeDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<AnimalType> findAnimalTypesByEmployee(int employeeId) {
        return jdbc.query(
                "SELECT at.* FROM animal_types at " +
                        "JOIN employees_animal_types eat ON eat.animal_type_id = at.id " +
                        "WHERE eat.employee_id = ?",
                new BeanPropertyRowMapper<>(AnimalType.class),
                employeeId
        );
    }

    public void add(int employeeId, int animalTypeId) {
        jdbc.update(
                "INSERT INTO employees_animal_types(employee_id, animal_type_id) VALUES (?, ?)",
                employeeId, animalTypeId
        );
    }

    public void remove(int employeeId, int animalTypeId) {
        jdbc.update(
                "DELETE FROM employees_animal_types WHERE employee_id = ? AND animal_type_id = ?",
                employeeId, animalTypeId
        );
    }

    public List<AnimalType> findAllAnimalTypes() {
        return jdbc.query(
                "SELECT * FROM animal_types",
                new BeanPropertyRowMapper<>(AnimalType.class)
        );
    }
}