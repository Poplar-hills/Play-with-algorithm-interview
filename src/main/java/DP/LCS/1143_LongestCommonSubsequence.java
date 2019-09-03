package DP.LIS;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Longest Common Subsequence
*
* - Given two strings text1 and text2, return the length of their longest common subsequence.
*
* - LCS 的应用非常广泛，最经典的就是文本处理，除此之外还用于基因工程中判断两段基因的相似性，LCS 约长则两段基因越相似。
* */

class Solution {
    public static int longestCommonSubsequence(String text1, String text2) {
        return 0;
    }

    public static void main(String[] args) {
        log(longestCommonSubsequence("abcde", "aebd"));  // expects 3. "ace"
        log(longestCommonSubsequence("abcde", "ace"));   // expects 3. "ace"
        log(longestCommonSubsequence("abc", "abc"));     // expects 3. "abc"
        log(longestCommonSubsequence("abc", "def"));     // expects 0.
    }
}