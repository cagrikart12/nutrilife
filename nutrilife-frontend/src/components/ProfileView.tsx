import React, { useState } from 'react';
import { profileService } from '../services/api';
import ProfileForm from './ProfileForm';

interface ProfileViewProps {
  profile: any;
  onProfileUpdated: (profile: any) => void;
  onProfileDeleted: () => void;
}

const ProfileView: React.FC<ProfileViewProps> = ({ profile, onProfileUpdated, onProfileDeleted }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const handleDelete = async () => {
    if (window.confirm('Profilinizi silmek istediğinizden emin misiniz?')) {
      setIsDeleting(true);
      try {
        await profileService.deleteProfile();
        onProfileDeleted();
      } catch (error) {
        console.error('Profile deletion error:', error);
        alert('Profil silinemedi');
      } finally {
        setIsDeleting(false);
      }
    }
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return 'Belirtilmemiş';
    return new Date(dateString).toLocaleDateString('tr-TR');
  };

  const formatGender = (gender: string) => {
    const genderMap: { [key: string]: string } = {
      'MALE': 'Erkek',
      'FEMALE': 'Kadın',
      'OTHER': 'Diğer'
    };
    return genderMap[gender] || 'Belirtilmemiş';
  };

  const formatActivityLevel = (level: string) => {
    const levelMap: { [key: string]: string } = {
      'SEDENTARY': 'Hareketsiz',
      'LIGHTLY_ACTIVE': 'Hafif Aktif',
      'MODERATELY_ACTIVE': 'Orta Aktif',
      'VERY_ACTIVE': 'Çok Aktif',
      'EXTRA_ACTIVE': 'Aşırı Aktif'
    };
    return levelMap[level] || 'Belirtilmemiş';
  };

  const formatGoal = (goal: string) => {
    const goalMap: { [key: string]: string } = {
      'WEIGHT_LOSS': 'Kilo Verme',
      'WEIGHT_GAIN': 'Kilo Alma',
      'WEIGHT_MAINTENANCE': 'Kilo Koruma',
      'MUSCLE_GAIN': 'Kas Kazanma',
      'GENERAL_HEALTH': 'Genel Sağlık'
    };
    return goalMap[goal] || 'Belirtilmemiş';
  };

  if (isEditing) {
    return (
      <div>
        <h3>Profili Düzenle</h3>
        <ProfileForm
          initialData={profile}
          isEdit={true}
          onProfileCreated={onProfileUpdated}
        />
        <button
          onClick={() => setIsEditing(false)}
          style={{
            marginTop: '10px',
            padding: '8px 16px',
            backgroundColor: '#6c757d',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
          }}
        >
          İptal
        </button>
      </div>
    );
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h3>Profil Bilgileri</h3>
        <div>
          <button
            onClick={() => setIsEditing(true)}
            style={{
              marginRight: '10px',
              padding: '8px 16px',
              backgroundColor: '#007bff',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
            }}
          >
            Düzenle
          </button>
          <button
            onClick={handleDelete}
            disabled={isDeleting}
            style={{
              padding: '8px 16px',
              backgroundColor: '#dc3545',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: isDeleting ? 'not-allowed' : 'pointer',
            }}
          >
            {isDeleting ? 'Siliniyor...' : 'Sil'}
          </button>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
        <div>
          <h4>Kişisel Bilgiler</h4>
          <div style={{ marginBottom: '10px' }}>
            <strong>Ad Soyad:</strong> {profile.firstName} {profile.lastName}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Telefon:</strong> {profile.phoneNumber || 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Doğum Tarihi:</strong> {formatDate(profile.birthDate)}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Yaş:</strong> {profile.age || 'Hesaplanamadı'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Cinsiyet:</strong> {formatGender(profile.gender)}
          </div>
        </div>

        <div>
          <h4>Fiziksel Özellikler</h4>
          <div style={{ marginBottom: '10px' }}>
            <strong>Boy:</strong> {profile.height ? `${profile.height} cm` : 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Kilo:</strong> {profile.weight ? `${profile.weight} kg` : 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Hedef Kilo:</strong> {profile.targetWeight ? `${profile.targetWeight} kg` : 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>BMI:</strong> {profile.bmi ? `${profile.bmi} (${profile.bmiCategory})` : 'Hesaplanamadı'}
          </div>
        </div>

        <div>
          <h4>Hedef ve Aktivite</h4>
          <div style={{ marginBottom: '10px' }}>
            <strong>Aktivite Seviyesi:</strong> {formatActivityLevel(profile.activityLevel)}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Hedef:</strong> {formatGoal(profile.goal)}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Günlük Kalori Hedefi:</strong> {profile.dailyCalorieGoal ? `${profile.dailyCalorieGoal} kcal` : 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>BMR:</strong> {profile.bmr ? `${profile.bmr} kcal/gün` : 'Hesaplanamadı'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>TDEE:</strong> {profile.tdee ? `${profile.tdee} kcal/gün` : 'Hesaplanamadı'}
          </div>
        </div>

        <div>
          <h4>Sağlık Bilgileri</h4>
          <div style={{ marginBottom: '10px' }}>
            <strong>Alerjiler:</strong> {profile.allergies || 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Tıbbi Durumlar:</strong> {profile.medicalConditions || 'Belirtilmemiş'}
          </div>
          <div style={{ marginBottom: '10px' }}>
            <strong>Diyet Tercihleri:</strong> {profile.dietaryPreferences || 'Belirtilmemiş'}
          </div>
        </div>
      </div>

      {profile.bio && (
        <div style={{ marginTop: '20px' }}>
          <h4>Biyografi</h4>
          <p style={{ padding: '10px', backgroundColor: '#f8f9fa', borderRadius: '4px' }}>
            {profile.bio}
          </p>
        </div>
      )}

      {profile.profilePictureUrl && (
        <div style={{ marginTop: '20px' }}>
          <h4>Profil Fotoğrafı</h4>
          <img
            src={profile.profilePictureUrl}
            alt="Profil"
            style={{ maxWidth: '200px', maxHeight: '200px', borderRadius: '8px' }}
            onError={(e) => {
              (e.target as HTMLImageElement).style.display = 'none';
            }}
          />
        </div>
      )}

      <div style={{ marginTop: '20px', fontSize: '12px', color: '#6c757d' }}>
        <p>Oluşturulma: {new Date(profile.createdAt).toLocaleString('tr-TR')}</p>
        <p>Son Güncelleme: {new Date(profile.updatedAt).toLocaleString('tr-TR')}</p>
      </div>
    </div>
  );
};

export default ProfileView;
