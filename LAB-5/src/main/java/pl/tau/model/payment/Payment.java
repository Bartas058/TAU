package pl.tau.model.payment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(

        UUID id,

        BigDecimal amount,

        PaymentCurrency currency,

        PaymentStatus status,

        UUID customerId,

        Instant createdAt
) { }
