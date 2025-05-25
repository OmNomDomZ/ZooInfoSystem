package v.rabetsky.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.annotations.AdminOnly;
import v.rabetsky.dao.PositionDAO;
import v.rabetsky.dto.EmployeeDTO;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.filters.EmployeeFilter;
import v.rabetsky.models.entities.Position;
import v.rabetsky.services.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/zoo/employees")
public class EmployeesController {

    private final EmployeeService employeeService;
    private final PositionDAO positionDAO;

    @Autowired
    public EmployeesController(EmployeeService employeeService,
                               PositionDAO positionDAO) {
        this.employeeService = employeeService;
        this.positionDAO     = positionDAO;
    }

    @ModelAttribute("positionsList")
    public List<Position> populatePositions() {
        return positionDAO.findAll();
    }

    @GetMapping("")
    public String index(EmployeeFilter filter, Model model) {
        model.addAttribute("filter", filter);

        List<EmployeeDTO> employees = employeeService.findAllWithPositionName(filter);
        model.addAttribute("employees", employees);

        return "employees/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("assignedAnimalTypes",
                employeeService.getAssignedAnimalTypes(id));
        model.addAttribute("assignedCages",
                employeeService.getAssignedCages(id));
        model.addAttribute("allAnimalTypes",
                employeeService.getAllAnimalTypes());
        model.addAttribute("allCages",
                employeeService.getAllCages());
        return "employees/show";
    }

    @AdminOnly
    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("employee") Employee employee) {
        return "employees/new";
    }

    @AdminOnly
    @PostMapping()
    public String create(@ModelAttribute("employee") @Valid Employee employee,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "employees/new";
        }

        try{
            employeeService.save(employee);
        } catch (DataAccessException ex) {
            throw ex;
        }

        return "redirect:/zoo/employees";
    }

    @AdminOnly
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/edit";
    }

    @AdminOnly
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "employees/edit";
        }
        try {
            employeeService.update(id, employee);
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/employees/" + id;
    }

    @AdminOnly
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        employeeService.delete(id);
        return "redirect:/zoo/employees";
    }

    @AdminOnly
    @PostMapping("/{id}/animal-types")
    public String addAnimalType(@PathVariable int id,
                                @RequestParam("animalTypeId") int animalTypeId) {
        employeeService.addAnimalTypeToEmployee(id, animalTypeId);
        return "redirect:/zoo/employees/" + id;
    }

    @AdminOnly
    @PostMapping("/{id}/animal-types/{atypeId}/delete")
    public String deleteAnimalType(@PathVariable int id,
                                   @PathVariable("atypeId") int atypeId) {
        employeeService.removeAnimalTypeFromEmployee(id, atypeId);
        return "redirect:/zoo/employees/" + id;
    }

    @AdminOnly
    @PostMapping("/{id}/cages")
    public String addCageAccess(@PathVariable int id,
                                @RequestParam("cageId") int cageId) {
        employeeService.addCageAccessToEmployee(id, cageId);
        return "redirect:/zoo/employees/" + id;
    }

    @AdminOnly
    @PostMapping("/{id}/cages/{cageId}/delete")
    public String deleteCageAccess(@PathVariable int id,
                                   @PathVariable int cageId) {
        employeeService.removeCageAccessFromEmployee(id, cageId);
        return "redirect:/zoo/employees/" + id;
    }
}
