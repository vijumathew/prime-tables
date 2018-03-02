(ns primes.core
  (:require [primes.prime :as p]
            [primes.table :as t]))

(defn -main
  ([]
   (-main "10"))
  ([n]
   (-> n
       read-string
       p/generate-n-primes
       t/build-multiplication-table
       t/print-table)))
