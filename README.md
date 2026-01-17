# backend-chat-websocket

## Run from WSL (Windows Subsystem for Linux)

### Prerequisites
- WSL2 distro installed (Ubuntu recommended).
- Docker Desktop with WSL integration enabled (or Docker Engine installed inside WSL).

### Steps
1. Open your WSL terminal and go to the project directory.
2. Build and start the app + database with Docker Compose:

   ```bash
   docker compose up --build
   ```

3. Once the containers are running, access the app at `http://localhost:8080`.
4. Stop the stack when finished:

   ```bash
   docker compose down
   ```

## Run tests locally

From the project root, execute:

```bash
./gradlew test
```

## Usage

1. Start the stack (see instructions above) and open `http://localhost:8080` in your browser. The UI is served from the Spring Boot static resources.
2. Enter a nickname on the join screen. The client passes the nickname as a STOMP header.
3. The UI immediately loads the last N messages from `GET /api/chat/history?limit=50` and renders them chronologically.
4. Online users are shown in the right panel and are refreshed via `GET /api/chat/users` and `/topic/chat.users` updates.
5. Post messages from the input field; they are sent to `/app/chat.send` and broadcast to `/topic/chat.messages` without page refresh.

## Current state of the project

### Runtime/API surface
- REST: OpenAPI spec defines a single health endpoint at `GET /api/ping` that returns `{ "message": "pong" }`. The spec is stored in `src/main/resources/openapi.yaml` and the Spring Boot app listens on port `8080`. 
- WebSocket/STOMP: The server exposes a SockJS-enabled STOMP endpoint at `/ws` with application destinations under `/app` and a simple broker on `/topic` and `/queue`. Clients send messages to `/app/chat.send`, and the server broadcasts to `/topic/chat.messages`.

### Domain and persistence
- Domain model includes `ChatMessage`, `MessageId`, and `UserId`. There are repositories for chat messages and users, plus a `MessageIdGenerator` implementation based on ULIDs. 
- Persistence uses Spring Data JPA with PostgreSQL, managed by Liquibase migrations in `src/main/resources/db/changelog`.
- There are two message flows today:
  - `PostMessageScenario` persists a `ChatMessage` to the database using the repository and ULID generator.
  - `PostChatMessageScenario` (used by the WebSocket controller) validates, logs, persists, and broadcasts chat messages.

### Observability/logging
- Posted chat messages are logged with author, id, and length (sanitized to single-line output) by `PostChatMessageScenario`.
