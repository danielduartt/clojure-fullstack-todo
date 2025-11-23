(ns cljs.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]
            [cljs.core.async]
            [cljs.core.async.interop])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [cljs.core.async.interop :refer [<p!]]))

;; --- 1. O "Cérebro" (O r/atom) ---
(defonce app-state (r/atom {:input-text ""
                            :todos []
                            :loading false
                            :saving false
                            :error nil}))

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

(defn create-todo-backend []
  (let [title (:input-text @app-state)]
    (when-not (str/blank? title)
      (swap! app-state assoc :saving true :error nil)
      (go
        (try
          (let [_ (<p! (fetch-json (str api-url "/todos")
                                   {:method "POST"
                                    :headers {"Content-Type" "application/json"}
                                    :body (js/JSON.stringify #js {:title title})}))]
            (swap! app-state assoc :input-text "")
            (get-todos)
            (swap! app-state assoc :saving false))
          (catch js/Error e
            (swap! app-state assoc :error (.-message e) :saving false)))))))

;; --- 3. Componentes ---
(defn todo-form []
  [:div.todo-input
   [:input {:type "text"
            :placeholder "Título do todo"
            :value (:input-text @app-state)
            :on-change #(swap! app-state assoc :input-text (-> % .-target .-value))}]
   [:button {:on-click create-todo-backend
             :disabled (or (:saving @app-state) (:loading @app-state))}
    (cond (:saving @app-state) "Salvando..."
          (:loading @app-state) "Carregando..."
          :else "Adicionar (API)")]])

(defn todo-list []
  [:ul.todo-list
   (for [todo (:todos @app-state)]
     ^{:key (:id todo)}
     [:li.todo-item (:title todo)])])

(defn app []
  [:div.todo-app
   [:h1 "Todo App"]
   [:p "Agora persistente via API + SQLite."]
   (when-let [err (:error @app-state)] [:p {:style {:color "red"}} err])
   [todo-form]
   [todo-list]])

;; --- 4. A Inicialização (React 18) ---
(defn ^:export init []
  (println "Frontend 'Todo' inicializado...")
  (get-todos)
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))