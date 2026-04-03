package car.rental.app.payment.adapter.out.provider;


import car.rental.app.payment.application.service.MetricsService;
import car.rental.app.payment.domain.model.Money;
import car.rental.app.payment.domain.model.Payment;
import car.rental.app.payment.domain.port.out.PaymentProvider;
import car.rental.app.payment.domain.port.out.PaymentProviderResult;
import car.rental.app.payment.domain.port.out.RefundProviderResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class StripePaymentProviderAdapter implements PaymentProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MetricsService metricsService;


    @Value("${stripe.secret-key}")
    private String secretKey;

    @Autowired
    public StripePaymentProviderAdapter(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 500, multiplier = 2.0)
    )
    public PaymentProviderResult charge(Payment payment) {

        Instant start = Instant.now(); // start pomiaru czasu

        String url = "https://api.stripe.com/v1/payment_intents";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("amount", convertAmount(payment.amount()));
        body.put("currency", payment.amount().currency().getCurrencyCode().toLowerCase());
        body.put("payment_method_types[]", "card");
        body.put("metadata[reservationId]", payment.reservationId());
        body.put("metadata[paymentId]", payment.id());

        String requestPayload = body.toString();

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String responsePayload = response.getBody();

            JsonNode json = objectMapper.readTree(responsePayload);
            String status = json.get("status").asText();
            String providerId = json.get("providerPaymentId").asText();

            PaymentProviderResult result;

            if ("succeeded".equals(status) || "requires_confirmation".equals(status)) {
                result = PaymentProviderResult.ok(providerId,requestPayload, responsePayload);
            } else {
                String declineReason = json.has("last_payment_error")
                        ? json.get("last_payment_error").get("message").asText()
                        : "Payment declined";

                result = PaymentProviderResult.failed(declineReason, requestPayload, responsePayload);
            }

            metricsService.recordProviderCall(payment, "stripe", start, result);

            return result;

        } catch (Exception ex) {

            metricsService.recordProviderException(payment, "stripe");

            throw new StripeException("Stripe API error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public RefundProviderResult refund(Payment payment) {

        Instant start = Instant.now();

        String url = "https://api.stripe.com/v1/refunds";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("payment_intent", payment.providerPaymentId()); // ID payment_intent z charge()
        body.put("metadata[paymentId]", payment.id());
        body.put("metadata[reservationId]", payment.reservationId());

        String requestPayload = body.toString();

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            String responsePayload = response.getBody();

            JsonNode json = objectMapper.readTree(responsePayload);
            String status = json.get("status").asText();

            RefundProviderResult result;

            if ("succeeded".equals(status)) {
                result = RefundProviderResult.ok(requestPayload, responsePayload);
            } else {
                String reason = json.has("failure_reason")
                        ? json.get("failure_reason").asText()
                        : "Refund failed";

                result = RefundProviderResult.failed(reason, requestPayload, responsePayload);
            }

            metricsService.recordProviderCall(payment, "stripe_refund", start, result);

            return result;

        } catch (Exception ex) {

            metricsService.recordProviderException(payment, "stripe_refund");

            throw new StripeException("Stripe refund error: " + ex.getMessage(), ex);
        }
    }

    private String convertAmount(Money money) {
        int fractionDigits = money.currency().getDefaultFractionDigits();

        BigDecimal multiplier = BigDecimal.TEN.pow(fractionDigits);
        BigDecimal scaled = money.amount().multiply(multiplier);

        return scaled.toBigIntegerExact().toString();
    }
}