package car.rental.app.payment.application.service;

import car.rental.app.payment.domain.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

class IdempotencyKeyGeneratorTest {

    private final IdempotencyKeyGenerator generator = new IdempotencyKeyGenerator();

    @Test
    void generate_producesConsistentKey() {
        Money money = new Money(new BigDecimal("100.00"), Currency.getInstance("PLN"));
        String key1 = generator.generate("res-1", "cust-1", money);
        String key2 = generator.generate("res-1", "cust-1", money);

        assertThat(key1).isEqualTo(key2);
    }

    @Test
    void generate_differentiatesByReservationId() {
        Money money = new Money(new BigDecimal("100.00"), Currency.getInstance("PLN"));
        String key1 = generator.generate("res-1", "cust-1", money);
        String key2 = generator.generate("res-2", "cust-1", money);

        assertThat(key1).isNotEqualTo(key2);
    }

    @Test
    void generate_differentiatesByAmount() {
        String key1 = generator.generate("res-1", "cust-1",
                new Money(new BigDecimal("100.00"), Currency.getInstance("PLN")));
        String key2 = generator.generate("res-1", "cust-1",
                new Money(new BigDecimal("200.00"), Currency.getInstance("PLN")));

        assertThat(key1).isNotEqualTo(key2);
    }
}
