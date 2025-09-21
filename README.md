# Projeto de Sistematização de **Engenharia e Projeto de Software**
## Definição do Problema
Na empresa Simplify Tecnologia, prezamos pela troca de conhecimento constante. Isso é feito por meio da troca de links de artigos, tecnologias novas, cursos, etc, no grupo do Telegram, além da relização de apresentações semanais sobre assuntos diversos. Porém, essas interações não são centralizadas em nenhum lugar específico, dificultando a sua consulta após o seu acontecimento. Além disso, há uma falta de incentivo para a participação nessas atividades.

## Requisitos
O software deve ter 2 objetivos principais:

1. Incentivar o compartilhamento de conhecimento (por meio de Gamificação)
2. Centralizar todo conteúdo compartilhado

### Requisitos Funcionais

**RF001 - Ver tarefas criadas**
- **Prioridade:** Alta
- **Descrição:** O usuário deve conseguir visualizar todas as tarefas que ele criou, independente do status
- **Atores:** Usuário
- **Fluxo de eventos:**
  1. Usuário acessa o sistema
  2. Sistema exibe lista de tarefas criadas pelo usuário
  3. Usuário visualiza tarefas com seus respectivos status

**RF002 - Criar tarefa**
- **Prioridade:** Alta
- **Descrição:** O usuário deve conseguir criar uma nova tarefa informando título e conteúdo
- **Atores:** Usuário
- **Fluxo de eventos:**
  1. Usuário acessa formulário de criação de tarefa
  2. Usuário preenche título e conteúdo
  3. Usuário confirma criação
  4. Sistema salva tarefa com status "Rascunho"

**RF003 - Atualizar tarefa**
- **Prioridade:** Alta
- **Descrição:** O usuário deve conseguir editar título e conteúdo de tarefas em status "Rascunho"
- **Atores:** Usuário
- **Fluxo de eventos:**
  1. Usuário seleciona tarefa em status "Rascunho"
  2. Sistema exibe formulário preenchido
  3. Usuário modifica campos desejados
  4. Usuário confirma alterações
  5. Sistema atualiza tarefa

**RF004 - Submeter tarefa para aprovação**
- **Prioridade:** Alta
- **Descrição:** O usuário deve conseguir submeter tarefas finalizadas para avaliação do administrador
- **Atores:** Usuário
- **Fluxo de eventos:**
  1. Usuário seleciona tarefa em status "Rascunho"
  2. Usuário confirma submissão
  3. Sistema altera status para "Aguardando Aprovação"
  4. Tarefa fica disponível para administrador avaliar

**RF005 - Gerenciar tarefas administrativas**
- **Prioridade:** Alta
- **Descrição:** O administrador deve conseguir visualizar todas as tarefas submetidas, aprovadas ou rejeitadas
- **Atores:** Administrador
- **Fluxo de eventos:**
  1. Administrador acessa painel administrativo
  2. Sistema exibe tarefas filtradas por status
  3. Administrador visualiza detalhes das tarefas

**RF006 - Aprovar ou rejeitar tarefa**
- **Prioridade:** Alta
- **Descrição:** O administrador deve conseguir aprovar ou rejeitar tarefas submetidas
- **Atores:** Administrador
- **Fluxo de eventos:**
  1. Administrador visualiza tarefa submetida
  2. Administrador avalia conteúdo
  3. Administrador seleciona "Aprovar" ou "Rejeitar"
  4. Sistema atualiza status da tarefa

**RF007 - Adicionar documentos na tarefa** (**PENDENTE**)
- **Prioridade:** Média
- **Descrição:** O usuário deve conseguir anexar arquivos ou imagens às tarefas
- **Atores:** Usuário
- **Fluxo de eventos:** A ser definido

**RF008 - Visualizar pontos recebidos** (**PENDENTE**)
- **Prioridade:** Média
- **Descrição:** O usuário deve conseguir visualizar os pontos obtidos com tarefas aprovadas
- **Atores:** Usuário
- **Fluxo de eventos:** A ser definido

**RF009 - Trocar pontos por prêmios** (**PENDENTE**)
- **Prioridade:** Baixa
- **Descrição:** O usuário deve conseguir resgatar prêmios utilizando pontos acumulados
- **Atores:** Usuário
- **Fluxo de eventos:** A ser definido

**RF010 - Solicitar ajustes** (**PENDENTE**)
- **Prioridade:** Média
- **Descrição:** O administrador deve conseguir solicitar ajustes específicos ao rejeitar uma tarefa
- **Atores:** Administrador
- **Fluxo de eventos:** A ser definido

**RF011 - Criar tipos de tarefas** (**PENDENTE**)
- **Prioridade:** Baixa
- **Descrição:** O administrador deve conseguir criar e gerenciar diferentes tipos/categorias de tarefas
- **Atores:** Administrador
- **Fluxo de eventos:** A ser definido

**RF012 - Criar prêmios** (**PENDENTE**)
- **Prioridade:** Baixa
- **Descrição:** O administrador deve conseguir criar e gerenciar prêmios disponíveis para resgate
- **Atores:** Administrador
- **Fluxo de eventos:** A ser definido

### Requisitos Não Funcionais

**NF001 - Controle de acesso e propriedade**
- **Descrição:** Um usuário não pode deletar, alterar ou visualizar conteúdos que não foram criados por ele. Apenas administradores têm acesso completo ao sistema.

**NF002 - Controle de estado de tarefas**
- **Descrição:** Um usuário não pode editar uma tarefa que já foi submetida, aprovada ou rejeitada. Apenas tarefas em status "Rascunho" podem ser modificadas pelo criador.


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

### Backend
O backend foi a camada mais complexa do sistema e que demandou maior reflexão e elaboração, principalmente devido à aplicação dos princípios de Clean Architecture e à necessidade de manter as camadas desacopladas do framework Spring.

A arquitetura segue uma organização em módulos funcionais (`tasks`, `users`, `auth`, `shared`), cada um estruturado em três camadas principais:

**Domain**: Contém as entidades de negócio (`Task`, `User`), enums (`TaskStatus`, `TaskType`), exceções específicas do domínio e interfaces de repositórios. Essa camada representa o núcleo das regras de negócio e não possui dependências externas.

**Application**: Implementa os casos de uso do sistema (`CreateTaskUseCase`, `LoginUseCase`, `ApproveTaskUseCase`, etc.). Cada Use Case encapsula uma operação específica da aplicação, recebendo suas dependências via construtor e operando exclusivamente com objetos do domínio.

**Infrastructure**: Responsável pela integração com tecnologias externas. Inclui controladores REST (`TaskController`, `AdminTaskController`), implementações de repositórios (`InMemoryTaskRepository`, `InMemoryUserRepository`), configurações do Spring (`TasksApplicationConfig`) e serviços de infraestrutura (`Slf4jLogger`, `Auth0JWTService`).

A comunicação entre as camadas segue o fluxo: `Infrastructure → Application → Domain`, onde os controladores recebem requisições HTTP, delegam para os Use Cases apropriados, que por sua vez utilizam as entidades de domínio e repositórios para executar as operações. O Spring gerencia a injeção de dependências através das classes de configuração, mantendo o desacoplamento entre as camadas.

### Frontend
O frontend utiliza React com TypeScript e segue uma arquitetura mais simples e direta, organizada em componentes funcionais que consomem a API REST do backend.

A estrutura é composta por: 
- **componentes** (`TaskForm`, `TaskList`, `AdminDashboard`, etc.) que implementam a interface de usuário
- **serviços** (`apiService`, `authService`) responsáveis pela comunicação HTTP e gerenciamento de autenticação 
- **contexts** (`AuthContext`) para compartilhamento de estado global 
- **hooks customizados** (`useAuth`) que encapsulam lógicas específicas
- **types** que definem as interfaces TypeScript

O gerenciamento de estado utiliza `useState` e `useEffect` do React, sem bibliotecas externas adicionais. A autenticação é baseada em JWT, com tokens armazenados no localStorage e validação automática em cada requisição. A aplicação renderiza diferentes interfaces baseadas no tipo de usuário (usuário ou administrador), mantendo a separação de responsabilidades de forma clara e simples.

### Como Executar o Projeto

#### Backend
Navegue até o diretório `backend` e execute:
```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```
O servidor será iniciado na porta 8080 com o profile de teste.

#### Frontend
Navegue até o diretório `frontend` e execute:
```bash
npm install
npm run dev
```
O frontend será iniciado na porta 5173.

#### Usuários de Teste
Com o profile de teste ativo, os seguintes usuários são criados automaticamente:

**Administrador:**
- Email: `admin@test.com`
- Senha: `admin123`

**Usuário comum:**
- Email: `user@test.com`
- Senha: `user123`

## Como Testar o Sistema

### Cenários de Teste - Usuário Usuário

1. **Teste de Login e Visualização de Tarefas**
   - Acesse o sistema com: `user@test.com` / `user123`
   - Verifique se a interface do usuário é exibida corretamente
   - Confirme que a lista de tarefas (inicialmente vazia) é mostrada

2. **Teste de Criação de Tarefa**
   - Preencha o formulário "Nova Tarefa" com título e conteúdo
   - Clique em "Salvar"
   - Verifique se a tarefa aparece na lista com status "RASCUNHO"

3. **Teste de Edição de Tarefa**
   - Clique em "Editar" em uma tarefa com status "RASCUNHO"
   - Modifique título ou conteúdo
   - Salve as alterações
   - Confirme que as modificações foram aplicadas

4. **Teste de Submissão para Aprovação**
   - Em uma tarefa com status "RASCUNHO", clique em "Submeter para Aprovação"
   - Confirme que o status mudou para "AGUARDANDO APROVAÇÃO"
   - Verifique que a tarefa não pode mais ser editada

5. **Teste de Restrições de Acesso**
   - Verifique que não há opção de edição para tarefas aguardando aprovação
   - Confirme que apenas suas próprias tarefas são exibidas

### Cenários de Teste - Administrador

1. **Teste de Login Administrativo**
   - Acesse o sistema com: `admin@test.com` / `admin123`
   - Confirme que há abas para "Pendentes" e "Processadas"

2. **Teste de Aprovação de Tarefas**
   - Na aba "Pendentes", clique em "Aprovar" e uma tarefa
   - Verifique se a tarefa some da aba "Pendentes" e muda para a aba "Processadas" com o status "Aprovada"

3. **Teste de Rejeição de Tarefas**
   - Na aba "Pendentes", selecione uma tarefa submetida
   - Clique em "Rejeitar"
   - Confirme que a tarefa aparece na aba "Processadas" com status "REJEITADA"

4. **Teste de Visualização de Tarefas Processadas**
   - Acesse a aba "Processadas"
   - Verifique que todas as tarefas aprovadas e rejeitadas são listadas
   - Confirme que tarefas de diferentes usuários são exibidas

### Casos de Teste de Erro

- Tente fazer login com credenciais inválidas
- Teste a responsividade em diferentes tamanhos de tela

## Sistema
### Login
<img width="1920" height="962" alt="image" src="https://github.com/user-attachments/assets/e6213bef-6297-4dca-ae5e-0038d14788a0" />

### Visualização do Usuário
<img width="1919" height="961" alt="image" src="https://github.com/user-attachments/assets/eca13f6c-bfdf-4ae4-92df-688eec2291a7" />

### Visualização do Administrador
<img width="1920" height="962" alt="image" src="https://github.com/user-attachments/assets/e97b4de7-22ea-4078-814b-32461120ee75" />
