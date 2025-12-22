package pl.tau.model.refund;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Refund(

        UUID id,

        UUID paymentId,

        BigDecimal amount,

        RefundCurrency currency,

        RefundStatus status,

        Instant createdAt
) { }
