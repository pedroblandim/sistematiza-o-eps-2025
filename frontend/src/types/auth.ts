export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string | null;
  user?: User;
}

export interface User {
  id: string;
  email: string;
  isAdmin: boolean;
}