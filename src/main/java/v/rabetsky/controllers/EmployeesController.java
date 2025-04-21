package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.PositionDAO;
import v.rabetsky.dto.EmployeeDTO;
import v.rabetsky.models.entities.Employee;
import v.rabetsky.models.EmployeeFilter;
import v.rabetsky.models.entities.Position;
import v.rabetsky.services.EmployeeService;

import javax.validation.Valid;
import java.util.List;

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
        return "employees/show";
    }

    @GetMapping("/new")
    public String newEmployee(@ModelAttribute("employee") Employee employee) {
        return "employees/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("employee") @Valid Employee employee,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "employees/new";
        }

        employeeService.save(employee);
        return "redirect:/zoo/employees";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "employees/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "employees/edit";
        }

        employeeService.update(id, employee);
        return "redirect:/zoo/employees";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        employeeService.delete(id);
        return "redirect:/zoo/employees";
    }
}
