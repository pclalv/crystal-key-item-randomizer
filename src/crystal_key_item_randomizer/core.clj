(ns crystal-key-item-randomizer.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn hello-world-app [req] ;(3)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello world"})

(defroutes app-routes ;(3)
  (GET "/" [] hello-world-app)
  (route/not-found "not found"))

(defn -main
  "This is our app's entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
