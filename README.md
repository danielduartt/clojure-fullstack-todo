# 📝 Clojure Full-Stack Todo App

![Clojure](https://img.shields.io/badge/Clojure-blue?style=for-the-badge&logo=clojure&logoColor=white)
![ClojureScript](https://img.shields.io/badge/ClojureScript-96CA2D?style=for-the-badge&logo=clojure&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)

## 👤 Nome do Aluno
**Daniel Duarte**

## 🔗 Link do Tutorial
[Tutorial Original - Notion](https://profsergiocosta.notion.site/Trabalho-em-Clojure-Desenvolvimento-de-um-Todo-List-2a7cce9750938082a001efb4661bfa92?pvs=74)

## 📖 Sobre o Projeto

Este projeto consiste em uma aplicação **Full-Stack** de gerenciamento de tarefas (Todo List), desenvolvida como atividade avaliativa da **Unidade 2** da disciplina de Paradigma de Programação Funcional.

Diferente da abordagem puramente teórica do Haskell (Unidade 1), este projeto foca no **pragmatismo do Clojure**, uma linguagem funcional moderna que roda sobre a JVM, amplamente utilizada no mercado por gigantes como **Nubank** e **PicPay**.

### 🎯 Objetivos de Aprendizagem
* ✅ Compreender a arquitetura de uma aplicação funcional moderna
* ✅ Praticar o desenvolvimento incremental com Git
* ✅ Conectar um Backend (API) com um Frontend Reativo
* ✅ Gerenciar persistência de dados com JDBC e SQLite

---

## ✨ Funcionalidades Implementadas

- ✅ **Criar tarefas** - Adicione novas tarefas via formulário
- ✅ **Listar tarefas** - Visualize todas as tarefas persistidas no banco
- ✅ **Marcar como concluído** - Alterne o status com um checkbox
- ✅ **Deletar tarefas** - Remova tarefas com um clique
- ✅ **Tema escuro/claro** - Alterne entre temas com um botão
- ✅ **Sincronização em tempo real** - UI atualiza imediatamente após operações
- ✅ **Design responsivo** - Funciona em desktop e mobile
- ✅ **Tratamento de erros** - Mensagens claras de erro quando necessário

---

## 🛠️ Tecnologias Utilizadas

A aplicação foi construída utilizando a stack padrão de mercado para Clojure:

**Backend:**
* [Clojure](https://clojure.org/) - Linguagem funcional na JVM
* [Ring](https://github.com/ring-clojure/ring) - Servidor HTTP
* [Reitit](https://metosin.github.io/reitit/) - Roteador de URLs
* [next.jdbc](https://github.com/seancorfield/next-jdbc) - Acesso ao banco de dados
* [SQLite](https://www.sqlite.org/) - Banco de dados persistente

**Frontend:**
* [ClojureScript](https://clojurescript.org/) - Clojure compilado para JavaScript
* [Shadow CLJS](http://shadow-cljs.org/) - Build tool e gerenciador de dependências
* [Reagent](https://reagent-project.github.io/) - Interface reativa (React wrapper)
* CSS3 - Estilos responsivos e tema escuro/claro

---

## 🚀 Como Executar

### Pré-requisitos

Certifique-se de que você tem instalado:

- **Java JDK 11+**
  - [Download](https://adoptium.net/)
  - Verificar: `java -version`

- **Clojure CLI**
  - [Download](https://clojure.org/guides/install_clojure)
  - Verificar: `clj --version`

- **Node.js 16+** (para npm e Shadow CLJS)
  - [Download](https://nodejs.org/)
  - Verificar: `node --version` e `npm --version`

- **Git**
  - [Download](https://git-scm.com/)
  - Verificar: `git --version`

### Passo 1: Clonar o repositório

```bash
git clone https://github.com/danielduartt/clojure-fullstack-todo.git
cd clojure-fullstack-todo
```

### Passo 2: Instalar dependências do Node.js

```bash
npm install
```

### Passo 3: Iniciar os servidores

**Terminal 1 - Backend (Clojure/Ring na porta 3000):**

```bash
clj -M:run
```

Você verá:
```
SLF4J: No SLF4J providers were found.
SLF4J: Defaulting to no-operation (NOP) logger implementation
Servidor iniciado na porta 3000
```

**Terminal 2 - Frontend (Shadow CLJS na porta 8000):**

```bash
npx shadow-cljs watch app
```

Você verá:
```
[:app] Build started
...
[:app] Build completed
```

### Passo 4: Acessar a aplicação

Abra seu navegador e acesse:
```
http://localhost:8000
```

---

## 📁 Estrutura do Projeto

```
clojure-fullstack-todo/
├── src/
│   ├── clj/                          # Backend Clojure
│   │   └── todo_app/
│   │       ├── core.clj              # Configuração do servidor
│   │       ├── handler.clj           # Handlers das rotas HTTP
│   │       ├── db.clj                # Queries do banco de dados
│   │       └── db_config.clj         # Configuração do banco
│   └── cljs/                         # Frontend ClojureScript
│       └── frontend/
│           └── core.cljs             # Componentes React com Reagent
├── resources/
│   ├── public/
│   │   ├── index.html                # HTML principal
│   │   └── css/
│   │       └── style.css             # Estilos CSS
│   └── js/
│       └── main.js                   # JS compilado (gerado)
├── deps.edn                          # Dependências Clojure
├── package.json                      # Dependências Node.js
├── shadow-cljs.edn                   # Configuração do Shadow CLJS
└── prod.db                           # Banco de dados SQLite (gerado)
```

---

## 🔌 API Endpoints

### GET /api/todos
Retorna lista de todas as tarefas

**Resposta:**
```json
{
  "todos": [
    {
      "id": 1,
      "title": "Aprender Clojure",
      "completed": 0,
      "created_at": "2025-11-23T10:00:00Z"
    }
  ]
}
```

### POST /api/todos
Cria uma nova tarefa

**Request:**
```json
{
  "title": "Nova tarefa"
}
```

**Resposta (201):**
```json
{
  "id": 2,
  "title": "Nova tarefa",
  "completed": 0,
  "created_at": "2025-11-23T10:05:00Z"
}
```

### PATCH /api/todos/:id/toggle
Alterna o status de conclusão da tarefa

**Resposta (200):**
```json
{
  "id": 1,
  "title": "Aprender Clojure",
  "completed": 1,
  "created_at": "2025-11-23T10:00:00Z"
}
```

### DELETE /api/todos/:id
Deleta uma tarefa

**Resposta (204):** Sem conteúdo

---

## 🎨 Recursos de UI

- **Tema Escuro/Claro:** Clique no botão "🌙 Dark" / "☀️ Light" para alternar
- **Checkbox:** Marque/desmarque para alternar conclusão
- **Botão Deletar:** Clique no "❌" para remover uma tarefa
- **Indicadores de Status:** "Loading" e "Saving" mostram operações em andamento
- **Mensagens de Erro:** Exibidas em vermelho se houver problemas

---

## 🐛 Troubleshooting

### Erro: "Cannot connect to localhost:3000"
- Certifique-se de que o Terminal 1 com `clj -M:run` está rodando
- Aguarde alguns segundos para o servidor iniciar

### Erro: "Cannot GET /index.html"
- Certifique-se de que o Terminal 2 com `npx shadow-cljs watch app` está rodando
- Recarregue a página (F5 ou Ctrl+Shift+R para limpar cache)

### Erro: "java: command not found"
- Java não está instalado ou não está no PATH
- Reinstale Java JDK e configure as variáveis de ambiente

### Banco de dados vazio após restart
- O arquivo `prod.db` é criado automaticamente
- Se quiser resetar, delete o arquivo `prod.db` e reinicie o servidor

---

## 📝 Comandos Úteis

```bash
# Compilar ClojureScript para produção
npx shadow-cljs release app

# Limpar cache
rm -rf .shadow-cljs

# Testar a API diretamente
curl http://localhost:3000/api/todos

# Resetar banco de dados
rm prod.db
```

---

## 🔄 Fluxo de Dados

```
Frontend (ClojureScript/Reagent)
    ↓ (Fetch API - JSON)
Backend (Clojure/Ring/Reitit)
    ↓ (Handler/Validação)
Database (SQLite via next.jdbc)
    ↓ (Resposta JSON)
Frontend Atualiza (Re-render Reagent)
```

---

## 📚 Recursos Adicionais

- [Clojure Official Docs](https://clojure.org/)
- [ClojureScript Guide](https://clojurescript.org/)
- [Reagent Documentation](https://reagent-project.github.io/)
- [Ring Documentation](https://github.com/ring-clojure/ring/wiki)
- [Reitit Router](https://metosin.github.io/reitit/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [next.jdbc](https://github.com/seancorfield/next-jdbc)

---

## 📄 Licença

MIT License - Veja LICENSE para detalhes

---
