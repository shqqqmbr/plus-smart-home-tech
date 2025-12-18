# Скрипт для проверки статуса Eureka и Config Server
# Использование: .\check-services.ps1

Write-Host "=== Проверка статуса сервисов ===" -ForegroundColor Green
Write-Host ""

# Проверка Eureka Server
Write-Host "Проверка Eureka Server (http://localhost:8761)..." -ForegroundColor Yellow
try {
    $eurekaResponse = Invoke-WebRequest -Uri "http://localhost:8761" -TimeoutSec 3 -UseBasicParsing -ErrorAction Stop
    if ($eurekaResponse.StatusCode -eq 200) {
        Write-Host "✓ Eureka Server работает" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ Eureka Server не доступен" -ForegroundColor Red
    Write-Host "  Убедитесь, что сервис запущен на порту 8761" -ForegroundColor Yellow
}

Write-Host ""

# Проверка Config Server
Write-Host "Проверка Config Server (http://localhost:8888)..." -ForegroundColor Yellow
try {
    $configResponse = Invoke-WebRequest -Uri "http://localhost:8888/telemetry/collector/default" -TimeoutSec 3 -UseBasicParsing -ErrorAction Stop
    if ($configResponse.StatusCode -eq 200) {
        Write-Host "✓ Config Server работает" -ForegroundColor Green
        Write-Host "  Конфигурация доступна" -ForegroundColor Green
    }
} catch {
    Write-Host "✗ Config Server не доступен" -ForegroundColor Red
    Write-Host "  Убедитесь, что сервис запущен на порту 8888" -ForegroundColor Yellow
}

Write-Host ""

# Проверка регистрации Config Server в Eureka
Write-Host "Проверка регистрации Config Server в Eureka..." -ForegroundColor Yellow
try {
    $eurekaApps = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -TimeoutSec 3 -UseBasicParsing -ErrorAction Stop
    if ($eurekaApps.Content -like "*CONFIG-SERVER*") {
        Write-Host "✓ Config Server зарегистрирован в Eureka" -ForegroundColor Green
    } else {
        Write-Host "⚠ Config Server не найден в реестре Eureka" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Не удалось проверить реестр Eureka" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Проверка завершена ===" -ForegroundColor Green

