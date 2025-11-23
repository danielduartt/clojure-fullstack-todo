(ns clj.todo_app.core
  (:require [ring.adapter.jetty :as jetty]        ;; 1. O software do Servidor (Jetty)
            [reitit.ring :as ring] 
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]           ;; 2. O Roteador (Reitit)
            [ring.middleware.cors :refer [wrap-cors]]
            [clj.todo_app.handler :as handler]  ;; 3. Nossas funções (handler.clj)
            [clj.todo_app.db :as db])
  
  ;; 4. IMPORTANTE: Para o 'clj -M:run' funcionar
  (:gen-class)) 

;; --- 1. Definição das Rotas ---
;; Criamos um roteador Reitit.
;; Dizemos a ele que a URL "/api/hello", quando acessada
;; com o método :get, deve executar nossa função handler/hello-handler.
(def app-routes
  (ring/router
   ["/api"
    ["/hello" {:get {:handler handler/hello-handler}}]
    ["/todos" {:get {:handler handler/list-todos-handler}
               :post {:handler handler/create-todo-handler}}]
    ["/todos/:id" {:delete {:handler handler/delete-todo-handler}}]]))
;; --- 2. Definição da Aplicação (App) ---
;; Criamos o 'app' final, que é a função Ring principal.
(def app
  (ring/ring-handler
   app-routes
   (ring/create-default-handler)
   {:middleware [;; --- ADICIONE ESTE VETOR ---
                 ;; Ele deve ser o primeiro da lista
                 [wrap-cors :access-control-allow-origin [#"http://localhost:8000"]
                            :access-control-allow-methods [:get :post :delete]]
                 wrap-json-response
                 [wrap-json-body {:keywords? true}]
                 wrap-params
                 wrap-keyword-params
                ]}))

;; --- 3. Função para Iniciar o Servidor ---
;; Uma função auxiliar que inicia o Jetty.
(defn start-server [port]
  (println (str "Servidor iniciado na porta " port))
  ;; #'app é a forma de passar a "var" da nossa app para o Jetty
  ;; :join? false é importante para não bloquear o terminal.
  (jetty/run-jetty #'app {:port port :join? false}))

;; --- 4. Ponto de Entrada Principal (-main) ---
;; Esta é a função que o alias :run (do deps.edn) procura.
(defn -main [& args]
  (let [port (Integer/parseInt (or (first args) "3000"))]
    ;; --- ADICIONE ESTA LINHA ---
    (db/initialize-database!) ;; Garante que a tabela exista
    ;; --------------------------
    (start-server port)))