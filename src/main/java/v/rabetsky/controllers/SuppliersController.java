package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.dao.SupplierDAO;
import v.rabetsky.models.entities.Supplier;

import javax.validation.Valid;

@Controller
@RequestMapping("/zoo/suppliers")
public class SuppliersController {
    private final SupplierDAO supplierDAO;

    @Autowired
    public SuppliersController(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("suppliers", supplierDAO.index());
        return "suppliers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("supplier", supplierDAO.show(id));
        return "suppliers/show";
    }

    @GetMapping("/new")
    public String newSupplier(@ModelAttribute("supplier") Supplier supplier) {
        return "suppliers/new";
    }

    @PostMapping("")
    public String create(@ModelAttribute("supplier") @Valid Supplier supplier,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "suppliers/new";
        }
        supplierDAO.save(supplier);
        return "redirect:/zoo/suppliers";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("supplier", supplierDAO.show(id));
        return "suppliers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("supplier") @Valid Supplier supplier,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "suppliers/edit";
        }
        supplierDAO.update(id, supplier);
        return "redirect:/zoo/suppliers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        supplierDAO.delete(id);
        return "redirect:/zoo/suppliers";
    }
}
