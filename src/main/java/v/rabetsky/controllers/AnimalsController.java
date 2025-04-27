package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.AnimalTypeDAO;
import v.rabetsky.dao.CageDAO;
import v.rabetsky.models.filters.AnimalFilter;
import v.rabetsky.models.entities.Animal;
import v.rabetsky.services.AnimalService;

import javax.validation.Valid;

@Controller
@RequestMapping("/zoo/animals")
public class AnimalsController {
    private final AnimalService animalService;
    private final CageDAO cageDAO;
    private final AnimalTypeDAO animalTypeDAO;

    @Autowired
    public AnimalsController(AnimalService animalService,
                             CageDAO cageDAO,
                             AnimalTypeDAO animalTypeDAO) {
        this.animalService   = animalService;
        this.cageDAO         = cageDAO;
        this.animalTypeDAO   = animalTypeDAO;
    }

    @GetMapping("")
    public String index(AnimalFilter filter, Model model) {
        model.addAttribute("filter",         filter);
        model.addAttribute("animalTypeList", animalTypeDAO.findAll());
        model.addAttribute("cageList",       cageDAO.findAll());                 
        model.addAttribute("animals",        animalService.getAllAnimals(filter));
        return "animals/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {
        model.addAttribute("animal",          animalService.findById(id));
        model.addAttribute("history",         animalService.getHistory(id));
        model.addAttribute("medicalRecords",  animalService.getMedicalRecords(id));
        model.addAttribute("birthRecords",    animalService.getBirthRecords(id));
        return "animals/show";
    }

    @GetMapping("/new")
    public String newAnimal(Model model) {
        model.addAttribute("animal", new Animal());
        model.addAttribute("cageList", cageDAO.findAll());
        model.addAttribute("animalTypeList", animalTypeDAO.findAll());
        return "animals/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute @Valid Animal animal,
                         BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("cageList", cageDAO.findAll());
            model.addAttribute("animalTypeList", animalTypeDAO.findAll());
            return "animals/new";
        }
        int newId = animalService.save(animal);
        return "redirect:/zoo/animals/" + newId + "/medical/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("animal", animalService.findById(id));
        model.addAttribute("cageList", cageDAO.findAll());
        model.addAttribute("animalTypeList", animalTypeDAO.findAll());
        return "animals/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Animal animal, @PathVariable int id) {
        animalService.update(id, animal);
        return "redirect:/zoo/animals";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        animalService.delete(id);
        return "redirect:/zoo/animals";
    }
}