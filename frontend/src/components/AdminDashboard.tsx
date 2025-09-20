import { useState, useEffect } from 'react';
import { ApprovalTaskList } from './ApprovalTaskList';
import { ProcessedTaskList } from './ProcessedTaskList';
import { apiService } from '../services/api';
import type { Task, ApprovalRequest } from '../types/task';

type TabType = 'pending' | 'processed';

export function AdminDashboard() {
  const [activeTab, setActiveTab] = useState<TabType>('pending');
  const [pendingTasks, setPendingTasks] = useState<Task[]>([]);
  const [processedTasks, setProcessedTasks] = useState<Task[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (activeTab === 'pending') {
      loadPendingTasks();
    } else {
      loadProcessedTasks();
    }
  }, [activeTab]);

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

  const loadProcessedTasks = async () => {
    setIsLoading(true);
    setError(null);

    try {
      const tasks = await apiService.getProcessedTasks();
      setProcessedTasks(tasks);
    } catch (err) {
      setError('Erro ao carregar tasks processadas: ' + (err as Error).message);
    } finally {
      setIsLoading(false);
    }
  };

  const refreshCurrentTab = () => {
    if (activeTab === 'pending') {
      loadPendingTasks();
    } else {
      loadProcessedTasks();
    }
  };

  const handleApproval = async (request: ApprovalRequest) => {
    try {
      const processedTask = await apiService.processTaskApproval(request);

      setPendingTasks(prev =>
        prev.filter(task => task.taskId !== request.taskId)
      );

      if (processedTask && Object.keys(processedTask).length > 0) {
        setProcessedTasks(prev => [processedTask, ...prev]);
      } else {
        if (activeTab === 'processed') {
          loadProcessedTasks();
        }
      }
    } catch (error) {
      console.error('Erro ao processar aprovação:', error);
      loadPendingTasks();
      if (activeTab === 'processed') {
        loadProcessedTasks();
      }
      throw error;
    }
  };

  return (
    <div className="admin-dashboard">
      <header className="admin-header">
        <h1>Painel Administrativo</h1>
        <button onClick={refreshCurrentTab} className="refresh-btn">
          Atualizar Lista
        </button>
      </header>

      <div className="admin-tabs">
        <button
          onClick={() => setActiveTab('pending')}
          className={`tab-btn ${activeTab === 'pending' ? 'active' : ''}`}
        >
          Pendentes ({pendingTasks.length})
        </button>
        <button
          onClick={() => setActiveTab('processed')}
          className={`tab-btn ${activeTab === 'processed' ? 'active' : ''}`}
        >
          Processadas ({processedTasks.length})
        </button>
      </div>

      <main className="admin-main">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {activeTab === 'pending' ? (
          <ApprovalTaskList
            tasks={pendingTasks}
            onApproveTask={handleApproval}
            isLoading={isLoading}
          />
        ) : (
          <ProcessedTaskList
            tasks={processedTasks}
            isLoading={isLoading}
          />
        )}
      </main>
    </div>
  );
}