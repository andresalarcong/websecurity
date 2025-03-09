package co.alarconq.websecurity.errors;

import co.alarconq.websecurity.i18n.LocaleMessageService;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2) // Alta prioridad para interceptar errores antes que el manejador predeterminado
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final LocaleMessageService localeMessageService;

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

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

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
        return ServerResponse.status(status)
                .contentType(MediaType.TEXT_HTML)
                .render("error", Map.of(
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", errorPropertiesMap.get("message"),
                        "path", request.path()
                ));
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