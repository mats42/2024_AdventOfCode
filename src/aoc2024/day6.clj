(ns aoc2024.day6
  (:require [clojure.string :as string])
  (:import ( it.unimi.dsi.fastutil.longs LongOpenHashSet)))

(def input (slurp "src/resources/day6.txt"))

(defn parse [input]
  (string/split-lines input))

(def up-char \^)
(def down-char \v)
(def left-char \<)
(def right-char \>)
(def obstacle-char \#)

;; Store position as integer = x + y * 256
;; Directions as integers: up = 0, right = 1, down = 2, left = 3
;; Store obstacles as int-set with position only
;; Store visited as int-set with position + direction * 65536 (Like a hash of position and direction)

(defn init-playfield [input]
  (let [parsed (parse input)
        width (count (first parsed))
        height (count parsed)
        obstacles (LongOpenHashSet.)
        guard (volatile! nil)
        in-bounds? (fn [pos]
                     (let [pos (bit-and pos 0xFFFF) ;; Remove possible direction part
                           x (mod pos 256)
                           y (quot pos 256)] ;; Note, negative y's will wrap around and be outside the upper bound
                       (and (>= y 0) (< y height) (>= x 0) (< x width))))]
    
    (doseq [y (range height)
            x (range width)
            :let [c (get (get parsed y) x)
                  pos (+ x (* y 256))]]
      (when (= c obstacle-char) (.add obstacles pos))
      (when (= c up-char) (vreset! guard pos))
      (when (= c right-char) (vreset! guard (+ pos (bit-shift-left 1 16))))
      (when (= c down-char) (vreset! guard (+ pos (bit-shift-left 2 16))))
      (when (= c left-char) (vreset! guard (+ pos (bit-shift-left 3 16)))))

    {:width width
     :height height
     :obstacles obstacles
     :guard @guard
     :in-bounds? in-bounds?}))


(defn next-step [guard]
  (let [dir (bit-shift-right guard 16)]
    (case dir
      0 (- guard 256)
      1 (inc guard)
      2 (+ guard 256)
      3 (dec guard))))

(defn turn-guard-cw [guard]
  (let [dir (bit-shift-right guard 16)
        next-dir (mod (inc dir) 4)
        pos (bit-and guard 0xFFFF)]
    (+ pos (bit-shift-left next-dir 16))))


(defn unique-positions [visited]
  (let [positions (LongOpenHashSet.)]
    (doseq [v (.toLongArray visited)] ;; Create new set with only positions, no directions
      (.add positions (bit-and v 0xFFFF)))
    positions))

(defn find-visited [playfield extra-obstacle]
  (let [obstacles (playfield :obstacles)
        in-bounds? (playfield :in-bounds?)
        guard (playfield :guard)
        visited (LongOpenHashSet. [guard])]

    (loop [guard guard
           counter 0]
      (let [test-step (next-step guard)
            test-pos (bit-and test-step 0xFFFF)
            collision (or (.contains obstacles test-pos) (= test-pos extra-obstacle))
            next-step (if collision (turn-guard-cw guard) test-step)
            counter (inc counter)]
        (cond
          (not (in-bounds? next-step)) [visited false] ;; No loop
          (.contains visited next-step) [visited true] ;; Loop found
          :else (do
                  (.add visited next-step)
                  (recur next-step counter)))))))

(defn solve1 [input]
  (let [playfield (init-playfield input)]
    (count (unique-positions
            (first (find-visited playfield -1))))))

(defn solve2 [input]
  (let [playfield (init-playfield input)
        obstacles (playfield :obstacles)
        visited (unique-positions (first (find-visited playfield -1)))
        loop-counter (volatile! 0)]
    (doseq [x (range (playfield :width))
          y (range (playfield :height))
          :let [extra-obstacle (+ x (* y 256))]
          :when (not (.contains obstacles extra-obstacle)) ;; Unnecessary to try where already an obstacle
          :when (.contains visited extra-obstacle)]        ;; Only meaningful to try somewhere on the guards original path
      (let [[_ is-loop] (find-visited playfield extra-obstacle)]
        (when is-loop
          ;; (println "Loop found at" x y)
          (vswap! loop-counter inc))))
    @loop-counter))

;; Print solutions
(println "Day 6, part 1:" (solve1 input))
(println "Day 6, part 2:" (solve2 input))


(comment

  (def example-input "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...")

  ;; Testing the example input
  (solve1 example-input)
  (solve2 example-input)
  
  (time (solve2 input))
 
  )
