(ns primes.util-test
  (:require [primes.util :refer [square-root pad-space]]
            [clojure.test :as t :refer [deftest testing is]]))

(deftest square-root-base-test
  (testing "square-root handles positive square numbers"
    (is (= (square-root 100) 10))
    (is (= (square-root 49) 7))
    (is (= (square-root 25) 5))))

(deftest square-root-non-positive-test
  (testing "square-root handles square negative and 0 values"
    (is (= (square-root -1)) 1)
    (is (= (square-root 0)) 0)
    (is (= (square-root -49) 7))))

(deftest square-root-rounding-test
  (testing "square-root rounds values down properly"
    (is (= (square-root 145)) 12)
    (is (= (square-root 35)) 5)
    (is (= (square-root -17)) 4)
    (is (= (square-root -48)) 6)))

(deftest pad-space-base-test
  (testing "pad space works properly for standard input"
    (is (= (pad-space 10 "hi") "hi        "))
    (is (= (pad-space 6 "hello") "hello "))))

(deftest pad-space-non-positive-test
  (testing "pad space handles negative and 0 length"
    (is (= (pad-space -7 "test") "test   "))
    (is (= (pad-space 0 "test") ""))))

(deftest pad-space-edge-strings
  (testing "pad space handles edge case for strings"
    (is (= (pad-space 10 "") "          "))
    (is (= (pad-space 3 "hello")) "hello")))

