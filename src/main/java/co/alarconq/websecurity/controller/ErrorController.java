package co.alarconq.websecurity.controller;

import co.alarconq.websecurity.i18n.LocaleMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/error")
public class ErrorController {

    private final LocaleMessageService localeMessageService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param localeMessageService servicio para obtener mensajes internacionalizados
     */
    public ErrorController(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    /**
     * Maneja errores 404 (No encontrado).
     *
     * @param exchange intercambio de servidor
     * @param model modelo para pasar datos a la vista
     * @return vista de error personalizada
     */
    @GetMapping("/404")
    public Mono<String> handle404Error(ServerWebExchange exchange, Model model) {
        return prepareErrorModel(exchange, model, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/403")
    public Mono<String> handle403Error(ServerWebExchange exchange, Model model) {
        return prepareErrorModel(exchange, model, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/401")
    public Mono<String> handle401Error(ServerWebExchange exchange, Model model) {
        return prepareErrorModel(exchange, model, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/400")
    public Mono<String> handle400Error(ServerWebExchange exchange, Model model) {
        return prepareErrorModel(exchange, model, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/500")
    public Mono<String> handle500Error(ServerWebExchange exchange, Model model) {
        return prepareErrorModel(exchange, model, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Prepara el modelo para la vista de error.
     *
     * @param exchange intercambio de servidor
     * @param model modelo para pasar datos a la vista
     * @param status código de estado HTTP
     * @return nombre de la vista de error
     */
    private Mono<String> prepareErrorModel(ServerWebExchange exchange, Model model, HttpStatus status) {
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("path", exchange.getRequest().getPath().value());

        String errorCode = "error." + status.value() + ".title";
        String messageCode = "error." + status.value() + ".message";

        return localeMessageService.getMessage(errorCode, exchange)
                .doOnNext(title -> model.addAttribute("errorTitle", title))
                .then(localeMessageService.getMessage(messageCode, exchange))
                .doOnNext(message -> model.addAttribute("errorMessage", message))
                .then(localeMessageService.getMessage("error.return", exchange))
                .doOnNext(returnText -> model.addAttribute("returnText", returnText))
                .thenReturn("error");
    }
}