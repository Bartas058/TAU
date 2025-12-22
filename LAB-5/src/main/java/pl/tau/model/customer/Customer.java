package pl.tau.model.customer;

import java.util.UUID;

public record Customer(

        UUID id,

        String email,

        String fullName,

        CustomerStatus status
) { }
