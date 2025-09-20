import { useState } from 'react';
import type { Task } from '../types/task';
import { TaskStatus } from '../types/task';

interface ProcessedTaskListProps {
  tasks: Task[];
  isLoading?: boolean;
}

export function ProcessedTaskList({ tasks, isLoading }: ProcessedTaskListProps) {
  const [filterStatus, setFilterStatus] = useState<'ALL' | 'APPROVED' | 'REJECTED'>('ALL');

  const getStatusText = (status: string) => {
    switch (status) {
      case TaskStatus.APPROVED:
        return 'Aprovada';
      case TaskStatus.REJECTED:
        return 'Rejeitada';
      default:
        return status;
    }
  };

  const getStatusClass = (status: string) => {
    switch (status) {
      case TaskStatus.APPROVED:
        return 'status-approved';
      case TaskStatus.REJECTED:
        return 'status-rejected';
      default:
        return 'status-default';
    }
  };

  const filteredTasks = tasks.filter(task => {
    if (filterStatus === 'ALL') {
      return task.status === TaskStatus.APPROVED || task.status === TaskStatus.REJECTED;
    }
    return task.status === filterStatus;
  });

  if (isLoading) {
    return (
      <div className="processed-list">
        <h2>Tasks Processadas</h2>
        <div className="loading">Carregando tasks...</div>
      </div>
    );
  }

  return (
    <div className="processed-list">
      <div className="processed-header">
        <h2>Tasks Processadas ({filteredTasks.length})</h2>

        <div className="filter-controls">
          <label htmlFor="status-filter">Filtrar por status:</label>
          <select
            id="status-filter"
            value={filterStatus}
            onChange={(e) => setFilterStatus(e.target.value as 'ALL' | 'APPROVED' | 'REJECTED')}
            className="status-filter"
          >
            <option value="ALL">Todas</option>
            <option value="APPROVED">Aprovadas</option>
            <option value="REJECTED">Rejeitadas</option>
          </select>
        </div>
      </div>

      {filteredTasks.length === 0 ? (
        <div className="processed-empty">
          Não há tasks processadas{filterStatus !== 'ALL' ? ` com status ${getStatusText(filterStatus)}` : ''}.
        </div>
      ) : (
        <div className="processed-tasks">
          {filteredTasks.map((task) => (
            <div key={task.taskId} className="processed-task-item">
              <div className="processed-task-header">
                <div className="task-title-status">
                  <h3>{task.title}</h3>
                  <span className={`task-status ${getStatusClass(task.status)}`}>
                    {getStatusText(task.status)}
                  </span>
                </div>
                <div className="processed-task-meta">
                  <span className="task-user">Por: {task.userEmail}</span>
                  <span className="task-date">
                    Processada em: {new Date(task.modifiedDate).toLocaleDateString('pt-BR')}
                  </span>
                </div>
              </div>

              <div className="processed-task-content">
                <p>{task.content}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}