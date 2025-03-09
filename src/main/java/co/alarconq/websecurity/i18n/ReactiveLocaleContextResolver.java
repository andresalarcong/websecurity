package co.alarconq.websecurity.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReactiveLocaleContextResolver implements LocaleContextResolver {

    public static final String LOCALE_CONTEXT_ATTRIBUTE = ReactiveLocaleContextResolver.class.getName() + ".LOCALE_CONTEXT";

    private Locale defaultLocale = Locale.getDefault();
    private List<Locale> supportedLocales = new ArrayList<>();

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        LocaleContext localeContext = (LocaleContext) exchange.getAttribute(LOCALE_CONTEXT_ATTRIBUTE);
        if (localeContext != null) {
            return localeContext;
        }
        return new SimpleLocaleContext(defaultLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        exchange.getAttributes().put(LOCALE_CONTEXT_ATTRIBUTE, localeContext);
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public void setSupportedLocales(List<Locale> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    public List<Locale> getSupportedLocales() {
        return this.supportedLocales;
    }
}

