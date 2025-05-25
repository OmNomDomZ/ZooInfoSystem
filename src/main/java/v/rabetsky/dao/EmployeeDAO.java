package v.rabetsky.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.entities.Position;
import v.rabetsky.models.filters.EmployeeFilter;

import java.util.List;

@Slf4j
@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;
    private final PositionDAO positionDAO;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate, PositionDAO positionDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.positionDAO = positionDAO;
    }

    public List<Employee> findByFilter(EmployeeFilter f) {
        List<Employee> employees = jdbcTemplate.query(
                "SELECT * FROM get_employees(?, ?, ?)",
                new BeanPropertyRowMapper<>(Employee.class),
                f.getFullName(),
                f.getPositionIds(),
                f.getGenders()
        );
        log.info("Employees: {}", employees);
        return employees;
    }

    public Employee show(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM employees WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: show employee id={}", id);
        return employee;
    }

    public void save(Employee employee) {
        Position position = positionDAO.findById(employee.getPositionId());
        int id;
        switch (position.getTitle()) {
            case "ветеринар" -> id = callAddVet(employee);
            case "смотритель" -> id = callAddKeeper(employee);
            case "уборщик"   -> id = callAddJanitor(employee);
            case "администратор" -> id = callAddAdmin(employee);
            default -> throw new UnsupportedOperationException("Неизвестный тип должности");
        }

        log.info("Employees: add new employee id={}", id);
    }

    private Integer callAddVet(Employee v) {
        String sql = "SELECT add_vet(?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(sql,Integer.class,
                v.getFullName(), v.getGender(), v.getHireDate(), v.getBirthDate(),
                v.getPositionId(), v.getSalary(), v.getContactInfo(),
                v.getLicenseNumber(), v.getSpecialization());
    }

    private Integer callAddKeeper(Employee k) {
        String sql = "SELECT add_keeper(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                k.getFullName(), k.getGender(), k.getHireDate(), k.getBirthDate(),
                k.getPositionId(), k.getSalary(), k.getContactInfo(),
                k.getSection());
    }

    private Integer callAddJanitor(Employee j) {
        String sql = "SELECT add_janitor(?,?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(sql,Integer.class,
                j.getFullName(), j.getGender(), j.getHireDate(), j.getBirthDate(),
                j.getPositionId(), j.getSalary(), j.getContactInfo(),
                j.getCleaningShift(), j.getArea(), j.getEquipment());
    }

    private Integer callAddAdmin(Employee a) {
        String sql = "SELECT add_administrator(?,?,?,?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(sql, Integer.class,
                a.getFullName(), a.getGender(), a.getHireDate(), a.getBirthDate(),
                a.getPositionId(), a.getSalary(), a.getContactInfo(),
                a.getDepartment(), a.getPhone());
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

    public Employee getVet(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM vets WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: get vet id={}", id);
        return employee;
    }

    public Employee getKeeper(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM keeper WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: get keeper id={}", id);
        return employee;
    }

    public Employee getJanitor(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM janitors WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: get janitor id={}", id);
        return employee;
    }

    public Employee getAdministrator(int id) {
        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM administrators WHERE id = ?", new BeanPropertyRowMapper<>(Employee.class), id);
        log.info("Employees: get administrator id={}", id);
        return employee;
    }
}