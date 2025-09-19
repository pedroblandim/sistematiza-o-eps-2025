import { useState, useEffect } from 'react';
import type { CreateTaskRequest, UpdateTaskRequest } from '../types/task';

interface TaskFormProps {
  onSubmit: (task: CreateTaskRequest | UpdateTaskRequest) => void;
  initialData?: {
    taskId?: string;
    title?: string;
    content?: string;
  };
  isUpdate?: boolean;
  onSuccess?: () => void;
}

export function TaskForm({ onSubmit, initialData, isUpdate = false, onSuccess }: TaskFormProps) {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [taskTypeId] = useState('b0e7d123-4567-89ab-cdef-123456789012');

  useEffect(() => {
    if (isUpdate && initialData) {
      setTitle(initialData.title || '');
      setContent(initialData.content || '');
    } else {
      setTitle('');
      setContent('');
    }
  }, [isUpdate, initialData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (isUpdate && initialData?.taskId) {
      await onSubmit({
        taskId: initialData.taskId,
        title,
        content,
      } as UpdateTaskRequest);
    } else {
      await onSubmit({
        title,
        content,
        taskTypeId,
      } as CreateTaskRequest);
    }

    if (!isUpdate) {
      setTitle('');
      setContent('');
    }

    onSuccess?.();
  };

  return (
    <form onSubmit={handleSubmit} className="task-form">
      <div className="form-group">
        <label htmlFor="title">Título:</label>
        <input
          type="text"
          id="title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          placeholder="Digite o título da tarefa"
        />
      </div>

      <div className="form-group">
        <label htmlFor="content">Conteúdo:</label>
        <textarea
          id="content"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Digite o conteúdo da tarefa"
          rows={4}
        />
      </div>

      <button type="submit" className="submit-btn">
        {isUpdate ? 'Atualizar Tarefa' : 'Criar Tarefa'}
      </button>
    </form>
  );
}