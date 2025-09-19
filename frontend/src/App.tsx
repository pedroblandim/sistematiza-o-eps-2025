import { useState, useEffect } from 'react'
import { TaskForm } from './components/TaskForm'
import { TaskList } from './components/TaskList'
import { apiService } from './services/api'
import type { Task, CreateTaskRequest, UpdateTaskRequest } from './types/task'
import './App.css'

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [editingTask, setEditingTask] = useState<Task | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const loadTasks = async () => {
      setIsLoading(true)
      setError(null)

      try {
        const taskList = await apiService.getTasks()
        setTasks(taskList)
      } catch (err) {
        setError('Erro ao carregar tarefas: ' + (err as Error).message)
      } finally {
        setIsLoading(false)
      }
    }

    loadTasks()
  }, [])

  const handleCreateTask = async (taskData: CreateTaskRequest) => {
    setIsLoading(true)
    setError(null)

    try {
      const newTask = await apiService.createTask(taskData)
      setTasks(prev => [...prev, newTask])
    } catch (err) {
      setError('Erro ao criar tarefa: ' + (err as Error).message)
    } finally {
      setIsLoading(false)
    }
  }

  const handleUpdateTask = async (taskData: UpdateTaskRequest) => {
    setIsLoading(true)
    setError(null)

    try {
      const updatedTask = await apiService.updateTask(taskData)
      console.log('Task updated:', updatedTask)
      console.log('Updating task with ID:', updatedTask.taskId)

      setTasks(prev => {
        const newTasks = prev.map(task => {
          console.log('Comparing:', task.taskId, 'with', updatedTask.taskId, task.taskId === updatedTask.taskId)
          return task.taskId === updatedTask.taskId ? updatedTask : task
        })
        console.log('New tasks array:', newTasks)
        return newTasks
      })
    } catch (err) {
      setError('Erro ao atualizar tarefa: ' + (err as Error).message)
    } finally {
      setIsLoading(false)
    }
  }

  const handleSubmitTask = async (taskData: CreateTaskRequest | UpdateTaskRequest) => {
    if ('taskId' in taskData) {
      await handleUpdateTask(taskData)
    } else {
      await handleCreateTask(taskData)
    }
  }

  const handleFormSuccess = () => {
    if (editingTask) {
      setEditingTask(null)
    }
  }

  const handleEditTask = (task: Task) => {
    setEditingTask(task)
  }

  const handleCancelEdit = () => {
    setEditingTask(null)
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>Sistema de Gamificação - Tarefas</h1>
      </header>

      <main className="app-main">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <section className="task-form-section">
          <h2>{editingTask ? 'Editar Tarefa' : 'Nova Tarefa'}</h2>

          {editingTask && (
            <button onClick={handleCancelEdit} className="cancel-btn">
              Cancelar Edição
            </button>
          )}

          <TaskForm
            onSubmit={handleSubmitTask}
            initialData={editingTask ? {
              taskId: editingTask.taskId,
              title: editingTask.title,
              content: editingTask.content
            } : undefined}
            isUpdate={!!editingTask}
            onSuccess={handleFormSuccess}
          />
        </section>

        <section className="task-list-section">
          <TaskList
            tasks={tasks}
            onEditTask={handleEditTask}
          />
        </section>

        {isLoading && (
          <div className="loading">
            Carregando...
          </div>
        )}
      </main>
    </div>
  )
}

export default App
