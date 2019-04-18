package Array.TwoPointerSlidingWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Utils.Helpers.log;

/*
* Longest Substring Without Repeating Characters
*
* - 从给定的字符串中找到不含重复字符的最长子串，返回该子串长度。
* - 注意是"子串"（substring），不是"子序列"（subsequence）- 子串是连续的，子序列可以不连续，如 "pwke"是"pwwkew"的子序列，但不是其子串。
* - 思路：对于这种找连续子串的问题，滑动窗口是很常用的解法，即根据题中条件来不断改变窗口的两个边界 l 和 r 找到所需子串。
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

        while (r < s.length() - 1) {               // 该题中只要 r 到头滑动过程就可以结束了（因为）
            if (r < s.length() - 1 && freq[(int) s.charAt(r + 1)] == 0)  // 若窗口中不包含 r+1 处的字符
                freq[(int) s.charAt(++r)]++;
            else                                   // 若窗口中包含 r+1 处的字符，或 r 已经抵达数组末尾
                freq[(int) s.charAt(l++)]--;
            maxLen = Math.max(r - l + 1, maxLen);  // 上面4行保证了当前窗口中没有重复字符，此时比较长度即可
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
        return 0;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring("abcabcbb"));  // expects 3 ("abc" or "bca" or "cab")
        log(lengthOfLongestSubstring("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring(""));          // expects 0
    }
}
