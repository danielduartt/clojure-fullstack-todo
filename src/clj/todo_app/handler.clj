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
  
  [_request] ;; 1. O handler recebe a 'request' como argumento.
             ;;    Usamos '_' para sinalizar que, nesta função,
             ;;    vamos ignorar esse argumento.
  
  ;; 2. O handler retorna um mapa de 'response'.
  {:status 200 ;; :status 200 é o código HTTP para "OK"
   
   :body "Hello, World!" ;; :body é o conteúdo que será enviado
                         ;; de volta para o navegador.
  })

(defn list-todos-handler
  "Handler para a rota GET /api/todos."
  [_request]
  ;; Chama nossa função de banco e a coloca no 'body'
  {:status 200
   :body {:todos (unqualify-keys (db/get-all-todos))}})

;; --- Handler para Criar um Todo ---
(defn create-todo-handler
  "Handler para a rota POST /api/todos."
  [request]
  ;; :body é um mapa Clojure que o *middleware*
  ;; (que adicionaremos no próximo passo) vai criar para nós
  ;; a partir do JSON que o cliente enviar.
  (let [todo-data (:body request)
        title (:title todo-data)]

    ;; É uma boa prática validar os dados que chegam.
    (if (and title (not (str/blank? title)))
      ;; Sucesso! Os dados são válidos.
      (let [new-todo (db/create-todo todo-data)]
        ;; Retornamos :status 201 (Created)
        {:status 201
         :body (first (unqualify-keys [new-todo]))})
      
      ;; Erro de validação.
      {:status 400 ;; :status 400 (Bad Request)
       :body {:error "O 'título' (title) é obrigatório"}})))

;; --- Handler para Atualizar um Todo ---
;; Handlers de update/delete removidos para reverter ao estado anterior.

