import type { User } from '@/types/User';
import api from './api';

export const authService = {
  login: async (email : string, password : string) => {
    return api.post('/auth/login', { email, password });
  },

  signup: async (userData : User) => {
    return api.post('/auth/register', userData);
  },

  getCurrentUser: async (user : any) => {
    return api.get(`/user/${user}`);
  },

  refreshToken: async () => {
    return api.post('/auth/refresh');
  },

  logout: async () => {
    return api.post('/auth/logout');
  }
};
