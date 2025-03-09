package co.alarconq.websecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    /**
     * Muestra la página de inicio de sesión.
     *
     * @return nombre de la vista para la página de login
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Busca la plantilla login.html
    }
}