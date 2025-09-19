import type { Task, CreateTaskRequest, UpdateTaskRequest } from '../types/task';

const API_BASE_URL = 'http://localhost:8080';

class ApiService {
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;

    const defaultOptions: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
      },
    };

    const response = await fetch(url, { ...defaultOptions, ...options });

    if (!response.ok) {
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
}

export const apiService = new ApiService();