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
