package ms.inditex.config;

import lombok.RequiredArgsConstructor;
import ms.inditex.ports.input.PriceInputPort;
import ms.inditex.ports.output.PriceOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    PriceInputPort priceInputPort(PriceOutputPort priceOutputPort) {
        return new PriceInputPort(priceOutputPort);
    }
}
