package ms.inditex.adapters.output.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.inditex.adapters.output.persistence.entities.PriceEntity;
import ms.inditex.adapters.output.persistence.exceptions.EntityNotFoundException;
import ms.inditex.adapters.output.persistence.mappers.PriceEntityMapper;
import ms.inditex.adapters.output.persistence.repositories.PriceEntityRepository;
import ms.inditex.entities.Price;

@ExtendWith(MockitoExtension.class)
class PriceOutputAdapterTest {

    @Mock
    private PriceEntityRepository priceEntityRepository;

    @Mock
    private PriceEntityMapper priceEntityMapper;

    @InjectMocks
    private PriceOutputAdapter priceOutputAdapter;

    private LocalDateTime applicationDate;
    private Long productId;
    private Integer brandId;

    @BeforeEach
    void setUp() {
        applicationDate = LocalDateTime.of(2025, 5, 15, 10, 30);
        productId     = 100L;
        brandId       = 1;
    }

    @Test
    void getPrice_ShouldReturnMappedPrice_WhenEntityIsFound() {
        PriceEntity entity = new PriceEntity();
        entity.setId(42L);
        entity.setBrandId(brandId);
        entity.setProductId(productId);
        entity.setStartDate(applicationDate.minusDays(1));
        entity.setEndDate(applicationDate.plusDays(1));
        entity.setPriceList(2);
        entity.setPriority(0);
        entity.setPrice(new BigDecimal("19.99"));
        entity.setCurrency(Currency.getInstance("EUR"));

        Price expectedDto = Price.builder()
                .id(42L)
                .brandId(brandId)
                .productId(productId)
                .startDate(applicationDate.minusDays(1))
                .endDate(applicationDate.plusDays(1))
                .priceList(2)
                .priority(0)
                .price(new BigDecimal("19.99"))
                .currency(Currency.getInstance("EUR"))
                .build();

        when(priceEntityRepository.findTopByCriteria(applicationDate, productId, brandId))
                .thenReturn(Optional.of(entity));
        when(priceEntityMapper.toDto(entity)).thenReturn(expectedDto);

        Price actual = priceOutputAdapter.getPrice(applicationDate, productId, brandId);

        assertEquals(expectedDto, actual, "Mapped Price should match the expected DTO");
        verify(priceEntityRepository).findTopByCriteria(applicationDate, productId, brandId);
        verify(priceEntityMapper).toDto(entity);
    }

    @Test
    void getPrice_ShouldThrowEntityNotFoundException_WhenEntityIsMissing() {
        when(priceEntityRepository.findTopByCriteria(applicationDate, productId, brandId))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> priceOutputAdapter.getPrice(applicationDate, productId, brandId),
                "Should throw EntityNotFoundException when no entity is found"
        );
        assertTrue(ex.getMessage().contains(productId.toString()));

        verify(priceEntityRepository).findTopByCriteria(applicationDate, productId, brandId);
        verifyNoInteractions(priceEntityMapper);
    }
}
