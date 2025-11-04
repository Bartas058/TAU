# University

Aplikacja testowa w **Java 21 + Gradle 8.14.3 + Selenium 4.38.0**, zawierająca testy E2E dla serwisów **PJATK** (`https://pja.edu.pl/en/`) oraz **Sklep PJATK** (`https://sklep.pja.edu.pl/`). Projekt wykorzystuje **JUnit 6**, **Selenium Manager** (bez bibliotek zewnętrznych) i wzorzec **Page Object Pattern**.

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

## Testy

Projekt zawiera dwa kompletne testy E2E:

* `PjaSearchTest` – test wyszukiwarki strony głównej PJATK.
* `PjaStoreTest` – test wyszukiwania produktu i dodania go do koszyka w sklepie PJATK.

Każdy test można uruchomić w co najmniej dwóch przeglądarkach: **Chrome** i **Firefox**.

---

## Uruchomienie

```bash
./gradlew test -Dbrowser=chrome -Dheadless=true && ./gradlew test -Dbrowser=firefox -Dheadless=true
```
