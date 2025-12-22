# Payment

Aplikacja testowa w **Java 21 + Gradle 8.14.3 + WireMock 3.13.2**, symulująca integrację z zewnętrznym REST API procesora płatności. Aplikacja nie implementuje rzeczywistego backendu, lecz opiera się na atrapach (stubach) odpowiedzi HTTP, serwowanych w testach przy użyciu WireMock.

---

## Endpointy

### 1) GET /api/v1/payments

Zwraca listę płatności.

### Response 200

```json
[
  {
    "id": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
    "amount": 100.00,
    "currency": "PLN",
    "status": "PENDING",
    "customerId": "2ea68767-1771-429b-9747-172418278ef0",
    "createdAt": "2025-12-20T10:15:30Z"
  },
  {
    "id": "89839cd7-266b-4de4-96ba-11f40fc327b9",
    "amount": 49.99,
    "currency": "EUR",
    "status": "CONFIRMED",
    "customerId": "fce37fbd-1044-4b1b-9d0f-556d1bfe29ae",
    "createdAt": "2025-12-20T11:00:00Z"
  },
  {
    "id": "3473a088-adc9-46df-98da-2f3fbc248115",
    "amount": 250.00,
    "currency": "USD",
    "status": "CANCELLED",
    "customerId": "2ea68767-1771-429b-9747-172418278ef0",
    "createdAt": "2025-12-21T08:05:00Z"
  }
]
```

### 2) GET /api/v1/payments/{paymentId}

Zwraca płatność o podanym Payment ID.

### Response 200

```json
{
  "id": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
  "amount": 100.00,
  "currency": "PLN",
  "status": "PENDING",
  "customerId": "2ea68767-1771-429b-9747-172418278ef0",
  "createdAt": "2025-12-20T10:15:30Z"
}
```

### 3) POST /api/v1/payments

Tworzy płatność.

### Request

```json
{
  "customerId": "2ea68767-1771-429b-9747-172418278ef0",
  "amount": 100.00,
  "currency": "PLN"
}
```

### Response 201

```json
{
  "id": "fdf507fb-318e-4ddf-b06c-93aa77032e00",
  "amount": 100.00,
  "currency": "PLN",
  "status": "PENDING",
  "customerId": "2ea68767-1771-429b-9747-172418278ef0",
  "createdAt": "2025-12-21T12:00:00Z"
}
```

### 4) POST /api/v1/payments/{paymentId}/confirm

Potwierdza płatność (np. po autoryzacji).

### Response 200

```json
{
  "id": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
  "amount": 100.00,
  "currency": "PLN",
  "status": "CONFIRMED",
  "customerId": "2ea68767-1771-429b-9747-172418278ef0",
  "createdAt": "2025-12-20T10:15:30Z"
}
```

### 5) POST /api/v1/payments/{paymentId}/cancel

Anuluje płatność.

### Response 200

```json
{
  "id": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
  "amount": 100.00,
  "currency": "PLN",
  "status": "CANCELLED",
  "customerId": "2ea68767-1771-429b-9747-172418278ef0",
  "createdAt": "2025-12-20T10:15:30Z"
}
```

### 6) GET /api/v1/transactions

Zwraca listę transakcji.

### Response 200

```json
[
  {
    "id": "0c184804-3a90-48cf-8576-08a7d48aedca",
    "paymentId": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
    "amount": 100.00,
    "currency": "PLN",
    "type": "AUTH",
    "status": "APPROVED",
    "createdAt": "2025-12-20T10:15:35Z"
  },
  {
    "id": "3a7bdb4a-4e39-41c8-8454-b7f56cd49941",
    "paymentId": "89839cd7-266b-4de4-96ba-11f40fc327b9",
    "amount": 49.99,
    "currency": "EUR",
    "type": "CAPTURE",
    "status": "SETTLED",
    "createdAt": "2025-12-20T11:00:10Z"
  },
  {
    "id": "82ff08ba-6b1c-4fd7-b155-e251cedda330",
    "paymentId": "3473a088-adc9-46df-98da-2f3fbc248115",
    "amount": 250.00,
    "currency": "USD",
    "type": "AUTH",
    "status": "DECLINED",
    "createdAt": "2025-12-21T08:05:10Z"
  }
]
```

### 7) GET /api/v1/transactions/{transactionId}

Zwraca transakcję po Transaction ID.

### Response 200

```json
{
  "id": "0c184804-3a90-48cf-8576-08a7d48aedca",
  "paymentId": "0ab77819-f257-49cb-96d0-a6cf9b6fed26",
  "amount": 100.00,
  "currency": "PLN",
  "type": "AUTH",
  "status": "APPROVED",
  "createdAt": "2025-12-20T10:15:35Z"
}
```

### 8) POST /api/v1/refunds

Tworzy zwrot środków do płatności.

### Request

```json
{
  "paymentId": "89839cd7-266b-4de4-96ba-11f40fc327b9",
  "amount": 49.99,
  "currency": "EUR"
}
```

### Response 201

```json
{
  "id": "6545302d-0f81-49ce-851e-690386c72a91",
  "paymentId": "89839cd7-266b-4de4-96ba-11f40fc327b9",
  "amount": 49.99,
  "currency": "EUR",
  "status": "CREATED",
  "createdAt": "2025-12-21T12:30:00Z"
}
```

### 9) GET /api/v1/customers

Zwraca listę klientów.

### Response 200

```json
[
  {
    "id": "2ea68767-1771-429b-9747-172418278ef0",
    "email": "oliwia@example.com",
    "fullName": "Oliwia Nowak",
    "status": "ACTIVE"
  },
  {
    "id": "fce37fbd-1044-4b1b-9d0f-556d1bfe29ae",
    "email": "klaudia@example.com",
    "fullName": "Klaudia Kowalska",
    "status": "ACTIVE"
  },
  {
    "id": "23f30312-bbfc-41ac-bb40-94e2a83debb0",
    "email": "ewelina@example.com",
    "fullName": "Ewelina Zielińska",
    "status": "SUSPENDED"
  }
]
```

### 10) GET /api/v1/customers/{customerId}

Zwraca klienta po Customer ID.

### Response 200

```json
{
  "id": "2ea68767-1771-429b-9747-172418278ef0",
  "email": "alice@example.com",
  "fullName": "Alice Nowak",
  "status": "ACTIVE"
}
```

---

## Testy

Projekt zawiera komplet testów integracyjnych, w których symulowana jest komunikacja z zewnętrznym REST API procesora płatności.

* WireMock uruchamiany jest na losowym porcie (dynamicPort).
* Odpowiedzi serwera są serwowane z plików w katalogu.
* Testowana jest wyłącznie logika klienta REST, bez potrzeby uruchamiania rzeczywistego backendu ani integracji z zewnętrznymi systemami.

Każdy endpoint API posiada dedykowany test, a odpowiedzi HTTP są stubowane przy użyciu WireMock na podstawie statycznych plików JSON (fixtures).

---

## Uruchomienie

```bash
./gradlew test
```
