package co.alarconq.websecurity.controller;

import co.alarconq.websecurity.i18n.LocaleMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class SaludoController {

    private final LocaleMessageService localeMessageService;

    public SaludoController(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    @GetMapping("/saludo")
    public Mono<String> obtenerSaludo(ServerWebExchange exchange) {
        return localeMessageService.getMessage("saludo", exchange);
    }
}