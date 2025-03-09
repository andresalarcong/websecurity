package co.alarconq.websecurity.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final MessageSource messageSource;
    private final LocaleContextResolver localeContextResolver;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param messageSource fuente de mensajes internacionalizados
     * @param localeContextResolver resolvedor de contexto de localización
     */
    public LocaleMessageService(MessageSource messageSource, LocaleContextResolver localeContextResolver) {
        this.messageSource = messageSource;
        this.localeContextResolver = localeContextResolver;
    }

    /**
     * Obtiene un mensaje internacionalizado según el idioma seleccionado.
     *
     * @param code código del mensaje
     * @param exchange intercambio de servidor para obtener información del idioma
     * @return flujo reactivo con el mensaje traducido
     */
    public Mono<String> getMessage(String code, ServerWebExchange exchange) {
        LocaleContext localeContext = localeContextResolver.resolveLocaleContext(exchange);
        Locale locale = localeContext.getLocale();
        return Mono.just(messageSource.getMessage(code, null, locale));
    }

    /**
     * Obtiene un mensaje internacionalizado usando el idioma predeterminado.
     *
     * @param code código del mensaje
     * @return flujo reactivo con el mensaje traducido
     */
    public Mono<String> getMessage(String code) {
        return Mono.just(messageSource.getMessage(code, null, Locale.getDefault()));
    }

    /**
     * Obtiene un mensaje internacionalizado de forma síncrona.
     *
     * @param code código del mensaje
     * @param locale localización deseada
     * @return mensaje traducido
     */
    public String getMessageSync(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}