(ns crystal-key-item-randomizer.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [crystal-key-item-randomizer.swaps :as swaps]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer :all])
            ;; [ring.middleware.defaults :refer :all]
  (:use [compojure.route :only [files not-found]]
        [compojure.core :only [defroutes GET]]
        [ring.middleware.reload :only [wrap-reload]]))

(defn swaps-handler [_req]
  (let [debug? (System/getenv "DEBUG")
        swaps-body (swaps/generate)
        swaps-body' (if debug?
                     swaps-body
                     (dissoc swaps-body :items-obtained :badges :conditions-met :beatable? :reasons))]
    {:status 200
     :headers {"Content-Type" "text/json"}
     :body (str (json/write-str swaps-body'))}))

(defroutes app-routes
  (GET "/swaps" [] swaps-handler)
  (files "")
  (files "assets")
  (not-found "not found"))

(defn wrap-logger [handler]
  (fn [request]
    (let [{:keys [request-method uri]} request
          {:keys [status body] :as response} (handler request)
          logline (str ["INFO: -- " {:method (-> request-method name .toUpperCase)
                                     :uri uri
                                     :status status
                                     :response-body body}])]
      (.println System/out logline)
      response)))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-logger)))

(defn event-logger [event]
  (println event)
  (.println System/out event))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (jetty/run-jetty #'app {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
