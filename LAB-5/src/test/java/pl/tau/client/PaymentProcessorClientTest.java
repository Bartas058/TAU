package pl.tau.client;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import pl.tau.model.payment.PaymentCurrency;
import pl.tau.model.payment.PaymentStatus;

import java.net.URI;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessorClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .dynamicPort()
                    .usingFilesUnderDirectory("src/test/resources/wiremock"))
            .build();

    private PaymentProcessorClient client() {
        return new PaymentProcessorClient(URI.create(wireMock.baseUrl()));
    }

    @Test
    void getPayments_shouldReturnList() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/payments"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("payments-list.json")));

        var payments = client().listPayments();
        assertEquals(3, payments.size());
        assertEquals(UUID.fromString("0ab77819-f257-49cb-96d0-a6cf9b6fed26"), payments.getFirst().id());
    }

    @Test
    void getPaymentById_shouldReturnPayment() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/payments/0ab77819-f257-49cb-96d0-a6cf9b6fed26"))
                .willReturn(okJsonFile("payment-0ab77819-f257-49cb-96d0-a6cf9b6fed26.json")));

        var payment = client().getPayment(UUID.fromString("0ab77819-f257-49cb-96d0-a6cf9b6fed26"));
        assertEquals(UUID.fromString("0ab77819-f257-49cb-96d0-a6cf9b6fed26"), payment.id());
        assertEquals(PaymentCurrency.PLN, payment.currency());
    }

    @Test
    void createPayment_shouldReturnCreatedPayment() throws Exception {
        wireMock.stubFor(post(urlEqualTo("/api/v1/payments"))
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("payment-created.json")));

        var created = client().createPayment(UUID.fromString("fdf507fb-318e-4ddf-b06c-93aa77032e00"));
        assertNotNull(created.id());
    }

    @Test
    void confirmPayment_shouldReturnConfirmedPayment() throws Exception {
        wireMock.stubFor(post(urlEqualTo("/api/v1/payments/0ab77819-f257-49cb-96d0-a6cf9b6fed26/confirm"))
                .willReturn(okJsonFile("payment-confirmed.json")));

        var confirmed = client().confirmPayment(UUID.fromString("0ab77819-f257-49cb-96d0-a6cf9b6fed26"));
        assertEquals(PaymentStatus.CONFIRMED, confirmed.status());
    }

    @Test
    void cancelPayment_shouldReturnCancelledPayment() throws Exception {
        wireMock.stubFor(post(urlEqualTo("/api/v1/payments/0ab77819-f257-49cb-96d0-a6cf9b6fed26/cancel"))
                .willReturn(okJsonFile("payment-cancelled.json")));

        var cancelled = client().cancelPayment(UUID.fromString("0ab77819-f257-49cb-96d0-a6cf9b6fed26"));
        assertEquals(PaymentStatus.CANCELLED, cancelled.status());
    }

    @Test
    void listTransactions_shouldReturnTransactions() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/transactions"))
                .willReturn(okJsonFile("transactions-list.json")));

        var transactions = client().listTransactions();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransaction_shouldReturnTransaction() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/transactions/0c184804-3a90-48cf-8576-08a7d48aedca"))
                .willReturn(okJsonFile("transaction-0c184804-3a90-48cf-8576-08a7d48aedca.json")));

        var transaction = client().getTransaction(UUID.fromString("0c184804-3a90-48cf-8576-08a7d48aedca"));
        assertEquals(UUID.fromString("0c184804-3a90-48cf-8576-08a7d48aedca"), transaction.id());
    }

    @Test
    void createRefund_shouldReturnRefund() throws Exception {
        wireMock.stubFor(post(urlEqualTo("/api/v1/refunds"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("refund-created.json")));

        var refund = client().createRefund(UUID.fromString("6545302d-0f81-49ce-851e-690386c72a91"));
        assertNotNull(refund.id());
    }

    @Test
    void listCustomers_shouldReturnCustomers() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/customers"))
                .willReturn(okJsonFile("customers-list.json")));

        var customers = client().listCustomers();
        assertFalse(customers.isEmpty());
    }

    @Test
    void getCustomer_shouldReturnCustomer() throws Exception {
        wireMock.stubFor(get(urlEqualTo("/api/v1/customers/2ea68767-1771-429b-9747-172418278ef0"))
                .willReturn(okJsonFile("customer-2ea68767-1771-429b-9747-172418278ef0.json")));

        var customer = client().getCustomer(UUID.fromString("2ea68767-1771-429b-9747-172418278ef0"));
        assertEquals(UUID.fromString("2ea68767-1771-429b-9747-172418278ef0"), customer.id());
    }

    private static ResponseDefinitionBuilder okJsonFile(String file) {
        return aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile(file);
    }
}
