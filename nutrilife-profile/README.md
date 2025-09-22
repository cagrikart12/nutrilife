# NutriLife Profile Service

NutriLife uygulaması için kullanıcı profil yönetimi microservice'i.

## 🚀 Özellikler

- **JWT Token Tabanlı Yetkilendirme**: Kullanıcı bilgileri JWT token'dan otomatik olarak alınır
- **Kapsamlı Profil Yönetimi**: Kullanıcıların detaylı profil bilgilerini saklar
- **Otomatik Hesaplamalar**: BMI, yaş, BMR, TDEE gibi değerleri otomatik hesaplar
- **Gelişmiş Arama**: Çeşitli kriterlere göre profil arama ve filtreleme
- **RESTful API**: Swagger dökümantasyonu ile tam API desteği
- **Docker Desteği**: Kolay deployment için Docker containerization
- **Microservice Mimarisi**: Eureka ile service discovery

## 📋 Profil Bilgileri

### Temel Bilgiler
- Ad, soyad, telefon numarası
- Doğum tarihi, cinsiyet
- Profil fotoğrafı, biyografi

### Fiziksel Özellikler
- Boy (cm), kilo (kg)
- Hedef kilo
- BMI ve BMI kategorisi (otomatik hesaplanır)

### Hedef ve Aktivite
- Hedef türü (kilo verme, alma, koruma, kas kazanma, genel sağlık)
- Aktivite seviyesi (hareketsiz, hafif aktif, orta aktif, çok aktif, aşırı aktif)
- Günlük kalori hedefi

### Sağlık Bilgileri
- Alerjiler
- Tıbbi durumlar
- Diyet tercihleri

### Otomatik Hesaplamalar
- **Yaş**: Doğum tarihinden hesaplanır
- **BMI**: Vücut kitle indeksi
- **BMR**: Bazal metabolizma hızı (Mifflin-St Jeor formülü)
- **TDEE**: Toplam günlük enerji harcaması

## 🛠️ Teknolojiler

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (JSON Web Token)**
- **Swagger/OpenAPI 3**
- **Docker**
- **Eureka Service Discovery**

## 🚀 Hızlı Başlangıç

### Docker ile Çalıştırma

1. **Repository'yi klonlayın:**
```bash
git clone <repository-url>
cd nutrilife-profile
```

2. **Docker ile başlatın:**
```bash
# Linux/Mac
./build-docker.sh

# Windows
build-docker.bat
```

3. **Servisleri kontrol edin:**
- Profile Service: http://localhost:8082
- Swagger UI: http://localhost:8082/swagger-ui.html
- Health Check: http://localhost:8082/api/profiles/health
- Eureka Server: http://localhost:8761

### Manuel Çalıştırma

1. **PostgreSQL veritabanını başlatın:**
```bash
docker run --name nutrilife-profile-db \
  -e POSTGRES_DB=nutrilife_profile \
  -e POSTGRES_USER=nutrilife_user \
  -e POSTGRES_PASSWORD=nutrilife_password \
  -p 5433:5432 \
  -d postgres:15-alpine
```

2. **Uygulamayı çalıştırın:**
```bash
mvn spring-boot:run
```

## 📚 API Dökümantasyonu

### Temel Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| POST | `/api/profiles` | Yeni profil oluştur |
| PUT | `/api/profiles` | Profil güncelle |
| GET | `/api/profiles` | Profil getir |
| GET | `/api/profiles/exists` | Profil var mı kontrol et |
| DELETE | `/api/profiles` | Profil sil |

### Arama Endpoints

| Method | Endpoint | Açıklama |
|--------|----------|----------|
| GET | `/api/profiles/search/name` | İsme göre ara |
| GET | `/api/profiles/search/goal` | Hedefe göre ara |
| GET | `/api/profiles/search/activity` | Aktivite seviyesine göre ara |
| GET | `/api/profiles/search/gender` | Cinsiyete göre ara |
| GET | `/api/profiles/search/age` | Yaş aralığına göre ara |
| GET | `/api/profiles/search/bmi` | BMI aralığına göre ara |
| GET | `/api/profiles/search/calories` | Kalori hedefine göre ara |
| GET | `/api/profiles/search/allergy` | Alerjiye göre ara |
| GET | `/api/profiles/search/dietary` | Diyet tercihine göre ara |

### Yetkilendirme

Tüm endpoint'ler JWT token ile korunmaktadır. İsteklerde `Authorization: Bearer <token>` header'ı gönderilmelidir.

## 🔧 Konfigürasyon

### Environment Variables

| Değişken | Açıklama | Varsayılan |
|----------|----------|------------|
| `SPRING_DATASOURCE_URL` | Veritabanı URL'i | `jdbc:postgresql://localhost:5432/nutrilife_profile` |
| `SPRING_DATASOURCE_USERNAME` | Veritabanı kullanıcı adı | `nutrilife_user` |
| `SPRING_DATASOURCE_PASSWORD` | Veritabanı şifresi | `nutrilife_password` |
| `JWT_SECRET` | JWT secret key | `nutrilife-secret-key...` |
| `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` | Eureka server URL | `http://localhost:8761/eureka/` |

### Port Konfigürasyonu

- **Profile Service**: 8082
- **PostgreSQL**: 5433
- **Eureka Server**: 8761

## 🐳 Docker Komutları

```bash
# Container'ları başlat
docker-compose up -d

# Container'ları durdur
docker-compose down

# Logları görüntüle
docker-compose logs -f nutrilife-profile-service

# Container'ları yeniden başlat
docker-compose restart

# Volume'ları temizle
docker-compose down -v
```

## 📊 Monitoring

### Health Check
- **Endpoint**: `GET /api/profiles/health`
- **Durum**: UP/DOWN
- **Detaylar**: Servis bilgileri ve timestamp

### Actuator Endpoints
- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

## 🔒 Güvenlik

- JWT token tabanlı yetkilendirme
- CORS konfigürasyonu
- SQL injection koruması
- XSS koruması
- CSRF koruması

## 📝 Loglama

Loglar aşağıdaki seviyelerde yapılandırılmıştır:
- **DEBUG**: Geliştirme ortamı
- **INFO**: Production ortamı
- **WARN**: Uyarılar
- **ERROR**: Hatalar

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakın.

## 📞 İletişim

- **Proje**: NutriLife Profile Service
- **Versiyon**: 1.0.0
- **Geliştirici**: NutriLife Team
