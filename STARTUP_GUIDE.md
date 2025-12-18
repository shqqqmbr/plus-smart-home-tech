# Инструкция по запуску Eureka и Spring Config Server

## Что можно проверить без запуска бизнес-сервисов

### ✅ Минимальная проверка (Eureka + Config Server)
Для проверки работы **только Eureka и Config Server** достаточно запустить:
1. **Eureka Server** (порт 8761)
2. **Config Server** (порт 8888)

В этом случае вы проверите:
- ✅ Работу Eureka Server
- ✅ Работу Config Server
- ✅ Регистрацию Config Server в Eureka

**Бизнес-сервисы (analyzer, aggregator, collector) запускать НЕ нужно.**

---

### ✅ Полная проверка регистрации всех сервисов
Для проверки регистрации **всех сервисов** в Eureka нужно запустить:
1. **Eureka Server** (порт 8761)
2. **Config Server** (порт 8888)
3. **collector** (зарегистрируется в Eureka)
4. **analyzer** (зарегистрируется в Eureka)
5. **aggregator** (зарегистрируется в Eureka)

---

## Порядок запуска сервисов

### 0. Запуск инфраструктуры (PostgreSQL, Kafka)

**Важно:** Перед запуском бизнес-сервисов (analyzer, aggregator) необходимо запустить PostgreSQL и Kafka.

#### Запуск через Docker Compose:
```bash
# Запуск PostgreSQL и Kafka
docker compose up -d postgres kafka kafka-init-topics
```

**Проверка:**
- PostgreSQL должен быть доступен на порту 5432
- Kafka должна быть доступна на порту 9092

---

### 1. Discovery Server (Eureka Server)
**Порт:** 8761  
**Путь:** `infra/discovery-server`

Eureka Server должен быть запущен первым, так как другие сервисы регистрируются в нем.

#### Запуск через Maven:
```bash
cd infra/discovery-server
mvn spring-boot:run
```

#### Запуск через JAR:
```bash
cd infra/discovery-server
mvn clean package
java -jar target/discovery-server-1.0-SNAPSHOT.jar
```

#### Проверка работы:
После запуска откройте в браузере: http://localhost:8761

Вы должны увидеть Eureka Dashboard с информацией о зарегистрированных сервисах.

---

### 2. Config Server (Spring Cloud Config Server)
**Порт:** 8888  
**Путь:** `infra/config-server`

Config Server должен быть запущен после Eureka Server, так как он регистрируется в Eureka.

#### Запуск через Maven:
```bash
cd infra/config-server
mvn spring-boot:run
```

#### Запуск через JAR:
```bash
cd infra/config-server
mvn clean package
java -jar target/config-server-1.0-SNAPSHOT.jar
```

#### Проверка работы:

1. **Проверка регистрации в Eureka:**
   - Откройте http://localhost:8761
   - В списке зарегистрированных сервисов должен появиться `CONFIG-SERVER`

2. **Проверка работы Config Server:**
   - Откройте http://localhost:8888/telemetry/collector/default
   - Должна вернуться конфигурация для сервиса `collector`
   - Или http://localhost:8888/telemetry/analyzer/default
   - Или http://localhost:8888/telemetry/aggregator/default

---

### 3. Бизнес-сервисы (опционально)

**Важно:** Перед запуском analyzer и aggregator убедитесь, что:
- ✅ PostgreSQL запущен (порт 5432)
- ✅ Kafka запущена (порт 9092)
- ✅ Eureka Server запущен (порт 8761)
- ✅ Config Server запущен (порт 8888)

Для проверки регистрации бизнес-сервисов в Eureka запустите их после Eureka и Config Server:

#### Collector:
```bash
cd telemetry/collector
mvn spring-boot:run
```

#### Analyzer:
```bash
cd telemetry/analyzer
mvn spring-boot:run
```

#### Aggregator:
```bash
cd telemetry/aggregator
mvn spring-boot:run
```

После запуска каждого сервиса проверьте Eureka Dashboard (http://localhost:8761) - сервисы должны появиться в списке зарегистрированных.

---

## Быстрый запуск (Windows PowerShell)

### Вариант 1: Только инфраструктура (Eureka + Config Server)

**Терминал 1 (Eureka):**
```powershell
cd infra/discovery-server
mvn spring-boot:run
```

**Терминал 2 (Config Server):**
```powershell
cd infra/config-server
mvn spring-boot:run
```

### Вариант 2: Запуск из корня проекта

**Eureka:**
```powershell
mvn spring-boot:run -pl infra/discovery-server
```

**Config Server:**
```powershell
mvn spring-boot:run -pl infra/config-server
```

**Бизнес-сервисы (если нужно):**
```powershell
mvn spring-boot:run -pl telemetry/collector
mvn spring-boot:run -pl telemetry/analyzer
mvn spring-boot:run -pl telemetry/aggregator
```

---

## Структура конфигураций

Config Server использует профиль `native` и хранит конфигурации в файловой системе:
- `infra/config-server/src/main/resources/config/telemetry/collector/application.yaml`
- `infra/config-server/src/main/resources/config/telemetry/analyzer/application.yaml`
- `infra/config-server/src/main/resources/config/telemetry/aggregator/application.yaml`

---

## Устранение проблем

### Проблема: Analyzer/Aggregator не запускается - ошибка подключения к PostgreSQL
**Ошибка:** `Connection refused: getsockopt` или `Подсоединение по адресу localhost:5432 отклонено`

**Решение:**
1. Убедитесь, что Docker Desktop запущен
2. Запустите PostgreSQL: `docker compose up -d postgres`
3. Проверьте, что PostgreSQL запущен: `docker ps`
4. Проверьте доступность: `docker compose ps postgres`

### Проблема: Config Server не запускается
- Убедитесь, что Eureka Server запущен и доступен на порту 8761
- Проверьте логи на наличие ошибок подключения к Eureka

### Проблема: Config Server не регистрируется в Eureka
- Проверьте, что зависимость `spring-cloud-starter-netflix-eureka-client` добавлена в `pom.xml`
- Убедитесь, что в `application.yaml` указан правильный URL Eureka: `http://localhost:8761/eureka/`

### Проблема: Бизнес-сервисы не регистрируются в Eureka
- Убедитесь, что Eureka Server и Config Server запущены
- Проверьте, что зависимость `spring-cloud-starter-netflix-eureka-client` добавлена в `pom.xml` сервиса
- Проверьте конфигурацию Eureka в `application.yaml` сервиса

### Проблема: Не удается получить конфигурацию
- Проверьте формат URL: `http://localhost:8888/{application}/{profile}`
- Убедитесь, что файлы конфигурации находятся в правильной директории

---

## Порты сервисов

- **Eureka Server:** 8761
- **Config Server:** 8888
- **Collector:** (порт из конфигурации Config Server)
- **Analyzer:** (порт из конфигурации Config Server)
- **Aggregator:** (порт из конфигурации Config Server)

Убедитесь, что эти порты свободны перед запуском.

---

## Резюме

**Для базовой проверки Eureka и Config Server:**
- ✅ Запустите только Eureka Server и Config Server
- ✅ Бизнес-сервисы запускать НЕ нужно

**Для полной проверки регистрации:**
- ✅ Запустите все сервисы в правильном порядке
- ✅ Проверьте Eureka Dashboard на наличие всех зарегистрированных сервисов
