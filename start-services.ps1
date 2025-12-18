# Скрипт для запуска Eureka и Config Server
# Использование: 
#   .\start-services.ps1          - запуск только Eureka и Config Server
#   .\start-services.ps1 -all     - запуск всех сервисов (Eureka, Config Server, collector, analyzer, aggregator)

param(
    [switch]$all
)

if ($all) {
    Write-Host "=== Запуск всех сервисов ===" -ForegroundColor Green
} else {
    Write-Host "=== Запуск инфраструктурных сервисов (Eureka + Config Server) ===" -ForegroundColor Green
    Write-Host "Для запуска всех сервисов используйте: .\start-services.ps1 -all" -ForegroundColor Cyan
}
Write-Host ""

# Проверка наличия Maven
try {
    $mvnVersion = mvn -version 2>&1 | Out-String
    Write-Host "Maven найден" -ForegroundColor Green
} catch {
    Write-Host "Ошибка: Maven не найден. Убедитесь, что Maven установлен и добавлен в PATH." -ForegroundColor Red
    exit 1
}

# Проверка Docker (для PostgreSQL и Kafka)
if ($all) {
    Write-Host ""
    Write-Host "Проверка Docker..." -ForegroundColor Yellow
    try {
        $dockerVersion = docker --version 2>&1 | Out-String
        Write-Host "Docker найден" -ForegroundColor Green
        
        Write-Host ""
        Write-Host "Запуск PostgreSQL и Kafka..." -ForegroundColor Yellow
        docker compose up -d postgres kafka kafka-init-topics 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✓ PostgreSQL и Kafka запущены" -ForegroundColor Green
            Write-Host "Ожидание готовности PostgreSQL (5 секунд)..." -ForegroundColor Yellow
            Start-Sleep -Seconds 5
        } else {
            Write-Host "⚠ Не удалось запустить PostgreSQL/Kafka через Docker Compose" -ForegroundColor Yellow
            Write-Host "Убедитесь, что Docker Desktop запущен" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "⚠ Docker не найден или не запущен" -ForegroundColor Yellow
        Write-Host "Убедитесь, что Docker Desktop установлен и запущен" -ForegroundColor Yellow
        Write-Host "Бизнес-сервисы (analyzer, aggregator) требуют PostgreSQL!" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Шаг 1: Запуск Eureka Server (порт 8761)..." -ForegroundColor Yellow
Write-Host "Откройте http://localhost:8761 для проверки" -ForegroundColor Cyan
Write-Host ""

# Запуск Eureka Server в фоновом процессе
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\infra\discovery-server'; Write-Host 'Запуск Eureka Server...' -ForegroundColor Green; mvn spring-boot:run" -WindowStyle Normal

# Ожидание запуска Eureka
Write-Host "Ожидание запуска Eureka Server (10 секунд)..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host ""
Write-Host "Шаг 2: Запуск Config Server (порт 8888)..." -ForegroundColor Yellow
Write-Host "Проверка: http://localhost:8888/telemetry/collector/default" -ForegroundColor Cyan
Write-Host ""

# Запуск Config Server в фоновом процессе
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\infra\config-server'; Write-Host 'Запуск Config Server...' -ForegroundColor Green; mvn spring-boot:run" -WindowStyle Normal

if ($all) {
    Write-Host ""
    Write-Host "Ожидание запуска Config Server (10 секунд)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 10
    
    Write-Host ""
    Write-Host "Шаг 3: Запуск бизнес-сервисов..." -ForegroundColor Yellow
    
    # Запуск Collector
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\telemetry\collector'; Write-Host 'Запуск Collector...' -ForegroundColor Green; mvn spring-boot:run" -WindowStyle Normal
    Start-Sleep -Seconds 5
    
    # Запуск Analyzer
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\telemetry\analyzer'; Write-Host 'Запуск Analyzer...' -ForegroundColor Green; mvn spring-boot:run" -WindowStyle Normal
    Start-Sleep -Seconds 5
    
    # Запуск Aggregator
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\telemetry\aggregator'; Write-Host 'Запуск Aggregator...' -ForegroundColor Green; mvn spring-boot:run" -WindowStyle Normal
}

Write-Host ""
Write-Host "=== Сервисы запущены ===" -ForegroundColor Green
Write-Host ""
Write-Host "Eureka Dashboard: http://localhost:8761" -ForegroundColor Cyan
Write-Host "Config Server: http://localhost:8888" -ForegroundColor Cyan
if ($all) {
    Write-Host ""
    Write-Host "Проверьте Eureka Dashboard для просмотра всех зарегистрированных сервисов" -ForegroundColor Cyan
}
Write-Host ""
Write-Host "Для остановки закройте окна терминалов с сервисами" -ForegroundColor Yellow

