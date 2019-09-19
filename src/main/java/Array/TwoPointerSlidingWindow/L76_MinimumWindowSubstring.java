package Array.TwoPointerSlidingWindow;

import static Utils.Helpers.log;

/*
* Minimum Window Substring
*
* - Given a string S and a string T, find the minimum substring in S which contain all the characters in T in complexity O(n).
* - If there is no such window in S that covers all characters in T, return the empty string "".
* - If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
* */

public class L76_MinimumWindowSubstring {
    /*
    * 解法1：滑动窗口
    * - 思路：
    *   - 先找到最小宽度的 substring，然后记录其起点和终点索引，从而能够根据这两个索引对 s 进行截取，最终获得该 substring。
    *   - 要找到最小宽度的 substring，可以采用滑动窗口：
    *     1. 先让窗口右界右滑，拓宽窗口，直到 t 中的所有字符都出现在窗口内（matchCount == t.length()）；
    *     2. 此时记录窗口长度，并与之前的最短记录比较；
    *     3. 之后开始让窗口向左界右滑，收窄窗口，直到窗口内不包含 t 中的所有字符；
    *     4. 重复该过程，直至窗口右界抵达 s 最后一个字母结束。
    * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
    * */
    public static String minWindow(String s, String t) {
        int[] freq = new int[256];                 // 也可以用 Map
        for (char c : t.toCharArray()) freq[c]++;  // t 中的所有字符在 freq 中初始化为1

        int l = 0, r = 0;
        int matchCount = 0, minLen = s.length() + 1;  // matchCount 记录匹配上的字符个数；minLen 记录匹配上的字符串的最小长度
        int start = -1;                               // 记录找到的子串的起始索引，用于最后截取

        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) matchCount++;  // 若 r 处的字符存在于 t 中且之前没有遇到过，则 matchCount++（之前遇到过的字符的 freq <= 0）
            freq[s.charAt(r)]--;                      // 即使该字符不存在于 t 中或存在于 t 中但之前已经遇到过，也要让 freq[..]--
            r++;

            while (matchCount == t.length()) {  // 若窗口中已经包含了 t 中的所有字符，则可以开始收缩窗口
                if (r - l < minLen) {           // 只要窗口包含所有 t 中的字符，每收缩一下都查看一次当前的窗口是否比之前的更短
                    minLen = r - l;
                    start = l;
                }
                if (freq[s.charAt(l)] == 0) matchCount--;  // 若 l 处的字符存在于 t 中，则 matchCount--（只有存在于 t 中的字符在经过上面 freq[..]-- 后能 == 0，其他的都 < 0）
                freq[s.charAt(l)]++;
                l++;
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        log(minWindow("ABAACBAB", "ABC"));       // expects "ACB"
        log(minWindow("ADOBECODEBANC", "ABC"));  // expects "BANC"（可以有多余的字符）
        log(minWindow("a", "aa"));               // expects ""
        log(minWindow("aa", "aa"));              // expects "aa"
        log(minWindow("bba", "ab"));             // expects "ba"
    }
}
