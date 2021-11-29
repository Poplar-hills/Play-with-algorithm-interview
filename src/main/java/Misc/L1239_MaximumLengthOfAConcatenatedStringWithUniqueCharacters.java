package Misc;

import java.util.Arrays;
import java.util.List;

import static Utils.Helpers.log;

/*
 * Longest Palindromic Substring
 *
 * - You are given an array of strings arr. A string s is formed by the concatenation of a subsequence of arr
 *   that has unique characters. Return the maximum possible length of s.
 * - A subsequence is an array that can be derived from another array by deleting some or no elements without
 *   changing the order of the remaining elements.
 * */

public class L1239_MaximumLengthOfAConcatenatedStringWithUniqueCharacters {
    /*
     * 解法1：
     * -
     * */
    public static int maxLength(List<String> arr) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxLength(Arrays.asList("un", "iq", "ue")));              // expects 4. ("uniq", "ique")
        log(maxLength(Arrays.asList("cha", "r", "act", "ers")));      // expects 6. ("chaers", "acters")
        log(maxLength(Arrays.asList("abcdefghijklmnopqrstuvwxyz")));  // expects 26
        log(maxLength(Arrays.asList("aa", "bb")));                    // expects 0
    }
}