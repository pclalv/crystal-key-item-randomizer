(defproject crystal-key-item-randomizer "0.1.0-SNAPSHOT"
  :description "Web app to randomize the key items in Pokemon Crystal"
  :min-lein-version "2.9.1"
  :url "https://github.com/pclalv/crystal-key-item-randomizer"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[compojure "1.6.1"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-devel "1.7.1"]
                 [ring/ring-defaults "0.3.2"]]
  :main ^:skip-aot crystal-key-item-randomizer.core
  :source-paths ["src/clj"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :production {:env {:production true}}}
  :uberjar-name "crystal-key-item-randomizer.jar")
