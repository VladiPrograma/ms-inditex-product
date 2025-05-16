package ms.inditex.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.inditex.adapters.output.persistence.entities.PriceEntity;
import ms.inditex.adapters.output.persistence.exceptions.EntityNotFoundException;
import ms.inditex.adapters.output.persistence.mappers.PriceEntityMapper;
import ms.inditex.adapters.output.persistence.repositories.PriceEntityRepository;
import ms.inditex.entities.Price;
import ms.inditex.ports.output.PriceOutputPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class PriceOutputAdapter implements PriceOutputPort {

    private final PriceEntityRepository priceEntityRepository;

    private final PriceEntityMapper priceEntityMapper;

    @Override
    public Price getPrice(LocalDateTime applicationDate, Long productId, Integer brandId) {
        PriceEntity priceEntity = priceEntityRepository.findTopByCriteria(applicationDate, productId, brandId)
                .orElseThrow(() -> new EntityNotFoundException(productId));
        return priceEntityMapper.toDto(priceEntity);
    }
}
