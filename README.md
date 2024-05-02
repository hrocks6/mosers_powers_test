# Mosers Powers Test

MosersPowersVerifier was written to verify the results from [mosers_powers](https://github.com/Eiim/mosers_powers). It was written in Java to provide an independent implementation.

The program receives an input file from the user and checks if the n and x (lines 1 and 2) satisfies the expression:

    f(x) < 2^n < f(x+1)

where

    f(x) = 1/24x^4 - 1/4x^3 + 23/24x^2 -3/4x + 1