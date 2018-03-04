# Prime Tables

## Description

tools for generating prime numbers and multiplication tables

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

## Building

## Prime generation algorithm
### Sieve of Eratosthenes
The method of prime number generation used here is based on an algorithm called the [Sieve of Eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes). In order to find all primes less than or equal to n:

    1. Create a list of consecutive integers from 2 to n.
    2. Let p equal 2, the smallest prime number.
    3. Create a list of integers from p^2 with step p less than n (p^2, p^2 + p, p^2 + 2p, p^2 + 3p, ...).
    4. If a number appears in the list generated from step 3, remove it from the original list.
    5. Increment p and repeat from step 3 if p^2 is less than n.
      
There is an optimization for this algorithm where we only use a number as p (in step 5) if that number is prime. That is on the Roadmap to be implemented in the future.
### Implementation
#### Primes less than N
The implementation is performed in a slightly different order than the steps above. We generate all the lists (steps 2, 3, and 5) first and then filter out the numbers (step 4). The steps are:

    1. Create a list of numbers to remove (`prime-tables.prime/generate-sieve`). 
       1. Create a list from 2 to sqrt(N)
       2. Generates a list of the form detailed in step 3 above (p^2, p^2 + p, p^2 + 2p, ...) for each number
       3. Concatenate them together
       4. Put them in a hash-set
    2. Iterate over numbers from 2 to N and filter out numbers above (`prime-tables.prime/generate-primes`).
  
#### N primes
Because the Sieve of Eratosthenes generates primes less than a certain number, it cannot be directly used to generate N primes. We [estimate](https://en.wikipedia.org/wiki/Prime-counting_function#Inequalities) the upper bound, but this only holds when N>=6. Thus this approach incrementally increases the bounds of the sieve without having to recreate the entire sieve. Consider the case where we want to get N primes, but the estimate function is off:

    1. Let E be the estimate upper bound to get N primes. 
    2. We create a sieve for numbers up to E and get all primes less than E.
    3. We don't have enough primes so we need to increase our range and make a new sieve.
    4. Let F be our new upper bound. Our new sieve needs to contain numbers of the form (p^2, p^2 + p, p^2 + 2p, ...) to F for p = 2, 3, ..., sqrt(F). Note our existing sieve contains numbers of that form to E for p = 2, 3, ..., sqrt(E).
       1. We create a sieve containing numbers of the form p^2 + x*p for p = 2, 3, ..., sqrt(E) where x is the first number that makes p^2 + x*p > E. This is done in (`prime-tables.prime/generate-missing-sieve`).
       2. We then create a sieve of numbers of the (p^2, p^2 + p, p^2 + 2p, ...) for integer p where sqrt(E) <= p < sqrt(F)
       3. We then concat these sieves together with our old sieve, meaning we now have all the numbers necessary.
    5. We use our new sieve to get all primes less than F. If this is still not enough, we can repeat step 3 and 4 again to increase the bound and make a new sieve.

This is implemented in `prime-tables.prime/generate-n-primes`.

## Roadmap
   - Implement optimization for prime generation algorithm to only generate range for a number if that number is prime.
   - Support cljs CLI tools.
