@echo off
REM NutriLife Profile Service Docker Build Script for Windows

echo 🚀 NutriLife Profile Service Docker Build başlatılıyor...

REM Docker image adı ve tag'i
set IMAGE_NAME=nutrilife-profile-service
set TAG=latest
set FULL_IMAGE_NAME=%IMAGE_NAME%:%TAG%

REM Mevcut container'ları durdur ve sil
echo 📦 Mevcut container'ları temizleniyor...
docker-compose down

REM Eski image'ları temizle
echo 🗑️ Eski image'lar temizleniyor...
docker rmi %FULL_IMAGE_NAME% 2>nul

REM Docker image'ı build et
echo 🔨 Docker image build ediliyor...
docker build -t %FULL_IMAGE_NAME% .

REM Build başarılı mı kontrol et
if %errorlevel% equ 0 (
    echo ✅ Docker image başarıyla build edildi: %FULL_IMAGE_NAME%
    
    REM Container'ları başlat
    echo 🚀 Container'lar başlatılıyor...
    docker-compose up -d
    
    REM Container'ların durumunu kontrol et
    echo 📊 Container durumları:
    docker-compose ps
    
    echo.
    echo 🎉 NutriLife Profile Service başarıyla başlatıldı!
    echo 📝 Servis URL'leri:
    echo    - Profile Service: http://localhost:8082
    echo    - Swagger UI: http://localhost:8082/swagger-ui.html
    echo    - Health Check: http://localhost:8082/api/profiles/health
    echo    - Eureka Server: http://localhost:8761
    echo.
    echo 📋 Logları görüntülemek için: docker-compose logs -f nutrilife-profile-service
    
) else (
    echo ❌ Docker build başarısız!
    exit /b 1
)
