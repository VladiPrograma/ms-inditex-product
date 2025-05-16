package ms.inditex.usecases;

import ms.inditex.entities.Price;
import ms.inditex.exeptions.AppException;

import java.time.LocalDateTime;

public interface PriceUseCases {

    Price getPrice(LocalDateTime applicationDate, Long productId, Integer brandId) throws AppException;

}
