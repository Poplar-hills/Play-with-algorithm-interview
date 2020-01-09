package Array.S5_TwoPointerSlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Longest Substring Without Repeating Characters
 *
 * - Given a string, find the length of the longest substring without repeating characters.
 *
 * - 注意：本题中求的是 substring（子串），而非 subsequence（子序列）—— 子串是连续的，子序列可以不连续，
 *   如"pwke"是"pwwkew"的子序列，但不是其子串。
 *
 * - 解法1最简单明了，解法3最精巧。
 * */

public class L3_LongestSubstringWithoutRepeatingCharacters {
    /*
     * 解法1：滑动窗口
     * - 思路：窗口初始宽度为0（不包含任何元素），每次检查 r+1 处的字符是否存在于窗口中，若不存在则窗口右边界右滑，否则窗口左边界
     *   右滑收窄一步，且每次窗口长度变化时都与之前的最长记录想比较。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * - 心得：对于这种找连续子串的问题，滑动窗口是最常用的解法，即根据题中条件来不断改变窗口的两个边界 l 和 r 找到所需子串。
     * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1;          // 右边界初始化为-1，使得初始窗口不包含任何元素，这样初始 maxLen 才能为0
        int[] freq = new int[256];  // ASCII 有256个字符 ∴ 开辟256的空间，若题中说明了只是0-9或者只是a-z，则开对应大小的空间即可（使用 Map 也行）

        while (r < s.length() - 1) {            // 该题中只要 r 到头滑动过程就可以结束了（不需要 l 到头）
            if (r < s.length() - 1 && freq[s.charAt(r + 1)] == 0)  // 若 r+1 不越界，且窗口中还没有 r+1 处的字符
                freq[s.charAt(++r)]++;          // 这里隐含一个转换：freq[字符]
            else                                // 若窗口中已有 r+1 处的字符，或 r 已经抵达数组末尾
                freq[s.charAt(l++)]--;
            maxLen = Math.max(maxLen, r - l + 1);  // 上面保证了当前窗口中没有重复字符，此时比较长度即可
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口的另一实现
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1;
        int[] freq = new int[256];

        while (r < s.length() - 1) {
            while (r < s.length() - 1 && freq[s.charAt(r + 1)] == 0)
                freq[s.charAt(++r)]++;
            maxLen = Math.max(maxLen, r - l + 1);  // 这句只能放在这里，不能放在最后

            if (r < s.length() - 1) {              // 这部分逻辑和上面部分是串行关系（都会执行），不像解法1中是分支关系
                freq[s.charAt(++r)]++;             // 此处 r++ 后窗口右边界才有重复元素进入
                while (l <= r && freq[s.charAt(r)] == 2)  // 窗口左边界向右滑，直到窗口内没有重复元素
                    freq[s.charAt(l++)]--;
            }
        }

        return maxLen;
    }

    /*
     * 解法3：滑动窗口的另一实现（最优解）
     * - 思路：该解法不再记录每个字符的出现频率，而是记录每个字符的索引，从而在发现右侧有重复元素进入时，让 l 直接跳过上一个重复元素。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * - 虽然和前两种解法的复杂度是一个量级的，但该解的优势在于：
     *   1. 当窗口中出现重复元素时，l 不再是一点一点向右滑动，而是取得重复元素的索引，直接跳到该索引+1的位置，从而去除了重复元素。
     *   2. 因为 l 是跳跃的，只有 r 在滑动，因此整个 s 只遍历了一遍（前两种解法中 l 和 r 都在滑动，实际上遍历了 s 两遍）。
     * - 劣势：需要遍历 charIndexes 将每个元素初始化为 -1。
     * */
    public static int lengthOfLongestSubstring3(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1;
        int[] charIndexes = new int[256];  // 保存每个字符在 s 中的索引（重复元素只保存最大索引）
        Arrays.fill(charIndexes, -1);      // 填充-1（不能再用默认值0了），这里多遍历了一遍

        while (r < s.length() - 1) {
            if (charIndexes[s.charAt(++r)] != -1)  // 与解法1、2不同，r 在这里会先右滑，即使 r+1 已存在与窗口中
                l = Math.max(l, charIndexes[s.charAt(r)] + 1);
            charIndexes[s.charAt(r)] = r;          // 若 r 上的元素在窗口中不存在则记录下来，若已存在则更新其索引
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法4：解法3的简化版（使用 Map）
     * - 思路：与解法3一致。
     * - 实现：1. 使用 Map 代替数组从而简化代码；
     *        2. 利用 map.put(k, v) 的返回值特性（若 k 已存在于 map 中则返回之前的 v，否则返回 null）简化对 l 的更新。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring0(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        Map<Character, Integer> indexMap = new HashMap<>();
        int maxLen = 0, l = 0;

        for (int r = 0; r < chars.length; r++) {
            Integer prevIndex = indexMap.put(chars[r], r);
            if (prevIndex != null)
                l = Math.max(l, prevIndex + 1);
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring0("abcabcbb"));  // expects 3 ("abc" or "bca" or "cab")
        log(lengthOfLongestSubstring0("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring0("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring0("abba"));      // expects 2 ("ab" or "ba")
        log(lengthOfLongestSubstring0("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring0("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring0(""));          // expects 0
    }
}
