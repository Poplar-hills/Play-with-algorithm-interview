package Array.TwoPointerSlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
* Longest Substring Without Repeating Characters
*
* - 从给定的字符串中找到不含重复字符的最长子串，返回该子串长度。
* - 注意是"子串"（substring），不是"子序列"（subsequence）- 子串是连续的，子序列可以不连续，如 "pwke"是"pwwkew"的子序列，但不是其子串。
* - 思路：对于这种找连续子串的问题，滑动窗口是很常用的解法，即根据题中条件来不断改变窗口的两个边界 l 和 r 找到所需子串。
* - 解法1最简单明了，解法3最精巧。
* */

public class L3_LongestSubstringWithoutRepeatingCharacters {
    /*
    * 解法1：滑动窗口
    * - 时间复杂度 O(n)，空间复杂度 O(len(charset))
    * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int l = 0, r = -1;  // 右边界初始化为-1，使得初始窗口不包含任何元素，这样初始 maxLen 才能为0
        int maxLen = 0;
        int[] freq = new int[128];  // ASCII 含有128个字符，因此开辟128的空间，若题中说明了只是0-9或者只是a-z，则开对应大小的空间即可

        while (r < s.length() - 1) {               // 该题中只要 r 到头滑动过程就可以结束了（不需要 l 到头）
            if (r < s.length() - 1 && freq[s.charAt(r + 1)] == 0)  // 若窗口中不包含 r+1 处的字符
                freq[s.charAt(++r)]++;
            else                                   // 若窗口中包含 r+1 处的字符，或 r 已经抵达数组末尾
                freq[s.charAt(l++)]--;
            maxLen = Math.max(r - l + 1, maxLen);  // 上面保证了当前窗口中没有重复字符，此时比较长度即可
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口的另一实现
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int l = 0, r = -1;
        int maxLen = 0;
        int[] freq = new int[128];

        while (r < s.length() - 1) {
            while (r < s.length() - 1 && freq[s.charAt(r + 1)] == 0)
                freq[s.charAt(++r)]++;
            maxLen = Math.max(r - l + 1, maxLen);  // 这句只能放在这里，不能放在最后

            if (r < s.length() - 1) {   // 这部分逻辑和上面部分是串行关系（都会执行），不像解法1中是分支关系
                freq[s.charAt(++r)]++;  // 此处 r++ 后窗口右边界才有重复元素进入
                while (l <= r && freq[s.charAt(r)] == 2)  // 窗口左边界向右滑，直到窗口内没有重复元素
                    freq[s.charAt(l++)]--;
            }
        }

        return maxLen;
    }

    /*
     * 解法3：滑动窗口的另一实现（最优解）
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))
     * - 思路：该解法不再记录每个字符的出现频率，而是记录每个字符的索引，从而在发现右侧有重复元素进入时，让 l 直接跳过上一个重复元素。
     * - 虽然和前两种解法的复杂度是一个量级的，但该解的优势在于：
     *   1. 当窗口中出现重复元素时，l 不再是一点一点向右滑动，而是取得重复元素的索引，直接跳到该索引+1的位置，从而去除了重复元素。
     *   2. 因为 l 是跳跃的，只有 r 在滑动，因此整个 s 只遍历了一遍（前两种解法中 l 和 r 都在滑动，实际上遍历了 s 两遍）。
     * - 劣势在于：需要遍历 charIndexes 将每个元素初始化为 -1。
     * */
    public static int lengthOfLongestSubstring3(String s) {
        if (s == null)
            throw new IllegalArgumentException("Illegal Arguments");

        int l = 0, r = -1;
        int maxLen = 0;
        int[] charIndexes = new int[128];  // 保存每个字符在 s 中的索引（重复元素只保存最大索引）
        Arrays.fill(charIndexes, -1);  // 填充-1（不能再用默认值0了），这里多遍历了一遍

        while (r < s.length() - 1) {
            if (charIndexes[s.charAt(++r)] != -1)  // r 在这里右滑
                l = Math.max(l, charIndexes[s.charAt(r)] + 1);
            charIndexes[s.charAt(r)] = r;          // 若 r 上的元素在窗口中不存在则记录下来，若已存在则更新其索引
            maxLen = Math.max(r - l + 1, maxLen);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring2("abcabcbb"));  // expects 3 ("abc" or "bca" or "cab")
        log(lengthOfLongestSubstring2("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring2("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring2("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring2("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring2(""));          // expects 0
    }
}
