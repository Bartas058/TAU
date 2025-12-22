package pl.tau.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.tau.model.customer.Customer;
import pl.tau.model.payment.Payment;
import pl.tau.model.refund.Refund;
import pl.tau.model.transaction.Transaction;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

public class PaymentProcessorClient {

    private final URI baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PaymentProcessorClient(URI baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<Payment> listPayments() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/payments"))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public Payment getPayment(UUID paymentId) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/payments/" + paymentId))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Payment.class);
    }

    public Payment createPayment(UUID customerId) throws IOException, InterruptedException {
        var body = """
                {"customerId":"%s","amount":100.00,"currency":"PLN"}
                """.formatted(customerId);

        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/payments"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Payment.class);
    }

    public Payment confirmPayment(UUID paymentId) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/payments/" + paymentId + "/confirm"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Payment.class);
    }

    public Payment cancelPayment(UUID paymentId) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/payments/" + paymentId + "/cancel"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Payment.class);
    }

    public List<Transaction> listTransactions() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/transactions"))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public Transaction getTransaction(UUID transactionId) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/transactions/" + transactionId))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Transaction.class);
    }

    public Refund createRefund(UUID paymentId) throws IOException, InterruptedException {
        var body = """
                {"paymentId":"%s","amount":50.00,"currency":"PLN"}
                """.formatted(paymentId);

        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/refunds"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Refund.class);
    }

    public List<Customer> listCustomers() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/customers"))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public Customer getCustomer(UUID customerId) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUrl.resolve("/api/v1/customers/" + customerId))
                .GET()
                .header("Accept", "application/json")
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensure2xx(response);
        return objectMapper.readValue(response.body(), Customer.class);
    }

    private static void ensure2xx(HttpResponse<?> response) {
        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("HTTP " + statusCode + ": " + response.body());
        }
    }
}
