package co.alarconq.websecurity.errors;

import co.alarconq.websecurity.service.LocaleMessageService;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2) // Alta prioridad para interceptar errores antes que el manejador predeterminado
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final LocaleMessageService localeMessageService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param errorAttributes atributos de error
     * @param resources recursos web
     * @param applicationContext contexto de aplicación
     * @param serverCodecConfigurer configurador de códec de servidor
     * @param localeMessageService servicio para obtener mensajes internacionalizados
     */
    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          WebProperties.Resources resources,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer,
                                          LocaleMessageService localeMessageService) {
        super(errorAttributes, resources, applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.localeMessageService = localeMessageService;
    }

    /**
     * Define la función de enrutamiento para manejar errores.
     *
     * @param errorAttributes atributos de error
     * @return función de enrutamiento configurada
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Renderiza la respuesta de error adecuada (JSON para API, HTML para web).
     *
     * @param request solicitud del servidor
     * @return respuesta del servidor con información de error
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable error = getError(request);
        HttpStatus status = determineHttpStatus(error);

        // Si es una solicitud a la API, devolver JSON
        if (request.path().startsWith("/api/")) {
            return ServerResponse.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(errorPropertiesMap));
        }

        // Si es una solicitud web, renderizar una vista HTML
        // Usamos HashMap mutable en lugar de Map.of() para evitar NPE con valores nulos
        Map<String, Object> model = new HashMap<>();
        model.put("status", status.value());
        model.put("error", status.getReasonPhrase());

        // Manejo seguro de posibles valores nulos
        String errorMessage = errorPropertiesMap.get("message") != null
                ? errorPropertiesMap.get("message").toString()
                : "No additional information available";
        model.put("errorMessage", errorMessage);

        model.put("path", request.path());

        // Agregamos atributos para la internacionalización
        // Estos se completarán más adelante en el flujo
        model.put("errorTitle", status.getReasonPhrase());
        model.put("returnText", "Return to Home Page");

        return ServerResponse.status(status)
                .contentType(MediaType.TEXT_HTML)
                .render("error", model);
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof ResponseStatusException) {
            return (HttpStatus) ((ResponseStatusException) error).getStatusCode();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Configuration
    public static class ErrorConfiguration {

        @Bean
        public WebProperties.Resources resources() {
            return new WebProperties.Resources();
        }
    }
}