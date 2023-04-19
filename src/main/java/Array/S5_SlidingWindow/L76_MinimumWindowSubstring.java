package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

import java.util.*;

/*
 * Minimum Window Substring
 *
 * - Given two strings S and T, find the minimum substring in S which contain all the characters in T.
 * - If there is no such window in S that covers all characters in T, return the empty string "".
 * - If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
 * */

public class L76_MinimumWindowSubstring {
    /*
     * 超时解：双指针遍历
     * - 思路：与 L3_LongestSubstringWithoutRepeatingCharacters 超时解类似，使用双指针遍历所有 substring，然后检查每个
     *   substring 是否包含 t 中的所有字符（即 brute force）。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static String minWindow0(String s, String t) {
        char[] sChars = s.toCharArray(), tChars = t.toCharArray();
        int start = -1, minLen = Integer.MAX_VALUE;

        for (int l = 0; l < sChars.length; l++) {
            for (int r = l; r < sChars.length; r++) {
                int len = r - l + 1;
                if (containAllChars(sChars, l, r, tChars) && len < minLen) {
                    start = l;
                    minLen = len;
                }
            }
        }

        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    private static boolean containAllChars(char[] sChars, int l, int r, char[] tChars) {
        Map<Character, Integer> tFreq = new HashMap<>();
        for (char c : tChars)
            tFreq.merge(c, 1, Integer::sum);
        for (int i = l; i <= r; i++) {
            if (tFreq.containsKey(sChars[i])) {
                tFreq.merge(sChars[i], -1, Integer::sum);
                if (tFreq.get(sChars[i]) == 0)
                    tFreq.remove(sChars[i]);
            }
        }
        return tFreq.isEmpty();
    }

    /**
     * 解法1：滑动窗口（最 intuitive 解）
     * - 💎 思路：∵ 是找连续子串的问题 ∴ 可尝试滑动窗口方法求解。
     *   - 滑动方式：先右移 r 扩展窗口，直到 t 中所有字符进入窗口，之后右移 l 收缩窗口，直到窗口中不再包含 t 中所有字符。
     *     重复该过程直到 r 抵达 s 末尾。
     *   - 记录方式：∵ 要从 s 中切割子串出来 ∴ 需要 start index 和切割的长度。给这俩变量赋值的时机是：
     *     1. 每次窗口扩展到包含 t 中所有字符的时候；
     *     2. 每次窗口收缩到不再包含 t 中所有字符的前一步。
     * - 实现：先构造 freq map。在遍历过程中，非 t 中的字符也会被添加到 freq 中，但值总是 < 0，只有 t 中的字符才有可能 >= 0。
     * - 例：
     *      0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16
     * - s="C  A  B  W  E  F  G  E  W  C  W  A  E  F  G  C  F", t="CAE"：
     *      lr             - 初始化 map(c:1,a:1,e:1), start to expand
     *      l-----------r   unmatchCount==0 ∴ start=0, minLen=5, start to shrink
     *         l--------r   miss c ∴ unmatchCount!=0, start to expand
     *         l-----------------------r   unmatchCount==0 ∴ start to shrink
     *            l--------------------r   miss b ∴ unmatchCount!=0, start to expand
     *            l--------------------------r   unmatchCount==0 ∴ start to shrink, update start & minLen on every step
     *                           l-----------r   still shrinking, start=7, minLen=5
     *                              l--------r   miss e ∴ unmatchCount!=0, start to expand
     *                              l-----------r   unmatchCount==0 ∴ start to shrink
     *                                 l--------r   still shrinking, start=9, minLen=4
     *                                    l-----r   missing c ∴ unmatchCount!=0, start to expand
     *                                    l--------------r   unmatchCount==0 ∴ start to shrink
     *                                          l--------r   missing a, start to expand
     *                                          l-----------r   r < n-1, loop ends
     */
    public static String minWindow(String s, String t) {
        if (s == null || t == null || s.isEmpty() || t.isEmpty()) return null;
        char[] chars = s.toCharArray();
        int l = 0, r = -1, n = chars.length, start = -1, minLen = n + 1;  // r 初始化为-1, minLen 为 n+1
        Map<Character, Integer> freq = new HashMap<>();
        boolean isShrinking = false;      // 标识窗口是在 expanding or shrinking

        for (char c : t.toCharArray())
            freq.merge(c, 1, Integer::sum);
        int unmatchCount = freq.size();   // unmatchCount 初始化为 t 中不重复的元素个数

        while (r < n - 1) {  // ∵ 扩展之后会充分收缩（由内层 while 保证）∴ 外层循环条件只需关注 r（n-1 是为了保证下面先 ++r 的时候不越界）
            while (!isShrinking && r < n - 1) {  // expanding
                freq.merge(chars[++r], -1, Integer::sum);  // ∵ r 初值为-1 ∴ 这里要先++
                if (freq.get(chars[r]) == 0) unmatchCount--;  // chars[r] 在 freq 中的个数为0表示当前窗口已包含了 t 中所有的该元素 ∴ unmatchCount--
                if (unmatchCount == 0) {       // 当前窗口已包含了若 t 中所有元素，则取 minLen & start 并开始 shrink
                    if (r - l + 1 < minLen) {  // 注意 ==minLen 的情况也要更新，否则 case s="TT",t="TT" 会失败
                        start = l;
                        minLen = r - l + 1;
                    }
                    isShrinking = true;
                }
            }
            while (isShrinking) {                // shrinking
                freq.merge(chars[l], 1, Integer::sum);  // 从窗口中移出 chars[l] 并更新 unmatchCount 后再 l++
                if (freq.get(chars[l]) > 0) unmatchCount++;
                l++;
                if (unmatchCount == 0 && (r - l + 1 < minLen)) {
                    start = l;
                    minLen = r - l + 1;
                }
                if (unmatchCount != 0 || l == r)  // 若 miss 了 t 中的任何元素，则 unmatchCount!=0（l==r 用于 case s="ab",t="a"）
                    isShrinking = false;
            }
        }

        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /*
     * 解法2：滑动窗口（解法1的简化版，🥇最优解）
     * - 思路：与解法1一致。
     * - 实现：与解法1的区别：
     *   1. 只用一个内层 while 来收缩窗口，而扩展窗口使用外层 while 控制 ∴ 不再需要 isShrinking 标志位；
     *   2. 使用 matchCount 正向记录命中 t 中元素的个数（∴ 初值为0）。
     * - 💎 套路：扩展/收缩滑动窗口类型的题目都可以采用该模式
     *   1. 大 while 循环内，让 r 向右充分扩展后再让 l 向右充分收缩（r 用 if 扩展，l 用 while 收缩）；
     *   2. 扩展/收缩时检测 r/l 处的字符在频谱 freq 中的值来增/减 matchCount；
     *   3. 在每次收缩后比较并记录子串长度；
     *   4. 大 while 结束条件为 r < n。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String minWindow2(String s, String t) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : t.toCharArray())
            freq.merge(c, 1, Integer::sum);

        char[] chars = s.toCharArray();
        int l = 0, r = 0, n = chars.length;
        int matchCount = 0, minLen = n + 1, start = -1;

        while (r < s.length()) {
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)  // 在窗口未包含 t 中所有字符之前不会进入下面的内层
                matchCount++;                                          // while ∴ 窗口会先得到充分扩展
            freq.merge(chars[r++], -1, Integer::sum);

            while (matchCount == t.length()) {  // 一旦窗口中包含了 t 中的所有字符，就开始续收缩窗口
                if (r - l < minLen) {           // 当所有该字符都已匹配上，且窗口宽度比之前更小时，更新 minLen、start
                    minLen = r - l;             // ∵ r 在上面已经++过了，指向下一个待处理的字符 ∴ 这里窗口长度为 r-l，而非 r-l+1
                    start = l;
                }
                if (freq.get(chars[l]) == 0)    // ∵ 只有 t 中字符才可能频率 >= 0 ∴ 若这里的字符频率为0，就意味着一定是
                    matchCount--;               // t 中字符，且频次也已匹配上了 ∴ 从窗口移出时需要 matchCount--
                freq.merge(chars[l++], 1, Integer::sum);
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        log(minWindow2("ABAACBAB", "ABC"));           // expects "ACB" or "CBA"
        log(minWindow2("BCAACBAB", "BBC"));           // expects "CBAB" (t 中存在重复字符的情况)
        log(minWindow2("TT", "TT"));                  // expects "TT"
        log(minWindow2("S", "SS"));                   // expects ""
        log(minWindow2("YYZ", "ZY"));                 // expects "YZ"
        log(minWindow2("CABWEFGEWCWAEFGCF", "CAE"));  // expects "CWAE"
        log(minWindow2("ab", "a"));                   // expects "a"
    }
}
