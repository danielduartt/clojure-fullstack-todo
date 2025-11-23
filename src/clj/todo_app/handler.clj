(ns clj.todo_app.handler
    "Este namespace define nossas 'funções de resposta' (Handlers)."
    (:require [clj.todo_app.db :as db] 
            [clojure.string :as str]))

(defn- unqualify-keys
  "Converte chaves qualificadas (ex: todos/id) em chaves simples (ex: id)"
  [maps]
  (map (fn [m]
         (into {} (map (fn [[k v]] [(keyword (name k)) v]) m)))
       maps))

(defn hello-handler
  "Nosso primeiro handler. Ele apenas diz 'Olá, Mundo!'"
  [_request]
  {:status 200
   :body "Hello, World!"})

(defn list-todos-handler
  "Handler para a rota GET /api/todos."
  [_request]
  {:status 200
   :body {:todos (unqualify-keys (db/get-all-todos))}})

(defn create-todo-handler
  "Handler para a rota POST /api/todos."
  [request]
  (let [todo-data (:body request)
        title (:title todo-data)]
    (if (and title (not (str/blank? title)))
      (let [new-todo (db/create-todo todo-data)]
        {:status 201
         :body (first (unqualify-keys [new-todo]))})
      {:status 400
       :body {:error "O 'título' (title) é obrigatório"}})))

(defn delete-todo-handler
  "Handler para a rota DELETE /api/todos/:id."
  [request]
  (let [id (Integer/parseInt (get-in request [:path-params :id]))]
    (db/delete-todo id)
    {:status 204
     :body nil}))

(defn toggle-todo-handler
  "Handler para a rota PATCH /api/todos/:id/toggle."
  [request]
  (let [id (Integer/parseInt (get-in request [:path-params :id]))
        updated-todo (db/toggle-todo-completed id)]
    {:status 200
     :body (first (unqualify-keys [updated-todo]))}))