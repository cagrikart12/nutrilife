import React, { useState } from 'react';
import { profileService } from '../services/api';

interface ProfileFormProps {
  onProfileCreated: (profile: any) => void;
  initialData?: any;
  isEdit?: boolean;
}

const ProfileForm: React.FC<ProfileFormProps> = ({ onProfileCreated, initialData, isEdit = false }) => {
  const [formData, setFormData] = useState({
    firstName: initialData?.firstName || '',
    lastName: initialData?.lastName || '',
    phoneNumber: initialData?.phoneNumber || '',
    birthDate: initialData?.birthDate || '',
    gender: initialData?.gender || '',
    height: initialData?.height || '',
    weight: initialData?.weight || '',
    activityLevel: initialData?.activityLevel || '',
    goal: initialData?.goal || '',
    targetWeight: initialData?.targetWeight || '',
    dailyCalorieGoal: initialData?.dailyCalorieGoal || '',
    allergies: initialData?.allergies || '',
    medicalConditions: initialData?.medicalConditions || '',
    dietaryPreferences: initialData?.dietaryPreferences || '',
    profilePictureUrl: initialData?.profilePictureUrl || '',
    bio: initialData?.bio || '',
  });

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const profileData = {
        ...formData,
        height: formData.height ? parseFloat(formData.height) : undefined,
        weight: formData.weight ? parseFloat(formData.weight) : undefined,
        targetWeight: formData.targetWeight ? parseFloat(formData.targetWeight) : undefined,
        dailyCalorieGoal: formData.dailyCalorieGoal ? parseInt(formData.dailyCalorieGoal) : undefined,
      };

      const response = isEdit 
        ? await profileService.updateProfile(profileData)
        : await profileService.createProfile(profileData);
      
      onProfileCreated(response);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Profil kaydedilemedi');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: '600px' }}>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
        <div>
          <label htmlFor="firstName">Ad *</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
        <div>
          <label htmlFor="lastName">Soyad *</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            required
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="phoneNumber">Telefon</label>
        <input
          type="tel"
          id="phoneNumber"
          name="phoneNumber"
          value={formData.phoneNumber}
          onChange={handleChange}
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
        <div>
          <label htmlFor="birthDate">Doğum Tarihi</label>
          <input
            type="date"
            id="birthDate"
            name="birthDate"
            value={formData.birthDate}
            onChange={handleChange}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
        <div>
          <label htmlFor="gender">Cinsiyet</label>
          <select
            id="gender"
            name="gender"
            value={formData.gender}
            onChange={handleChange}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          >
            <option value="">Seçiniz</option>
            <option value="MALE">Erkek</option>
            <option value="FEMALE">Kadın</option>
            <option value="OTHER">Diğer</option>
          </select>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
        <div>
          <label htmlFor="height">Boy (cm)</label>
          <input
            type="number"
            id="height"
            name="height"
            value={formData.height}
            onChange={handleChange}
            min="50"
            max="300"
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
        <div>
          <label htmlFor="weight">Kilo (kg)</label>
          <input
            type="number"
            id="weight"
            name="weight"
            value={formData.weight}
            onChange={handleChange}
            min="20"
            max="500"
            step="0.1"
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
        <div>
          <label htmlFor="activityLevel">Aktivite Seviyesi</label>
          <select
            id="activityLevel"
            name="activityLevel"
            value={formData.activityLevel}
            onChange={handleChange}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          >
            <option value="">Seçiniz</option>
            <option value="SEDENTARY">Hareketsiz</option>
            <option value="LIGHTLY_ACTIVE">Hafif Aktif</option>
            <option value="MODERATELY_ACTIVE">Orta Aktif</option>
            <option value="VERY_ACTIVE">Çok Aktif</option>
            <option value="EXTRA_ACTIVE">Aşırı Aktif</option>
          </select>
        </div>
        <div>
          <label htmlFor="goal">Hedef</label>
          <select
            id="goal"
            name="goal"
            value={formData.goal}
            onChange={handleChange}
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          >
            <option value="">Seçiniz</option>
            <option value="WEIGHT_LOSS">Kilo Verme</option>
            <option value="WEIGHT_GAIN">Kilo Alma</option>
            <option value="WEIGHT_MAINTENANCE">Kilo Koruma</option>
            <option value="MUSCLE_GAIN">Kas Kazanma</option>
            <option value="GENERAL_HEALTH">Genel Sağlık</option>
          </select>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px', marginBottom: '15px' }}>
        <div>
          <label htmlFor="targetWeight">Hedef Kilo (kg)</label>
          <input
            type="number"
            id="targetWeight"
            name="targetWeight"
            value={formData.targetWeight}
            onChange={handleChange}
            min="20"
            max="500"
            step="0.1"
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
        <div>
          <label htmlFor="dailyCalorieGoal">Günlük Kalori Hedefi</label>
          <input
            type="number"
            id="dailyCalorieGoal"
            name="dailyCalorieGoal"
            value={formData.dailyCalorieGoal}
            onChange={handleChange}
            min="800"
            max="5000"
            style={{ width: '100%', padding: '8px', marginTop: '5px' }}
          />
        </div>
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="allergies">Alerjiler</label>
        <input
          type="text"
          id="allergies"
          name="allergies"
          value={formData.allergies}
          onChange={handleChange}
          placeholder="Virgülle ayırın (örn: fındık, süt, yumurta)"
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="medicalConditions">Tıbbi Durumlar</label>
        <input
          type="text"
          id="medicalConditions"
          name="medicalConditions"
          value={formData.medicalConditions}
          onChange={handleChange}
          placeholder="Virgülle ayırın"
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="dietaryPreferences">Diyet Tercihleri</label>
        <input
          type="text"
          id="dietaryPreferences"
          name="dietaryPreferences"
          value={formData.dietaryPreferences}
          onChange={handleChange}
          placeholder="Virgülle ayırın (örn: vejetaryen, glutensiz)"
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="profilePictureUrl">Profil Fotoğrafı URL</label>
        <input
          type="url"
          id="profilePictureUrl"
          name="profilePictureUrl"
          value={formData.profilePictureUrl}
          onChange={handleChange}
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      <div style={{ marginBottom: '15px' }}>
        <label htmlFor="bio">Biyografi</label>
        <textarea
          id="bio"
          name="bio"
          value={formData.bio}
          onChange={handleChange}
          rows={3}
          maxLength={500}
          style={{ width: '100%', padding: '8px', marginTop: '5px' }}
        />
      </div>

      {error && (
        <div style={{ color: 'red', marginBottom: '15px' }}>
          {error}
        </div>
      )}

      <button
        type="submit"
        disabled={isLoading}
        style={{
          padding: '10px 20px',
          backgroundColor: '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '4px',
          cursor: isLoading ? 'not-allowed' : 'pointer',
        }}
      >
        {isLoading ? 'Kaydediliyor...' : (isEdit ? 'Güncelle' : 'Profil Oluştur')}
      </button>
    </form>
  );
};

export default ProfileForm;
