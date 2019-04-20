package Array.TwoPointerSlidingWindow;

/*
* Minimum Window Substring
*
*
* */

import static Utils.Helpers.log;

public class L76_MinimumWindowSubstring {
    public static String minWindow(String s, String t) {

    }

    public static void main(String[] args) {
        log(minWindow("ADOBECODEBANC", "ABC"));  // expects "BANC"
        log(minWindow("a", "aa"));               // expects ""
        log(minWindow("aa", "aa"));              // expects "aa"
        log(minWindow("bba", "ab"));             // expects "ba"
    }
}
