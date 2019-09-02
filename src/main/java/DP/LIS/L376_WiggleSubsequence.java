package DP.LIS;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Wiggle Subsequence
*
* - 若一个序列中相邻数字是升序、降序轮流交替的，则该序列是一个 wiggle subsequence。例如：
*   ✅ [1, 7, 4, 9, 2, 5]
*   ❎ [1, 4, 7, 2, 5]
*   ❎ [1, 7, 4, 5, 5]
*   给定一个数组，求其最长 wiggle subsequence。
* */

public class L376_WiggleSubsequence {
    public static int wiggleMaxLength(int[] nums) {
        return 0;
    }

    public static void main(String[] args) {
        log(wiggleMaxLength(new int[]{1, 7, 4, 9, 2, 5}));                    // expects 6
        log(wiggleMaxLength(new int[]{1, 17, 5, 10, 13, 15, 10, 5, 16, 8}));  // expects 7
        log(wiggleMaxLength(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));           // expects 2
    }
}