(ns aoc2024.day3
  (:require
   [clojure.string :as string]))

(def input (slurp "src/resources/day3.txt"))

(defn find-mul-ops [input]
  (->> input
       (re-seq #"mul\(\d{1,3},\d{1,3}\)")))

(defn find-all-ops [input]
  (->> input
       (re-seq #"do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)")))

(find-all-ops "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

(defn run-op [op]
  (let [[_ a b] (re-find #"mul\((\d{1,3}),(\d{1,3})\)" op)
        a (Integer/parseInt a)
        b (Integer/parseInt b)]
    (* a b)))

(defn solve1 [input]
  (->> (find-mul-ops input)
       (mapv run-op)
       (reduce +)))


(defn solve2 [input]
  (loop [[op & rest] (find-all-ops input)
         result 0
         enabled true]
    ;; (println op result enabled)
    (cond
      (nil? op) result
      (= op "do()") (recur rest result true)
      (= op "don't()") (recur rest result false)
      enabled (recur rest (+ result (run-op op)) enabled)
      :else (recur rest result enabled))))

(print
 "Day 3, part 1:"
 (solve1 input)
 "\nDay 3, part 2:"
 (solve2 input))



(comment
  ;; Testing the example input

  (solve1 "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

  (solve2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"))
  
  
