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
      :top-movies []
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
                      (let [year (.-value (.-target e))]
                        (ajax/GET (str api-server "/" year) 
                          {:handler 
                          (fn [movies] 
                            (let [sorted-movies (take 100 (sort #(compare (:avgRating %2) (:avgRating %1)) movies))]
                              (swap! app-state assoc-in [:top-movies] sorted-movies)
                            ))
                           :response-format :json
                           :keywords? true
                          })
                        (.log js/console year)                        
                      ))}
        (map (fn [year] (dom/option #js {:key (str year "_dd")} year)) 
             (range 2000 2017))
        ))))

(defn movie [cursor owner] 
  (reify om/IRender
    (render [this]       
      (dom/div #js {:className "hiClass"} 
        (dom/span #js {:className "col-md-2 danger"} (str "Rating:" (:avgRating cursor)))
        (dom/span #js {:className "col-md-2 danger"} (str "Total Votes: " (:numVotes cursor)))
        (dom/span nil (:title cursor))        
        ))))

(defn movies-list [cursor owner]
  (reify om/IRender
    (render [this]
      (dom/div nil ;;(:welcome-msg cursor)
        (om/build-all movie (:top-movies cursor) {:key :movieId})))))

(om/root
  (fn [cursor owner]
    (reify om/IRender
      (render [_]
        (dom/div nil
          (dom/div nil "Select a Year to View the Top 100 Movies for that Year")
          (om/build movie-year-input nil nil)
          (om/build movies-list cursor nil)))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
