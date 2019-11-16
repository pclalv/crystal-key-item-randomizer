(defproject crystal-key-item-randomizer "0.1.0-SNAPSHOT"
  :description "Web app to randomize the key items in Pokemon Crystal"
  :min-lein-version "2.9.1"
  :url "https://github.com/pclalv/crystal-key-item-randomizer"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-json "0.5.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [compojure "1.6.1"]
                 [reagent "0.9.0-rc1"]]
  :main ^:skip-aot crystal-key-item-randomizer.core
  :source-paths ["src/clj" "src/cljc"]
  :target-path "target/%s"

  :plugins [[lein-ring "0.12.5"]
            [lein-cljsbuild "1.1.7"]]
  :ring {:handler crystal-key-item-randomizer.core/app}
  :hooks [leiningen.cljsbuild]

  :cljsbuild {:builds {:prod {:source-paths ["src/cljs"]
                              :jar true
                              :compiler {:output-to "public/assets/js/main.js"
                                         :optimizations :advanced}}}}

  :profiles {:dev {:dependencies [[ring/ring-devel "1.4.0"]]}
             :production {:env {:production true}}}
  :uberjar-name "crystal-key-item-randomizer.jar"

  )
