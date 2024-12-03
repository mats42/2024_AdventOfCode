(ns aoc2024.day1
  (:require 
   [clojure.string :as string]))

(def input (slurp "src/resources/day1.txt"))

(defn unzip [array-of-arrays]
  (apply map vector array-of-arrays))

(defn parse [input]
  (->> input
       string/split-lines
       (mapv (fn [line] (mapv #(parse-long %) (string/split line #"\s+"))))
       unzip))

(defn solve1 [input]
  (let [[left right] (parse input)
        line-deltas (mapv (fn [a b] (abs (- a b)))
                   (sort left) (sort right))
        total-delta (reduce + line-deltas)]
    total-delta))

(defn solve2 [input]
  (let [[left right] (parse input)
        right-frequency (frequencies right)
        similarity-scores (mapv (fn [location-id]
                                  (* location-id (get right-frequency location-id 0)))
                                left)
        total-similarity (reduce + similarity-scores)]
    total-similarity))

(print
 "Day 1, part 1:"
 (solve1 input)
 "\nDay 1, part 2:"
 (solve2 input))


(comment
  ;; Testing the example input

  (solve1 "3   4
4   3
2   5
1   3
3   9
3   3
")

  (solve2 "3   4
4   3
2   5
1   3
3   9
3   3
")
  )
  

