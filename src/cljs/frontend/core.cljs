(ns cljs.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]))

;; --- 1. O "Cérebro" (O r/atom) ---
(defonce app-state (r/atom {:input-text ""
                            :todos []
                            :loading false
                            :saving false
                            :error nil
                            :dark? true}))

(def api-url "http://localhost:3000/api")

(defn fetch-json [url options]
  (-> (js/fetch url (clj->js options))
      (.then (fn [response]
               (when-not (.-ok response)
                 (throw (js/Error. (str "HTTP error: " (.-status response)))))
               ;; Se for 204 No Content, não tenta parsear JSON
               (if (= 204 (.-status response))
                 #js {}
                 (.json response))))
      (.then #(js->clj % :keywordize-keys true))))

(defn get-todos []
  (swap! app-state assoc :loading true :error nil)
  (let [response-promise
        (-> (fetch-json (str api-url "/todos") {:method "GET"})
            (.then (fn [response]
                     (swap! app-state assoc :todos (:todos response) :loading false)
                     (js/console.log "[DEBUG] Todos carregados:" (clj->js (:todos @app-state)))))
            (.catch (fn [e]
                      (swap! app-state assoc :error (.-message e) :loading false))))]
    response-promise))

;; Funções de toggle/delete removidas para reverter ao estado anterior

(defn create-todo-backend []
  (let [title (:input-text @app-state)]
    (when-not (str/blank? title)
      (swap! app-state assoc :saving true :error nil)
      (-> (fetch-json (str api-url "/todos")
                      {:method "POST"
                       :headers {"Content-Type" "application/json"}
                       :body (js/JSON.stringify #js {:title title})})
          (.then (fn [_]
                   (swap! app-state assoc :input-text "")
                   (get-todos)))
          (.then (fn [_]
                   (swap! app-state assoc :saving false)))
          (.catch (fn [e]
                    (swap! app-state assoc :error (.-message e) :saving false)))))))

(defn delete-todo-backend [id]
  (swap! app-state assoc :saving true :error nil)
  (-> (fetch-json (str api-url "/todos/" id) {:method "DELETE"})
      (.then (fn [_]
               (get-todos)))
      (.then (fn [_]
               (swap! app-state assoc :saving false)))
      (.catch (fn [e]
                (swap! app-state assoc :error (.-message e) :saving false)))))

;; --- 3. Componentes ---
(defn theme-toggle []
  [:button.theme-toggle
   {:on-click #(swap! app-state update :dark? not)
    :aria-label "Alternar tema"}
   (if (:dark? @app-state) "🌙 Dark" "☀️ Light")])

(defn todo-form []
  [:div.todo-input
   [:input {:type "text"
            :placeholder "Título do todo"
            :value (:input-text @app-state)
            :on-change #(swap! app-state assoc :input-text (-> % .-target .-value))
            :aria-label "Campo título"}]
   [:button {:on-click create-todo-backend
             :disabled (or (:saving @app-state) (:loading @app-state))
             :aria-disabled (boolean (or (:saving @app-state) (:loading @app-state)))}
    (cond (:saving @app-state) "Salvando..."
          (:loading @app-state) "Carregando..."
          :else "Adicionar")]])

(defn todo-list []
  [:ul.todo-list
   (for [todo (:todos @app-state)]
     (let [title (or (:title todo) (:todos/title todo) "(sem título)")]
       ^{:key (:id todo)}
       [:li.todo-item
        [:span title]
        [:button.delete-btn 
         {:on-click #(delete-todo-backend (:id todo))
          :title "Deletar tarefa"}
         "❌"]]))])

(defn app []
  (let [dark? (:dark? @app-state)]
    (when-let [body js/document.body]
      (.classList.toggle body "light" (not dark?)))
    [:div.todo-app
     [:h1 "Todo App"]
     [:p.subtitle "Persistente via API (SQLite) • Reagent + Shadow-CLJS"]
     [:div.controls
      [theme-toggle]
      (when (:loading @app-state) [:span.badge "Loading"])
      (when (:saving @app-state) [:span.badge "Saving"])]
     (when-let [err (:error @app-state)] [:div.error-msg err])
     (when (and (empty? (:todos @app-state)) (not (:loading @app-state)))
       [:div.info-msg "Nenhum todo ainda. Adicione o primeiro!"])
     [todo-form]
     [todo-list]
     [:div.footer "Feito em Clojure/ClojureScript • " [:a {:href "https://clojure.org" :target "_blank"} "Site"]]]))

;; --- 4. A Inicialização (React 18) ---
(defn ^:export init []
  (println "Frontend 'Todo' inicializado...")
  (get-todos)
  (let [root (rdom/create-root (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))