(ns crystal-key-item-randomizer.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [org.httpkit.server :as server]
            [crystal-key-item-randomizer.swaps :as swaps])
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [defroutes GET]]))

(defn swaps-handler [_req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body (str (json/write-str {:swaps (swaps/generate)}))})

(defroutes app-routes
  (GET "/swaps" [] swaps-handler)
  (files "")
  (files "assets")
  (not-found "not found"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
