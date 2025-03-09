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

    /**
     * Constructor con inyección de dependencias.
     *
     * @param localeMessageService servicio para obtener mensajes internacionalizados
     */
    public MiControlador(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    /**
     * Muestra la página de inicio con mensaje de bienvenida internacionalizado.
     *
     * @param exchange intercambio de servidor para obtener información de la solicitud
     * @param model modelo para pasar datos a la vista
     * @return flujo reactivo con el nombre de la vista
     */
    @GetMapping("/")
    public Mono<String> inicio(ServerWebExchange exchange, Model model) {
        return localeMessageService.getMessage("welcome.message", exchange)
                .doOnNext(msg -> model.addAttribute("welcomeMessage", msg))
                .thenReturn("inicio");
    }

    /**
     * Muestra la página de contenido público.
     *
     * @return flujo reactivo con el nombre de la vista
     */
    @GetMapping("/publico")
    public Mono<String> publico() {
        return Mono.just("publico");
    }

    /**
     * Muestra la página de contenido privado (requiere autenticación).
     *
     * @return flujo reactivo con el nombre de la vista
     */
    @GetMapping("/privado")
    public Mono<String> privado() {
        return Mono.just("privado");
    }
}