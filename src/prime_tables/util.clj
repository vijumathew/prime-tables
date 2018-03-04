(ns prime-tables.util)

(defn square-root
  "returns the square-root of N as an int
  that is rounded down."
  [n]
  (int (Math/sqrt (Math/abs n))))

(defn pad-space
  "pad S with spaces to make it LENGTH
  characters long."
  [length s]
  (if (= length 0) ""
      (format (str "%1$-" (Math/abs length) "s") s)))
