package Array.S5_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Longest Nice Substring
 *
 * - A string s is nice if, for every letter of the alphabet that s contains, it appears both in uppercase
 *   and lowercase. For example, "abABB" is nice because 'A' and 'a' appear, and 'B' and 'b' appear. However,
 *   "abA" is not because 'b' appears, but 'B' does not.
 * - Given a string s, return the longest substring of s that is nice. If there are multiple, return the
 *   substring of the earliest occurrence. If there are none, return an empty string.
 *
 * - Following question: Instead of returning the longest, return the shortest nice substring of s.
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
            for (int r = l + 2; r <= s.length(); r++) {  // ∵ nice substring 至少要有两个字符 ∴ r 要从 l+2 开始，且 r <= n
                String sub = s.substring(l, r);
                if (result.length() < sub.length() && isNice(sub))
                    result = sub;
            }
        }
        return result;
    }

    public static boolean isNice(String str) {
        char[] chars = str.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char c : chars) set.add(c);

        for (char c : chars)
            if (!set.contains(Character.toLowerCase(c)) || !set.contains(Character.toUpperCase(c)))
                return false;

        return true;
    }

    /*
     * 解法2：Divide & Conquer + 递归
     * - 思路：采用分治思想。遍历 [0,n)，若其中某一字符 c 在 s 中没有对应的大/小写字符，则将其 exclude 掉，然后在其左右子字符串
     *   中继续递归查找。
     * - 例："Y  a  z  a  A  a  y"
     *       i                    - 有配对，continue
     *          i                 - 有配对，continue
     *             i              - 无配对，在"Ya", "aAay"中继续查找，前者返回""，后者返回"aAa"
     *                i           - 有配对，continue
     *                   i        - 有配对，continue
     *                      i     - 有配对，continue
     *                         i  - 有配对，continue，最终返回"aAa"
     *       -> "Y  a"
     *           i     - 无配对，在"", "a"中继续查找，都返回""
     *       -> "a  A  a  y"
     *           i           - 有配对，continue
     *              i        - 有配对，continue
     *                 i     - 有配对，continue
     *                    i  - 无配对，在"aAa", ""中继续查找，前者返回"aAa"
     *          -> "a  A  a"
     *              i           - 有配对，continue
     *                 i        - 有配对，continue
     *                    i     - 有配对，continue，返回"aAa"
     *
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static String longestNiceSubstring2(String s) {
        if (s.length() < 2) return "";
        char[] chars = s.toCharArray();

        Set<Character> set = new HashSet<>();
        for (char c : chars) set.add(c);

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!set.contains(Character.toUpperCase(c)) || set.contains(Character.toLowerCase(c))) { // 若 c 在 s 中没有配对
                String sub1 = longestNiceSubstring2(s.substring(0, i));             // 在 c 左边的字符串中查找
                String sub2 = longestNiceSubstring2(s.substring(i + 1));  // 在 c 右边的字符串中查找
                return sub1.length() >= sub2.length() ? sub1 : sub2;
            }
        }

        return s;
    }

    /*
     * For the following question - find the shortest nice substring of s.
     * 解法1：双指针遍历（Brute force）
     * - 思路：双指针遍历所有 substring，在 for 循环内部，检查 substring 的每一个字符，若字符为大小，则放入小写 Set 中，若为
     *   大写，则先转换为小写后再放入大写 Set 中。这样若该 substring 是 nice 的，则最后得到的2个 Set 中的元素应该一模一样。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static String shortestNiceSubstring(String s) {
        String res = "";
        if (s == null || s.isEmpty()) return res;
        char[] chars = s.toCharArray();
        int minLen = s.length() + 1;

        for (int l = 0; l < s.length(); l++) {
            for (int r = l + 1; r < s.length(); r++) {
                if (isNice2(chars, l, r) && r - l + 1 < minLen) {
                    res = s.substring(l, r + 1);
                    minLen = r - l + 1;
                }
            }
        }

        return res;
    }

    public static boolean isNice2(char[] chars, int l, int r) {
        Set<Character> lowerSet = new HashSet<>(), upperSet = new HashSet<>();

        for (int i = l; i <= r; i++) {            // 遍历 substring 的每一个字符
            if (Character.isLowerCase(chars[i]))  // 若是小写，放入小写 Set 中
                lowerSet.add(chars[i]);
            else                                  // 若是大写，先转换成小写后放入大写 Set 中
                upperSet.add(Character.toLowerCase(chars[i]));
        }

        return lowerSet.equals(upperSet);
    }

    public static void main(String[] args) {
        log(longestNiceSubstring("YazaAay"));      // expects "aAa"
        log(longestNiceSubstring("deDEE"));        // expects "deDEE"
        log(longestNiceSubstring("Bb"));           // expects "Bb"
        log(longestNiceSubstring("c"));            // expects ""
        log(longestNiceSubstring("abc"));          // expects ""

        log(shortestNiceSubstring("azABaabza"));   // expects "ABaab"
        log(shortestNiceSubstring("CATattac"));    // expects "ATat"
        log(shortestNiceSubstring("CATattaA"));    // expects "aA"
        log(shortestNiceSubstring("TacoCat"));     // expects ""
        log(shortestNiceSubstring("Madam"));       // expects ""
        log(shortestNiceSubstring("AcZCbaBz"));    // expects "AcZCbaBz"
        log(shortestNiceSubstring("aZABcabbCa"));  // expects "ABcabbC"
    }
}
