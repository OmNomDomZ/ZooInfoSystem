package v.rabetsky.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import v.rabetsky.dao.ZooDAO;
import v.rabetsky.dto.AnimalDTO;
import v.rabetsky.models.entities.Zoo;
import v.rabetsky.models.filters.AnimalFilter;
import v.rabetsky.services.AnimalService;
import v.rabetsky.services.TransferService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/zoo/transfers")
public class TransfersController {
    private final TransferService transferService;
    private final AnimalService animalService;
    private final ZooDAO zooDAO;

    public TransfersController(TransferService transferService,
                               AnimalService animalService,
                               ZooDAO zooDAO) {
        this.transferService = transferService;
        this.animalService   = animalService;
        this.zooDAO          = zooDAO;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("transfers", transferService.findAll());
        return "transfers/index";
    }

    @GetMapping("/new")
    public String newTransfer(Model model) {
        List<AnimalDTO> animals = animalService.getAllAnimals(new AnimalFilter());
        model.addAttribute("animals", animals);

        List<Zoo> zoos = zooDAO.findAll();
        model.addAttribute("zoos", zoos);

        return "transfers/new";
    }

    @PostMapping
    public String create(@RequestParam("animalId") int animalId,
                         @RequestParam("destinationZooId") Integer destinationZooId,
                         @RequestParam("reason") String reason,
                         @RequestParam("transferDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transferDate) {
        // Записываем перемещение
        transferService.recordOutgoing(animalId, reason, destinationZooId, transferDate);
        //  Удаляем животное из нашего зоопарка
        animalService.delete(animalId);
        return "redirect:/zoo/transfers";
    }
}
