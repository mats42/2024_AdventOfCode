(ns aoc2024.day8
    (:require [clojure.string :as string]))

  (def padded-width 100000)  ;; We add plenty of overflow space to simplify bounds checking
  (def input (slurp "src/resources/day8.txt"))

  (defn parse [input]
    (string/split-lines input))

;; Create frequency -> Set of locations map
  (defn map-antennas [parsed]
    (let [width (count (first parsed))
          height (count parsed)]
      (reduce (fn [acc [x y]]
                (let [freq (nth (get parsed y) x)]
                  (if (= freq \.) acc
                      (update acc freq (fnil conj #{}) (+ x (* y padded-width))))))
              {}
              (for [y (range height)
                    x (range width)]
                [x y]))))

  (defn in-bounds? [pos width height]
    (when (and (>= pos 0) (< (mod pos padded-width) width) (< pos (* padded-width height))) pos))

  (defn find-antinodes [loc1 loc2 width height]
    (let [antinode1 (- (* 2 loc1) loc2)
          antinode2 (- (* 2 loc2) loc1)]
      [(when (in-bounds? antinode1 width height) antinode1)
       (when (in-bounds? antinode2 width height) antinode2)]))

  (defn antenna-combinations
    "Return list of possible antenna pairs from a set of antennas"
    [antennas]
    (for [antenna1 antennas
          antenna2 antennas
          :when (not= antenna1 antenna2)] ;; Get all combinations of two antennas from set of locations 
      [antenna1 antenna2]))

  (defn map-antinodes [antenna-map width height]
    (->> antenna-map
         (map (fn [[_ antennas]]
                (->> (antenna-combinations antennas)
                     (map (fn [[a1 a2]]
                            (find-antinodes a1 a2 width height)))))) ;; Find antinodes for each pair of antennas
         (flatten)
         (remove nil?)
         (set)))

  (defn solve1 [input]
    (let [parsed (parse input)
          width (count (first parsed))
          height (count parsed)
          antenna-map (map-antennas parsed)
          antinodes (map-antinodes antenna-map width height)]
      (count antinodes)))


  (defn is-antinode? [loc antenna-pair]
    (let [delta (abs (- (second antenna-pair) (first antenna-pair)))
          displaced (mod (- (first antenna-pair) loc) delta)
          is-antinode (= 0 (mod displaced delta))]
      ;;(println loc antenna-pair displaced delta is-antinode)
      is-antinode
      ))


  (defn solve2 [input]
    (let [parsed (parse input)
          width (count (first parsed))
          height (count parsed)
          antenna-map (map-antennas parsed)
          all-antenna-pairs (->> antenna-map
                                 (map second)
                                 (map antenna-combinations)
                                 (mapcat identity))]
      ;; Anothoer approach in 2, check each location against all antenna pairs
      (->> (for [y (range height)
                 x (range width)
                 :let [loc (+ x (* y padded-width))]
                 :when (some #(is-antinode? loc %) all-antenna-pairs)]
             loc) 
           count)))

;; Print solutions
(println "Day 8, part 1:" (solve1 input))
(println "Day 8, part 2:" (solve2 input))

(comment

  (def example-input "............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............")

  ;; Testing the example input
  (solve1 example-input)
  (solve2 example-input)

  (time (solve1 input))
  (time (solve2 input)))
