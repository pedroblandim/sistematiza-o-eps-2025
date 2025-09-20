import { useState } from 'react';
import type { Task, SubmitTaskRequest } from '../types/task';
import { TaskStatus } from '../types/task';

interface TaskListProps {
  tasks: Task[];
  onEditTask: (task: Task) => void;
  onSubmitTask: (request: SubmitTaskRequest) => Promise<void>;
}

export function TaskList({ tasks, onEditTask, onSubmitTask }: TaskListProps) {
  const [submittingTasks, setSubmittingTasks] = useState<Set<string>>(new Set());

  const handleSubmitTask = async (taskId: string) => {
    setSubmittingTasks(prev => new Set(prev).add(taskId));

    try {
      await onSubmitTask({ taskId });
    } catch (error) {
      console.error('Erro ao submeter task:', error);
    } finally {
      setSubmittingTasks(prev => {
        const newSet = new Set(prev);
        newSet.delete(taskId);
        return newSet;
      });
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case TaskStatus.DRAFT:
        return 'Rascunho';
      case TaskStatus.SUBMITTED_FOR_APPROVAL:
        return 'Aguardando Aprovação';
      case TaskStatus.APPROVED:
        return 'Aprovada';
      case TaskStatus.REJECTED:
        return 'Rejeitada';
      case TaskStatus.CANCELLED:
        return 'Cancelada';
      default:
        return status;
    }
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case TaskStatus.DRAFT:
        return 'status-draft';
      case TaskStatus.SUBMITTED_FOR_APPROVAL:
        return 'status-pending';
      case TaskStatus.APPROVED:
        return 'status-approved';
      case TaskStatus.REJECTED:
        return 'status-rejected';
      case TaskStatus.CANCELLED:
        return 'status-cancelled';
      default:
        return 'status-default';
    }
  };

  if (tasks.length === 0) {
    return (
      <div className="task-list-empty">
        <p>Nenhuma tarefa encontrada.</p>
      </div>
    );
  }

  return (
    <div className="task-list">
      <h2>Tarefas</h2>
      {tasks.map((task) => {
        const isSubmitting = submittingTasks.has(task.taskId);
        const canEdit = task.status === TaskStatus.DRAFT;
        const canSubmit = task.status === TaskStatus.DRAFT;

        return (
          <div key={task.taskId} className="task-item">
            <div className="task-header">
              <div className="task-title-status">
                <h3>{task.title}</h3>
                <span className={`task-status ${getStatusClass(task.status)}`}>
                  {getStatusText(task.status)}
                </span>
              </div>
              <div className="task-actions">
                {canEdit && (
                  <button
                    onClick={() => onEditTask(task)}
                    className="edit-btn"
                    disabled={isSubmitting}
                  >
                    Editar
                  </button>
                )}
                {canSubmit && (
                  <button
                    onClick={() => handleSubmitTask(task.taskId)}
                    className="submit-btn"
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? 'Submetendo...' : 'Submeter para Aprovação'}
                  </button>
                )}
              </div>
            </div>

            <div className="task-content">
              <p>{task.content}</p>
            </div>

            <div className="task-dates">
              <span>Criado: {new Date(task.createDate).toLocaleDateString('pt-BR')}</span>
              <span>Modificado: {new Date(task.modifiedDate).toLocaleDateString('pt-BR')}</span>
            </div>
          </div>
        );
      })}
    </div>
  );
}