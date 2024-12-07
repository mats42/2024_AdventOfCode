(ns aoc2024.day7
  (:require [clojure.string :as string]))

(def input (slurp "src/resources/day7.txt"))

(defn parse [input]
  (string/split-lines input))

(defn operator-choice [p i ops]
  (let [n (count ops)
        op-idx (mod (quot p (long (Math/pow n i))) n)]
    (get ops op-idx)))

(defn try-solve [eqn ops]
  (let [[target-result & terms] (mapv parse-long (string/split eqn #": |\s"))
        [first-term & terms] terms
        p-count (long (Math/pow (count ops) (count terms)))]

    (some identity
     (for [p (range p-count)]
       (let [[result _] (reduce (fn [[acc idx] val]
                                  (let [op (operator-choice p idx ops)
                                        next-acc (op acc val)
                                        next-acc (if (= op str) (parse-long next-acc) next-acc)]
;;                                    (when (= idx 0) (print acc ""))
;;                                    (print (cond
;;                                             (= op str) "||"
;;                                             (= op +) "+"
;;                                             (= op *) "*") val "")
                                      [next-acc (inc idx)]))
                                [first-term 0]
                                terms)
             is-ok  (= target-result result)]
;;         (when (not is-ok) (println "!=" target-result "FAIL"))
;;         (when  is-ok (println "==" target-result "OK"))
         (when is-ok result)))))) ;; Return result if correct, otherwise nil
      

(defn solve1 [input]
  (->> (parse input)
       (map #(try-solve % [+ *]))
       (filter identity)
       (reduce +)))

(defn solve2 [input]
  (->> (parse input)
       (map #(try-solve % [+ * str]))
       (filter identity)
       (reduce +)))

;; Print solutions
(println "Day 7, part 1:" (solve1 input))
(println "Day 7, part 2:" (solve2 input))

(comment

  (def example-input "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20")

  ;; Testing the example input
  (solve1 example-input) 
  (solve2 example-input)
  
  (time (solve1 input))
  (time (solve2 input))
 
  )
