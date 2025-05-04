package ms.inditex.ports.output;

import ms.inditex.entities.Price;

import java.time.LocalDateTime;

public interface PriceOutputPort {

    Price getPrice(LocalDateTime applicationDate, Integer productId, Integer brandId);

}
