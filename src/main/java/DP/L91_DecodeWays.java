package DP;

import static Utils.Helpers.log;

/*
* Decode Ways
*
* - A message containing letters from A-Z is being encoded to numbers using the following mapping: 'A' -> 1,
*   'B' -> 2, ..., 'Z' -> 26. Given a non-empty string containing only digits, determine the total number
*   of ways to decode it.
* */
public class L91_DecodeWays {
    public static int numDecodings(String s) {
        return 0;
    }

    public static void main(String[] args) {
        log(numDecodings("12"));   // expects 2. It could be decoded as "AB" (1 2) or "L" (12)
        log(numDecodings("226"));  // expects 3. It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6)
    }
}
