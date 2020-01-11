package Array.S5_TwoPointerSlidingWindow;

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
     *     - Map freq 记录在窗口内同时又在 t 中的字符的频率；
     *     - int matchCount 记录已经匹配上的 t 中的字符的个数，若 matchCount == t.size() 说明 t 中所有字符已在窗口内。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String minWindow(String s, String t) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : t.toCharArray())       // 先构建 t 的频谱
            freq.merge(c, 1, Integer::sum);  // 相当于 freq.put(c, freq.get(c) + 1);

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;  // minLen 记录匹配上的子串的最小长度，start 记录其起始索引，用于最后截取
        char[] chars = s.toCharArray();

        while (r < s.length()) {
            // 先扩展窗口，减小 r 处字符在频谱中的频率
            if (freq.get(chars[r]) > 0) matchCount++;  // chars[r] 的频率 >0 表示 r 处的字符是待匹配字符（存在于 t 中且出现次数没有超过 t 中该字符的个数）
            freq.merge(chars[r], -1, Integer::sum);    // 相当于 freq.put(c, freq.get(c) - 1);
            r++;

            // 当窗口中包含了 t 中的所有字符时再开始收缩窗口，增大 l 处字符在频谱中的频率
            while (matchCount == t.length()) {
                if (r - l < minLen) {                  // 每收缩一下都查看一次当前的窗口是否比之前的更短
                    minLen = r - l;
                    start = l;
                }
                if (freq.get(chars[l]) == 0) matchCount--;  // 若该字符的频率为0，说明 t 中的该字符已经都被匹配上了
                freq.merge(chars[l], 1, Integer::sum);
                l++;
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /*
     * 解法2：解法1的 int[256] 版
     * - 思路：与解法1一致。
     * - 实现：采用 int[256] 代替解法1中的 Map，并化简合并某些过程。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static String minWindow2(String s, String t) {
        int[] freq = new int[256];
        for (char c : t.toCharArray()) freq[c]++;

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;

        while (r < s.length()) {
            if (freq[s.charAt(r++)]-- > 0)  // 若 r 处的字符存在于 t 中且之前没有遇到过，则 matchCount++（之前遇到过的字符的 freq <= 0）
                matchCount++;
            while (matchCount == t.length()) {  // 若窗口中已经包含了 t 中的所有字符，则可以开始收缩窗口
                if (r - l < minLen)             // 只要窗口包含所有 t 中的字符，每收缩一下都查看一次当前的窗口是否比之前的更短
                    minLen = r - (start = l);
                if (freq[s.charAt(l++)]++ == 0)  // 若 l 处的字符存在于 t 中，则 matchCount--（只有存在于 t 中的字符在经过上面 freq[..]-- 后能 == 0，其他的都 < 0）
                    matchCount--;
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        log(minWindow("ABAACBAB", "ABC"));       // expects "ACB"
        log(minWindow("ADOBECODEBANC", "ABC"));  // expects "BANC"（可以有多余的字符）
        log(minWindow("TT", "TT"));              // expects "TT"（若 t 中有重复字符，则解中也需要包含重复字符）
        log(minWindow("S", "SS"));               // expects ""
        log(minWindow("YYZ", "ZY"));             // expects "YZ"
    }
}
