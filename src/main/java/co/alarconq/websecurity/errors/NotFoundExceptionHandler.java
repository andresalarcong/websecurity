package co.alarconq.websecurity.errors;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Order(-1) // Debe ejecutarse después del GlobalErrorWebExceptionHandler
public class NotFoundExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;

            if (responseStatusException.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return handleNotFound(exchange);
            }

            // Gestionar otros errores de estado HTTP comunes
            if (responseStatusException.getStatusCode().is4xxClientError()) {
                return handleClientError(exchange, (HttpStatus) responseStatusException.getStatusCode());
            }
        }

        // No manejamos este error, dejamos que lo manejen otros handlers
        return Mono.error(ex);
    }

    private Mono<Void> handleNotFound(ServerWebExchange exchange) {
        // Para API, devolver 404 JSON
        if (exchange.getRequest().getPath().value().startsWith("/api/")) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String errorJson = "{\"status\":404,\"error\":\"Not Found\",\"message\":\"Resource not found\"}";
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(errorJson.getBytes()))
            );
        }

        // Para solicitudes web, redirigir a página de error 404
        exchange.getResponse().setStatusCode(HttpStatus.FOUND); // 302 Redirect
        String lang = exchange.getRequest().getQueryParams().getFirst("lang");
        if (lang == null) lang = "en";
        exchange.getResponse().getHeaders().setLocation(URI.create("/error/404?lang=" + lang));
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> handleClientError(ServerWebExchange exchange, HttpStatus status) {
        // Para API, devolver código de estado y JSON
        if (exchange.getRequest().getPath().value().startsWith("/api/")) {
            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String errorJson = String.format(
                    "{\"status\":%d,\"error\":\"%s\",\"message\":\"%s\"}",
                    status.value(), status.getReasonPhrase(), getDefaultMessage(status)
            );
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(errorJson.getBytes()))
            );
        }

        // Para solicitudes web, redirigir a página de error correspondiente
        exchange.getResponse().setStatusCode(HttpStatus.FOUND); // 302 Redirect
        String lang = exchange.getRequest().getQueryParams().getFirst("lang");
        if (lang == null) lang = "en";
        exchange.getResponse().getHeaders().setLocation(
                URI.create("/error/" + status.value() + "?lang=" + lang));
        return exchange.getResponse().setComplete();
    }

    private String getDefaultMessage(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST:
                return "Bad Request";
            case UNAUTHORIZED:
                return "Unauthorized";
            case FORBIDDEN:
                return "Forbidden";
            case NOT_FOUND:
                return "Not Found";
            default:
                return "Error";
        }
    }
}