package Misc;

import static Utils.Helpers.log;

/*
 * Longest Palindromic Substring
 *
 * - Given a string s, return the longest palindromic substring in s.
 * */

public class L5_LongestPalindromicSubstring {
    /*
     * 解法1：
     * -
     * */
    public static String longestPalindrome(String s) {
        return null;
    }

    public static void main(String[] args) {
        log(longestPalindrome("babad"));  // expects "bab" or "aba"
        log(longestPalindrome("cbbd"));   // expects "bb"
        log(longestPalindrome("a"));      // expects "a"
        log(longestPalindrome("ac"));     // expects "a"
    }
}
