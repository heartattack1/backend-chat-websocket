# Project plan: Backend Chat WebSocket (актуализировано)

## 1. Требования из ТЗ (классификация)

### MUST (обязательные)
- Один общий чат.
- Ввод ника перед входом в чат.
- Пользователь видит последние **N** сообщений сразу после входа.
- Новые сообщения доставляются всем подключенным пользователям мгновенно (без refresh).
- Список пользователей онлайн.
- История чата хранится в БД.
- Java + сервер приложений (Spring Boot допустим).
- Исходники + собранный артефакт; минимальные инструкции по сборке/запуску/использованию.
- Не использовать готовые движки веб-чатов.

### SHOULD / NICE-TO-HAVE (плюсы)
- Docker-образ.
- Максимально использовать сторонние библиотеки (open-source).
- Авторизация через отечественную соцсеть.
- Hibernate/JPA для БД.
- Доставка обновлений без постоянного refresh (WebSocket/SSE/long-poll и т.п.).

### Нефункциональные требования
- Сборка и запуск должны быть воспроизводимы.
- Описание как собрать, запускать и пользоваться.

---

## 2. Текущее состояние репозитория (кратко)

- **Стек:** Spring Boot + WebSocket/STOMP + SockJS, Spring Data JPA, Liquibase, PostgreSQL.
- **WebSocket:** endpoint `/ws`, STOMP broker `/topic` и `/queue`, application destinations `/app`.
- **REST/OpenAPI:** `GET /api/ping`, `GET /api/chat/history`, `GET /api/chat/users`.
- **Контракты WebSocket:**
  - отправка: `/app/chat.send`, payload `{ text }`, header `author` (опционально);
  - сообщения: `/topic/chat.messages`, payload `{ id, authorId, authorName, text, createdAt }`;
  - онлайн-пользователи: `/topic/chat.users`, payload `["alice", "bob"]` (список).
- **Username/presence:** `username`/`author` headers, fallback `guest-<session>`.
- **Persistence:** JPA-сущности и репозитории для сообщений; миграции Liquibase.
- **Presence:** in-memory реестр пользователей + broadcast `/topic/chat.users`.
- **Docker:** присутствуют `Dockerfile` и `docker-compose.yml`.
- **UI:** статический `src/main/resources/static/index.html` подключен к WebSocket и REST контрактам, есть ввод ника и отображение истории/онлайна.

### Что уже реализовано (подтверждающие пути)
- WebSocket конфигурация: `src/main/java/.../adapters/inbound/websocket/WebSocketConfig.java`.
- WebSocket controller: `src/main/java/.../adapters/inbound/websocket/ChatWebSocketController.java`.
- Сообщения из WebSocket **сохраняются** через `PostChatMessageScenario` → `PostMessageScenario`.
- История сообщений: `GetChatHistoryScenario`, репозиторий `ChatMessageRepository`.
- REST историю/онлайн: `ChatController`, OpenAPI `src/main/resources/openapi.yaml`.
- Presence/онлайн: `OnlineUserRegistry`, `WebSocketSessionEventListener`, broadcaster `/topic/chat.users`.
- Liquibase миграции БД: `src/main/resources/db/changelog/**`.
- Docker артефакты: `Dockerfile`, `docker-compose.yml`.
- Базовое описание: `README.md`.

### Зазоры/недостающие компоненты
- Соц. авторизация (опционально).

---

## 3. Таблица соответствия ТЗ → статус

| Требование | Статус | Доказательство (пути) | Комментарий |
|---|---|---|---|
| Один общий чат | Done | `ChatWebSocketController`, `WebSocketConfig`, `src/main/resources/static/index.html` | Канал подключен к UI. |
| Ввод ника перед входом | Done | `UsernameResolver`, `ChatWebSocketController`, `src/main/resources/static/index.html` | UI передает заголовок `username` и использует его для сессии. |
| Последние N сообщений на входе | Done (REST) | `GetChatHistoryScenario`, `ChatController`, OpenAPI | Возвращается через `GET /api/chat/history`. |
| Мгновенная доставка сообщений без refresh | Done | `WebSocketConfig`, `ChatWebSocketController` | WebSocket/STOMP работает. |
| Список пользователей онлайн | Done | `OnlineUserRegistry`, `WebSocketSessionEventListener` | Есть REST и broadcast. |
| История чата в БД | Done | `PostMessageScenario`, JPA + Liquibase | WS-поток сохраняет сообщения. |
| Java + сервер | Done | Spring Boot project structure | Ок. |
| Исходники + артефакт + инструкции | Done | `README.md`, `Dockerfile`, `docker-compose.yml` | Добавлены инструкции по использованию. |
| Не использовать готовые webchat движки | Done | — | В проекте не обнаружено. |
| Docker образ | Done | `Dockerfile`, `docker-compose.yml` | Уже есть. |
| Hibernate/JPA | Done | JPA репозитории/сущности | Уже есть. |
| Соц. авторизация (отечественная) | Not started | — | Опционально. |

---

## 4. План работ (оставшиеся части)

- **Соц. авторизация (опционально):** если потребуется, выбрать провайдера и добавить OAuth2 flow.

---

## 5. Архитектурные решения (предлагаемые)

1. **Транспорт обновлений:** WebSocket + STOMP (уже есть). Альтернатива SSE не требуется.
2. **Модель данных:** `ChatMessage(id, authorUserId/authorName, text, createdAt)`.
3. **История сообщений:** запрос `last N` из БД, сортировка ASC для отображения.
4. **Presence:** in-memory реестр online пользователей, обновляемый по событиям connect/disconnect.
5. **Пагинация:** ограниченный лимит `N` (например, 50/100 по умолчанию).
6. **Конкурентность/транзакции:** сохранение сообщений через JPA, транзакция на запись.
7. **Идентификаторы:** единая стратегия ID (ULID или UUID) для домена и сообщений события.

---

## 6. Инструкции по сборке/запуску/использованию (скелет)

- **Сборка:** Gradle (`./gradlew build`), Java 17+.
- **Запуск локально:** Postgres доступен по `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (см. `application.yml`).
- **Запуск через Docker:** `docker compose up --build`.
- **Использование:** открыть `http://localhost:8080`, ввести ник, отправить сообщение, увидеть online list, получить историю последних N сообщений.

---

## 7. Опциональные улучшения (плюсы)

- **Docker-образ:** уже есть, актуализировать инструкции.
- **Hibernate/JPA:** уже используется; расширить по необходимости.
- **Соц. авторизация (отечественная):**
  - Варианты: VK ID, Gosuslugi/ЕСИА (при наличии SDK/доступов), Яндекс ID (не отечественная соцсеть, но провайдер).
  - Оценить реализацию через OAuth2 client.
- **Больше open-source библиотек:** STOMP клиент, UI-библиотека для минимального интерфейса.

---

## 8. Вопросы/риски

- Нужен ли REST-API для истории, или исключительно WS?
- Какой N по умолчанию?
- Требуется ли персистить никнейм в таблицу пользователей или достаточно transient presence?
- Какая соцсеть предпочтительна для авторизации?
