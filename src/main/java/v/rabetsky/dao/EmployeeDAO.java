package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.Employee;

import java.util.List;

@Slf4j
@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> index() {
        log.info("Employees: show all employees");
        return jdbcTemplate.query("SELECT * FROM employees", new BeanPropertyRowMapper<>(Employee.class));
    }

    public Employee show(int id) {
        List<Employee> employees = jdbcTemplate.query("SELECT * FROM employees WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: show employee id={}", id);
        return employees.stream().findAny().orElse(null);
    }

    public void save(Employee employee) {
        jdbcTemplate.update("INSERT INTO employees (full_name, gender, hire_date, birth_date, position_id, salary, contact_info) VALUES (?, ?, ?, ?, ?, ?, ?)",
                employee.getFull_name(), employee.getGender(), employee.getHire_date(), employee.getBirth_date(),
                employee.getPosition_id(), employee.getSalary(), employee.getContact_info());
        log.info("Employees: add new employee id={}", employee.getId());
    }

    public void update(int id, Employee employee) {
        jdbcTemplate.update("UPDATE employees SET full_name=?, gender=?, hire_date=?, birth_date=?, " +
                        "position_id=?, salary=?, contact_info=? WHERE id=?",
                employee.getFull_name(), employee.getGender(), employee.getHire_date(), employee.getBirth_date(),
                employee.getPosition_id(), employee.getSalary(), employee.getContact_info(), id);
        log.info("Employees: edit employee id={}", employee.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM employees WHERE id=?", id);
        log.info("Employees: delete {} employee", id);
    }
}