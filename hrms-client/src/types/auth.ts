export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  confirmPassword?: string;
  firstName: string;
  lastName: string;
  phone?: string;
  dateOfBirth: string;
  dateOfJoining: string;
  profilePhotoUrl?: string;
  department: string;
  designation: string;
  role?: 'EMPLOYEE' | 'HR' | 'MANAGER';
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  email: string;
  role: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}
src/types/user.ts
typescriptexport interface User {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  dateOfBirth?: string;
  dateOfJoining: string;
  profilePhotoUrl?: string;
  department: string;
  designation: string;
  role: 'EMPLOYEE' | 'HR' | 'MANAGER';
  createdAt: string;
  updatedAt: string;
}
