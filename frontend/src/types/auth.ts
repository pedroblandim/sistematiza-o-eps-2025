export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string | null;
}

export interface User {
  id: string;
  email: string;
}