import { useState } from 'react';
import type { Task, ApprovalRequest } from '../types/task';

interface ApprovalTaskListProps {
  tasks: Task[];
  onApproveTask: (request: ApprovalRequest) => Promise<void>;
  isLoading?: boolean;
}

export function ApprovalTaskList({ tasks, onApproveTask, isLoading }: ApprovalTaskListProps) {
  const [processingTasks, setProcessingTasks] = useState<Set<string>>(new Set());
  const [comments, setComments] = useState<Record<string, string>>({});

  const handleApproval = async (taskId: string, approved: boolean) => {
    setProcessingTasks(prev => new Set(prev).add(taskId));

    try {
      await onApproveTask({
        taskId,
        approved,
        comments: comments[taskId] || undefined,
      });

      // Limpar comentários após processamento
      setComments(prev => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { [taskId]: _, ...rest } = prev;
        return rest;
      });
    } catch (error) {
      console.error('Erro ao processar aprovação:', error);
    } finally {
      setProcessingTasks(prev => {
        const newSet = new Set(prev);
        newSet.delete(taskId);
        return newSet;
      });
    }
  };

  const handleCommentChange = (taskId: string, comment: string) => {
    setComments(prev => ({
      ...prev,
      [taskId]: comment,
    }));
  };

  if (isLoading) {
    return (
      <div className="approval-list">
        <h2>Tasks Pendentes de Aprovação</h2>
        <div className="loading">Carregando tasks...</div>
      </div>
    );
  }

  if (tasks.length === 0) {
    return (
      <div className="approval-list">
        <h2>Tasks Pendentes de Aprovação</h2>
        <div className="approval-empty">
          Não há tasks pendentes de aprovação.
        </div>
      </div>
    );
  }

  return (
    <div className="approval-list">
      <h2>Tasks Pendentes de Aprovação ({tasks.length})</h2>

      <div className="approval-tasks">
        {tasks.map((task) => {
          const isProcessing = processingTasks.has(task.taskId);

          return (
            <div key={task.taskId} className="approval-task-item">
              <div className="approval-task-header">
                <h3>{task.title}</h3>
                <div className="approval-task-meta">
                  <span className="task-user">Por: {task.userEmail || task.userId}</span>
                  <span className="task-date">
                    Submetida em: {new Date(task.modifiedDate).toLocaleDateString('pt-BR')}
                  </span>
                </div>
              </div>

              <div className="approval-task-content">
                <p>{task.content}</p>
              </div>

              <div className="approval-task-actions">
                <div className="approval-comments">
                  <label htmlFor={`comments-${task.taskId}`}>
                    Comentários (opcional):
                  </label>
                  <textarea
                    id={`comments-${task.taskId}`}
                    value={comments[task.taskId] || ''}
                    onChange={(e) => handleCommentChange(task.taskId, e.target.value)}
                    placeholder="Adicione comentários sobre a decisão..."
                    disabled={isProcessing}
                    rows={3}
                  />
                </div>

                <div className="approval-buttons">
                  <button
                    onClick={() => handleApproval(task.taskId, false)}
                    disabled={isProcessing}
                    className="reject-btn"
                  >
                    {isProcessing ? 'Processando...' : 'Rejeitar'}
                  </button>

                  <button
                    onClick={() => handleApproval(task.taskId, true)}
                    disabled={isProcessing}
                    className="approve-btn"
                  >
                    {isProcessing ? 'Processando...' : 'Aprovar'}
                  </button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}