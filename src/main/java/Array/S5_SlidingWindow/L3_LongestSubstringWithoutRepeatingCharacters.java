package Array.S5_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Longest Substring Without Repeating Characters
 *
 * - Given a string, find the length of the longest substring without repeating characters.
 *
 * - 注意：本题中求的是 substring（子串）而非 subsequence（子序列）—— 子串是连续的，子序列可以不连续，例如"pwke"是"pwwkew"
 *   的子序列，但不是其子串。
 *
 * - 💎心得：对于这种找连续子串的问题，滑动窗口是最常用的解法，即根据题中条件来不断改变窗口的左右界，从而找到所需子串。
 * */

public class L3_LongestSubstringWithoutRepeatingCharacters {
    /*
     * 解法1：滑动窗口 + Set
     * - 思路：窗口左右界初始都在0位置上（即初始窗口中包含一个元素），每次检查 r 处的字符是否存在于窗口中，若不存在则 r++，否则
     *   l++，且每次窗口长度变化时都与之前的最长记录比较。
     * - 实现：1. 借助 Set 检查 r 处的字符是否位于窗口中；
     *        2. 计算当前窗口长度时要用 r-l（而非 r-l+1）∵ r++ 后会指向下一个待进入窗口的字符，而非当前窗口中的最后一个字符。
     * - 👉注意：滑动窗口的题目要定义好窗口左右边界的语义，如右边界是指向当前窗口中的最后一个字符还是指向下一个待进入窗口的字符。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = 0, n = chars.length;
        Set<Character> set = new HashSet<>();

        while (r < n) {
            if (r < n && !set.contains(chars[r]))
                set.add(chars[r++]);
            else
                set.remove(chars[l++]);  // l 总是 ≤ r ∴ 不用担心 l 会越界
            maxLen = Math.max(maxLen, r - l);
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口 + Set
     * - 思路：与解法1一致。
     * - 实现：不同于解法1，本解法中对窗口右边界 r 的语义定义为指向当前窗口中的最后一个字符，而非指向下一个待进入窗口的字符。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = -1, n = chars.length;   // 初始窗口中没有元素 ∴ r 指向-1
        Set<Character> set = new HashSet<>();

        while (l < n && r < n) {                           // ∵ l 可能越界 ∴ 比解法1多了 l < n 的条件（可去掉调试一下）
            if (r < n - 1 && !set.contains(chars[r + 1]))  // ∵ 要取 r+1 处的值 ∴ 要 r < n-1 来保证不越界
                set.add(chars[++r]);                       // r 要先++ 才指向下一个待进入窗口的字符
            else
                set.remove(chars[l++]);
            maxLen = Math.max(maxLen, r - l + 1);          // ∵ r 指向当前窗口中的最后一个字符 ∴ 窗口长度可正常来取
        }

        return maxLen;
    }

    /*
     * 解法3：解法1的 int[256] 版
     * - 思路：与解法1、2一致。
     * - 实现：1. 使用 int[256] 而非 Set 来记录窗口中的元素（ASCII 有256个字符，若题中说明字符集只是 0-9 或 a-z，则可开辟相应大小的空间）；
     *        2. r 初始化为-1，从而使窗口最初不包含任何元素（与解法2一致）。
     *        注：ASCII 全集有256个字符，其中前128个是最常用的，后128个属于扩展字符集。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring3(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1, n = s.length();
        int[] freq = new int[256];

        while (l < n && r < n) {           // 与解法2一样，防止 l 越界
            if (r < n - 1 && freq[s.charAt(r + 1)] == 0)
                freq[s.charAt(++r)]++;     // 这里隐含一个 freq[char] -> freq[int] 的 ASCII 转换
            else
                freq[s.charAt(l++)]--;
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法4：滑动窗口 + 双 while
     * - 思路：不同于解法1、2、3，该解法内层使用两个 while 分别右移 r 直到重复元素进入窗口，以及右移 l 直到窗口内没有重复元素，
     *   而窗口长度的计算发生在这两个移动过程中间。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring4(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = 0, n = chars.length;
        Set<Character> set = new HashSet<>();

        while (r < n) {
            while (r < n && !set.contains(chars[r]))     // 右移 r 直到重复元素进入窗口（并停在重复元素上）
                set.add(chars[r++]);

            maxLen = Math.max(maxLen, r - l);            // 计算窗口长度

            if (r < n)                                   // 若 r 已到达末尾则不用再移动 l
                while (l < r && set.contains(chars[r]))  // 右移 l 直到窗口内没有重复元素（l 最后与 r 重合）
                    set.remove(chars[l++]);
        }

        return maxLen;
    }

    /*
     * 解法5：解法4的 int[256] 版
     * - 思路：与解法4一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring5(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1, n = s.length();
        int[] freq = new int[256];

        while (r < n - 1) {
            while (r < n - 1 && freq[s.charAt(r + 1)] == 0)
                freq[s.charAt(++r)]++;

            maxLen = Math.max(maxLen, r - l + 1);

            if (r < n - 1) {
                freq[s.charAt(++r)]++;   // 此处 r++ 后窗口右边界才有重复元素进入
                while (l <= r && freq[s.charAt(r)] == 2)
                    freq[s.charAt(l++)]--;
            }
        }

        return maxLen;
    }

    /*
     * 解法6：滑动窗口 + Map 记录字符索引（最优解）
     * - 思路：该解法使用 Map 记录每个字符的索引，重复元素进入窗口时，l 不再是一步一步右移来越过重复元素，而是从 indexMap 中取
     *   得重复元素的索引，并直接跳到该索引+1处，从而快速去除了重复元素。该思路与前面解法的最大不同点是，l 是跳跃的，只有 r 在滑动。
     * - 实现：使用了 map.put(k, v) 的返回值特性（若 k 已存在于 map 中则返回之前的 v，否则返回 null）简化对 l 的更新。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring6(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        Map<Character, Integer> indexMap = new HashMap<>();
        int maxLen = 0, l = 0;

        for (int r = 0; r < chars.length; r++) {
            Integer prevIndex = indexMap.put(chars[r], r);
            if (prevIndex != null)
                l = Math.max(l, prevIndex + 1);  // 取 Math.max 是为了确保 test case 4（"abba"）
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法7：解法6的 int[256] 版
     * - 思路：与解法6一致。
     * - 实现：用 int[256] 代替 Map 来记录每个字符的出现位置。缺点是需要遍历 int[256] 来将每个字符的索引初始化为-1。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring7(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0;
        int[] indexes = new int[256];
        Arrays.fill(indexes, -1);             // 将每个字符的索引初始化为-1（∵ 不能用默认值0）

        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (indexes[c] != -1)             // 若 r 处的字符存在于窗口中
                l = Math.max(l, indexes[c] + 1);
            indexes[c] = r;                   // 在数组中记录 r 处字符的索引（∵ 数组没有 Map.put 返回旧值的功能 ∴ 只能在用完旧值之后再覆盖）
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring5("abcabcbb"));  // expects 3 ("abc" or "bca" or "cab")
        log(lengthOfLongestSubstring5("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring5("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring5("abba"));      // expects 2 ("ab" or "ba")
        log(lengthOfLongestSubstring5("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring5("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring5(""));          // expects 0
    }
}
