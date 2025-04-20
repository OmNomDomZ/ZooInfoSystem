package v.rabetsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/zoo")
public class ZooController {

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("welcomeMessage", "Добро пожаловать в Зоопарк!");
        return "zoo/index";
    }
}
