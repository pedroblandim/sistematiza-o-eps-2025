import type { Task, CreateTaskRequest, UpdateTaskRequest, ApprovalRequest } from '../types/task';
import { authService } from './authService';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

class ApiService {
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const token = authService.getToken();

    const defaultOptions: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
      },
    };

    const mergedOptions = { ...defaultOptions, ...options };
    if (options?.headers) {
      mergedOptions.headers = {
        ...defaultOptions.headers,
        ...options.headers,
      };
    }

    const response = await fetch(url, mergedOptions);

    if (!response.ok) {
      if (response.status === 401) {
        authService.logout();
        window.location.reload();
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }

  async createTask(taskData: CreateTaskRequest): Promise<Task> {
    return this.request<Task>('/tasks', {
      method: 'POST',
      body: JSON.stringify(taskData),
    });
  }

  async updateTask(taskData: UpdateTaskRequest): Promise<Task> {
    return this.request<Task>('/tasks', {
      method: 'PUT',
      body: JSON.stringify(taskData),
    });
  }

  async getTasks(): Promise<Task[]> {
    return this.request<Task[]>('/tasks');
  }

  // Admin methods
  async getTasksForApproval(): Promise<Task[]> {
    return this.request<Task[]>('/admin/tasks/pending-approval');
  }

  async processTaskApproval(request: ApprovalRequest): Promise<Task> {
    const endpoint = request.approved ? '/admin/tasks/approve' : '/admin/tasks/reject';
    return this.request<Task>(endpoint, {
      method: 'POST',
      body: JSON.stringify(request),
    });
  }
}

export const apiService = new ApiService();