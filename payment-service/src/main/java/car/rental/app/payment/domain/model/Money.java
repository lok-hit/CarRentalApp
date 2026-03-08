package car.rental.app.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    private BigDecimal amount;
    private Currency currency;
}
