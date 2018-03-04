(ns prime-tables.test-runner
  (:require  [clojure.test :refer [run-tests]]
             prime-tables.prime-test
             prime-tables.table-test
             prime-tables.util-test))

(run-tests 'prime-tables.prime-test
           'prime-tables.table-test
           'prime-tables.util-test)
