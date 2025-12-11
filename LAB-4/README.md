# University – Cucumber

Aplikacja testowa w **Java 21 + Gradle 8.14.3 + Selenium 4.39.0**, **Cucumber 7.33.0**, zawierająca testy E2E dla serwisów **PJATK** (`https://pja.edu.pl/en/`) oraz **Sklep PJATK** (`https://sklep.pja.edu.pl/`). Projekt wykorzystuje **JUnit 6**, **Selenium Manager** (bez bibliotek zewnętrznych), **Cucumber** i wzorzec **Page Object Pattern**.

---

## Scenariusze

### PJATK Website – wyszukiwanie informacji

**Cel:** Zweryfikować funkcjonalność wyszukiwarki na stronie głównej PJATK.

**Kroki:**

1. Otwórz stronę główną `https://pja.edu.pl/en/`.
2. Zaakceptuj pliki cookies (jeśli pojawi się baner).
3. Wyszukaj frazę **"test"**.
4. Zweryfikuj, że strona wyników zawiera listę wyników (co najmniej jeden rezultat).
5. Otwórz pierwszy wynik wyszukiwania.

**Oczekiwany rezultat:** Strona wyników poprawnie wyświetla wyniki wyszukiwania dla frazy "test", a pierwszy wynik otwiera poprawną podstronę.

### PJATK Store – dodawanie produktu do koszyka

**Cel:** Zweryfikować działanie wyszukiwarki oraz procesu dodawania produktu do koszyka w sklepie internetowym PJATK.

**Kroki:**

1. Otwórz stronę główną sklepu `https://sklep.pja.edu.pl/`.
2. Wyszukaj produkt o nazwie **"karafka"**.
3. Zweryfikuj, że lista wyników wyszukiwania zawiera produkty.
4. Otwórz pierwszy produkt z wyników.
5. Dodaj produkt do koszyka.
6. Otwórz stronę koszyka.
7. Zweryfikuj, że w koszyku znajduje się dokładnie jeden produkt.

**Oczekiwany rezultat:** Wyszukiwarka sklepu działa poprawnie, produkt "karafka" może zostać dodany do koszyka, a koszyk poprawnie pokazuje dodany produkt.

---

## Scenariusze - Cucumber (Gherkin)

### `pja_search.feature`

```gherkin
# language: en
Feature: Searching for information on the PJATK website

    As a PJATK website user
    I want to search for information using the search bar
    So that I can find content related to a given phrase

    Scenario: PJATK Website – searching and opening the first result
        Given I open the PJATK homepage
        And I accept cookies - if visible
        When I search for the phrase "test" on the PJATK website
        Then I should see at least one search result
        And I open the first search result
```

### `pja_store.feature`

```gherkin
# language: en
Feature: Adding a product to the PJATK store cart

    As a PJATK store customer
    I want to search for a product and add it to the cart
    So that I can verify that the purchasing process works correctly

    Scenario: PJATK Store – searching, opening a product and adding it to the cart
        Given I open the PJATK store homepage
        When I search for the product "karafka" in the PJATK store
        Then I should see at least one search result in the store
        When I open the first product from the search results in the store
        And I add the product to the cart
        And I open the cart page
        Then the cart should contain exactly 1 product
```

---

## Testy

Projekt zawiera dwa kompletne testy E2E:

* `PjaSearchSteps` – test wyszukiwarki strony głównej PJATK.
* `PjaStoreSteps` – test wyszukiwania produktu i dodania go do koszyka w sklepie PJATK.

Każdy test można uruchomić w co najmniej dwóch przeglądarkach: **Chrome** i **Firefox**.

---

## Uruchomienie

```bash
./gradlew test -Dbrowser=chrome -Dheadless=true && ./gradlew test -Dbrowser=firefox -Dheadless=true
```
