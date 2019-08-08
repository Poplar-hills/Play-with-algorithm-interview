package DP;

import static Utils.Helpers.log;

/*
* Integer Break
*
* - Given a positive integer n, break it into the sum of at least two positive integers and maximize the
*   product of those integers. Return the maximum product you can get.
* */

public class L343_IntegerBreak {
    public static int integerBreak(int n) {
        return 0;
    }
    public static void main(String[] args) {
        log(integerBreak(2));   // expects 1. (2 = 1 + 1, 1 × 1 = 1)
        log(integerBreak(10));  // expects 36. (10 = 3 + 3 + 4, 3 × 3 × 4 = 36)
    }
}