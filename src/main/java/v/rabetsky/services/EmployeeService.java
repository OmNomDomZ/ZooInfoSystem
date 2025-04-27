package v.rabetsky.services;

import org.springframework.stereotype.Service;
import v.rabetsky.dao.EmployeeDAO;
import v.rabetsky.dao.PositionDAO;
import v.rabetsky.dto.EmployeeDTO;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.filters.EmployeeFilter;
import v.rabetsky.models.entities.Position;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final PositionDAO positionDAO;

    public EmployeeService(EmployeeDAO employeeDAO, PositionDAO positionDAO) {
        this.employeeDAO = employeeDAO;
        this.positionDAO = positionDAO;
    }

    public List<EmployeeDTO> findAllWithPositionName(EmployeeFilter filter) {
        List<Employee> employees = employeeDAO.findByFilter(filter);
        Map<Integer, String> posMap = positionDAO.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getTitle));

        return employees.stream()
                .map(e -> EmployeeDTO.builder()
                        .id(e.getId())
                        .fullName(e.getFullName())
                        .gender(e.getGender())
                        .hireDate(e.getHireDate())
                        .birthDate(e.getBirthDate())
                        .positionId(e.getPositionId())
                        .positionName(posMap.get(e.getPositionId()))
                        .salary(e.getSalary())
                        .contactInfo(e.getContactInfo())
                        .specialAttributes(e.getSpecialAttributes())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public EmployeeDTO findById(Integer id) {
        Employee e = employeeDAO.show(id);
        Map<Integer, String> posMap = positionDAO.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getTitle));

        return EmployeeDTO.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .gender(e.getGender())
                .hireDate(e.getHireDate())
                .birthDate(e.getBirthDate())
                .positionId(e.getPositionId())
                .positionName(posMap.get(e.getPositionId()))
                .salary(e.getSalary())
                .contactInfo(e.getContactInfo())
                .specialAttributes(e.getSpecialAttributes())
                .build();
    }

    public Employee show(int id) {
        return employeeDAO.show(id);
    }

    public void save(Employee employee) {
        employeeDAO.save(employee);
    }

    public void update(int id, Employee employee) {
        employeeDAO.update(id, employee);
    }

    public void delete(int id) {
        employeeDAO.delete(id);
    }


}