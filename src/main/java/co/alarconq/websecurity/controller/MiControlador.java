package co.alarconq.websecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MiControlador {

    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/publico")
    public String publico() {
        return "publico";
    }

    @GetMapping("/privado")
    public String privado() {
        return "privado";
    }
}