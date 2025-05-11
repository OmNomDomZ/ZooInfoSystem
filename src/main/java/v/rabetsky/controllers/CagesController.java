package v.rabetsky.controllers;

import org.springframework.dao.DataAccessException;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.CageDTO;
import v.rabetsky.models.CageForm;
import v.rabetsky.models.entities.AnimalType;
import v.rabetsky.services.CageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/zoo/cages")
public class CagesController {
    private final CageService cageService;

    public CagesController(CageService cageService) {
        this.cageService = cageService;
    }

    @GetMapping
    public String index(Model model) {
        List<CageDTO> cages = cageService.getAllCages();
        Map<Integer, List<AnimalDTO>> byCage = cageService.getAnimalsByCage();
        List<AnimalType> types = cageService.getAllAnimalTypes();

        model.addAttribute("cages", cages);
        model.addAttribute("animalsByCage", byCage);
        model.addAttribute("allCages", cages);
        model.addAttribute("allTypes", types);
        model.addAttribute("newCage", new CageForm());
        model.addAttribute("moveForm", new CageForm.MoveForm());
        return "cages/index";
    }

    @PostMapping
    public String create(@ModelAttribute("newCage") @Valid CageForm form,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) {
            return index(model);
        }
        try {
            cageService.createCage(form.getAnimalTypeId(), form.getCapacity());
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/cages";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        try {
            cageService.deleteCage(id);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (IllegalStateException ex) {
            model.addAttribute("deleteError", ex.getMessage());
            return index(model);
        }
        return "redirect:/zoo/cages";
    }

    @PostMapping("/{id}/move")
    public String move(@PathVariable("id") int fromCage,
                       @ModelAttribute("moveForm") @Valid CageForm.MoveForm form,
                       BindingResult br,
                       Model model) {
        if (br.hasErrors()) {
            return index(model);
        }

        try {
            cageService.moveAnimal(form.getAnimalId(), form.getNewCageId());
        } catch (DataAccessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            model.addAttribute("deleteError", ex.getMessage());
            return index(model);
        }
        return "redirect:/zoo/cages";
    }
}
