package co.alarconq.websecurity.controller;

import co.alarconq.websecurity.i18n.LocaleMessageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
public class MiControlador {

    private final LocaleMessageService localeMessageService;

    public MiControlador(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    @GetMapping("/")
    public Mono<String> inicio(ServerWebExchange exchange, Model model) {
        return localeMessageService.getMessage("welcome.message", exchange)
                .doOnNext(msg -> model.addAttribute("welcomeMessage", msg))
                .thenReturn("inicio");
    }

    @GetMapping("/publico")
    public Mono<String> publico() {
        return Mono.just("publico");
    }

    @GetMapping("/privado")
    public Mono<String> privado() {
        return Mono.just("privado");
    }
}