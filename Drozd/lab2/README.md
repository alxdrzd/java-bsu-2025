### Иван! Представляю решение 2-ой (второй) лабораторной работы по курсу "Промышленное программирование" Белорусский государственный университет факультет приклалной математики и информатики 1-ый (первый) поток (информатика) 2-ой (второй) курс 1-ая (первая) группа ###
<img width="960" height="1280" alt="image" src="https://github.com/user-attachments/assets/1a014155-ceeb-42b6-b06a-b505987313d8" />

## Ответ на вопросы ##

1) __Was ist das UUID?__ A UUID, or Universally Unique Identifier, is a 128-bit number used to uniquely identify information in computer systems. It is represented as a 36-character hexadecimal string (e.g., 123e4567-e89b-12d3-a456-426614174000). UUIDs are used to assign unique identities without a central coordinator, making them useful in distributed systems where a collision is extremely unlikely.
2) Реализация принципов SOLID
*   **S — Single Responsibility (Единственная ответственность):**
    *   `Account` — только данные и состояние счета.
    *   `TransactionService` — только оркестрация операций.
    *   `JDBCAccountRepository` — только SQL-запросы.
*   **O — Open/Closed (Открытость/Закрытость):**
    *   Реализован через паттерн **Стратегия**. Новые типы транзакций добавляются созданием нового класса `NewStrategy`, не изменяя код сервиса.
*   **L — Liskov Substitution (Принцип подстановки):**
    *   Интерфейс `AccountRepository` имеет реализации `InMemory...` и `JDBC...`. Они полностью взаимозаменяемы без нарушения логики работы сервиса.
*   **I — Interface Segregation (Разделение интерфейса):**
    *   Интерфейс `TransactionListener` содержит только необходимые методы (`onSuccess`, `onFailure`), не перегружая клиентов (UI) лишними зависимостями.
*   **D — Dependency Inversion (Инверсия зависимостей):**
    *   `TransactionService` зависит от абстракции (`AccountRepository`), а не от конкретной реализации базы данных. Зависимость внедряется через конструктор.

3) Использованные паттерны проектирования

*   **STRATEGY (Стратегия):**
    *   Используется для инкапсуляции алгоритмов транзакций (`DepositStrategy`, `TransferStrategy`, `FreezeStrategy`). Сервис выбирает алгоритм в зависимости от типа операции.
*   **FACTORY (Фабрика):**
    *   `TransactionFactory` упрощает создание объектов `Transaction`, скрывая генерацию UUID и timestamp.
*   **OBSERVER (Наблюдатель):**
    *   Реализован через интерфейс `TransactionListener`. UI подписывается на события сервиса и асинхронно получает уведомления о статусе операций.
*   **SINGLETON (Одиночка):**
    *   `InMemoryAccountRepository` и `InMemoryUserRepository` существуют в единственном экземпляре, гарантируя целостность данных в памяти.
*   **COMMAND (Команда):**
    *   Объект `Transaction` инкапсулирует параметры запроса, позволяя передавать его между слоями и обрабатывать отложенно.

4) Асинхронность и Concurrency

1.  **Асинхронная обработка:**
    *   Используется `ExecutorService` (Thread Pool) в `TransactionService`. Тяжелые операции выполняются в фоновых потоках, не блокируя основной интерфейс (UI).
2.  **Потокобезопасность (Thread Safety):**
    *   **`ReentrantLock`:** Используется в классе `Account` для блокировки баланса при изменении. Это гарантирует атомарность (никто не спишет деньги, пока идет пополнение).
    *   **`ConcurrentHashMap`:** Используется в репозиториях для безопасного доступа к данным из множества потоков.
3.  **Предотвращение Deadlock (Взаимных блокировок):**
    *   В `TransferStrategy` реализован алгоритм сортировки блокировок. Счета всегда блокируются в порядке возрастания их UUID, что математически исключает возможность возникновения deadlock при встречных переводах.
