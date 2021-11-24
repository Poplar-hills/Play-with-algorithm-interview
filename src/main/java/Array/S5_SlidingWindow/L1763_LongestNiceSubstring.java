package Array.S5_SlidingWindow;

import java.util.HashSet;
import java.util.Set;

import static Utils.Helpers.log;

/*
 * Longest Nice Substring
 *
 * - A string s is nice if, for every letter of the alphabet that s contains, it appears both in uppercase
 *   and lowercase. For example, "abABB" is nice because 'A' and 'a' appear, and 'B' and 'b' appear. However,
 *   "abA" is not because 'b' appears, but 'B' does not.
 * - Given a string s, return the longest substring of s that is nice. If there are multiple, return the
 *   substring of the earliest occurrence. If there are none, return "-1".
 *
 * - Following question: Instead of returning the longest, return the shortest substring of s.
 * */

public class L1763_LongestNiceSubstring {

    /*
     * 解法1：
     * */
    public static String longestNiceSubstring(String s) {
        String result = "";

        for (int l = 0; l < s.length(); l++) {
            for (int r = l + 1; r <= s.length(); r++) {
                String sub = s.substring(l, r);
                if (sub.length() > 1 && result.length() < sub.length() && isNice(sub))
                    result = sub;
            }
        }

        return result.isEmpty() ? "-1" : result;
    }

    public static boolean isNice(String str) {
        for (char c : str.toCharArray())
            if (str.contains(Character.toUpperCase(c) + "") != str.contains(Character.toLowerCase(c) + ""))
                return false;
        return true;
    }

    /*
     * For the following question - find the shortest nice substring of s.
     * 解法1：
     * - for (r=1; r<n; r++)                      for (l=0; l<n; l++)
     *      for (l=0; l<r; l++)                     for (r=l+1; r<n; r++)
     *   "C A T a t t a c"                        "C A T a t t a c"
     *    l-r               - "CA"                   l-r              - "CA"
     *    l---r             - "CAT"                  l---r            - "CAT"
     *      l-r             - "AT"                   l-----r          - "CATa"
     *    l-----r           - "CATa"                 l-------r        - "CATat"
     *      l---r           - "ATa"                  l---------r      - "CATatt"
     *        l-r           - "Ta"                   l-----------r    - "CATatta"
     *    l-------r         - "CATat"                l-------------r  - "CATattac"（找到的第一个解）
     *      l-----r         - "ATat"（找到的第一个解）
     * */
    public static String shortestNiceSubstring(String s) {
        if (s == null || s.isEmpty()) return "-1";
        char[] chars = s.toCharArray();

        for (int r = 1; r < s.length(); r++) {
            for (int l = 0; l < r; l++) {
                Set<Character> lowerSet = new HashSet<>();
                Set<Character> upperSet = new HashSet<>();

                for (int i = l; i <= r; i++) {
                    if (Character.isLowerCase(chars[i]))
                        lowerSet.add(chars[i]);
                    else
                        upperSet.add(Character.toLowerCase(chars[i]));
                }

                if (lowerSet.equals(upperSet))
                    return s.substring(l, r + 1);
            }
        }

        return "-1";
    }

    public static void main(String[] args) {
        log(longestNiceSubstring("YazaAay"));      // expects "aAa"
        log(longestNiceSubstring("Bb"));           // expects "Bb"
        log(longestNiceSubstring("c"));            // expects "-1"
        log(longestNiceSubstring("dDzeE"));        // expects "dD"

        log(shortestNiceSubstring("azABaabza"));   // expects "ABaab"
        log(shortestNiceSubstring("CATattac"));    // expects "ATat"
        log(shortestNiceSubstring("TacoCat"));     // expects "-1"
        log(shortestNiceSubstring("Madam"));       // expects "-1"
        log(shortestNiceSubstring("AcZCbaBz"));    // expects "AcZCbaBz"
        log(shortestNiceSubstring("aZABcabbCa"));  // expects "ABcabbC"
    }
}
