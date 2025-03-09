package co.alarconq.websecurity.i18n;

import org.springframework.context.i18n.LocaleContext;

import java.util.Locale;

public class SimpleLocaleContext implements LocaleContext {
    private final Locale locale;

    public SimpleLocaleContext(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public String toString() {
        return "SimpleLocaleContext{locale=" + this.locale + '}';
    }
}