(ns primes.test-runner
  (:require  [clojure.test :refer [run-tests]]
             primes.prime-test
             primes.table-test
             primes.util-test))

(run-tests 'primes.prime-test
           'primes.table-test
           'primes.util-test)
