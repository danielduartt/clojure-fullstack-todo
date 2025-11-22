# 📝 Clojure Full-Stack Todo App

![Clojure](https://img.shields.io/badge/Clojure-blue?style=for-the-badge&logo=clojure&logoColor=white)
![ClojureScript](https://img.shields.io/badge/ClojureScript-96CA2D?style=for-the-badge&logo=clojure&logoColor=white)
![Status](https://img.shields.io/badge/Status-Concluído-success?style=for-the-badge)

## 📖 Sobre o Projeto

Este projeto consiste em uma aplicação **Full-Stack** de gerenciamento de tarefas (Todo List), desenvolvida como atividade avaliativa da **Unidade 2** da disciplina de Paradigma de Programação Funcional.

Diferente da abordagem puramente teórica do Haskell (Unidade 1), este projeto foca no **pragmatismo do Clojure**, uma linguagem funcional moderna que roda sobre a JVM, amplamente utilizada no mercado por gigantes como **Nubank** e **PicPay**.

### 🎯 Objetivos de Aprendizagem
* Compreender a arquitetura de uma aplicação funcional moderna.
* Praticar o desenvolvimento incremental (Git).
* Conectar um Backend (API) com um Frontend Reativo.
* Gerenciar persistência de dados com JDBC.

---

## 🛠️ Tecnologias Utilizadas

A aplicação foi construída utilizando a stack padrão de mercado para Clojure:

* **Linguagem:** [Clojure](https://clojure.org/) (Backend) & [ClojureScript](https://clojurescript.org/) (Frontend).
* **Build Tool:** `Shadow-CLJS` (para compilação do frontend e gerenciamento de dependências npm).
* **Frontend Framework:** [Reagent](https://reagent-project.github.io/) (Interface reativa minimalista baseada em React).
* **Backend/Servidor:** `Ring` & `Reitit` (Roteamento e tratamento de requisições HTTP).
* **Banco de Dados:** `next.jdbc` (Camada de persistência SQL).

---

## 🚀 Como Executar

### Pré-requisitos
* [Java JDK 11+](https://adoptium.net/)
* [Node.js & NPM](https://nodejs.org/) (para o Shadow-CLJS)
* [Clojure CLI](https://clojure.org/guides/install_clojure)

### Passo 1: Clonar o repositório
```bash
git clone [https://github.com/SEU-USUARIO/clojure-fullstack-todo.git](https://github.com/SEU-USUARIO/clojure-fullstack-todo.git)
cd clojure-fullstack-todo