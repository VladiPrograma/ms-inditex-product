package ms.inditex.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.inditex.adapters.input.rest.apis.PriceApi;
import ms.inditex.adapters.input.rest.apis.model.PriceDto;
import ms.inditex.adapters.input.rest.mappers.PriceMapper;
import ms.inditex.entities.Price;
import ms.inditex.usecases.PriceUseCases;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PriceInputAdapter implements PriceApi {

    private final PriceUseCases priceUseCases;
    private final PriceMapper priceMapper;

    @Override
    public PriceDto getPrice(LocalDateTime applicationDate, Long productId, Integer brandId) {
        log.info("Retrieving price for productId={}, brandId={}, applicationDate={}",
                productId, brandId, applicationDate);
        Price price = priceUseCases.getPrice(applicationDate, productId, brandId);
        return priceMapper.toDto(price);
    }
}
