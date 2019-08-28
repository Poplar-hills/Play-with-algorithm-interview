package DP.KnapsackProblem;

import static Utils.Helpers.*;

/*
* Target Sum
*
* - 给定一个非零数字序列，在这些数字前面加上"+"或"-"号，求一共有多少种方式使其计算结果为给定的整数 S。
* */

public class L494_TargetSum {
    public static int findTargetSumWays(int[] nums, int S) {
        return 0;
    }

    public static void main(String[] args) {
        log(findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
        /*
        * expects 5. There are 5 ways:
        *   -1+1+1+1+1 = 3
        *   +1-1+1+1+1 = 3
        *   +1+1-1+1+1 = 3
        *   +1+1+1-1+1 = 3
        *   +1+1+1+1-1 = 3
        * */
    }
}
