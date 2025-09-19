import type { Task } from '../types/task';

interface TaskListProps {
  tasks: Task[];
  onEditTask: (task: Task) => void;
}

export function TaskList({ tasks, onEditTask }: TaskListProps) {
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
      {tasks.map((task) => (
        <div key={task.taskId} className="task-item">
          <div className="task-header">
            <h3>{task.title}</h3>
            <button
              onClick={() => onEditTask(task)}
              className="edit-btn"
            >
              Editar
            </button>
          </div>

          <div className="task-content">
            <p>{task.content}</p>
          </div>

          <div className="task-dates">
            <span>Criado: {new Date(task.createDate).toLocaleDateString('pt-BR')}</span>
            <span>Modificado: {new Date(task.modifiedDate).toLocaleDateString('pt-BR')}</span>
          </div>
        </div>
      ))}
    </div>
  );
}