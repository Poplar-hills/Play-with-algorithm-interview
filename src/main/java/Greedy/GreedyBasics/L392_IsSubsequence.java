package Greedy.GreedyBasics;

import static Utils.Helpers.log;

/*
* Is Subsequence
*
* - Given a string s and a string t, check if s is subsequence of t.
*
* - Assumptions:
*   1. There is only lower case English letters in both s and t.
*   2. t is potentially a very long (length ~= 500,000) string, and s is a short string (<=100).
*
* - Follow up:
*   If there are lots of incoming S, say S1, S2, ... , Sk where k >= 1B, and you want to check one by one to
*   see if T has its subsequence. In this scenario, how would you change your code?
*
* */

public class L392_IsSubsequence {
    /*
     * 解法1：双指针
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;

        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) i++;
            j++;
        }

        return i == s.length();
    }

    public static void main(String[] args) {
        log(isSubsequence("abc", "ahbgdc"));  // expects true
        log(isSubsequence("axc", "ahbgdc"));  // expects false
    }
}
