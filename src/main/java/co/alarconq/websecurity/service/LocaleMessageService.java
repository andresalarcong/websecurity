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

    public LocaleMessageService(MessageSource messageSource, LocaleContextResolver localeContextResolver) {
        this.messageSource = messageSource;
        this.localeContextResolver = localeContextResolver;
    }

    public Mono<String> getMessage(String code, ServerWebExchange exchange) {
        LocaleContext localeContext = localeContextResolver.resolveLocaleContext(exchange);
        Locale locale = localeContext.getLocale();
        return Mono.just(messageSource.getMessage(code, null, locale));
    }

    // Sobrecarga para usar sin ServerWebExchange (usa el locale por defecto)
    public Mono<String> getMessage(String code) {
        return Mono.just(messageSource.getMessage(code, null, Locale.getDefault()));
    }

    // Método síncrono para obtener mensajes
    public String getMessageSync(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}