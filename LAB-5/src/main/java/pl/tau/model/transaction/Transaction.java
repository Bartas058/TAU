package pl.tau.model.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(

        UUID id,

        UUID paymentId,

        BigDecimal amount,

        TransactionCurrency currency,

        TransactionType type,

        TransactionStatus status,

        Instant createdAt
) { }
