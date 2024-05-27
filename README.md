# 2. Gra Wisielec

## Spis treści
1. [Opis projektu](#opis-projektu)
2. [Funkcjonalności](#funkcjonalności)
3. [Instrukcja obsługi](#instrukcja-obsługi)
4. [Kod źródłowy](#kod-źródłowy)
5. [Przykładowe dane wejściowe](#przykładowe-dane-wejściowe)

## Opis projektu

Projekt "Gra Wisielec" jest implementacją popularnej gry słownej, w której użytkownik próbuje odgadnąć słowo poprzez zgadywanie pojedynczych liter. Aplikacja została napisana w języku Java przy użyciu biblioteki Swing do stworzenia graficznego interfejsu użytkownika oraz SQLite do zarządzania bazą danych słów i wyników.

## Funkcjonalności

- **Wybór losowego słowa z bazy słów**: Gra wybiera losowe słowo do odgadnięcia na podstawie wybranego poziomu trudności.
- **Możliwość edytowania/dodawania bazy słów**: Użytkownik może dodawać nowe słowa, edytować istniejące oraz usuwać słowa z bazy danych.
- **Możliwość wyboru poziomu trudności**: Użytkownik może wybrać poziom trudności gry (łatwy, średni, trudny), co wpływa na liczbę dozwolonych błędnych prób.
- **Dostęp do statystyk**: Gra przechowuje wyniki (liczbę zwycięstw, porażek oraz całkowitą liczbę prób) i pozwala na ich przeglądanie.
- **Interfejs graficzny**: Gra posiada graficzny interfejs użytkownika stworzony przy użyciu Swing.

## Instrukcja obsługi

### Wymagania

- Java Development Kit (JDK) w wersji 8 lub nowszej.
- Biblioteka JDBC dla SQLite.

### Uruchomienie aplikacji

1. **Pobierz kod źródłowy**: Sklonuj repozytorium lub pobierz pliki projektu na swój komputer.

2. **Instalacja biblioteki JDBC dla SQLite**:
    - Pobierz `sqlite-jdbc.jar` z [Maven Central Repository](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc).
    - Dodaj `sqlite-jdbc.jar` do ścieżki klas projektu w IDE.

3. **Kompilacja i uruchomienie**:
    - Otwórz projekt w IDE (np. IntelliJ IDEA, Eclipse).
    - Skompiluj projekt i uruchom klasę `Main`.

### Pliki

- **Main.java**: Główna klasa uruchamiająca aplikację.
- **Game.java**: Klasa zawierająca logikę gry.
- **WordDatabase.java**: Klasa zarządzająca bazą danych SQLite.
- **Statistics.java**: Klasa do zarządzania statystykami gry.
- **README.md**: Dokumentacja projektu.
