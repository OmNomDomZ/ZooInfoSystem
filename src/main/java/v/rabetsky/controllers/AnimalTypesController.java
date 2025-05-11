package v.rabetsky.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.models.AnimalTypeForm;
import v.rabetsky.services.AnimalTypeService;

import javax.validation.Valid;

@Controller
@RequestMapping("/zoo/animal-types")
public class AnimalTypesController {
    private final AnimalTypeService animalTypeService;

    public AnimalTypesController(AnimalTypeService animalTypeService) {
        this.animalTypeService = animalTypeService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("types", animalTypeService.findAll());
        model.addAttribute("animalsByType", animalTypeService.getAnimalsByType());
        model.addAttribute("dietTypes", animalTypeService.getAllDietTypes());
        model.addAttribute("newType", new AnimalTypeForm());
        return "animal_types/index";
    }

    @PostMapping
    public String add(@ModelAttribute("newType") @Valid AnimalTypeForm form,
                      BindingResult br,
                      Model model) {
        if (br.hasErrors()) return index(model);
        animalTypeService.addType(form.getType(), form.getDietTypeId());
        return "redirect:/zoo/animal-types";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, Model model) {
        try {
            animalTypeService.deleteType(id);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (IllegalStateException ex) {
            model.addAttribute("deleteError", ex.getMessage());
            model.addAttribute("types", animalTypeService.findAll());
            model.addAttribute("animalsByType", animalTypeService.getAnimalsByType());
            model.addAttribute("dietTypes", animalTypeService.getAllDietTypes());
            model.addAttribute("newType", new AnimalTypeForm()); // нужно, чтобы не упал form
            return "animal_types/index";
        }
        return "redirect:/zoo/animal-types";
    }
}

