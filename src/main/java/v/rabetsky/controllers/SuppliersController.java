package v.rabetsky.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import v.rabetsky.annotations.AdminOnly;
import v.rabetsky.models.entities.*;
import v.rabetsky.models.filters.SupplierFilter;
import v.rabetsky.services.SupplierService;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/zoo/suppliers")
public class SuppliersController {
    private final SupplierService service;

    @Autowired
    public SuppliersController(SupplierService service) {
        this.service = service;
    }

    @ModelAttribute("foodList")
    public List<Food> populateFood() {
        return service.getAllFood();
    }

    @ModelAttribute("foodTypeList")
    public List<FoodType> populateFoodTypes() {
        return service.getAllFoodTypes();
    }

    @GetMapping("")
    public String index(SupplierFilter filter, Model m) {
        m.addAttribute("filter", filter);
        m.addAttribute("suppliers", service.getAllSuppliers(filter));
        return "suppliers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model m) {
        m.addAttribute("supplier", service.findSupplier(id));
        m.addAttribute("deliveries", service.getDeliveries(id));
        m.addAttribute("newDelivery", new SupplierFood());
        return "suppliers/show";
    }

    @AdminOnly
    @GetMapping("/new")
    public String newSupplier(@ModelAttribute("supplier") Supplier s) {
        return "suppliers/new";
    }

    @AdminOnly
    @PostMapping("")
    public String create(@Valid @ModelAttribute Supplier s, BindingResult br) {
        if (br.hasErrors()) return "suppliers/new";

        try {
            service.saveSupplier(s);
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/suppliers";
    }

    @AdminOnly
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable int id, Model m) {
        m.addAttribute("supplier", service.findSupplier(id));
        return "suppliers/edit";
    }

    @AdminOnly
    @PatchMapping("/{id}")
    public String update(@PathVariable int id,
                         @Valid @ModelAttribute Supplier s,
                         BindingResult br) {
        if (br.hasErrors()) return "suppliers/edit";

        try {
            service.updateSupplier(id, s);
        } catch (DataAccessException ex) {
            throw ex;
        }
        return "redirect:/zoo/suppliers";
    }

    @AdminOnly
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.deleteSupplier(id);
        return "redirect:/zoo/suppliers";
    }

    @AdminOnly
    @PostMapping("/{supplierId}/deliveries")
    public String addDelivery(@PathVariable int supplierId,
                              @Valid @ModelAttribute("newDelivery") SupplierFood sf,
                              BindingResult br) {
        if (!br.hasErrors()) {
            sf.setSupplierId(supplierId);
            service.addDelivery(sf);
        }
        return "redirect:/zoo/suppliers/" + supplierId;
    }

    @AdminOnly
    @PatchMapping("/{supplierId}/deliveries/{delivId}")
    public String editDelivery(@PathVariable int supplierId,
                               @PathVariable int delivId,
                               @Valid @ModelAttribute SupplierFood sf,
                               BindingResult br) {
        if (!br.hasErrors()) {
            service.updateDelivery(delivId, sf);
        }
        return "redirect:/zoo/suppliers/" + supplierId;
    }

    @AdminOnly
    @DeleteMapping("/{supplierId}/deliveries/{delivId}")
    public String deleteDelivery(@PathVariable int supplierId,
                                 @PathVariable int delivId) {
        service.deleteDelivery(delivId);
        return "redirect:/zoo/suppliers/" + supplierId;
    }
}
