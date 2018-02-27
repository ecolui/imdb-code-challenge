(ns om-tut.core
    (:require [om.core :as om :include-macros true]
              [om.dom :as dom :include-macros true]
              [ajax.core :as ajax]
              [goog.string :as gstring]
              [goog.string.format]
              [goog.i18n.NumberFormat.Format]
              )
              (:import
                (goog.i18n NumberFormat)
                (goog.i18n.NumberFormat Format)))

(enable-console-print!)
(println "Console printing is enabled")

(def api-server "http://localhost:8080")

(defonce app-state 
  (atom 
    {      
      :selected-year nil      
      :welcome-msg "Welcome to the top movies"
      :top-movies []
    }))

;;number formatters
(def nff
  (NumberFormat. Format/DECIMAL))
(defn- nf
  [num]
  (.format nff (str num)))

(defn movie-year-input [_ _]
  (reify om/IRender
    (render [_]
      (dom/select #js {
          :onChange (fn [e] 
            (let [year (.-value (.-target e))]
              (swap! app-state assoc :selected-year year)
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
             (range 2000 2018))
        ))))

(defn show-movie-cast [movie-id]
  (log js/console (str "The movie id is " movie-id))
  )

(defn movie [cursor owner] 
  (reify om/IRender
    (render [this]       
      (dom/div #js {:className "row data-row"} 
        (dom/input #js {:type "button" :className "btn btn-primary btn-sm col-md-1" :value "View Cast"})        
        (dom/div #js {:className "col-md-1"} (str "Rating:" (gstring/format "%.2f" (:avgRating cursor)) ))
        (dom/div #js {:className "col-md-2"} (str "Total Votes: " (nf (:numVotes cursor))))
        (dom/div #js {:className "col-md-6"} (:title cursor)))
        )))

(defn movies-list [cursor owner]
  (reify om/IRender
    (render [this]
      (dom/div nil
        (om/build-all movie (:top-movies cursor) {:key :movieId})))))

(om/root
  (fn [cursor owner]
    (reify om/IRender
      (render [_]
        (dom/div #js {:className "main-page"}
          (dom/h3 nil "Select a Year to View the Top 100 Movies for that Year")
          (om/build movie-year-input nil nil)
          (om/build movies-list cursor nil)
          (dom/div #js {
            :className (str 
              " alert alert-danger " 
              (if (and (< (count (:top-movies @app-state)) 100) (not= (:selected-year @app-state) nil)) "visible" "hidden"))
            } 
            (str "Only " (count (:top-movies @app-state)) " movies were selected. For a movie to qualify in this list, it must have at least 10,000 votes."))
          ))))
  app-state
  {:target (. js/document (getElementById "app"))})

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
