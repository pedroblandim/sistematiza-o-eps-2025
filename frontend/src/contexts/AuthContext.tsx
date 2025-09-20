import { useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { authService } from '../services/authService';
import type { LoginRequest, User } from '../types/auth';
import { AuthContext } from './AuthContextType';

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = authService.getToken();
    let userData = authService.getUser();

    if (token && !userData) {
      try {
        userData = authService.extractUserFromTokenPublic(token);
        authService.setUser(userData);
      } catch {
        authService.logout();
      }
    }

    setIsAuthenticated(!!token);
    setUser(userData);
    setIsLoading(false);
  }, []);

  const login = async (credentials: LoginRequest) => {
    const { user: userData } = await authService.login(credentials);
    setIsAuthenticated(true);
    setUser(userData || null);
  };

  const logout = () => {
    authService.logout();
    setIsAuthenticated(false);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}

