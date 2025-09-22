# NutriLife Frontend

NutriLife uygulaması için React tabanlı frontend microservice'i.

## 🚀 Özellikler

- **React 18 + TypeScript**: Modern React geliştirme
- **React Router**: Sayfa yönlendirme
- **Axios**: HTTP istekleri
- **Context API**: State yönetimi
- **JWT Authentication**: Token tabanlı yetkilendirme
- **Responsive Design**: Mobil uyumlu tasarım
- **Docker Support**: Containerization

## 📋 Sayfalar

### 🔐 Authentication
- **Login**: Kullanıcı girişi
- **Register**: Yeni kullanıcı kaydı

### 👤 Profile Management
- **Dashboard**: Ana sayfa
- **Profile Form**: Profil oluşturma/düzenleme
- **Profile View**: Profil görüntüleme

## 🛠️ Teknolojiler

- **React 18**
- **TypeScript**
- **React Router DOM**
- **Axios**
- **Docker**
- **Nginx**

## 🚀 Hızlı Başlangıç

### Geliştirme Ortamı

1. **Bağımlılıkları yükleyin:**
```bash
npm install
```

2. **Uygulamayı başlatın:**
```bash
npm start
```

3. **Tarayıcıda açın:**
```
http://localhost:3000
```

### Docker ile Çalıştırma

1. **Docker image'ı build edin:**
```bash
docker build -t nutrilife-frontend .
```

2. **Container'ı çalıştırın:**
```bash
docker run -p 3000:80 nutrilife-frontend
```

### Ana Proje ile Çalıştırma

Ana proje dizininde:
```bash
docker-compose up -d
```

## 🔧 Konfigürasyon

### Environment Variables

| Değişken | Açıklama | Varsayılan |
|----------|----------|------------|
| `REACT_APP_API_BASE_URL` | Core API URL | `http://localhost:8080` |
| `REACT_APP_AUTH_API_URL` | Auth API URL | `http://localhost:8081` |
| `REACT_APP_PROFILE_API_URL` | Profile API URL | `http://localhost:8082` |

### API Entegrasyonu

Frontend aşağıdaki servislerle entegre çalışır:

- **Auth Service** (Port 8081): Kullanıcı girişi ve kaydı
- **Profile Service** (Port 8082): Profil yönetimi
- **Core Service** (Port 8080): Ana işlevler

## 📱 Kullanım

### 1. Kayıt Olma
- `/register` sayfasında yeni hesap oluşturun
- Gerekli bilgileri doldurun
- Kayıt olduktan sonra otomatik giriş yapılır

### 2. Giriş Yapma
- `/login` sayfasında mevcut hesabınızla giriş yapın
- JWT token otomatik olarak saklanır

### 3. Profil Yönetimi
- Dashboard'da profil oluşturun veya düzenleyin
- Fiziksel özellikler, hedefler ve sağlık bilgilerini girin
- BMI, BMR, TDEE gibi değerler otomatik hesaplanır

## 🔒 Güvenlik

- JWT token tabanlı authentication
- Token otomatik yenileme
- 401 durumunda otomatik logout
- CORS konfigürasyonu
- XSS ve CSRF koruması

## 📊 Özellikler

### Profil Yönetimi
- Kişisel bilgiler (ad, soyad, telefon, doğum tarihi)
- Fiziksel özellikler (boy, kilo, hedef kilo)
- Aktivite seviyesi ve hedefler
- Sağlık bilgileri (alerjiler, tıbbi durumlar)
- Otomatik hesaplamalar (BMI, BMR, TDEE)

### Kullanıcı Deneyimi
- Responsive tasarım
- Loading states
- Error handling
- Form validation
- Auto-logout on token expiry

## 🐳 Docker

### Build
```bash
docker build -t nutrilife-frontend .
```

### Run
```bash
docker run -p 3000:80 nutrilife-frontend
```

### Docker Compose
```yaml
nutrilife-frontend:
  build: ./nutrilife-frontend
  ports:
    - "3000:80"
  environment:
    REACT_APP_AUTH_API_URL: http://localhost:8081
    REACT_APP_PROFILE_API_URL: http://localhost:8082
```

## 📝 Geliştirme

### Proje Yapısı
```
src/
├── components/          # React bileşenleri
│   ├── Login.tsx
│   ├── Register.tsx
│   ├── Dashboard.tsx
│   ├── ProfileForm.tsx
│   └── ProfileView.tsx
├── contexts/            # Context providers
│   └── AuthContext.tsx
├── services/            # API servisleri
│   └── api.ts
├── config/              # Konfigürasyon
│   └── environment.ts
└── App.tsx
```

### Scripts
```bash
npm start          # Geliştirme sunucusu
npm run build      # Production build
npm test           # Testleri çalıştır
npm run eject      # Eject (dikkatli kullanın)
```

## 🤝 Katkıda Bulunma

1. Fork yapın
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push yapın (`git push origin feature/amazing-feature`)
5. Pull Request oluşturun

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## 📞 İletişim

- **Proje**: NutriLife Frontend
- **Versiyon**: 1.0.0
- **Geliştirici**: NutriLife Team