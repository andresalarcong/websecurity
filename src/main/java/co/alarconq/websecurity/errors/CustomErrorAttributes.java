package co.alarconq.websecurity.errors;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Throwable error = getError(request);

        // Personalizar error basado en estatus HTTP
        HttpStatus status = determineHttpStatus(error);
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());

        // Mensaje personalizado dependiendo del tipo de error
        if (status.is4xxClientError()) {
            customizeClientErrorMessage(errorAttributes, status);
        }

        return errorAttributes;
    }

    private void customizeClientErrorMessage(Map<String, Object> errorAttributes, HttpStatus status) {
        switch (status) {
            case NOT_FOUND:
                errorAttributes.put("message", "Resource not found");
                break;
            case FORBIDDEN:
                errorAttributes.put("message", "Access denied");
                break;
            case UNAUTHORIZED:
                errorAttributes.put("message", "Authentication required");
                break;
            case BAD_REQUEST:
                errorAttributes.put("message", "Invalid request");
                break;
            default:
                // Mantener el mensaje predeterminado para otros códigos
                break;
        }
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof ResponseStatusException) {
            return (HttpStatus) ((ResponseStatusException) error).getStatusCode();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}