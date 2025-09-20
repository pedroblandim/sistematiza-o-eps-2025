import type { LoginRequest, LoginResponse, User } from '../types/auth';
import { jwtDecode } from 'jwt-decode';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class AuthService {
  private tokenKey = 'auth_token';
  private userKey = 'auth_user';

  async login(credentials: LoginRequest): Promise<LoginResponse> {
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

    const userData = this.extractUserFromToken(data.token);
    this.setUser(userData);

    return { ...data, user: userData };
  }

  private extractUserFromToken(token: string): User {
    return this.extractUserFromTokenPublic(token);
  }

  extractUserFromTokenPublic(token: string): User {
    try {
      const payload = jwtDecode<{
        sub?: string;
        userId?: string;
        email: string;
        isAdmin: boolean
      }>(token);

      return {
        id: payload.userId || payload.sub || 'unknown',
        email: payload.email,
        isAdmin: payload.isAdmin || false,
      };
    } catch (error) {
      console.error('Erro ao decodificar token JWT:', error);
      throw new Error('Token JWT inválido');
    }
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  setUser(user: User): void {
    localStorage.setItem(this.userKey, JSON.stringify(user));
  }

  getUser(): User | null {
    const userData = localStorage.getItem(this.userKey);
    return userData ? JSON.parse(userData) : null;
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }
}

export const authService = new AuthService();