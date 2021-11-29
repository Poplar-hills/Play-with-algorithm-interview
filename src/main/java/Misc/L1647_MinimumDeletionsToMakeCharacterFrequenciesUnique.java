package Misc;

import static Utils.Helpers.log;

/*
 * Longest Palindromic Substring
 *
 * - A string s is called good if there are no two different characters in s that have the same frequency.
 * - Given a string s, return the minimum number of characters you need to delete to make s good.
 * - The frequency of a character in a string is the number of times it appears in the string. For example,
 *   in the string "aab", the frequency of 'a' is 2, while the frequency of 'b' is 1.
 * */

public class L1647_MinimumDeletionsToMakeCharacterFrequenciesUnique {
    /*
     * 解法1：
     * -
     * */
    public static int minDeletions(String s) {
        return 0;
    }

    public static void main(String[] args) {
        log(minDeletions("aab"));
        // expects 0. s is already good.

        log(minDeletions("aaabbbcc"));
        // expects 2. delete two 'b's to get "aaabcc" or delete one 'b' and one 'c' to get "aaabbc".

        log(minDeletions("ceabaacb"));
        // expects 2. delete both 'c's to get "eabaab". Note that we only care about characters that are
        // still in the string at the end (i.e. frequency of 0 is ignored).
    }
}
