# Projeto de Sistematização de **Engenharia e Projeto de Software**
## Definição do Problema
Na empresa Simplify Tecnologia, prezamos pela troca de conhecimento constante. Isso é feito por meio da troca de links de artigos, tecnologias novas, cursos, etc, no grupo do Telegram, além da relização de apresentações semanais sobre assuntos diversos. Porém, essas interações não são centralizadas em nenhum lugar específico, dificultando a sua sua consulta após o seu acontecimento. Além disso, há uma falta de incentivo para a participação nessas atividades.

## Requisitos
O software deve ter 2 objetivos principais:

1. Incentivar o compartilhamento de conhecimento (por meio de Gamificação)
2. Centralizar todo conteúdo compartilhado

### Requisitos Funcionais:
#### Colaborador:
- Ver tarefas criadas
- Criar uma tarefa com título e conteúdo
- Atualizar tarefa
- Submeter tarefa para aprovação
- Adicionar documentos na tarefa, como arquivos ou imagens (**PENDENTE**)
- Visualizar os pontos recebidos das tarefas aprovadas (**PENDENTE**)
- Trocar pontos por prêmios (**PENDENTE**)

#### Administrador
- Ver tarefas submetidas, aprovadas ou rejeitadas
- Aprovar ou rejeitar tarefa
- Solicitar ajustes ao rejeitar tarefa (**PENDENTE**)
- Criar tipos de tarefas (**PENDENTE**)
- Criar prêmios (**PENDENTE**)

### Requisitos Não Funcionais:
- Um usuário não pode deletar ou alterar conteúdos que não foram criados por ele
- Um usuário não pode visualizar tarefas de outros usuários
- Um usuário não pode editar uma tarefa que já foi submetida, aprovada ou rejeitada


## Diagrama UML
### Diagrama de Caso de Uso
<img width="483" height="481" alt="Casos de uso drawio" src="https://github.com/user-attachments/assets/dd2d07dc-da1e-49cc-a7c4-dd155e1b32f3" />

### Diagrama de Classes
<img width="650" height="376" alt="Classes drawio" src="https://github.com/user-attachments/assets/24fcb8da-7c6c-4f86-8e2b-62ac6450e912" />

## Documentação
Esse projeto foi implementado seguindo, principalmente, uma arquitetura baseada no conceito de Clean Architecture. Além disso, a arquitetura adotou o padrão de camadas, na qual divide o backend do frontend, com uma comunicação entre os dois realizada por requisições assíncronas (AJAX), sendo esse uma abordagem bem comum no mercado.

Esse projeto ainda se encontra em um estado de protótipo, ou seja, ainda está distante de ser um código disponível para ir para um ambiente de produção. 

Seguir os princípios do Clean Arch possibilitou postergar escolhar e implementações referentes a infraestrutura do projeto, de maneira que eu conseguisse priorizar as regras de negócio. Um grande exemplo disso foi o fato de que todas as regras relacionadas ao gerenciamento de tarefas já estão implementadas, porém, não foi implementado nenhum código referente a banco de dados (relacional ou não relacional). O código foi implementado de maneira que a camada de *Repository* ficasse independente de como esse dado será persistido, possibilitando que eu criase uma implementação simplificada que persiste os dados na memória *InMemoryTaskRepository*. Em algum momento, na evolução desse projeto, será necessário implementar uma camada de persistência mais robusto, com um banco de dados. 

Outras questões relacionadas a infraestrutura também foram postergadas, muitas delas relacionadas à segurança, que não faz sentido priorizar em uma fase inicial como esta.

### Desafios
#### Arquitetura
Um dos maiores desafios, certamente, foi definir a arquitetura do backend. A definição dos módulos e da estrutura de arquivos, por exemplo, influencia muito nesses quesitos. 

Outro desafio que encontrei, também nesse sentido, foi o de criar minha camada de domínio de maneira que fosse independente do framework web escolhido, nesse caso, o Spring, pelo fato deste ser um framework que, por padrão, funciona de uma maneira que o código dele deve estar constantemente ligado aos códigos do domínio da aplicação. Para contornar isso, utilizei o padrão de *Dependency Inversion*, definindo interfaces no domínio (como `TaskRepository` e `Logger`) e implementando suas dependências na camada de infraestrutura (como `InMemoryTaskRepository` e `Slf4jLogger`).

Os *Use Cases* na camada de aplicação recebem essas dependências via *Dependency Injection* configurada pelo Spring através da classe `TasksApplicationConfig`, que define os beans e injeta as implementações corretas. Dessa forma, as camadas de domínio e aplicação não possuem qualquer conhecimento sobre o Spring ou outras tecnologias externas.

Um aspecto fundamental dessa abordagem é que nenhum código das camadas *domain* e *application* possui importações de frameworks externos. Todas as dependências dessas camadas apontam exclusivamente para outros códigos do próprio domínio, garantindo total independência tecnológica e facilitando testes unitários isolados.

#### TDD
Outro desafio enfrentado foi a tentativa de implementar *Test-Driven Development* (TDD) no início do projeto. Embora a abordagem tenha sido iniciada com a escrita de testes antes da implementação, a necessidade de finalizar o projeto de sistematização no prazo e a falta de aderência à rigorosidade exigida pela prática do TDD levaram ao abandono dessa metodologia.

#### Ferramenta de CLI para Testes
Durante o desenvolvimento, foi implementada uma classe `TaskCommands` para facilitar o teste dos Use Cases de tarefas através de comandos CLI, permitindo validar a lógica de negócio sem necessidade de interface gráfica. Porém, após a criação dos controllers REST e a integração com o frontend, essa classe deixou de ser mantida e atualizada, o que fez com que ela ficasse deprecada e teve que ser comentada para evitar erros na inicialização do Spring. Um trabalho futuro importante é refatorar essa classe para que volte a ser uma ferramenta útil de teste dos Use Cases, independente das controllers HTTP.

### Documentação

#### Backend
O backend foi a camada mais complexa do sistema e que demandou maior reflexão e elaboração, principalmente devido à aplicação dos princípios de Clean Architecture e à necessidade de manter as camadas desacopladas do framework Spring.

A arquitetura segue uma organização em módulos funcionais (`tasks`, `users`, `auth`, `shared`), cada um estruturado em três camadas principais:

**Domain**: Contém as entidades de negócio (`Task`, `User`), enums (`TaskStatus`, `TaskType`), exceções específicas do domínio e interfaces de repositórios. Essa camada representa o núcleo das regras de negócio e não possui dependências externas.

**Application**: Implementa os casos de uso do sistema (`CreateTaskUseCase`, `LoginUseCase`, `ApproveTaskUseCase`, etc.). Cada Use Case encapsula uma operação específica da aplicação, recebendo suas dependências via construtor e operando exclusivamente com objetos do domínio.

**Infrastructure**: Responsável pela integração com tecnologias externas. Inclui controladores REST (`TaskController`, `AdminTaskController`), implementações de repositórios (`InMemoryTaskRepository`, `InMemoryUserRepository`), configurações do Spring (`TasksApplicationConfig`) e serviços de infraestrutura (`Slf4jLogger`, `Auth0JWTService`).

A comunicação entre as camadas segue o fluxo: `Infrastructure → Application → Domain`, onde os controladores recebem requisições HTTP, delegam para os Use Cases apropriados, que por sua vez utilizam as entidades de domínio e repositórios para executar as operações. O Spring gerencia a injeção de dependências através das classes de configuração, mantendo o desacoplamento entre as camadas.

#### Frontend
O frontend utiliza React com TypeScript e segue uma arquitetura mais simples e direta, organizada em componentes funcionais que consomem a API REST do backend.

A estrutura é composta por: 
- **componentes** (`TaskForm`, `TaskList`, `AdminDashboard`, etc.) que implementam a interface de usuário
- **serviços** (`apiService`, `authService`) responsáveis pela comunicação HTTP e gerenciamento de autenticação 
- **contexts** (`AuthContext`) para compartilhamento de estado global 
- **hooks customizados** (`useAuth`) que encapsulam lógicas específicas
- **types** que definem as interfaces TypeScript

O gerenciamento de estado utiliza `useState` e `useEffect` do React, sem bibliotecas externas adicionais. A autenticação é baseada em JWT, com tokens armazenados no localStorage e validação automática em cada requisição. A aplicação renderiza diferentes interfaces baseadas no tipo de usuário (colaborador ou administrador), mantendo a separação de responsabilidades de forma clara e simples.
