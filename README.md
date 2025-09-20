# 🥗 NutriLife - Mikroservice Mimarisi

NutriLife, beslenme takibi için geliştirilmiş mikroservice tabanlı bir uygulamadır.

## 🏗️ Mimari

### Mikroservisler
- **nutrilife-auth** (Port: 8081) - Kimlik doğrulama servisi
- **nutrilife-core** (Port: 8080) - Temel iş mantığı servisi
- **PostgreSQL** (Port: 5432) - Veritabanı
- **Redis** (Port: 6379) - Cache ve Session Yönetimi

## 🚀 Hızlı Başlangıç

### Docker ile Çalıştırma (Önerilen)

```bash
# Tüm servisleri başlat
docker-compose up --build

# Background'da çalıştır
docker-compose up -d

# Servisleri durdur
docker-compose down
```

### Manuel Çalıştırma

```bash
# 1. PostgreSQL'i başlat
# 2. Core servisi
cd nutrilife-core
mvn spring-boot:run

# 3. Auth servisi (yeni terminal)
cd nutrilife-auth
mvn spring-boot:run
```

## 🔗 API Endpoints

### Auth Service (Port: 8081)
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **Health Check**: http://localhost:8081/api/auth/health

#### Endpoints:
- `POST /api/auth/register` - Kullanıcı kaydı
- `POST /api/auth/login` - Kullanıcı girişi
- `POST /api/auth/refresh` - Token yenileme
- `POST /api/auth/validate` - Token doğrulama
- `POST /api/auth/revoke` - Token iptal etme
- `POST /api/auth/logout` - Çıkış yap (Token blacklist)

### Core Service (Port: 8080)
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/core/health

#### Endpoints:
- `GET /api/core/foods` - Tüm yemekleri listele
- `POST /api/core/foods` - Yemek oluştur
- `GET /api/core/foods/{id}` - Yemek detayı
- `GET /api/core/foods/search?q=...` - Yemek ara
- `PUT /api/core/foods/{id}` - Yemek güncelle
- `DELETE /api/core/foods/{id}` - Yemek sil

## 🧪 Test Örnekleri

### 1. Kullanıcı Kaydı
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Password123!",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### 2. Kullanıcı Girişi
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "Password123!"
  }'
```

### 3. Yemek Oluşturma
```bash
curl -X POST http://localhost:8080/api/core/foods \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Tavuk Göğsü",
    "description": "Izgara tavuk göğsü",
    "calories": 165.0,
    "protein": 31.0,
    "carbohydrates": 0.0,
    "fat": 3.6,
    "fiber": 0.0,
    "source": "USER"
  }'
```

## 🛠️ Geliştirme

### Gereksinimler
- Java 17+
- Maven 3.9+
- PostgreSQL 15+
- Docker & Docker Compose

### Proje Yapısı
```
nutrilife/
├── nutrilife-auth/          # Kimlik doğrulama servisi
├── nutrilife-core/          # Temel iş mantığı servisi
├── docker-compose.yml       # Docker Compose konfigürasyonu
├── Dockerfile              # Multi-stage Docker build
└── README.md               # Bu dosya
```

## 📊 Veritabanı

### Auth Database (nutrilife_auth)
- `users` - Kullanıcı bilgileri
- `refresh_tokens` - Refresh token'lar

### Core Database (nutrilife_core)
- `foods` - Yemek bilgileri
- `meals` - Öğün bilgileri
- `meal_items` - Öğün içerikleri

## 🔴 Redis Cache Sistemi

### Redis Konfigürasyonu
NutriLife projesi Redis'i 3 farklı amaç için kullanır:

#### **Database 0: JWT Token Blacklist**
```yaml
# Token blacklist için Redis konfigürasyonu
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
    timeout: 2000ms
```

**Kullanım Alanları:**
- İptal edilen JWT token'ları saklama
- Logout işleminde token'ı geçersiz kılma
- Token doğrulama sırasında blacklist kontrolü

**Redis Key Formatı:**
```
blacklist:{token} = "blacklisted"
user_tokens:{username} = Set<token>
```

#### **Database 1: Rate Limiting**
```yaml
# Rate limiting için Redis konfigürasyonu
spring:
  redis:
    database: 1
```

**Rate Limiting Kuralları:**
- **Login Attempts**: 5 deneme / 15 dakika
- **Password Reset**: 3 istek / 1 saat
- **API Requests**: 60 istek / 1 dakika

**Redis Key Formatı:**
```
login_attempts:{username}:{ip} = counter
password_reset:{email} = counter
api_rate_limit:{clientId}:{minute} = counter
```

#### **Database 2: User Data Caching**
```yaml
# User caching için Redis konfigürasyonu
spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000 # 10 dakika
```

**Cache Edilen Veriler:**
- Kullanıcı profil bilgileri
- JWT token doğrulama sonuçları
- Sık kullanılan kullanıcı verileri

**Redis Key Formatı:**
```
users:{username} = User object
users:{email} = User object
```

### Redis Servisleri

#### **1. TokenBlacklistService**
```java
// Token'ı blacklist'e ekle
tokenBlacklistService.blacklistToken(token, expirationTime);

// Token blacklist'te mi kontrol et
boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(token);

// Kullanıcının tüm token'larını iptal et
tokenBlacklistService.blacklistAllUserTokens(username);
```

#### **2. RateLimitingService**
```java
// Login denemesi kontrolü
boolean allowed = rateLimitingService.isLoginAllowed(username, ipAddress);

// Başarısız login kaydet
rateLimitingService.recordFailedLogin(username, ipAddress);

// API rate limit kontrolü
boolean apiAllowed = rateLimitingService.isApiRequestAllowed(clientId);
```

#### **3. UserCacheService**
```java
// Kullanıcıyı cache'den getir
User user = userCacheService.getUserByUsername(username);

// Cache'i temizle
userCacheService.evictUserCache(username);
```

### Redis Monitoring

#### **Redis CLI ile İnceleme**
```bash
# Redis container'ına bağlan
docker exec -it nutrilife-redis redis-cli

# Tüm key'leri listele
KEYS *

# Belirli pattern'deki key'leri bul
KEYS blacklist:*
KEYS login_attempts:*
KEYS users:*

# Key'in değerini görüntüle
GET blacklist:eyJhbGciOiJIUzI1NiJ9...

# Set içeriğini görüntüle
SMEMBERS user_tokens:testuser

# Key'in TTL'sini kontrol et
TTL blacklist:eyJhbGciOiJIUzI1NiJ9...

# Key'i sil
DEL blacklist:eyJhbGciOiJIUzI1NiJ9...
```

#### **Redis Memory Kullanımı**
```bash
# Memory bilgilerini görüntüle
INFO memory

# Database boyutlarını kontrol et
DBSIZE

# Key sayılarını kontrol et
INFO keyspace
```

### Redis Performans Optimizasyonu

#### **Connection Pooling**
```yaml
spring:
  redis:
    jedis:
      pool:
        max-active: 8    # Maksimum aktif bağlantı
        max-idle: 8      # Maksimum boşta bağlantı
        min-idle: 0      # Minimum boşta bağlantı
```

#### **Memory Management**
```yaml
# Docker Compose'da Redis konfigürasyonu
redis:
  command: redis-server --appendonly yes --maxmemory 256mb --maxmemory-policy allkeys-lru
```

**Memory Policy Açıklaması:**
- `allkeys-lru`: En az kullanılan key'leri sil
- `maxmemory 256mb`: Maksimum 256MB kullan
- `appendonly yes`: AOF (Append Only File) aktif

### Redis Güvenlik

#### **Production Ayarları**
```yaml
# Güvenli Redis konfigürasyonu
spring:
  redis:
    password: ${REDIS_PASSWORD:}
    ssl: true
    timeout: 2000ms
```

#### **Network Güvenliği**
```yaml
# Docker Compose'da network isolation
networks:
  nutrilife-network:
    driver: bridge
    internal: true  # Sadece internal network
```

### Redis Troubleshooting

#### **Yaygın Sorunlar**

1. **Connection Refused**
```bash
# Redis servisinin çalışıp çalışmadığını kontrol et
docker ps | grep redis

# Redis log'larını kontrol et
docker logs nutrilife-redis
```

2. **Memory Full**
```bash
# Memory kullanımını kontrol et
docker exec -it nutrilife-redis redis-cli INFO memory

# Eski key'leri temizle
docker exec -it nutrilife-redis redis-cli FLUSHDB
```

3. **Slow Queries**
```bash
# Yavaş sorguları kontrol et
docker exec -it nutrilife-redis redis-cli SLOWLOG GET 10
```

### Redis Backup ve Restore

#### **Backup Alma**
```bash
# Redis verilerini backup al
docker exec nutrilife-redis redis-cli BGSAVE

# Backup dosyasını kopyala
docker cp nutrilife-redis:/data/dump.rdb ./redis-backup.rdb
```

#### **Restore Etme**
```bash
# Backup dosyasını container'a kopyala
docker cp ./redis-backup.rdb nutrilife-redis:/data/dump.rdb

# Redis'i yeniden başlat
docker restart nutrilife-redis
```

## 🔐 Güvenlik

### Kimlik Doğrulama
- JWT tabanlı kimlik doğrulama
- Refresh token sistemi
- Token blacklist (Redis ile)
- Parola politikası (8+ karakter, büyük/küçük harf, rakam, özel karakter)
- E-posta uniq kontrolü
- RBAC (Role-Based Access Control)

### Rate Limiting (Redis ile)
- **Login Protection**: 5 deneme / 15 dakika
- **Password Reset**: 3 istek / 1 saat  
- **API Protection**: 60 istek / 1 dakika
- **IP-based Limiting**: IP adresine göre sınırlama

### Cache Güvenliği
- **User Data Caching**: Kullanıcı bilgileri Redis'te güvenli saklama
- **Token Validation**: Cache'den hızlı token doğrulama
- **Session Management**: Aktif oturumları Redis'te takip etme

### Network Güvenliği
- **Docker Network Isolation**: Servisler arası güvenli iletişim
- **Redis Password Protection**: Production'da şifre koruması
- **SSL/TLS Support**: Güvenli bağlantı desteği

## 📝 API Dokümantasyonu

Her servis kendi Swagger UI'ına sahiptir:
- **Auth Service**: http://localhost:8081/swagger-ui.html
- **Core Service**: http://localhost:8080/swagger-ui.html

## 🐳 Docker Komutları

### Temel Komutlar
```bash
# Container'ları listele
docker ps

# Log'ları görüntüle
docker-compose logs -f nutrilife-auth
docker-compose logs -f nutrilife-core
docker-compose logs -f nutrilife-redis

# Container'a bağlan
docker exec -it nutrilife-auth bash
docker exec -it nutrilife-core bash
docker exec -it nutrilife-redis redis-cli
```

### Redis Komutları
```bash
# Redis'e bağlan
docker exec -it nutrilife-redis redis-cli

# Redis durumunu kontrol et
docker exec -it nutrilife-redis redis-cli INFO

# Redis verilerini temizle
docker exec -it nutrilife-redis redis-cli FLUSHALL

# Redis memory kullanımını kontrol et
docker exec -it nutrilife-redis redis-cli INFO memory
```

### Volume Yönetimi
```bash
# Volume'ları listele
docker volume ls

# Volume'ları temizle
docker-compose down -v

# Belirli volume'u sil
docker volume rm nutrilife_postgres_data
docker volume rm nutrilife_redis_data
```

### Servis Yönetimi
```bash
# Sadece Redis'i başlat
docker-compose up redis

# Sadece veritabanlarını başlat
docker-compose up postgres redis

# Servisleri yeniden başlat
docker-compose restart nutrilife-auth
docker-compose restart nutrilife-core
```

## 🚨 Sorun Giderme

### Port Çakışması
Eğer portlar kullanımda ise, `application.yml` dosyalarında port numaralarını değiştirin.

### Veritabanı Bağlantı Hatası
PostgreSQL'in çalıştığından emin olun:
```bash
docker-compose up postgres
```

### Maven Bağımlılık Hatası
```bash
mvn clean install
```

## 📞 İletişim

- **E-posta**: info@nutrilife.com
- **Website**: https://nutrilife.com
- **GitHub**: https://github.com/nutrilife

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakın.
