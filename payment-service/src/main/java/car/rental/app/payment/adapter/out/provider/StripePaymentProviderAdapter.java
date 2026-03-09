package car.rental.app.payment.adapter.out.provider;


import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class StripePaymentProviderAdapter implements PaymentProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2.0),
            include = { RuntimeException.class }
    )
    public boolean charge(Payment payment) {

        String url = "https://api.stripe.com/v1/payment_intents";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("amount", convertAmount(new Money(payment.amount().amount(), payment.amount().currency())));
        body.put("currency", payment.amount().currency().toString());
        body.put("payment_method_types[]", "card");
        body.put("metadata[reservationId]", payment.reservationId());
        body.put("metadata[paymentId]", payment.id());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode json = objectMapper.readTree(response.getBody());
                return "requires_confirmation".equals(json.get("status").asText())
                        || "succeeded".equals(json.get("status").asText());
            }

            return false;

        } catch (Exception ex) {
            throw new RuntimeException("Stripe API error: " + ex.getMessage(), ex);
        }
    }

    private String convertAmount(Money money) {
        BigDecimal multiplied = money.amount().multiply(BigDecimal.valueOf(100));
        return multiplied.toBigInteger().toString();
    }
}
