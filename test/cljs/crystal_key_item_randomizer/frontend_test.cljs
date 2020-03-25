(ns crystal-key-item-randomizer.frontend-test
  (:require [cljs.test :refer (deftest testing is)]
            [crystal-key-item-randomizer.frontend :as frontend])
  (:require-macros [crystal-key-item-randomizer.test-utils :as test-utils]))

;; how to test reagent components?
;; https://clojureverse.org/t/making-my-way-through-shadow-cljs-and-reagent-asking-for-opinion/2217/4

;; https://stackoverflow.com/questions/34180048/clojurescript-reagent-unit-testing-component-output-and-behaviour
;; (deftest weather-component-test-in
;;   (let [comp (r/render-component [w/weather-component "Paris" 12]
;;                                  (. js/document (getElementById "test")))]
;;     ;;ASSERT
;;     (is (= (d/html (sel1 :#city)) "Paris"))
;;     (is (= (d/html (sel1 :#temp)) "12"))))
