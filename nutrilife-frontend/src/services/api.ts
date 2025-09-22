import axios from 'axios';
import { config } from '../config/environment';

const API_BASE_URL = config.API_BASE_URL;
const AUTH_API_URL = config.AUTH_API_URL;
const PROFILE_API_URL = config.PROFILE_API_URL;

// Axios instance'ları
export const authApi = axios.create({
  baseURL: AUTH_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const profileApi = axios.create({
  baseURL: PROFILE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Token'ı localStorage'dan al ve header'a ekle
const addAuthToken = (config: any) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

// Request interceptor - her istekte token ekle
authApi.interceptors.request.use(addAuthToken);
profileApi.interceptors.request.use(addAuthToken);

// Response interceptor - 401 durumunda token'ı temizle
const handleAuthError = (error: any) => {
  if (error.response?.status === 401) {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  }
  return Promise.reject(error);
};

authApi.interceptors.response.use(
  (response) => response,
  handleAuthError
);

profileApi.interceptors.response.use(
  (response) => response,
  handleAuthError
);

// Auth API fonksiyonları
export const authService = {
  login: async (credentials: { username: string; password: string }) => {
    const response = await authApi.post('/api/auth/login', credentials);
    return response.data;
  },

  register: async (userData: {
    username: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
  }) => {
    const response = await authApi.post('/api/auth/register', userData);
    return response.data;
  },

  validateToken: async (token: string) => {
    const response = await authApi.post('/api/auth/validate', { token });
    return response.data;
  },

  refreshToken: async (refreshToken: string) => {
    const response = await authApi.post('/api/auth/refresh', { refreshToken });
    return response.data;
  },
};

// Profile API fonksiyonları
export const profileService = {
  createProfile: async (profileData: any) => {
    const response = await profileApi.post('/api/profiles', profileData);
    return response.data;
  },

  getProfile: async () => {
    const response = await profileApi.get('/api/profiles');
    return response.data;
  },

  updateProfile: async (profileData: any) => {
    const response = await profileApi.put('/api/profiles', profileData);
    return response.data;
  },

  deleteProfile: async () => {
    const response = await profileApi.delete('/api/profiles');
    return response.data;
  },

  checkProfileExists: async () => {
    const response = await profileApi.get('/api/profiles/exists');
    return response.data;
  },

  searchProfiles: async (params: any) => {
    const response = await profileApi.get('/api/profiles/search', { params });
    return response.data;
  },
};
