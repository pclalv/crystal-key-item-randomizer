(defproject crystal-key-item-randomizer "0.1.0-SNAPSHOT"
  :description "Web app to randomize the key items in Pokemon Crystal"
  :min-lein-version "2.9.1"
  :url "https://github.com/pclalv/crystal-key-item-randomizer"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.597"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.1"]
                 [reagent "0.9.0-rc1"]]
  :main ^:skip-aot crystal-key-item-randomizer.server
  :source-paths ["src/clj" "src/cljc"]
  :test-paths ["test/clj"]
  :target-path "target/%s"
  
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]
                                  [ring/ring-devel "1.4.0"]]
                   :plugins [[lein-ring "0.12.5"]]
                   :ring {:handler crystal-key-item-randomizer.server/app}
                   :injections [(require 'crystal-key-item-randomizer.server) ;; all instrumented fns should be loaded here
                                (require 'clojure.spec.test.alpha)
                                (clojure.spec.test.alpha/instrument)]}
             :uberjar {:aot :all
                       :env {:production true}
                       :injections ^:replace []
                       :plugins [[lein-cljsbuild "1.1.7"]]
                       :prep-tasks ["compile" ["cljsbuild" "once"]]
                       :cljsbuild {:builds [{:id :frontend
                                             :source-paths ["src/cljs/crystal_key_item_randomizer"]
                                             :compiler {:modules {:main
                                                                  {:entries [crystal-key-item-randomizer.frontend]
                                                                   :output-to "resources/public/assets/js/main.js"}

                                                                  :tracker
                                                                  {:entries [crystal-key-item-randomizer.tracker]
                                                                   :output-to "resources/public/assets/js/tracker.js"}

                                                                  :cljs-base
                                                                  {:output-to "resources/public/assets/js/cljs-base.js"}}
                                                        :optimizations :advanced}}]}}}
  :uberjar-name "crystal-key-item-randomizer.jar")
