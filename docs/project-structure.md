# Estrutura e Documentação do Projeto

Este documento explica, pasta por pasta, o que existe no repositório e qual a função de cada arquivo.

## Visão Geral

```
.
├─ deps.edn                  # Dependências e aliases Clojure (CLI)
├─ shadow-cljs.edn           # Builds do ClojureScript (frontend)
├─ package.json              # Dependências NPM (React, shadow-cljs runner)
├─ resources/
│  └─ public/
│     ├─ index.html          # Página principal (div#app)
│     └─ js/                 # Saída gerada pelo shadow-cljs (build :app)
└─ src/
   ├─ clj/
   │  └─ todo_app/
   │     ├─ core.clj         # Ponto de entrada (-main), Jetty e rotas
   │     ├─ handler.clj      # Handlers HTTP (GET/POST /api/todos, /api/hello)
   │     └─ db.clj           # "Banco" em memória (atom) para todos
   └─ cljs/
      └─ frontend/
         └─ core.cljs        # UI Reagent + integração (opcional) com a API
```

## Arquivos de Configuração

- `deps.edn`: lista dependências do backend e define o alias `:run` que aponta para `clj.todo_app.core/-main`. Usado pelo comando `clj -M:run`.
- `shadow-cljs.edn`: define o build `:app` para navegador (`:target :browser`), onde sairão os arquivos JS (`resources/public/js`) e qual função iniciar (`cljs.frontend.core/init`). Também configura o servidor de desenvolvimento (porta 8000).
- `package.json`: dependências NPM necessárias para o frontend (React, ReactDOM, shadow-cljs como runner).

## Backend (`src/clj/todo_app`)

- `core.clj`:
  - Cria as rotas com Reitit (ex.: `/api/hello`, `/api/todos`).
  - Aplica middlewares JSON (parse e response) e inicia o Jetty.
  - Define `-main` para subir o servidor: `clj -M:run`.
- `handler.clj`:
  - Funções (handlers) que respondem às rotas HTTP.
  - `GET /api/todos` retorna os todos armazenados.
  - `POST /api/todos` valida e cria um novo todo.
- `db.clj`:
  - Armazena dados em memória via `atom` (reseta quando o servidor reinicia).

## Frontend (`src/cljs/frontend`)

- `core.cljs`:
  - Define o estado da aplicação com `reagent/atom`.
  - Componentes Reagent para formulário e lista de tarefas.
  - Função `init` (entrypoint) que faz o render no `#app` do `index.html`.
  - Integração opcional com a API (`get-todos`), usando `core.async` e `<p!`.

## Recursos Estáticos (`resources/public`)

- `index.html`:
  - HTML inicial com `<div id="app">`.
  - Inclui `/js/main.js`, gerado pelo build `:app` do shadow‑cljs.

## Como Rodar

1) Instalar dependências do frontend (uma vez):

```powershell
npm install
```

2) Subir o Backend (porta 3000):

```powershell
clj -M:run
```

3) Subir o Frontend (dev server na 8000):

```powershell
npx shadow-cljs watch app
```

Acesse em `http://localhost:8000`. A API responde em `http://localhost:3000`.

## API (Resumo)

- `GET /api/hello` → "Hello, World!"
- `GET /api/todos` → `{ "todos": [ {"id": 1, "title": "..."}, ... ] }`
- `POST /api/todos` (JSON: `{ "title": "..." }`) → `201` com o todo criado.

Observação: o armazenamento é em memória.

## Dicas & Troubleshooting

- Namespaces CLJS: erros como "Invalid namespace declaration" normalmente indicam `(:require ...)` mal formatado. Confira `src/cljs/frontend/core.cljs`.
- Portas ocupadas: altere a porta no `-M:run` (backend) ou em `shadow-cljs.edn` (`:devtools :http-port`).
- CORS (se necessário): adicione middleware de CORS (como `ring-cors`).
- Cache/Build: se o watch não refletir mudanças, pare o processo, remova `.shadow-cljs/` e `.cpcache/`, e rode novamente.
