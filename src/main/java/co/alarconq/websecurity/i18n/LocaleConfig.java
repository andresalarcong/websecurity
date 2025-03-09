package co.alarconq.websecurity.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig implements WebFluxConfigurer {

    private static final List<Locale> SUPPORTED_LOCALES = List.of(Locale.ENGLISH, new Locale("es"), new Locale("fr"));
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;


    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
    }

    @Bean
    public LocaleContextResolver localeContextResolver() {
        AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(List.of(Locale.ENGLISH, new Locale("es"), new Locale("fr")));
        return resolver;
    }

    @Bean
    public WebFilter localeChangeFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            String langParam = exchange.getRequest().getQueryParams().getFirst("lang");

            Locale selectedLocale = DEFAULT_LOCALE;
            if (langParam != null) {
                Locale requestedLocale = Locale.forLanguageTag(langParam);
                if (SUPPORTED_LOCALES.contains(requestedLocale)) {
                    selectedLocale = requestedLocale;
                }
            }

            // Store the locale in request attributes
            exchange.getAttributes().put("locale", selectedLocale);

            return chain.filter(exchange);
        };
    }
}
