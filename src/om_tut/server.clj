(ns om-tut.server
    (:require 
        [ring.adapter.jetty :as jetty]
        [ring.middleware.json :refer [wrap-json-response]]
        [ring.util.response :refer [response]]
        [ring.middleware.cors :refer [wrap-cors]]
        [compojure.route]        
        [compojure.core :refer [GET PUT POST defroutes]]
        [om-tut.server-data :refer [movie-data actor-data]]
    )
)

(defn get-all-movies [request-map]
  (response @movie-data))

(defn get-movies-by-year [year]
    (do
    (response (filter (fn [movie] (= (:year movie) year)) (:top-movies @movie-data)))    
    )
  )

(defn search-actors [actor-name]
    (do
        (doall (println "The NAME IS " actor-name))
        (println "getting atom")
        (println (count @actor-data))
        (println "done")
        (println (count @actor-data))
        (println "done on retry")
        (response (filter (fn [actor] (.contains (:name actor) actor-name) ) @actor-data))
    )
  )

(defn str->int [str] (read-string str))

(defroutes app
  (GET "/" request (wrap-json-response get-all-movies))
  (GET "/:year" [year] ((wrap-json-response get-movies-by-year) (str->int year)))
  (GET "/actorsearch/:name" [name] ((wrap-json-response search-actors) name) )
  ;;(GET "/movie/cast/:movieId")
)

(def cors-enabled-routes
    (wrap-cors #'app 
        :access-control-allow-origin [#".*"]
        :access-control-allow-methods [:get :put :post :delete])
    )

(defn start-server []
  (jetty/run-jetty #'cors-enabled-routes {:port 8080 :host "0.0.0.0" :join? false})
    )


