package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.AnimalTypeDAO;
import v.rabetsky.dao.DietTypeDAO;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.models.AnimalFilter;
import v.rabetsky.models.entities.Animal;
import v.rabetsky.models.entities.AnimalType;
import v.rabetsky.models.entities.DietType;
import v.rabetsky.services.AnimalService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/zoo/animals")
public class AnimalsController {
    private final AnimalService animalService;
    private final AnimalTypeDAO animalTypeDAO;
    private final DietTypeDAO dietTypeDAO;

    @Autowired
    public AnimalsController(AnimalService animalService, AnimalTypeDAO animalTypeDAO, DietTypeDAO dietTypeDAO) {
        this.animalService = animalService;
        this.animalTypeDAO = animalTypeDAO;
        this.dietTypeDAO = dietTypeDAO;
    }

    @ModelAttribute("animalTypeList")
    public List<AnimalType> animalTypes() {
        return animalTypeDAO.findAll();
    }

    @ModelAttribute("dietTypeList")
    public List<DietType> dietTypes() {
        return dietTypeDAO.findAll();
    }

    @GetMapping("")
    public String index(AnimalFilter filter, Model model) {
        model.addAttribute("filter", filter);

        List<AnimalDTO> animals = animalService.getAllAnimals(filter);
        model.addAttribute("animals", animals);
        return "animals/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("animal", animalService.findById(id));
        return "animals/show";
    }

    @GetMapping("/new")
    public String newAnimal(@ModelAttribute("animal") Animal animal) {
        return "animals/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("animal") @Valid Animal animal,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "animals/new";
        }
        animalService.save(animal);
        return "redirect:/zoo/animals";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("animal", animalService.findById(id));
        return "animals/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("animal") @Valid Animal animal,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "animals/edit";
        }
        animalService.update(id, animal);
        return "redirect:/zoo/animals";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        animalService.delete(id);
        return "redirect:/zoo/animals";
    }
}
