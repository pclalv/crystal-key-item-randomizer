(defproject crystal-key-item-randomizer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :min-lein-version "2.9.1"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [org.clojure/clojure "1.10.0"]
                 [reduce-fsm "0.1.4"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot crystal-key-item-randomizer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :production {:env {:production true}}}
  :uberjar-name "crystal-key-item-randomizer.jar")
