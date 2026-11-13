import axios from 'axios';

export const api = axios.create({
  baseURL: 'https://oficina-gestao-os.onrender.com/api',
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('oficina_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
