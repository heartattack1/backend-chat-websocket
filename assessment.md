# Оценка соответствия ТЗ и best practices (чат)

## 1. Соответствие требованиям из `project.md`

Шкала: **0–5**, где **5** — полностью соответствует, **0** — нет реализации. Требования и формулировки — из `project.md`.【F:project.md†L3-L33】

| Требование (MUST/SHOULD) | Оценка | Статус | Доказательство в коде/проекте | Комментарий |
| --- | --- | --- | --- | --- |
| Один общий чат (MUST) | 5 | Реализовано | Сообщения принимаются через `/app/chat.send` и рассылаются в общий топик `/topic/chat.messages`.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/ChatWebSocketController.java†L16-L41】【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/websocket/ChatBroadcasterWebSocketAdapter.java†L1-L21】 | Единый канал сообщений без разбиения по комнатам. |
| Ввод ника перед входом (MUST) | 5 | Реализовано | UI просит ник и передает в STOMP header `username`; сервер читает header и использует для presence/автора.【F:frontend/index.html†L736-L931】【F:src/main/java/com/example/backendchatwebsocket/application/presence/UsernameResolver.java†L14-L55】 | Есть гостевой префикс, если имя не передано. |
| Последние N сообщений на входе (MUST) | 5 | Реализовано | REST `/api/chat/history` возвращает сообщения с лимитом (default 50, max 100).【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/rest/ChatHistoryApiImpl.java†L23-L53】【F:src/main/java/com/example/backendchatwebsocket/application/scenario/GetChatHistoryScenario.java†L1-L41】 | UI запрашивает историю при входе и отрисовывает.【F:frontend/index.html†L838-L941】 |
| Мгновенная доставка без refresh (MUST) | 5 | Реализовано | STOMP/WebSocket endpoint `/ws`, брокер `/topic`, подписки в UI на `/topic/chat.messages` и `/topic/chat.users`.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】【F:frontend/index.html†L771-L812】 | Реализовано через WebSocket/SockJS. |
| Список пользователей онлайн (MUST) | 4 | Реализовано | Реестр в памяти и broadcast списка при connect/disconnect; REST `/api/chat/users` возвращает список.【F:src/main/java/com/example/backendchatwebsocket/application/presence/OnlineUserRegistry.java†L1-L36】【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketSessionEventListener.java†L14-L49】【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/rest/ChatHistoryApiImpl.java†L23-L53】 | Для single-instance OK; для многосерверной среды нужен shared storage. |
| История чата хранится в БД (MUST) | 5 | Реализовано | Сообщения сохраняются через `PostMessageScenario` в JPA-репозиторий и таблицу `chat_message`.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/PostMessageScenario.java†L1-L34】【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/JpaChatMessageRepositoryAdapter.java†L1-L40】【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/ChatMessageJpaEntity.java†L1-L49】 | Запись идет из WS-потока через `PostChatMessageScenario`.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/PostChatMessageScenario.java†L1-L67】 |
| Java + сервер приложений (MUST) | 5 | Реализовано | Spring Boot/WebSocket конфигурация и контроллеры в Java.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L1-L43】【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/ChatWebSocketController.java†L1-L41】 | Технологический стек соответствует. |
| Исходники + артефакт + инструкции (MUST) | 4 | Частично | README содержит шаги запуска и использования + команды тестов.【F:README.md†L1-L35】 | Инструкция есть, но без описания сборки артефакта (Gradle build) и зависимостей БД вне Docker. |
| Не использовать готовые движки web-чатов (MUST) | 5 | Реализовано | Собственный UI + SockJS/STOMP без chat-engine. (Косвенно по структуре проекта.)【F:frontend/index.html†L660-L941】 | Нарушений не видно. |
| Docker-образ (SHOULD) | 5 | Реализовано | `Dockerfile` и `docker-compose.yml` есть; запуск описан в README.【F:README.md†L1-L21】 | Выполнено. |
| Hibernate/JPA (SHOULD) | 5 | Реализовано | JPA entity + Spring Data репозиторий.【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/ChatMessageJpaEntity.java†L1-L49】【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/ChatMessageSpringDataRepository.java†L1-L12】 | Выполнено. |
| Соц. авторизация (отечественная) (SHOULD) | 0 | Не реализовано | В проекте нет OAuth/соц. авторизации. Требование помечено как опциональное в `project.md`.【F:project.md†L17-L24】【F:project.md†L147-L154】 | Можно добавить VK ID/ЕСИА и т.п. |

**Итог соответствия MUST:** 8/9 требований выполнены полностью, один пункт частично (подробность инструкций по сборке/артефактам).【F:README.md†L1-L35】

---

## 2. Оценка по best practices реализации чатов

Шкала: **0–5**, где **5** — полностью соответствует best practices для базового чата.

| Область | Оценка | Наблюдения | Доказательства |
| --- | --- | --- | --- |
| Безопасность и контроль доступа | 2 | Нет авторизации; разрешены любые origin в WebSocket; ник передается в header без верификации. | WebSocket допускает `setAllowedOriginPatterns("*")`; username резолвится из header/атрибутов без валидации/уникальности.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】【F:src/main/java/com/example/backendchatwebsocket/application/presence/UsernameResolver.java†L14-L55】 |
| Надежность доставки и обработка ошибок | 3 | Сообщение валидируется по длине/пустоте и логируется; но ошибки в сценарии выбрасываются как `IllegalArgumentException` без явной обработки на уровне WS. | Валидация и логирование в `PostChatMessageScenario`; нет заметной обработки исключений в WS контроллере.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/PostChatMessageScenario.java†L19-L67】【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/ChatWebSocketController.java†L16-L41】 |
| Масштабируемость (multi-instance) | 2 | Simple broker и in-memory presence подходят для single-instance; для горизонтального масштаба нужен внешний брокер и shared presence. | Simple broker включен; реестр пользователей в памяти процесса.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】【F:src/main/java/com/example/backendchatwebsocket/application/presence/OnlineUserRegistry.java†L1-L36】 |
| Хранение истории и лимиты | 4 | История в БД, лимиты 50/100; выдача в хронологическом порядке; нет пагинации или курсора. | `GetChatHistoryScenario` лимитирует; `JpaChatMessageRepositoryAdapter` сортирует ASC после выборки last N.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/GetChatHistoryScenario.java†L1-L41】【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/JpaChatMessageRepositoryAdapter.java†L1-L40】 |
| UX и клиентская устойчивость | 4 | UI хранит ник, делает автоподключение и повторное подключение, экранирует HTML; загружает историю/список. | Клиентские скрипты: reconnect logic, localStorage, escapeHtml, запросы истории/онлайна.【F:frontend/index.html†L693-L941】 |

---

## 3. Комментарии и возможные улучшения

Ниже — предложения улучшений, сгруппированные по важности и с привязкой к текущему состоянию.

### 3.1. Критично/важно
1. **Добавить аутентификацию/авторизацию (опционально по ТЗ, но важно для продакшена).**
   - Реализовать OAuth2 для отечественной соцсети (VK ID/ЕСИА), чтобы имя пользователя было доверенным.
   - Сейчас ник берется из headers без подтверждения и может быть подменен.【F:src/main/java/com/example/backendchatwebsocket/application/presence/UsernameResolver.java†L14-L55】
2. **Ограничить origin и CORS для WebSocket.**
   - `setAllowedOriginPatterns("*")` открывает доступ с любых доменов; лучше ограничить whitelist доменов UI.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】
3. **Обработка ошибок при отправке сообщений по WebSocket.**
   - Сейчас `IllegalArgumentException` из сценария не переводится в ответ клиенту; стоит добавить `@MessageExceptionHandler` и отправлять ошибки в `/queue/errors` или аналогичный канал.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/PostChatMessageScenario.java†L19-L67】

### 3.2. Масштабирование и надежность
1. **Внешний брокер и shared presence для multi-instance.**
   - Перейти на RabbitMQ/Redis для STOMP broker и хранить presence/сессии в Redis, если нужен горизонтальный масштаб и sticky sessions не гарантированы.【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】【F:src/main/java/com/example/backendchatwebsocket/application/presence/OnlineUserRegistry.java†L1-L36】
2. **Политики ретеншена и индексация.**
   - Добавить TTL/архивирование истории, индекс по `created_at` для ускорения `last N` (особенно при больших объемах). Таблица `chat_message` уже есть, но индексы не описаны в коде (требует миграции).【F:src/main/java/com/example/backendchatwebsocket/adapters/outbound/persistence/ChatMessageJpaEntity.java†L1-L49】
3. **Rate limiting и защита от спама.**
   - На уровне WS/REST ограничить частоту сообщений и размер payload; сейчас только длина текста ограничена 1000 символов.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/PostChatMessageScenario.java†L19-L55】

### 3.3. UX и продуктовые улучшения
1. **Пагинация/ленивая загрузка истории.**
   - Сейчас клиент получает только последние 50 сообщений; можно добавить курсор/offset для подгрузки более старых сообщений.【F:src/main/java/com/example/backendchatwebsocket/application/scenario/GetChatHistoryScenario.java†L1-L41】【F:frontend/index.html†L838-L941】
2. **Подтверждение доставки/статусы.**
   - Добавить ack/статусы (доставлено/прочитано) и обработку ошибок отправки на клиенте. Сейчас UI отправляет сообщения без явного подтверждения сервера.【F:frontend/index.html†L842-L861】
3. **Улучшить документацию сборки.**
   - В README добавить `./gradlew build` и описание профиля/переменных БД для запуска без Docker.【F:README.md†L1-L35】

---

## 4. Итоговая оценка

- **Соответствие MUST-требованиям:** высокое (≈ 90%+), ключевые функции чата реализованы. 【F:README.md†L1-L35】【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/ChatWebSocketController.java†L16-L41】
- **Best practices:** средний уровень — хорошая базовая реализация, но без прод-уровня безопасности, масштабирования и контроля качества доставки. 【F:src/main/java/com/example/backendchatwebsocket/adapters/inbound/websocket/WebSocketConfig.java†L18-L43】【F:src/main/java/com/example/backendchatwebsocket/application/presence/OnlineUserRegistry.java†L1-L36】

