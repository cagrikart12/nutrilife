import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { profileService } from '../services/api';
import ProfileForm from './ProfileForm';
import ProfileView from './ProfileView';

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();
  const [hasProfile, setHasProfile] = useState<boolean | null>(null);
  const [profile, setProfile] = useState<any>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    checkProfile();
  }, []);

  const checkProfile = async () => {
    try {
      const exists = await profileService.checkProfileExists();
      setHasProfile(exists);
      
      if (exists) {
        const profileData = await profileService.getProfile();
        setProfile(profileData);
      }
    } catch (err: any) {
      setError('Profil bilgileri yüklenemedi');
      console.error('Profile check error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleProfileCreated = (newProfile: any) => {
    setProfile(newProfile);
    setHasProfile(true);
  };

  const handleProfileUpdated = (updatedProfile: any) => {
    setProfile(updatedProfile);
  };

  const handleLogout = () => {
    logout();
  };

  if (isLoading) {
    return (
      <div style={{ textAlign: 'center', marginTop: '50px' }}>
        <p>Yükleniyor...</p>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' }}>
        <h1>NutriLife Dashboard</h1>
        <div>
          <span style={{ marginRight: '20px' }}>Hoş geldin, {user?.firstName} {user?.lastName}</span>
          <button
            onClick={handleLogout}
            style={{
              padding: '8px 16px',
              backgroundColor: '#dc3545',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
            }}
          >
            Çıkış Yap
          </button>
        </div>
      </div>

      {error && (
        <div style={{ color: 'red', marginBottom: '20px', padding: '10px', backgroundColor: '#f8d7da', border: '1px solid #f5c6cb', borderRadius: '4px' }}>
          {error}
        </div>
      )}

      {hasProfile === false ? (
        <div>
          <h2>Profil Oluştur</h2>
          <p>Devam etmek için profil bilgilerinizi tamamlayın.</p>
          <ProfileForm onProfileCreated={handleProfileCreated} />
        </div>
      ) : hasProfile === true ? (
        <div>
          <h2>Profil Yönetimi</h2>
          <ProfileView 
            profile={profile} 
            onProfileUpdated={handleProfileUpdated}
            onProfileDeleted={() => {
              setHasProfile(false);
              setProfile(null);
            }}
          />
        </div>
      ) : null}
    </div>
  );
};

export default Dashboard;
