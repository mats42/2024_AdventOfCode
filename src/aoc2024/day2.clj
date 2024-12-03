(ns aoc2024.day2
  (:require
   [clojure.string :as string]))

(def input (slurp "src/resources/day2.txt"))

(defn parse [input]
  (->> input
       string/split-lines
       (mapv (fn [line] (mapv #(parse-long %) (string/split line #"\s+"))))))

(defn is-safe [report]
  (let [deltas (mapv - (rest report) report) ;; Calculate the deltas between each pair of numbers
        is-increasing (every? #(<= 1 % 3) deltas) ;; Strictly increasing with delta between 1 and 3
        is-decreasing (every? #(<= -3 % -1) deltas) ;; Strictly decreasing with delta between -1 and -3
        is-safe (or is-increasing is-decreasing)]
    is-safe))

(defn remove-at [v idx]
  (into [] (concat (subvec v 0 idx) (subvec v (inc idx)))))

(defn dampen
  "Return a vector of all subvectors of report with one element removed"
  [report]
  (->> (range (count report))
       (mapv #(remove-at report %))
       (cons report))) ;; Include the original report in the list of dampened reports

(defn solve1 [input]
  (let [reports (parse input)
        safe-reports (filter is-safe reports)]
    (count safe-reports)))

(defn is-any-safe [reports]
  (some is-safe reports))


;; Go for lazy solution and test all possible reports with one element removed
;; Reports are short so this is feasible
(defn solve2 [input]
  (let [reports (parse input)
        dampened-reports (mapv dampen reports) ;; Generate all possible reports with one element removed
        safe-reports (mapv last (filter is-any-safe dampened-reports))
        ;;_ (println safe-reports)
        ]
   (count safe-reports)))

;; Print solutions
(println "Day 2, part 1:" (solve1 input))
(println "Day 2, part 2:" (solve2 input))


(comment
  ;; Testing the example input

  (solve1 "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9")

  (solve2 "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9")

  )
