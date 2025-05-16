package ms.inditex.adapters.output.persistence.repositories;

import ms.inditex.adapters.output.persistence.entities.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceEntityRepository extends JpaRepository<PriceEntity, Long> {

    @Query(
            value = """
                    SELECT * FROM PRICES p
                    WHERE p.PRODUCT_ID      = :productId
                      AND p.BRAND_ID        = :brandId
                      AND :applicationDate BETWEEN p.START_DATE AND p.END_DATE
                    ORDER BY p.PRIORITY DESC
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    Optional<PriceEntity> findTopByCriteria(LocalDateTime applicationDate, Long productId, Integer brandId);
}