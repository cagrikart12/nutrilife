#!/bin/bash

# NutriLife Profile Service Docker Build Script

echo "🚀 NutriLife Profile Service Docker Build başlatılıyor..."

# Docker image adı ve tag'i
IMAGE_NAME="nutrilife-profile-service"
TAG="latest"
FULL_IMAGE_NAME="${IMAGE_NAME}:${TAG}"

# Mevcut container'ları durdur ve sil
echo "📦 Mevcut container'ları temizleniyor..."
docker-compose down

# Eski image'ları temizle
echo "🗑️ Eski image'lar temizleniyor..."
docker rmi ${FULL_IMAGE_NAME} 2>/dev/null || true

# Docker image'ı build et
echo "🔨 Docker image build ediliyor..."
docker build -t ${FULL_IMAGE_NAME} .

# Build başarılı mı kontrol et
if [ $? -eq 0 ]; then
    echo "✅ Docker image başarıyla build edildi: ${FULL_IMAGE_NAME}"
    
    # Container'ları başlat
    echo "🚀 Container'lar başlatılıyor..."
    docker-compose up -d
    
    # Container'ların durumunu kontrol et
    echo "📊 Container durumları:"
    docker-compose ps
    
    echo ""
    echo "🎉 NutriLife Profile Service başarıyla başlatıldı!"
    echo "📝 Servis URL'leri:"
    echo "   - Profile Service: http://localhost:8082"
    echo "   - Swagger UI: http://localhost:8082/swagger-ui.html"
    echo "   - Health Check: http://localhost:8082/api/profiles/health"
    echo "   - Eureka Server: http://localhost:8761"
    echo ""
    echo "📋 Logları görüntülemek için: docker-compose logs -f nutrilife-profile-service"
    
else
    echo "❌ Docker build başarısız!"
    exit 1
fi
