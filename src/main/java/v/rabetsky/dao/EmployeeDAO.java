package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.EmployeeFilter;

import java.util.List;

@Slf4j
@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findByFilter(EmployeeFilter f) {
        return jdbcTemplate.query(
                "SELECT * FROM get_employees(?, ?, ?)",
                new BeanPropertyRowMapper<>(Employee.class),
                f.getFullName(),
                f.getPositionIds(),
                f.getGenders()
        );
    }

    public Employee show(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM employees WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: show employee id={}", id);
        return employee;
    }

    public void save(Employee employee) {
        jdbcTemplate.update("INSERT INTO employees (full_name, gender, hire_date, birth_date, position_id, salary, contact_info) VALUES (?, ?, ?, ?, ?, ?, ?)",
                employee.getFullName(), employee.getGender(), employee.getHireDate(), employee.getBirthDate(),
                employee.getPositionId(), employee.getSalary(), employee.getContactInfo());
        log.info("Employees: add new employee id={}", employee.getId());
    }

    public void update(int id, Employee employee) {
        jdbcTemplate.update("UPDATE employees SET full_name=?, gender=?, hire_date=?, birth_date=?, " +
                        "position_id=?, salary=?, contact_info=? WHERE id=?",
                employee.getFullName(), employee.getGender(), employee.getHireDate(), employee.getBirthDate(),
                employee.getPositionId(), employee.getSalary(), employee.getContactInfo(), id);
        log.info("Employees: edit employee id={}", employee.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM employees WHERE id=?", id);
        log.info("Employees: delete {} employee", id);
    }
}