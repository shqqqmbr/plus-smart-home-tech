# Исправление проблем с запуском Analyzer и Aggregator

## Проблема

При запуске analyzer и aggregator возникает ошибка:
```
org.postgresql.util.PSQLException: Подсоединение по адресу localhost:5432 отклонено
```

## Решение

### 1. Запустите PostgreSQL через Docker Compose

**Важно:** Перед запуском analyzer и aggregator необходимо запустить PostgreSQL.

```powershell
# Убедитесь, что Docker Desktop запущен
docker compose up -d postgres
```

### 2. Проверьте, что PostgreSQL запущен

```powershell
docker ps
```

Должен быть виден контейнер `postgres` со статусом `Up`.

### 3. Перезапустите Config Server

После исправления конфигурации (пути к десериализаторам) перезапустите Config Server, чтобы изменения вступили в силу.

### 4. Запустите analyzer и aggregator

Теперь сервисы должны запуститься успешно.

---

## Что было исправлено

1. ✅ Исправлены пути к десериализаторам в конфигурации analyzer:
   - `kafka.serializer.HubEventDeserializer` → `ru.yandex.practicum.HubEventDeserializer`
   - `kafka.serializer.SensorSnapshotDeserializer` → `ru.yandex.practicum.SensorSnapshotDeserializer`

2. ✅ Обновлен скрипт `start-services.ps1` для автоматического запуска PostgreSQL и Kafka

3. ✅ Обновлена инструкция `STARTUP_GUIDE.md` с информацией о необходимости запуска PostgreSQL

---

## Быстрый запуск всех сервисов

Используйте обновленный скрипт:

```powershell
.\start-services.ps1 -all
```

Скрипт автоматически:
1. Проверит наличие Docker
2. Запустит PostgreSQL и Kafka
3. Запустит Eureka Server
4. Запустит Config Server
5. Запустит все бизнес-сервисы

---

## Ручной запуск PostgreSQL

Если скрипт не работает, запустите PostgreSQL вручную:

```powershell
# Запуск только PostgreSQL
docker compose up -d postgres

# Или запуск PostgreSQL и Kafka вместе
docker compose up -d postgres kafka kafka-init-topics
```

---

## Проверка подключения к PostgreSQL

После запуска PostgreSQL проверьте подключение:

```powershell
# Проверка статуса контейнера
docker compose ps postgres

# Проверка логов
docker compose logs postgres
```

PostgreSQL должен быть доступен на `localhost:5432` с параметрами:
- **User:** `dbuser`
- **Password:** `12345`
- **Database:** `postgres`

