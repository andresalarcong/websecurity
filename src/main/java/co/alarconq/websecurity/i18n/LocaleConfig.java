package co.alarconq.websecurity.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig implements WebFluxConfigurer {
    private static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.ENGLISH,
            new Locale("es"),
            new Locale("fr")
    );
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * Configura la fuente de mensajes internacionalizados.
     *
     * @return fuente de mensajes configurada
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * Configura el resolvedor de contexto de localización.
     *
     * @return resolvedor de contexto de localización
     */
    @Bean
    public LocaleContextResolver localeContextResolver() {
        return new LocaleContextResolver() {
            @Override
            public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
                String lang = exchange.getRequest().getQueryParams().getFirst("lang");

                Locale targetLocale = DEFAULT_LOCALE;
                if (lang != null && !lang.isEmpty()) {
                    Locale requestedLocale = Locale.forLanguageTag(lang);
                    if (SUPPORTED_LOCALES.stream().anyMatch(locale ->
                            locale.getLanguage().equals(requestedLocale.getLanguage()))) {
                        targetLocale = requestedLocale;
                    }
                }

                return new SimpleLocaleContext(targetLocale);
            }

            @Override
            public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
                // No se requiere implementación
            }
        };
    }

    /**
     * Filtro web para procesar el contexto de localización.
     *
     * @return filtro web configurado
     */
    @Bean
    public WebFilter localeWebFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            return chain.filter(exchange);
        };
    }
}