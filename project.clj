(defproject crystal-key-item-randomizer "0.1.0-SNAPSHOT"
  :description "Web app to randomize the key items in Pokemon Crystal"
  :min-lein-version "2.9.1"
  :url "https://github.com/pclalv/crystal-key-item-randomizer"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-devel "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.1"]]
  :main ^:skip-aot crystal-key-item-randomizer.core
  :source-paths ["src/clj"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :production {:env {:production true}}}
  :uberjar-name "crystal-key-item-randomizer.jar"
  ;; jkutner's thing?
  ;; https://jkutner.github.io/2015/10/14/clojurescript-on-heroku.html
  :uberjar {:hooks [leiningen.cljsbuild]
            :env {:production true}
            :omit-source true
            :aot :all
            :main my-app.server
            :cljsbuild {:builds {:app {:source-paths ["env/prod/cljs"]
                                       :compiler {:optimizations :advanced
                                                  :pretty-print false}}}}}

  :plugins [[lein-ring "0.12.5"]
            [lein-cljsbuild "1.1.7"]]
  :ring {:handler crystal-key-item-randomizer.core/app})
