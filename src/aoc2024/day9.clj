(ns aoc2024.day9
   (:require [clojure.string :as string]))

 (set! *unchecked-math* :warn-on-boxed)

 (def input (slurp "src/resources/day9.txt"))

 (defn digit-string->byte-array [input]
   (let [len (count input)
         bytes (byte-array len)]
     (dotimes [i len]
       (aset bytes i (byte (- (int (.charAt input i)) (int \0))))) bytes))

 (defn make-block-table [input]
   (let [bytes (digit-string->byte-array input)
         len (count bytes)
         block-count (reduce (fn [^long acc ^long x] (+ acc (int x))) bytes)
         block-table (int-array block-count)]
     (loop [i 0
            pos 0]
       (when (< i len)
         (let [block-size (int (aget bytes i))
               file-id (quot i 2)
               is-file? (even? i)]
           (if is-file?
             (dotimes [j block-size] (aset block-table (+ pos j) file-id)) ;; Add file blocks
             (dotimes [j block-size] (aset block-table (+ pos j) -1))) ;; Add free blocks
           (recur (inc i) (+ pos block-size)))))
     block-table))

 (defn compact-blocks [block-table]
   (let [len (count block-table)]
     (loop [a 0
            b (dec len)]
       (cond
         (>= a b) block-table ;; No more work to do
         (= -1 (aget block-table b)) (recur a (dec b)) ;; Find block to move to the right
         (not= -1 (aget block-table a)) (recur (inc a) b) ;; Find free space from te left 
         :else (let [a-val (aget block-table a)  ;; Swap blocks at a and b
                     b-val (aget block-table b)]
                 (aset block-table a b-val)
                 (aset block-table b a-val)
                 (recur (inc a) (dec b)))))))

 (defn checksum [block-table]
   (let [len (count block-table)]
     (loop [i 0, checksum 0]
       (let [^int id (aget block-table i)]
         (if (== -1 id) checksum
             (recur (inc i) (+ checksum (* i id))))))))

 (defn solve1 [input]
   (->> input
        make-block-table
        compact-blocks
        checksum))

(time (solve1 input))
 
;; Part 2 and later days in Python...
