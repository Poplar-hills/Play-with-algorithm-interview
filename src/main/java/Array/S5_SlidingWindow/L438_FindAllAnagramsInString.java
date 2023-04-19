package Array.S5_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Find All Anagrams in a String
 *
 * - Given a string s and a non-empty string p, find all the start indices of p's anagrams (由颠倒字母顺序而
 *   构成的词) in s. Note strings consists of lowercase English letters only and the order of output does
 *   not matter.
 *
 * - 该题与 L76_MinimumWindowSubstring 非常类似，L76 是找包含目标字符串的最短 substring，该题是找 anagrams（没有多余字符）。
 * */

public class L438_FindAllAnagramsInString {
    /**
     * 解法1：滑动窗口
     * - 思路：与 L76_MinimumWindowSubstring 解法1一致，先充分扩展窗口，直到 p 中所有字符都进入窗口，然后再充分收缩，直到
     *   窗口内不再包含 p 中所有字符。另外 ∵ Anagram 的定义是不能含有多余字符，即需要 len(解)==len(p) ∴ 在充分收缩后要再
     *   检查窗口的 size 是否 == p.length，若是才算找到一个解。
     * - 例：s="e  e  c  b  a  e  b  a  b  a  c  d", p="abc"
     *         lr
     *         l-----------r                         - all in window ∴ start to shrink
     *               l-----r                         - found a solution: 2
     *                  l--r                         - not all in window ∴ start to expand
     *                  l--------------------r       - all in window ∴ start to shrink
     *                                 l-----r       - found a solution: 8
     *                                    l--r       - not all in window ∴ start to expand
     *                                    l------r   - r==n, loop ends
     * - 实现：与 L76_MinimumWindowSubstring 解法1一致：
     *   1. 先为 p 生成 freq map，将 unmatchCount 初始化为 freq map size；
     *   2. 内外两层 while：
     *      - 内层2个 while 控制窗口扩展和收缩，退出条件为 unmatchCount == 或 != 0（即 p 中所有字符是否已进入窗口）；
     *      - 外层 while 控制整个循环 ∵ 内层2个 while 已经充分扩展和收缩了 ∴ 外层 while 的退出条件只需关注 r==n；
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null || s.isEmpty() || p.isEmpty() || p.length() > s.length()) return res;
        char[] chars = s.toCharArray();
        Map<Character, Integer> freq = new HashMap<>();

        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int unmatchCount = freq.size(), l = 0, r = 0, n = s.length();  // unmatchCount 初始化为 freq map size

        while (r < n) {
            while (r < n && unmatchCount != 0) {     // 若 p 中所有字符还未进入窗口就持续扩展
                freq.merge(chars[r], -1, Integer::sum);
                if (freq.get(chars[r]) == 0) unmatchCount--;
                r++;
            }
            while (l <= r && unmatchCount == 0) {    // 若 p 中所有字符已经进入窗口则持续收缩
                freq.merge(chars[l], 1, Integer::sum);
                if (freq.get(chars[l]) > 0) unmatchCount++;
                l++;
            }
            if (r - l + 1 == p.length())  // 检查窗口大小是否 == p.length。注意 ∵ 上面扩展和收缩之后 r、l 都++过，
                res.add(l - 1);           // 即已经指向了下一个字符 ∴ 这里窗口大小的计算应该是 (r-1) - (l-1) + 1
        }

        return res;
    }

    /*
     * 解法2：滑动窗口
     * - 思路：与 L76_MinimumWindowSubstring 解法2一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0, n = s.length();
        char[] chars = s.toCharArray();

        while (true) {
            if (!(r < n)) break;
            if (freq.containsKey(chars[r])) {
                if (freq.get(chars[r]) > 0) matchCount++;
                freq.merge(chars[r], -1, Integer::sum);
            }
            r++;
            while (matchCount == p.length()) {
                if (r - l == p.length())
                    res.add(l);
                if (freq.containsKey(chars[l])) {
                    if (freq.get(chars[l]) == 0) matchCount--;
                    freq.merge(chars[l], 1, Integer::sum);
                }
                l++;
            }
        }

        return res;
    }

    /*
     * 解法3：滑动窗口（解法1、2的简化版，🥇最优解）
     * - 思路：与 L76_MinimumWindowSubstring 解法2一致。
     * - 实现：与解法1的区别：
     *   1. 只用一个内层 while 来收缩窗口，而扩展窗口使用外层 while 控制 ∴ 不再需要 isShrinking 标志位；
     *   2. 使用 matchCount 正向记录命中 t 中元素的个数（∴ 初值为0）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams3(String s, String p) {
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

            while (matchCount == p.length()) {        // 若窗口中包含了 p 的所有字符（找到了全部字符的 substring）
                if (r - l == p.length()) res.add(l);  // 若 substring 的长度等于 p 的长度，说明是 p 的 anagram
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
