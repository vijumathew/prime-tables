(ns prime-tables.prime-test
  (:require [prime-tables.prime :as p]
            [clojure.test :as t :refer [deftest testing is]]))

(def primes-below-100 '(2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97))
(defn prime-num? [n]
  (contains? (apply hash-set primes-below-100) n))
(defn to-hash-set [l] (apply hash-set l))

;;private functions, so only testing happy path
(deftest generate-range-test
  (testing "generate range returns proper sequences for Sieve of Eratosthenes"
    (let [gen-range (var p/generate-range)]
      (is (= (gen-range 20 2) '(4 6 8 10 12 14 16 18)))
      (is (= (gen-range 70 7) '(49 56 63)))
      (is (= (gen-range 50 4) '(16 20 24 28 32 36 40 44 48))))))

(deftest generate-sieve-test
  (testing "generate-sieve returns correct sieve"
    (let [gen-sieve (var p/generate-sieve)]
      (is (= (gen-sieve 30) (to-hash-set (remove prime-num? (range 2 30)))))
      (is (= (gen-sieve 70) (to-hash-set (remove prime-num? (range 2 70))))))))

(deftest round-to-start-test
  (testing "round-to-start goes to the correct number"
    (let [rnd-to-start (var p/round-to-start)]
      (is (= (rnd-to-start 20 2) 20))
      (is (= (rnd-to-start 20 3) 21))
      (is (= (rnd-to-start 20 4) 20))
      (is (= (rnd-to-start 58 7) 63))
      (is (= (rnd-to-start 95 8) 96)))))

(deftest generate-missing-sieve-test
  (testing "missing sieve returns correct numbers"
    (let [gen-missing-sieve (var p/generate-missing-sieve)]
      (is (= (to-hash-set (gen-missing-sieve 20 50))
             #{20 27 24 39 46 48 21 32 40 33 22 36 44 28 34 45 26 38 30 42}))
      (is (= (to-hash-set (gen-missing-sieve 30 70))
             (to-hash-set (concat (range 30 70 2) (range 30 70 3) (range 32 70 4)
                                        (range 30 70 5))))))))

(deftest generate-sieve-with-missing-test
  (testing "generate sieve with missing works properly"
    (let [gen-missing-sieve (var p/generate-missing-sieve)
          gen-sieve (var p/generate-sieve)]
      (is (= (gen-sieve 100) (gen-sieve 60 100 (concat (gen-sieve 60) (gen-missing-sieve 60 100)))))
      (is (= (gen-sieve 850) (gen-sieve 600 850
                                        (concat (gen-sieve 400 600 (concat (gen-sieve 400)
                                                                           (gen-missing-sieve 400 600)))
                                                (gen-missing-sieve 600 850)))))
      (is (= (gen-sieve 307) (gen-sieve 283 307 (concat (gen-sieve 283) (gen-missing-sieve 283 307))))))))

;;public functions
(deftest generate-primes-test
  (testing "generate-primes returns correct list of primes"
    (is (= (p/generate-primes 100) primes-below-100))
    (is (= (p/generate-primes 85) (filter (partial > 85) primes-below-100)))
    (is (= (p/generate-primes 59) (filter (partial > 59) primes-below-100)))
    (is (= (p/generate-primes -1) '()))
    (is (= (p/generate-primes 0) '()))
    (is (= (p/generate-primes 1) '()))
    (is (= (p/generate-primes 2) '()))
    (is (= (p/generate-primes 3) '(2)))
    (is (= (p/generate-primes 5) '(2 3)))))

(deftest generate-n-primes-test
  (testing "generate-n-primes returns correct list of primes"
    (is (= (p/generate-n-primes 10) (take 10 primes-below-100)))
    (is (= (p/generate-n-primes 25) (take 25 primes-below-100)))
    (is (= (p/generate-n-primes -1) '()))
    (is (= (p/generate-n-primes 0) '()))
    (is (= (p/generate-n-primes 1) '(2)))
    (is (= (p/generate-n-primes 2) '(2 3)))))
