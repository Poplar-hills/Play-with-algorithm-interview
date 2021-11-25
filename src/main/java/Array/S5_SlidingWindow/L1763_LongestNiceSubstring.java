package Array.S5_SlidingWindow;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

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
     * 解法1：双指针遍历
     * - 💎 思路：首先，这类求 XXXsubstring、XXXsubarray 的题目通常有两种解法：
     *     1. 滑动窗口：如 L76_MinimumWindowSubstring、L438_FindAllAnagramsInString、L209_MinimumSizeSubarraySum
     *     2. 双指针遍历：如 L560_SubarraySumEqualsK
     *     - 从复杂度看，双指针滑动至少是 O(n^2)，而左右伸缩滑动可以是 O(n)。
     *   若采用左右伸缩滑动，则需一个条件用于判断何时伸何时缩，但对于该题来说找不到这样的条件（∵ 要找最长 substring ∴ 不能
     *   因为当前 substring 是 nice 了就停止伸长）∴ 只能采用双指针滑动，这也是最 intuitive 的解法 —— 通过双指针遍历所有
     *   substring，并检查每个是否 nice，同时使用一个外部变量 result 维护 the longest nice substring 即可。演示：
     *     "d D e E d E"
     *      l-r          - 找到一个 nice, result="dD"
     *      l---r
     *      l-----r      - 找到一个 nice, result="dDeE"
     *      l-------r    - 找到一个 nice, result="dDeEd"
     *      l---------r  - 找到一个 nice, result="dDeEdE"
     *        l-r
     *        l---r
     *        l-----r    - 找到一个 nice, result="DeEd"
     *        l-------r  - 找到一个 nice, result="DeEdE"
     *          l-r      - 找到一个 nice, result="eE"
     *          l---r
     *          l-----r
     *            l-r
     *            l---r
     *              l-r
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
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
     * 解法2：Divide & Rule + 递归
     * - 思路：采用分治思想，
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。
     * */
    public static String longestNiceSubstring2(String s) {
        if (s.length() < 2) return "";
        char[] chars = s.toCharArray();

        Set<Character> set = new HashSet<>();
        for (char c : chars) set.add(c);

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (set.contains(Character.toUpperCase(c)) && set.contains(Character.toLowerCase(c)))
                continue;
            String sub1 = longestNiceSubstring2(s.substring(0, i));
            String sub2 = longestNiceSubstring2(s.substring(i + 1));
            return sub1.length() >= sub2.length() ? sub1 : sub2;
        }

        return s;
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
        log(longestNiceSubstring("deDEE"));        // expects "deDEE"

        log(shortestNiceSubstring("azABaabza"));   // expects "ABaab"
        log(shortestNiceSubstring("CATattac"));    // expects "ATat"
        log(shortestNiceSubstring("TacoCat"));     // expects "-1"
        log(shortestNiceSubstring("Madam"));       // expects "-1"
        log(shortestNiceSubstring("AcZCbaBz"));    // expects "AcZCbaBz"
        log(shortestNiceSubstring("aZABcabbCa"));  // expects "ABcabbC"
    }
}
