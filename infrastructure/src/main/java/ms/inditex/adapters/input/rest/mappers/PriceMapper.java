package ms.inditex.adapters.input.rest.mappers;

import ms.inditex.adapters.input.rest.apis.model.PriceDto;
import ms.inditex.entities.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDto toDto(Price request);
}
