import { useState, useEffect } from 'react';
import { ApprovalTaskList } from './ApprovalTaskList';
import { apiService } from '../services/api';
import type { Task, ApprovalRequest } from '../types/task';

export function AdminDashboard() {
  const [pendingTasks, setPendingTasks] = useState<Task[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadPendingTasks();
  }, []);

  const loadPendingTasks = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const tasks = await apiService.getTasksForApproval();
      setPendingTasks(tasks);
    } catch (err) {
      setError('Erro ao carregar tasks pendentes: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleApproval = async (request: ApprovalRequest) => {
    await apiService.processTaskApproval(request);

    // Remover a task da lista após processamento
    setPendingTasks(prev =>
      prev.filter(task => task.taskId !== request.taskId)
    );

    // Opcional: mostrar mensagem de sucesso
    console.log(
      `Task ${request.approved ? 'aprovada' : 'rejeitada'} com sucesso`
    );
  };

  return (
    <div className="admin-dashboard">
      <header className="admin-header">
        <h1>Painel Administrativo</h1>
        <button onClick={loadPendingTasks} className="refresh-btn">
          Atualizar Lista
        </button>
      </header>

      <main className="admin-main">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <ApprovalTaskList
          tasks={pendingTasks}
          onApproveTask={handleApproval}
          isLoading={isLoading}
        />
      </main>
    </div>
  );
}