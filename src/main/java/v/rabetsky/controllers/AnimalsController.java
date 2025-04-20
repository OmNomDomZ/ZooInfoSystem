package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.AnimalDAO;
import v.rabetsky.models.entities.Animal;

import javax.validation.Valid;

@Controller
@RequestMapping("/zoo/animals")
public class AnimalsController {
    private final AnimalDAO animalDAO;

    @Autowired
    public AnimalsController(AnimalDAO animalDAO) {
        this.animalDAO = animalDAO;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("animals", animalDAO.index());
        return "animals/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("animal", animalDAO.show(id));
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
        animalDAO.save(animal);
        return "redirect:/zoo/animals";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("animal", animalDAO.show(id));
        return "animals/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("animal") @Valid Animal animal,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "animals/edit";
        }
        animalDAO.update(id, animal);
        return "redirect:/zoo/animals";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        animalDAO.delete(id);
        return "redirect:/zoo/animals";
    }
}
