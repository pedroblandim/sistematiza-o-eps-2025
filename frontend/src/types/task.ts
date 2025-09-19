export interface Task {
  taskId: string;
  title: string;
  content: string;
  createDate: string;
  modifiedDate: string;
}

export interface CreateTaskRequest {
  title: string;
  content: string;
  taskTypeId: string;
}

export interface UpdateTaskRequest {
  taskId: string;
  title: string;
  content: string;
}

export const TaskStatus = {
  DRAFT: 'DRAFT',
  SUBMITTED_FOR_APPROVAL: 'SUBMITTED_FOR_APPROVAL',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  CANCELLED: 'CANCELLED'
} as const;

export type TaskStatus = typeof TaskStatus[keyof typeof TaskStatus];