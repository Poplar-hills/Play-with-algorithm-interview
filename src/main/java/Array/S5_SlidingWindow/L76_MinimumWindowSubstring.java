package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
 * Minimum Window Substring
 *
 * - Given two strings S and T, find the minimum substring in S which contain all the characters in T.
 * - If there is no such window in S that covers all characters in T, return the empty string "".
 * - If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
 * */

public class L76_MinimumWindowSubstring {
    /*
     * 解法1：
     * - 思路：∵ 该题是找连续子串的问题 ∴ 可尝试滑动窗口方法求解 —— 控制窗口左右边界的滑动来找到所需子串。通过观察 test case 1
     *   可知要求的最小子串需要包含 t 中所有字符，且尽量少的包含重复字符 ∴ 可得到窗口滑动控制方式：先右移 r 扩展窗口，直到 t 中
     *   所有字符进入窗口。之后右移 l 收缩窗口，直到窗口中不再包含 t 中所有字符，此时记录窗口长度，重复该过程直到 r 抵达 s 末尾。
     *   例：对于 "ABAACBAB" 来说：先右移 r，当到达 C 时发现 t 中的所有字符都已进入窗口，此时再右移 l，直到达第2个 A 时发现
     *   窗口中已不再包含 t 中的所有字符，则此时的 s[l-1,r] 即 "BAAC" 就是当前找到的最小子串。
     * - 实现：该过程中需要知道，当任意边界右移一步后：
     *     1. 边界上的字符是否是 t 中的字符；
     *     2. 此时窗口内是否包含 t 中的所有字符。
     *   这需要两个结构来实现：
     *     - Map freq 记录在窗口内同时又在 t 中的字符的频次；
     *     - int matchCount 记录已经匹配上的 t 中的字符的个数，若 matchCount == t.size() 说明 t 中所有字符已在窗口内。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String minWindow(String s, String t) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : t.toCharArray())            // 先构建 t 的频谱
            freq.merge(c, 1, Integer::sum);       // 相当于 freq.put(c, freq.getDefault(c)+1);

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;  // minLen 记录匹配上的子串的最小长度，start 记录其起始索引，用于最后截取
        char[] chars = s.toCharArray();

        while (r < s.length()) {
            // 先扩展窗口（该过程中减小 r 处字符在频谱中的频次）
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)  // 在频谱中且频次 >0 表示 r 处字符匹配上了
                matchCount++;
            freq.merge(chars[r++], -1, Integer::sum);  // 扩展窗口、r 处字符频次-1

            // 当窗口中包含了 t 中的所有字符时开始收缩窗口（该过程中增大 l 处字符在频谱中的频次）
            while (matchCount == t.length()) {
                if (freq.get(chars[l]) == 0) {   // l 处字符频次为0说明 t 中所有的该字符已经刚好被匹配完了，此时要记录 minLen
                    matchCount--;                // （若匹配过多则频次会 <0，说明是冗余字符，可以不需记录 minLen 而直接收缩窗口）
                    if (r - l < minLen) {        // 当所有该字符都已匹配上 & 窗口宽度比之前的更小时，更新 minLen、start
                        minLen = r - l;
                        start = l;
                    }
                }
                freq.merge(chars[l++], 1, Integer::sum);  // 收缩窗口、chars[l] 频次+1
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /*
     * 解法2：解法1的 int[256] 版
     * - 思路：与解法1一致。
     * - 实现：采用 int[256] 代替解法1中的 Map，从而得以简化语句（这种类型的题目中，int[256] 的解法通常都能比 Map 更简洁）。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static String minWindow2(String s, String t) {
        int[] freq = new int[256];
        for (char c : t.toCharArray()) freq[c]++;

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;

        while (r < s.length()) {
            if (freq[s.charAt(r++)]-- > 0)  // 这一个条件在解法1中需要两个条件才能实现 ∵ int[256] 为所有字符都设了初值0
                matchCount++;
            while (matchCount == t.length()) {
                if (r - l < minLen)
                    minLen = r - (start = l);    // 2 assignments in 1 line
                if (freq[s.charAt(l++)]++ == 0)
                    matchCount--;
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        log(minWindow("ABAACBAB", "ABC"));  // expects "ACB"
        log(minWindow("BCAACBAB", "BBC"));  // expects "CBAB" (t 中也可能存在重复字符)
        log(minWindow("TT", "TT"));         // expects "TT"
        log(minWindow("S", "SS"));          // expects ""
        log(minWindow("YYZ", "ZY"));        // expects "YZ"
    }
}
