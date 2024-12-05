(ns aoc2024.day5
  (:require
   [clojure.string :as string]
   [clojure.core :as core]))

(def input (slurp "src/resources/day5.txt"))

(defn parse [input]
  (let [[rules pages] (string/split input #"\n\n")
        rules (string/split-lines rules)
        rules (mapv #(string/split % #"\|") rules)
        pages (string/split-lines pages)
        pages (mapv #(string/split % #",") pages)]
    [rules pages]))

(defn index-of [coll val] (first (keep-indexed (fn [idx v] (when (= v val) idx)) coll)))

(defn check-errata
  "Check if the errata is valid according to the rules. 
   Return list of mis-ordered indices. Returns empty list if all rules were valid."
  [rules errata]
  (let [relevant-rules (filter #(every? (set errata) %) rules)
        rule-results (mapv (fn [[l r]]
                             (let [l-idx (index-of errata l)
                                   r-idx (index-of errata r)]
                               [l-idx r-idx]))
                           relevant-rules)
        errors (filter (fn [[l-idx r-idx]] (> l-idx r-idx)) rule-results)]
    errors))

(defn middle-page-number [errata]
  (Integer/parseInt (errata (/ (dec (count errata)) 2))))


(defn solve1 [input]
  (let [[rules erratas] (parse input)]
    (->> erratas
         (filter #(empty? (check-errata rules %))) ;; Include only erratas that pass the check
         (mapv middle-page-number)                 ;; Pick middle page number
         (reduce +))))                             ;; Sum it up


(defn mend-one-error
  [errata errors]
  (let [[left-idx right-idx] (first errors) ;; Pick the first error 
        left (errata left-idx)
        right (errata right-idx)
        mended (assoc errata right-idx left left-idx right)]
    mended))

(defn mend-it [rules errata]
  (loop [cnt 0
         errata errata
         errors (check-errata rules errata)]
    (let [maybe-mended (mend-one-error errata errors)
          errors (check-errata rules maybe-mended)]
      (if (empty? errors)
        maybe-mended  ;; Return the mended errata
        (recur (inc cnt) maybe-mended errors)))))  ;; Try mend it again


(defn solve2 [input]
  (let [[rules erratas] (parse input)
        broken-erratas (filter #(not-empty (check-errata rules %)) erratas)]
    (->> broken-erratas
         (mapv #(mend-it rules %))
         (mapv middle-page-number)                 ;; Pick middle page number
         (reduce +))))                             ;; Sum it up


;; Print solutions
(println "Day 5, part 1:" (solve1 input))
(println "Day 5, part 2:" (solve2 input))


(comment
  (def example-input "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47")

  ;; Testing the example input
  (solve1 example-input)
  (solve2 example-input)
  (solve2 input))
