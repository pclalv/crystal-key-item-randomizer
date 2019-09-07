(ns crystal-key-item-randomizer.core
  (:gen-class)
  (:require [org.httpkit.server :as server])
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [defroutes GET POST DELETE ANY context]]))

(defn hello-world-app [req] ;(3)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello world"})

(defroutes app-routes ;(3)
  (GET "/" [] hello-world-app)
  (files "")
  (files "assets")
  (not-found "not found"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
