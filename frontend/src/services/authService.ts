import type { LoginRequest, LoginResponse } from '../types/auth';

const API_BASE_URL = 'http://localhost:8080';

class AuthService {
  private tokenKey = 'auth_token';

  async login(credentials: LoginRequest): Promise<string> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      throw new Error('Falha na autenticação');
    }

    const data: LoginResponse = await response.json();

    if (!data.token) {
      throw new Error('Credenciais inválidas');
    }

    this.setToken(data.token);
    return data.token;
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }

  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }
}

export const authService = new AuthService();