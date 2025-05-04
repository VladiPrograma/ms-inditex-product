package ms.inditex.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleContextResolver localeContextResolver() {
    AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }
}
