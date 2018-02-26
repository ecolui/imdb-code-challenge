(ns om-tut.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [ajax.core :as ajax]
              ))

(enable-console-print!)
(println "Console printing is enabled")

(def api-server "http://localhost:8080")

(defonce app-state 
  (atom 
    {
      :welcome-msg "Welcome to the top movies"
      :top-movies [
          {:id 1 :year 2000 :title "Title 1 in 2000"}
          {:id 2 :year 2000 :title "Title 2 in 2000"}
          {:id 3 :year 2000 :title "Title 3 in 2000"}
          {:id 4 :year 2001 :title "Title 1 in 2001"}
          {:id 5 :year 2001 :title "Title 2 in 2001"}
        ]
    }
    ))

(defn get-top-movies [year]
  nil  
  )

(defn movie-year-input [_ _]
  (reify om/IRender
    (render [_]
      (dom/select #js {
                    :onChange (fn [e] 
                      (do
;;                        (ajax/GET (str api-server) {:handler (fn [resp] (.log js/console resp ))})
                        (ajax/GET (str api-server) 
                          {:handler (fn [resp] (reset! app-state resp ))
                             :response-format :json
                             :keywords? true
                          })
                        (.log js/console (.-value (.-target e)))                        
                      )
                      )
                  }
        (map (fn [year] (dom/option #js {:key (str year "_dd")} year)) 
             (range 2000 2017))
        ))))

(defn movie [cursor owner] 
  (reify om/IRender
    (render [this]       
      (dom/span #js {:className "hiClass"} 
        (dom/div nil (:title cursor))
        ))))

(defn movies-list [cursor owner]
  (reify om/IRender
    (render [this]
      (dom/div nil (:welcome-msg cursor)
        (om/build-all movie (:top-movies cursor) {:key :id})))))

(om/root
  (fn [cursor owner]
    (reify om/IRender
      (render [_]
        (dom/div nil
          (om/build movie-year-input nil nil)
          (om/build movies-list cursor nil)))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
