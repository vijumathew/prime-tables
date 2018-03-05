# Prime Tables

## Description

Tools for generating prime numbers and multiplication tables.

```
--------------------------------
|    | 2  | 3  | 5  | 7  | 11  |
--------------------------------
| 2  | 4  | 6  | 10 | 14 | 22  |
--------------------------------
| 3  | 6  | 9  | 15 | 21 | 33  |
--------------------------------
| 5  | 10 | 15 | 25 | 35 | 55  |
--------------------------------
| 7  | 14 | 21 | 35 | 49 | 77  |
--------------------------------
| 11 | 22 | 33 | 55 | 77 | 121 |
--------------------------------

------------------------
| this  | is   | a     |
------------------------
| table | with | words |
------------------------
```

## Running

### CLI

Download the repo and cd into it:

```bash
$ cd prime-tables
```

The following also take an optional input N for the number of primes to output.

#### Clojure CLI

at your terminal enter:

```bash
$ clojure -m prime-tables.core
```
for a multiplication table of the first 5 primes:
```bash
$ clojure -m prime-tables.core 5
```

#### Leiningen
at your terminal enter:

```bash
$ lein run
```
for a multiplication table of the first 5 primes:
```bash
$ lein run 5
```

## Packaging
The simplest way to package this is to use Leiningen.

### Standalone JAR
Go into the directory containing the project and run uberjar. That is

```bash
$ lein uberjar
```

This can be executed as a standalone executable.
```bash
$ java -jar target/prime-tables-0.1.0-standalone.jar
```

### JAR without dependencies
This creates a lighter JAR, but it needs to be executed with Clojure on the classpath to work. To build the jar go into the directory containing the project and:

```bash
$ lein jar
```

This is run by including Clojure on the classpath and executing the JAR. Notice the underscore!

```bash
$ java -cp target/prime-tables-0.1.0.jar:clojure.jar prime_tables.core
```
## API
### Prime
The `prime-tables.prime` namespace contains functions for generating prime numbers.

* `generate-primes` - return a list of primes less than a number N.
* `generate-n-primes` - return the first N primes.

### Table
The `prime-tables.table` namespace contains functions for generating multiplication tables and printing tables.

* `build-multiplication-table` - builds a multiplication table from a provided seq. Returns a list of lists
* `print-table` - takes a seq of seqs and prints them formatted as a table. This is specific to this usecase and  assumes the largest elements are in the last seq. Fixing this is on the [Roadmap](#roadmap).

## Prime generation algorithm
### Sieve of Eratosthenes
The method of prime number generation used here is based on an algorithm called the [Sieve of Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes). In order to find all primes less than or equal to n:

1. Create a list of consecutive integers from 2 to n.
2. Let p = 2, the smallest prime number.
3. Create a list of integers less than `n` of the form `p^2 + x*p`. The list will be like `p^2, p^2 + p, p^2 + 2p, p^2 + 3p, ...`.
4. If a number appears in the list generated from step 3, remove it from the original list.
5. Increment p and repeat from step 3 if `p^2 < n`.

There is an optimization for this algorithm where we only use a number as p (in step 5) if that number is prime. That is on the [Roadmap](#roadmap) to be implemented in the future.
### Implementation
#### Primes less than N
The implementation is performed in a slightly different order than the steps above. We generate all the lists (steps 2, 3, and 5) first and then filter out the numbers (step 4). The steps are:

1. Create a list of numbers to remove (`prime-tables.prime/generate-sieve`).
   1. Create a list from 2 to sqrt(N)
   2. Generates a list of the form detailed in step 3 above `p^2, p^2 + p, p^2 + 2p, ...` for each number
   3. Concatenate them together
   4. Put them in a hash-set
2. Iterate over numbers from 2 to N and filter out numbers above (`prime-tables.prime/generate-primes`).

#### N primes
Because the Sieve of Eratosthenes generates primes less than a certain number, it cannot be directly used to generate N primes. We [estimate](https://en.wikipedia.org/wiki/Prime-counting_function#Inequalities) the upper bound, but this only holds when N>=6. Thus this approach incrementally increases the bounds of the sieve without having to recreate the entire sieve. Consider the case where we want to get N primes, but the estimate function is off:

1. Let E be the estimate upper bound to get N primes.
2. We create a sieve for numbers up to E and get all primes less than E.
3. We don't have enough primes so we need to increase our range and make a new sieve.
4. Let F be our new upper bound. Our new sieve needs to contain numbers of the form `p^2, p^2 + p, p^2 + 2p, ...` to F for `p = 2, 3, ..., sqrt(F)`. Note our existing sieve contains numbers of that form to E for `p = 2, 3, ..., sqrt(E)`.
   1. We create a sieve containing numbers of the form `p^2 + x*p` for `p = 2, 3, ..., sqrt(E)` where x is the first number such that `p^2 + x*p > E`. This is done in (`prime-tables.prime/generate-missing-sieve`).
   2. We then create a sieve of numbers of the form `p^2, p^2 + p, p^2 + 2p, ...` for integer p where `sqrt(E) <= p < sqrt(F)`
   3. We then concat these sieves together with our old sieve, meaning we now have all the numbers necessary detailed above.
5. We use our new sieve to get all primes less than F. If this is still not enough, we can repeat step 3 and 4 again to increase the bound and make a new sieve.

This is implemented in `prime-tables.prime/generate-n-primes`.

## Roadmap
   - Implement optimization for prime generation algorithm to only generate range for a number if that number is prime
   - Support cljs CLI tools
   - Support generic tables in `print-table`
