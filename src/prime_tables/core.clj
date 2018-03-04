(ns prime-tables.core
  (:require [prime-tables.prime :as p]
            [prime-tables.table :as t]))

(defn -main
  ([]
   (-main "10"))
  ([n]
   (-> n
       read-string
       p/generate-n-primes
       t/build-multiplication-table
       t/print-table)))
