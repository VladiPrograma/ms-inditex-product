package ms.inditex.ports.input;

import lombok.RequiredArgsConstructor;
import ms.inditex.entities.Price;
import ms.inditex.exeptions.AppException;
import ms.inditex.ports.output.PriceOutputPort;
import ms.inditex.usecases.PriceUseCases;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PriceInputPort implements PriceUseCases {

    private final PriceOutputPort outputPort;

    @Override
    public Price getPrice(LocalDateTime applicationDate, Long productId, Integer brandId) throws AppException {
        return outputPort.getPrice(applicationDate, productId, brandId);
    }
}
