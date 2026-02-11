import { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';
import toast from 'react-hot-toast';
import type { User } from '@/types/User';

const AuthContext = createContext(null);


export const AuthProvider = ({ children } : any) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(localStorage.getItem('token'));
  
  useEffect(() => {
    const initAuth = async () => {
      const storedToken = localStorage.getItem('token');
      const storedUser = localStorage.getItem('user');

      if (storedToken && storedUser) {
        setToken(storedToken);
        setUser(JSON.parse(storedUser));
      }
      
      setLoading(false);
    };

    initAuth();
  }, []);

  const login = async (email : string, password : string) => {
    try {
      const response = await authService.login(email, password);
      console.log(response);
      
      
      localStorage.setItem('token', response.data.accessToken);
      localStorage.setItem('user', JSON.stringify(response.data.email));
      
      setToken(response.data.accessToken);
      setUser(response.data.email);
      
      toast.success('Login successful!');
      return { success: true };
    } catch (error : any) {
      const message = error.response?.data?.message || 'Login failed';
      console.log(message);
      toast.error(message);
      return { success: false, error: message };
    }
  };

  const signup = async (userData : User ) => {
    try {
      const response = await authService.signup(userData);
      toast.success('Account created successfully! Please login.');
      return { success: true };
    } catch (error : any) {
      const message = error.response?.data?.message || 'Signup failed';
      console.log(message);
      toast.error("Signup failed");
      return { success: false, error: message };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
    toast.success('Logged out successfully');
  };

  const isAuthenticated = () => {
    return Boolean(token) && !!user;
  };

  const hasRole = (role : any) => {
    return user?.role === role;
  };

  const value = {
    user,
    token,
    loading,
    login,
    signup,
    logout,
    isAuthenticated,
    hasRole,
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}


export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};