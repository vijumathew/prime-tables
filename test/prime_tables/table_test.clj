(ns prime-tables.table-test
  (:require [prime-tables.table :as table]
            [clojure.test :as t :refer [deftest testing is]]))

;;private functions, so only testing main case
(deftest replace-first-of-first-test
  (testing "replace-first-of-first handles list of lists correctly"
    (let [rep-first-of-first (var table/replace-first-of-first)]
      (is (= (rep-first-of-first '((1 2 3) (2 4 5))) '((" " 2 3) (2 4 5))))
      (is (= (rep-first-of-first '((1) (2) (3))) '((" ") (2) (3)))))))

(deftest format-table-row-test
  (testing "format-table-row properly formats row"
    (let [fmt-table-row (var table/format-table-row)
          length-1-2-3 (cycle [1 2 3])
          length-2 (repeat 2)]
      (is (= (fmt-table-row length-2 '(12 2 31 4))
             "| 12 | 2  | 31 | 4  |"))
      (is (= (fmt-table-row length-1-2-3 [1 2 301 4 52 6])
             "| 1 | 2  | 301 | 4 | 52 | 6   |")))))

(deftest format-table-test
  (testing "format-table properly formats table"
    (let [fmt-table (var table/format-table)]
      (is (= (fmt-table '((1 2 3) (2 33 44) (111 22 333)))
             '("------------------"
               "| 1   | 2  | 3   |\n------------------"
               "| 2   | 33 | 44  |\n------------------"
               "| 111 | 22 | 333 |\n------------------")))
      (is (= (fmt-table [[13 2012 3] '(201 3 44) '(111 2287 333)])
             '("--------------------"
               "| 13  | 2012 | 3   |\n--------------------"
               "| 201 | 3    | 44  |\n--------------------"
               "| 111 | 2287 | 333 |\n--------------------")))
      (is (= (fmt-table '((13 2012 3) [201 3 44] [111 2287 333]))
             '("--------------------"
               "| 13  | 2012 | 3   |\n--------------------"
               "| 201 | 3    | 44  |\n--------------------"
               "| 111 | 2287 | 333 |\n--------------------")))
      (is (= (fmt-table '((13 2012) (201 3) (111 2287)))
             '("--------------"
               "| 13  | 2012 |\n--------------"
               "| 201 | 3    |\n--------------"
               "| 111 | 2287 |\n--------------"))))))

;;public functions
(deftest build-multiplication-table-test
  (testing "build-multiplication-table handles all cases"
    (is (= (table/build-multiplication-table [1 2 3]) '((" " 1 2 3)
                                                        (1   1 2 3)
                                                        (2   2 4 6)
                                                        (3   3 6 9))))
    (is (= (table/build-multiplication-table '(1 2)) '((" " 1 2)
                                                       (1   1 2)
                                                       (2   2 4))))
    (is (= (table/build-multiplication-table '(9)) '((" " 9)
                                                     (9  81))))
    (is (= (table/build-multiplication-table []) '()))))

(deftest print-table-test
  (testing "print-table handles all cases"
    (is (= (with-out-str (table/print-table '())) ""))
    (is (= (with-out-str (table/print-table [[1] [2000]]))
           (apply str '("--------\n"
                        "| 1    |\n--------\n"
                        "| 2000 |\n--------\n"))))
    (is (= (with-out-str (table/print-table [[4 779]]))
           (apply str '("-----------\n"
                        "| 4 | 779 |\n-----------\n"))))
    (is (= (with-out-str (table/print-table '([1 0 1] (1221 9 82738))))
           (apply str '("--------------------\n"
                        "| 1    | 0 | 1     |\n--------------------\n"
                        "| 1221 | 9 | 82738 |\n--------------------\n"))))))
