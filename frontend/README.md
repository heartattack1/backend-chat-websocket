# ui-chat-stopm (локально импортировано)

## Что внутри

- **Формат:** один статический `index.html` с инлайновыми CSS/JS. Сборщик не используется.
- **Зависимости:** подключаются через CDN
  - `sockjs-client@1`
  - `stompjs@2.3.3`
- **Точки входа:**
  - HTML: `src/main/resources/static/index.html`
  - JS: инлайн-скрипт внутри `index.html`
  - CSS: инлайн-стили внутри `index.html`

## Контракты текущего UI (как есть в index.html)

- **STOMP endpoint:** `/ws` (SockJS)
- **Публикация сообщений:** `/app/chat.send`
  - Сейчас отправляется payload `{ author, content, timestamp }`
- **Подписки:**
  - `/topic/chat`
  - `/topic/participants`
- **Авторизация:** в текущем UI есть логика JWT refresh, но **по договорённости использовать не будем**.

## Целевой контракт (без авторизации)

- **STOMP endpoint:** `/ws` (SockJS)
- **Публикация сообщений:** `/app/chat.send`
  - Payload `{ text }`
  - `author` передаётся через STOMP header `author`
- **Подписка на сообщения:** `/topic/chat.messages`
  - Payload `{ id, text, author, createdAt }`
- **Список онлайн:**
  - Начальная загрузка: REST `GET /api/chat/users`
  - Обновления: `/topic/chat.users` (полный список или diff — согласовать)

## Совмещение с backend-chat-websocket (что нужно привести к одному виду)

### Совпадения
- Endpoint `/ws` уже совпадает с серверной конфигурацией.

### Несовпадения
- **Топик сообщений:** UI подписывается на `/topic/chat`, backend публикует в `/topic/chat.messages`.
- **Формат входящего сообщения:** UI ожидает `{ author, content, timestamp }`, backend публикует `{ id, text, author, createdAt }`.
- **Payload при отправке:** UI отправляет `{ author, content, timestamp }`, backend ожидает `{ text }` и `author` в header.
- **Онлайн-участники:** UI подписывается на `/topic/participants`, backend должен публиковать `/topic/chat.users` и отдавать стартовый список через REST.
- **Авторизация:** UI содержит JWT-логику, но по решению задачи она должна быть удалена/отключена.

### План интеграции (предварительный)
1. **Сообщения:**
   - В UI заменить подписку на `/topic/chat.messages`.
   - В UI маппить `text -> content`, `createdAt -> timestamp`.
   - При отправке использовать payload `{ text }`, а `author` передавать через STOMP header `author`.
2. **История сообщений:** реализовать REST или WS команду под историю (ожидается реализация на backend).
3. **Онлайн-пользователи:**
   - Добавить REST `GET /api/chat/users` для первичного списка.
   - Подписаться на `/topic/chat.users` для обновлений.
4. **Авторизация:** удалить/отключить JWT refresh и `Authorization` header в UI.

## Как открыть локально

Открыть `http://localhost:8080` после запуска приложения через Docker Compose.
