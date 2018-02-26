(ns om-tut.server
    (:require 
        [ring.adapter.jetty :as jetty]
        [ring.middleware.json :refer [wrap-json-response]]
        [ring.util.response :refer [response]]
        [ring.middleware.cors :refer [wrap-cors]]
        [compojure.route]        
        [compojure.core :refer [GET PUT POST defroutes]]
    )
)

(defonce movie-data 
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

(defn get-all-movies [request-map]
  (response @movie-data))

(defn get-movies-by-year [year]
    (response (filter (fn [movie] (= (:year movie) year)) (:top-movies @movie-data)))    
  )

(defn str->int [str] (read-string str))

(defroutes app
  (GET "/" request (wrap-json-response get-all-movies))
  (GET "/:year" [year] ((wrap-json-response get-movies-by-year) (str->int year)))
)

(def cors-enabled-routes
    (wrap-cors #'app 
        :access-control-allow-origin [#".*"]
        :access-control-allow-methods [:get :put :post :delete])
    )

(defn start-server []
  (jetty/run-jetty #'cors-enabled-routes {:port 8080 :host "0.0.0.0" :join? false})
    )

