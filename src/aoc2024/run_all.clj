;; This script will load and run all dayX solutions in the aoc2024 namespace
(ns aoc2024.run-all
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn list-day-namespaces []
  (->> (file-seq (io/file "src/aoc2024"))
       (map #(.getName %)) ; Extract the filename
       (filter #(re-matches #"day\d+\.clj$" %)) ; Filter only dayX.clj files
       (sort-by #(Integer/parseInt (re-find #"\d+" %))) ; Sort numerically
       (map #(str "aoc2024." (str/replace % #"\.clj$" ""))) ; Convert to namespaces
       (map symbol))) ; Convert to symbols

;; Run using: `clj -M -m aoc2024.run-all`
(defn -main []
  (doseq [ns-sym (list-day-namespaces)]
    (println "Running" ns-sym)
    (require ns-sym)
    (println)))