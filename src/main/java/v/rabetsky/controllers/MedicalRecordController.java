package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.MedicalRecordDAO;
import v.rabetsky.models.entities.MedicalRecord;

import javax.validation.Valid;

@Controller
@RequestMapping("/zoo/animals/{animalId}/medical")
public class MedicalRecordController {
    private final MedicalRecordDAO medicalRecordDAO;

    @Autowired
    public MedicalRecordController(MedicalRecordDAO medicalRecordDAO) {
        this.medicalRecordDAO = medicalRecordDAO;
    }

    @GetMapping("/new")
    public String newRecord(@PathVariable int animalId, Model model) {
        MedicalRecord mr = new MedicalRecord();
        mr.setAnimalId(animalId);
        model.addAttribute("medicalRecord", mr);
        return "animals/medical_new";
    }

    @PostMapping("")
    public String create(@ModelAttribute @Valid MedicalRecord medicalRecord,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "animals/medical_new";
        }
        try {
            medicalRecordDAO.saveForAnimal(medicalRecord.getAnimalId(), medicalRecord);
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/animals/" + medicalRecord.getAnimalId();
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int animalId,
                       @PathVariable int id,
                       Model model) {
        model.addAttribute("medicalRecord", medicalRecordDAO.findById(id));
        return "animals/medical_edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute @Valid MedicalRecord medicalRecord,
                         BindingResult br) {
        if (br.hasErrors()) {
            return "animals/medical_edit";
        }
        try {
            medicalRecordDAO.update(medicalRecord);
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/animals/" + medicalRecord.getAnimalId();
    }
}
