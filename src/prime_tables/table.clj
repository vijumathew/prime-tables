(ns prime-tables.table
  (:require [clojure.string :refer [join]]
            [prime-tables.util :refer [pad-space]]))

(defonce ^:private blank " ")
(defonce ^:private col "|")
(defonce ^:private col-separator (str blank col blank))
(defonce ^:private row-separator "-")
(defonce ^:private line-separator "\n")

(defn- replace-first-of-first
  "replaces first element of first element with a `blank`.
  this is for a list of lists."
  [table]
  (let [[x & r] table]
    (conj r (conj (drop 1 x) blank))))

(defn build-multiplication-table
  "builds a table, represented as a seq of seqs,
from ROW, a list, by multiplying the entire ROW
 by each element of ROW.

this function creates the axes by appending 1 to
the lists and multiplying ROW by that and then in
removing the top left corner, which is where the
added 1 * 1 would lie.

in summary, the table created will be as follows.
the first list in the table will be ROW times the
 1st element which is 1, the second list is created by
multiplying ROW by the 2nd element, etc."
  [row]
  (if (= (count row) 0) '()
      (let [mult-row (conj (seq row) 1)
            table (for [i mult-row]
                    (->> mult-row
                         (map (partial * i))))]
        (replace-first-of-first table))))

(defn- format-table-row
  "formats ROW into a table row.
  inserts separators between every element and
  makes every element the length of the
  item at that position in LENGTHS."
  [lengths row]
  (let [adjust-space (fn [lengths index element]
                       (pad-space (nth lengths index) element))
        row-xform (comp (map-indexed
                         (partial adjust-space lengths))
                        (interpose col-separator))]
    (str col blank (transduce row-xform str row) blank col)))

(defn- format-table
  "formats TABLE to be printed by padding
  elements with spaces by using `adjust-space`
  and inserts row and column separators.

  this assumes the last element of TABLE
  is the longest row passed in."
  [table]
  (let [end (last table)
        cell-widths (map (comp count str) end)
        table-width (count (format-table-row cell-widths end))
        divider-row (apply str (repeat table-width row-separator))]
    (conj
     (map #(str (format-table-row cell-widths %)
                line-separator
                divider-row)
          table)
     divider-row)))

(defn print-table
  "prints all the rows in unformatted table
  TABLE. TABLE is a seq of seqs of all
  the same length.

  this assumes the last element of TABLE
  is the longest row passed in."
  [table]
  (when-not (empty? table)
    (let [formatted-table (format-table table)]
      (doseq [row formatted-table]
        (println row)))))
