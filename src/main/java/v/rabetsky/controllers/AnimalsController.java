package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.annotations.AdminOnly;
import v.rabetsky.dao.AnimalTypeDAO;
import v.rabetsky.dao.CageDAO;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.dto.CageDTO;
import v.rabetsky.models.entities.Animal;
import v.rabetsky.models.filters.AnimalFilter;
import v.rabetsky.services.AnimalService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        model.addAttribute("cageList",       cageDAO.findAllDTO());
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

    @AdminOnly
    @GetMapping("/new")
    public String newAnimal(Model m) {
        m.addAttribute("animal", new Animal());
        m.addAttribute("animalTypeList", animalTypeDAO.findAll());
        m.addAttribute("cageList",       cageDAO.findAllDTO());
        m.addAttribute("allAnimals",     animalService.getAllAnimals(new AnimalFilter()));
        return "animals/new";
    }

    @AdminOnly
    @PostMapping("")
    public String create(@ModelAttribute("animal") @Valid Animal animal,
                         BindingResult br,
                         @RequestParam("arrivalType")       String arrivalType,
                         @RequestParam(value="originZooName", required = false) String originZoo,
                         @RequestParam(value="parent1Id",     required = false) Integer p1,
                         @RequestParam(value="parent2Id",     required = false) Integer p2,
                         Model m) {

        if (br.hasErrors()) {
            prepareNewForm(m);
            return "animals/new";
        }

        try {
            int newId = animalService.saveAnimalWithOrigin(animal, arrivalType, originZoo, p1, p2);
            return "redirect:/zoo/animals/" + newId + "/medical/new";
        } catch (DataAccessException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            br.reject("", ex.getMessage());
            prepareNewForm(m);
            return "animals/new";
        }
    }

    private void prepareNewForm(Model m) {
        m.addAttribute("animalTypeList", animalTypeDAO.findAll());
        m.addAttribute("cageList",       cageDAO.findAllDTO());
        m.addAttribute("allAnimals",     animalService.getAllAnimals(new AnimalFilter()));
    }

    @AdminOnly
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
        model.addAttribute("animal", animalService.findById(id));
        model.addAttribute("cageList", cageDAO.findAllDTO());
        model.addAttribute("animalTypeList", animalTypeDAO.findAll());
        return "animals/edit";
    }

    @AdminOnly
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("animal") @Valid Animal animal,
                         BindingResult br,
                         @PathVariable int id,
                         Model model) {

        try {
            animalService.update(id, animal);
            return "redirect:/zoo/animals";
        } catch (DataAccessException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            model.addAttribute("cageList", cageDAO.findAllDTO());
            model.addAttribute("animalTypeList", animalTypeDAO.findAll());
            br.reject("", ex.getMessage());
            return "animals/edit";
        }
    }

    @AdminOnly
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        animalService.delete(id);
        return "redirect:/zoo/animals";
    }

    @AdminOnly
    @GetMapping("/{id}/move")
    public String moveForm(@PathVariable int id, Model model) {
        AnimalDTO animal = animalService.findById(id);
        List<CageDTO> all = cageDAO.findAllDTO();
        List<CageDTO> options = all.stream()
                .filter(c -> c.getAnimalTypeName().equals(animal.getAnimalTypeName()))
                .collect(Collectors.toList());
        model.addAttribute("animal",       animal);
        model.addAttribute("cageOptions",  options);
        return "animals/move";
    }

    @AdminOnly
    @PostMapping("/{id}/move")
    public String moveSubmit(@PathVariable int id,
                             @RequestParam("cageId") int cageId) {
        animalService.moveCage(id, cageId);
        return "redirect:/zoo/animals/" + id;
    }
}