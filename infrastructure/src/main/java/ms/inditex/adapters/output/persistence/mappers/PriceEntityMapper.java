package ms.inditex.adapters.output.persistence.mappers;

import ms.inditex.adapters.output.persistence.entities.PriceEntity;
import ms.inditex.entities.Price;
import org.mapstruct.Mapper;

/**
 * Mapper to convert between PriceEntity and domain Price.
 */
@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

    PriceEntity toEntity(Price price);

    Price toDto(PriceEntity entity);

}
