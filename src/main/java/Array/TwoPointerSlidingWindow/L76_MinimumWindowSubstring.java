package Array.TwoPointerSlidingWindow;

/*
* Minimum Window Substring
*
* - find the minimum substring in S which contain all the characters in T.
* - 思路：滑动窗口
* - 时间复杂度 O(n)，空间复杂度 O(len(charset))
* */

import static Utils.Helpers.log;

public class L76_MinimumWindowSubstring {
    public static String minWindow(String s, String t) {
        int[] freq = new int[128];  // 也可以用 hash map
        for (char c : t.toCharArray()) freq[c]++;

        int l = 0, r = 0, count = 0, minLen = s.length() + 1, head = -1;  // head 记录找到的子串的起始索引，用于后面 substring
        while (r < s.length()) {
            if (freq[s.charAt(r++)]-- > 0)       // r 处的字符若匹配上了，则 count++（即使没匹配上，r 也会向右滑动，继续匹配，直到 count == t.length）
                count++;
            while (count == t.length()) {        // 若窗口中已经包含了所有 t 中的字符，此时可以开始收缩窗口
                if (r - l < minLen)              // 只要窗口包含所有 t 中的字符，每收缩一下都查看一次当前的窗口是否比之前的更短
                    minLen = r - (head = l);
                if (freq[s.charAt(l++)]++ == 0)  // 收缩窗口（l 右滑）
                    count--;
            }
        }
        return head == -1 ? "" : s.substring(head, head + minLen);
    }

    public static void main(String[] args) {
        log(minWindow("ABAACBAB", "ABC"));       // expects "ACB"
//        log(minWindow("ADOBECODEBANC", "ABC"));  // expects "BANC"（可以有多余的字符）
//        log(minWindow("a", "aa"));               // expects ""
//        log(minWindow("aa", "aa"));              // expects "aa"
//        log(minWindow("bba", "ab"));             // expects "ba"
    }
}
