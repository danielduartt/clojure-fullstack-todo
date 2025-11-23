(ns cljs.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]
            [cljs.core.async]
            [cljs.core.async.interop])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [cljs.core.async.interop :refer [<p!]]))

;; --- 1. O "Cérebro" (O r/atom) ---
(defonce app-state (r/atom {:next-id 1
                            :input-text ""
                            :todos []}))

(def api-url "http://localhost:3000/api")

(defn fetch-json [url options]
  (-> (js/fetch url (clj->js options))
      (.then (fn [response]
               (when-not (.-ok response)
                 (throw (js/Error. (str "HTTP error: " (.-status response)))))
               (.json response)))
      ;; A CORREÇÃO ESTÁ AQUI:
      (.then #(js->clj % :keywordize-keys true))))

(defn get-todos []
  (swap! app-state assoc :loading true :error nil)
  (go
    (try
      (let [response (<p! (fetch-json (str api-url "/todos") {:method "GET"}))]
        (swap! app-state assoc :todos (:todos response) :loading false))
      (catch js/Error e
        (swap! app-state assoc :error (.-message e) :loading false)))))

(defn create-todo [todo-data]
  (swap! app-state assoc :loading true :error nil)
  (go
    (try
      (<p! (fetch-json (str api-url "/todos")
                       {:method "POST"
                        :headers {"Content-Type" "application/json"}
                        ;; Converte o mapa Clojure em uma string JSON
                        :body (js/JSON.stringify (clj->js todo-data))}))

      ;; Se o POST funcionou, recarregamos a lista
      (get-todos)
      (catch js/Error e
        (swap! app-state assoc :error (.-message e) :loading false)))))

;; --- 3. Componentes ---
(defn todo-form []
  [:div.todo-input
   [:input
    {:type "text"
     :placeholder "O que precisa ser feito?"
     :value (:input-text @app-state)
     :on-change #(swap! app-state assoc :input-text (-> % .-target .-value))}]

   [:button
    {:on-click (fn []
                 (create-todo {:title (:input-text @app-state)})
                 (swap! app-state assoc :input-text ""))} ;; Limpa o input
    "Adicionar"]])

(defn todo-list []
  [:ul.todo-list
   (for [todo (:todos @app-state)]
     ^{:key (:id todo)}
     [:li.todo-item
      (:title todo)])])

(defn app []
  [:div.todo-app
   [:h1 "Todo App (Somente Frontend)"]
   [:p "Isto é 100% local. Recarregue (F5) para ver os dados sumirem."]
   [todo-form]
   [todo-list]])

;; --- 4. A Inicialização (React 18) ---
(defn ^:export init []
  (println "Frontend 'Todo' inicializado...")
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    ;; CORREÇÃO 2: Renderiza o [app] em vez do [counter-app]
    (.render root (r/as-element [app]))))