package ms.inditex;

import ms.inditex.adapters.input.rest.PriceInputAdapter;
import ms.inditex.adapters.input.rest.apis.model.PriceDto;
import ms.inditex.adapters.input.rest.mappers.PriceMapper;
import ms.inditex.entities.Price;
import ms.inditex.usecases.PriceUseCases;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PriceInputAdapter.class)
@ContextConfiguration(classes = ProductPricingApplication.class)
class PriceInputAdapterTest {

    private static final Integer BRAND_ID = 1;
    private static final Long PRODUCT_ID = 35455l;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PriceUseCases priceUseCases;
    @MockBean
    private PriceMapper priceMapper;

    private void mockUseCase(LocalDateTime date, Integer priceList, BigDecimal price) {
        Price mockPrice = Price.builder().build();
        mockPrice.setBrandId(BRAND_ID);
        mockPrice.setProductId(PRODUCT_ID);
        mockPrice.setPriceList(priceList);
        mockPrice.setStartDate(date.minusHours(1));
        mockPrice.setEndDate(date.plusHours(1));
        mockPrice.setPrice(price);
        mockPrice.setCurrency(Currency.getInstance("EUR"));

        PriceDto dto = PriceDto.builder().build();
        dto.setBrandId(BRAND_ID);
        dto.setProductId(PRODUCT_ID);
        dto.setPriceList(priceList);
        dto.setStartDate(mockPrice.getStartDate());
        dto.setEndDate(mockPrice.getEndDate());
        dto.setPrice(price.doubleValue());
        dto.setCurrency("EUR");

        when(priceUseCases.getPrice(date, PRODUCT_ID, BRAND_ID)).thenReturn(mockPrice);
        when(priceMapper.toDto(mockPrice)).thenReturn(dto);
    }

    @Test
    @DisplayName("Test 1: 2020-06-14 10:00")
    void test1() throws Exception {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        mockUseCase(date, 1, new BigDecimal("35.50"));

        mockMvc.perform(get("/prices")
                        .param("applicationDate", date.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Test 2: 2020-06-14 16:00")
    void test2() throws Exception {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        mockUseCase(date, 2, new BigDecimal("25.45"));

        mockMvc.perform(get("/prices")
                        .param("applicationDate", date.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    @DisplayName("Test 3: 2020-06-14 21:00")
    void test3() throws Exception {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 21, 0);
        mockUseCase(date, 1, new BigDecimal("35.50"));

        mockMvc.perform(get("/prices")
                        .param("applicationDate", date.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Test 4: 2020-06-15 10:00")
    void test4() throws Exception {
        LocalDateTime date = LocalDateTime.of(2020, 6, 15, 10, 0);
        mockUseCase(date, 3, new BigDecimal("30.50"));

        mockMvc.perform(get("/prices")
                        .param("applicationDate", date.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    @DisplayName("Test 5: 2020-06-16 21:00")
    void test5() throws Exception {
        LocalDateTime date = LocalDateTime.of(2020, 6, 16, 21, 0);
        mockUseCase(date, 4, new BigDecimal("38.95"));

        mockMvc.perform(get("/prices")
                        .param("applicationDate", date.toString())
                        .param("productId", PRODUCT_ID.toString())
                        .param("brandId", BRAND_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95));
    }
}
