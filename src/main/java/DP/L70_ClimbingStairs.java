package DP;

import static Utils.Helpers.log;

/*
* Climbing Stairs
*
* - You are climbing a stair case. It takes n steps to reach to the top. Each time you can either climb 1 or 2 steps.
*   In how many distinct ways can you climb to the top? Note: Given n will be a positive integer.
* */

public class L70_ClimbingStairs {
    public static int climbStairs(int n) {

    }

    public static void main(String[] args) {
        log(climbStairs(2));  // expects 2 (1 + 1, 2 in one go)
        log(climbStairs(3));  // expects 2 (1 + 1, 1 + 2, 2 + 1)
    }
}
