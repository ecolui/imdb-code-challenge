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

(def enable-cors {
    "Access-Control-Allow-Origin" "*"
    "Access-Control-Allow-Methods" "GET, POST, OPTIONS"
    "Access-Control-Allow-Headers" "Content-Type"
    "Access-Control-Max-Age" "31536000"
})

(defn with-cors [resp] (update-in resp [:headers] merge enable-cors))

(defn handler [request-map]
  (response @movie-data)
  )

(defn handler-with-cors [request-map]
    (wrap-json-response handler)
  )

(defroutes app
  (GET "/" request (handler-with-cors request))
    )

(def cors-enabled-routes
    (wrap-cors #'app :access-control-allow-origin [#".*"]
                       :access-control-allow-methods [:get :put :post :delete])
    )

(defn start-server []
  (jetty/run-jetty #'cors-enabled-routes {:port 8080 :host "0.0.0.0" :join? false})
    )

;;lein repl
;;(load-file "src/om_tut/server.clj")
;;(require '[om-tut.server :as server])
;;(def jetty (om-tut.server/start-server))

