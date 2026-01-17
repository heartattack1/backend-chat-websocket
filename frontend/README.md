# Frontend (статическая страница чата)

Фронтенд реализован как один статический файл `src/main/resources/static/index.html`, который раздаётся самим Spring Boot приложением. Отдельной сборки, npm-зависимостей и bundler'а нет.

## Что внутри

- **HTML/CSS/JS:** всё инлайн в `index.html`.
- **Зависимости через CDN:**
  - `sockjs-client@1`
  - `stompjs@2.3.3`
- **Хранение ника:** `localStorage` (`chat_username`).

## Контракты с backend

### WebSocket/STOMP

- **Endpoint:** `/ws` (SockJS).
- **Отправка сообщений:** `/app/chat.send`
  - Payload: `{ "text": "..." }`
  - Header: `author` (опционально), если пусто — сервер использует fallback.
- **Подписки:**
  - `/topic/chat.messages` — новые сообщения `{ id, authorId, authorName, text, createdAt }`
  - `/topic/chat.users` — список онлайна `["alice", "bob"]`

### REST

- `GET /api/chat/history?limit=50` — история сообщений (по умолчанию 50, максимум 100).
- `GET /api/chat/users` — текущий список онлайн-пользователей.

## Пользовательский сценарий

1. При первом входе отображается форма для ввода ника.
2. Ник сохраняется в `localStorage` и отправляется в header `username` при WebSocket handshake.
3. После подключения:
   - загружается история сообщений через REST,
   - подписка на сообщения и онлайн пользователей обновляется через WS.

## Как открыть

1. Запустите backend (`docker compose up --build` или `./gradlew bootRun`).
2. Откройте страницу `http://localhost:8080`.
