package Array.S5_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Find All Anagrams in a String
 *
 * - Given a string s and a non-empty string p, find all the start indices of p's anagrams (由颠倒字母顺序而
 *   构成的词) in s.
 *
 * - Note:
 *   - Strings consists of lowercase English letters only.
 *   - The order of output does not matter.
 * */

public class L438_FindAllAnagramsInString {
    /*
     * 解法1：滑动窗口
     * - 思路：与 L76_MinimumWindowSubstring 解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0;
        char[] sChars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(sChars[r])) {
                if (freq.get(sChars[r]) > 0) matchCount++;
                freq.merge(sChars[r], -1, Integer::sum);
            }
            r++;
            while (matchCount == p.length()) {
                if (r - l == p.length())
                    res.add(l);
                if (freq.containsKey(sChars[l])) {
                    if (freq.get(sChars[l]) == 0) matchCount--;
                    freq.merge(sChars[l], 1, Integer::sum);
                }
                l++;
            }
        }

        return res;
    }

    /*
     * 解法2：滑动窗口（解法1的简化版）
     * - 思路：与解法1一致。
     * - 实现：与 L76_MinimumWindowSubstring 解法2一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0;
        char[] sChars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(sChars[r]) && freq.get(sChars[r]) > 0)
                matchCount++;
            freq.merge(sChars[r++], -1, Integer::sum);

            while (matchCount == p.length()) {
                if (r - l == p.length()) res.add(l);
                if (freq.get(sChars[l]) == 0) matchCount--;
                freq.merge(sChars[l++], 1, Integer::sum);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(findAnagrams("eecbaebabacd", "abc"));  // expects [2, 8] ("cba", "bac")
        log(findAnagrams("xxyxxy", "xxy"));        // expects [0, 1, 2, 3] ("xxy", "xyx", "yxx", "xxy")
        log(findAnagrams("cccddd", "dc"));         // expects [2] ("cd")
        log(findAnagrams("z", "zz"));              // expects []
        log(findAnagrams("hhh", "hhh"));           // expects [0]
        log(findAnagrams("aaaabaaaa", "aaaa"));    // expects [0, 5]
    }
}
