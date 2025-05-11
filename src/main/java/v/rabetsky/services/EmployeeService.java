package v.rabetsky.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import v.rabetsky.dao.EmployeeAnimalTypeDAO;
import v.rabetsky.dao.EmployeeCageAccessDAO;
import v.rabetsky.dao.EmployeeDAO;
import v.rabetsky.dao.PositionDAO;
import v.rabetsky.dto.CageDTO;
import v.rabetsky.dto.EmployeeDTO;
import v.rabetsky.models.entities.AnimalType;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.entities.Position;
import v.rabetsky.models.filters.EmployeeFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final PositionDAO positionDAO;
    private final EmployeeCageAccessDAO employeeCageAccessDAO;
    private final EmployeeAnimalTypeDAO employeeAnimalTypeDAO;

    public EmployeeService(EmployeeDAO employeeDAO,
                           PositionDAO positionDAO,
                           EmployeeCageAccessDAO employeeCageAccessDAO,
                           EmployeeAnimalTypeDAO employeeAnimalTypeDAO) {
        this.employeeDAO = employeeDAO;
        this.positionDAO = positionDAO;
        this.employeeCageAccessDAO = employeeCageAccessDAO;
        this.employeeAnimalTypeDAO = employeeAnimalTypeDAO;
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

    public void save(Employee employee) {
        employeeDAO.save(employee);
    }

    public void update(int id, Employee employee) {
        employeeDAO.update(id, employee);
    }

    public void delete(int id) {
        employeeDAO.delete(id);
    }


    public List<AnimalType> getAssignedAnimalTypes(int empId) {
        return employeeAnimalTypeDAO.findAnimalTypesByEmployee(empId);
    }
    public List<CageDTO> getAssignedCages(int empId) {
        return employeeCageAccessDAO.findCagesByEmployee(empId);
    }

    public List<AnimalType> getAllAnimalTypes() {
        return employeeAnimalTypeDAO.findAllAnimalTypes();
    }
    public List<CageDTO> getAllCages() {
        return employeeCageAccessDAO.findAllCages();
    }

    public void addAnimalTypeToEmployee(int empId, int animalTypeId) {
        employeeAnimalTypeDAO.add(empId, animalTypeId);
    }
    public void removeAnimalTypeFromEmployee(int empId, int animalTypeId) {
        employeeAnimalTypeDAO.remove(empId, animalTypeId);
    }
    public void addCageAccessToEmployee(int empId, int cageId) {
        employeeCageAccessDAO.add(empId, cageId);
    }
    public void removeCageAccessFromEmployee(int empId, int cageId) {
        employeeCageAccessDAO.remove(empId, cageId);
    }
}