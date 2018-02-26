(ns om-tut.server
    (:require 
;;        [bidi.bidi :as bidi]
;;        [bidi.ring]
;;        [schema.core :as s]
        [ring.adapter.jetty :as jetty]
        [ring.middleware.json :refer [wrap-json-response]]
        [ring.util.response :refer [response]]
        [ring.middleware.cors :refer [wrap-cors]]
        [compojure.route]        
        [compojure.core :refer [GET PUT POST defroutes]]
;;        [cognitect.transit :as transit])
;;    (:import [java.io ByteArrayInputStream ByteArrayOutputStream])
    )
;;    (use '[compojure.core :only (GET PUT POST defroutes)])
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

;;Use Cognitects standardized serialization format
;;(defn to-transit [x]
;;    (let [baos (ByteArrayOutputStream. 4096)
;;          writer (transit/write boas :json {})]
;;      (transit/write writer x)
;;      (.toString boas)))

;;(defn from-transit [x]
;;    (let [bais (ByteArrayInputStream. (.getBytes s "UTF-8"))
;;          reader (transit/reader bais :json)]
;;      (transit/read reader)))

(def enable-cors {
    "Access-Control-Allow-Origin" "*"
    "Access-Control-Allow-Methods" "GET, POST, OPTIONS"
    "Access-Control-Allow-Headers" "Content-Type"
    "Access-Control-Max-Age" "31536000"
})

(defn with-cors [resp] (update-in resp [:headers] merge enable-cors))


;;(defn transit-response [resp]
;;    {:pre [(map? resp)]}
;;    (-> resp
;;        (update-in [:headers "Content-Type"] 
;;            #(or % "application/transit+json; charset=UTF-8"))
;;        (update-in [:status]
;;            #(or % 200))
;;        (update-in [:body] (fn [body] (str (to-transit body))))
;;        (with-cors))
;;  )

;;(defn get-movies [{:keys [route-params] :as req}]
;;  (transit-response {:body })
;;    )

;;(def routes ["/" {
;;    "movies" {:get get-movies}   
;;}])

;;(defn handler [req]
;;    (let [resp ((bidi.ring/make-handler routes) req)]
;;      (println (:request-method req) (:uri req) "->" (:status resp))
;;      resp))

(defn handler [request-map]
  (response
;;    {:movies {:year 2005 :title "Coming to America"}}
    @movie-data


;;  {:status 200
;;   :headers {"Content-Type" "application/json"}
;;   :body {:movies {:year 2005 :title "Coming to America"}}}

;;   :headers {"Content-Type" "text"}
;;   :body "hi world"}
    )
  )

(defn handler-with-cors [request-map]
;;    (wrap-json-response (with-cors (handler request-map)))
;;    (wrap-json-response (handler request-map))
;;        (handler request-map)
    (wrap-json-response handler)
  )

(defroutes app
;;  (GET "/" request "Welcome!")
  (GET "/" request (handler-with-cors request))
    )

(def cors-enabled-routes
    (wrap-cors #'app :access-control-allow-origin [#".*"]
;;        :access-control-allow-headers ["Origin" "X-Requested-With"
;;                                                          "Content-Type" "Accept"]
                       :access-control-allow-methods [:get :put :post :delete])
    )

(defn start-server []
;;  (jetty/run-jetty #'handler-with-cors {:port 8080 :host "0.0.0.0" :join? false})
;;  (jetty/run-jetty #'app {:port 8080 :host "0.0.0.0" :join? false})
  (jetty/run-jetty #'cors-enabled-routes {:port 8080 :host "0.0.0.0" :join? false})
    )


;;lein repl
;;(load-file "src/om_tut/server.clj")
;;(require '[om-tut.server :as server])
;;(def jetty (om-tut.server/start-server))

