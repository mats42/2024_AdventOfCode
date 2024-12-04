(ns aoc2024.day4
  (:require
   [clojure.string :as string]))

(def input (slurp "src/resources/day4.txt"))

(defn parse [input]
  (->> input
       string/split-lines))

(defn find-word [grid word]
  (let [y-size (count grid)
        x-size (count (first grid))
        directions [[-1 0] [-1 1] [0 1] [1 1] [1 0] [1 -1] [0 -1] [-1 -1]]
        in-bounds? (fn [x y] (and (>= x 0) (< x y-size) (>= y 0) (< y x-size)))
        is-word-at? (fn [x y [dx dy]]
                      (every? true?
                       (map-indexed
                        (fn [idx char]
                          (and
                           (in-bounds? (+ y (* idx dy)) x)
                           (= char (get-in grid [(+ y (* idx dy)) (+ x (* idx dx))]))))
                        word)))]
    (->> (for [x (range x-size)
               y (range y-size)
               [dx dy] directions]
           (is-word-at? x y [dx dy]))
         (filter true?)
         count)))

(defn get-char-at [grid x y]
  (-> grid
      (nth y)
      (nth x)))

(defn find-x-mas [grid]
  (let [x-size (- (count (first grid)) 2) ;; Compensate now we search a 3x3 grid
        y-size (- (count grid) 2)
        x-mas-at? (fn [x y]
                    (let [a (get-char-at grid y x)              ;; a.c
                          b (get-char-at grid (+ 2 y) (+ 2 x))  ;; .e.
                          c (get-char-at grid y (+ 2 x))        ;; d.b
                          d (get-char-at grid (+ 2 y) x)
                          e (get-char-at grid (+ 1 y) (+ 1 x))
                          leg1 (str a e b)
                          leg2 (str c e d)]
                      ;; (println "Legs at " x y "are" leg1 leg2)
                      (and
                       (or (= leg1 "MAS") (= leg1 "SAM"))
                       (or (= leg2 "MAS") (= leg2 "SAM")))))]
    (->>
     (for [y (range y-size)
           x (range x-size)]
       (let [found (x-mas-at? x y)]
         ;; (when found (println "Found X-MAS at" x y))
         found))
     (filter true?)
     count)))
        
  

(defn solve1 [input]
  (find-word (parse input) "XMAS"))


(defn solve2 [input]
  (find-x-mas (parse input))
)


;; Print solutions
(println "Day 4, part 1:" (solve1 input))
(println "Day 4, part 2:" (solve2 input))


(comment
  ;; Testing the example input

  (solve1 "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX")

  (solve2 "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX")

  )
