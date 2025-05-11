package v.rabetsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("role") String role, HttpSession s) {
        // "ADMIN" или что-то другое → READER
        s.setAttribute("ROLE", "ADMIN".equals(role) ? "ADMIN" : "READER");
        return "redirect:/zoo";          // перенаправьте, куда нужно
    }
}
