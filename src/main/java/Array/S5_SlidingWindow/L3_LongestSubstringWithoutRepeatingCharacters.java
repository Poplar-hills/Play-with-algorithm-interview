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
 * - 💎 心得：对于这种找连续子串的问题，滑动窗口是最常用的解法，即根据题中条件来不断改变窗口的左右界，从而找到所需子串。
 * */

public class L3_LongestSubstringWithoutRepeatingCharacters {
    /*
     * 解法1：滑动窗口 + Set
     * - 思路：窗口左右界初始都在0位置上，每次检查 r 处的字符是否存在于窗口中，若不存在则纳入窗口并 r++，否则将 l 处的字符从窗口
     *   中移除并 l++。在窗口每次长度增加时比较并记录窗口最大长度。
     * - 实现：使用 Set 作为窗口，set.size() 即为窗口长度。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0;
        Set<Character> set = new HashSet<>();

        for (int l = 0, r = 0; r < chars.length; ) {
            if (!set.contains(chars[r])) {
                set.add(chars[r++]);
                maxLen = Math.max(maxLen, set.size());
            } else {
                set.remove(chars[l++]);
            }
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口 + Set
     * - 思路：与解法1一致。
     * - 实现：与解法1不同，不以 Set 为窗口，而是以 [l,r] 为窗口，r-l+1 为窗口长度 ∴ r 要初始化为 -1，让初始窗口中没有元素。
     * - 👉 注意：滑动窗口的题目要定义好语义，如：
     *   1. 是以谁为窗口：set 还是 [l,r]；
     *   2. 窗口左右边界：r 是指向当前窗口中的最后一个元素，还是指向下一个待进入窗口的元素。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = -1, n = chars.length;   // 初始窗口中没有元素 ∴ r 指向-1
        Set<Character> set = new HashSet<>();

        while (l < n && r < n) {                           // 当 remove 元素后 l 可能 > r ∴ 比解法1多了 l < n 的条件
            if (r < n - 1 && !set.contains(chars[r + 1]))  // ∵ 要取 r+1 处的值 ∴ 要 r < n-1 来保证不越界
                set.add(chars[++r]);                       // r 要先++ 才指向下一个待进入窗口的字符
            else
                set.remove(chars[l++]);
            maxLen = Math.max(maxLen, r - l + 1);          // 窗口长度发生变化时比较并记录最大长度
        }

        return maxLen;
    }

    /*
     * 解法3：解法2的 int[256] 版
     * - 思路：与解法1、2一致。
     * - 实现：使用 int[256] 而非 Set 来记录窗口中的元素（ASCII 全集有256个字符，其中前128个是最常用的，后128个属于扩展字符集
     *   ∴ 若题中说明字符集只是 0-9 或 a-z，则可只开辟 int[128] 大小）。
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
     * - 思路：与解法1、2、3一致。
     * - 实现：该解法内层使用两个 while 分别右移 r 直到重复元素进入窗口，以及右移 l 直到窗口内没有重复元素，
     *   而窗口长度的计算发生在这两个移动过程中间。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring4(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = 0, n = chars.length;  // 窗口初始长度为1，r 指向下一个要进入窗口的元素
        Set<Character> set = new HashSet<>();

        while (r < n) {
            while (r < n && !set.contains(chars[r]))     // 右移 r 直到重复元素进入窗口（并停在重复元素上）
                set.add(chars[r++]);

            maxLen = Math.max(maxLen, r - l);            // 每当窗口长度增长后，计算窗口最大长度（注意是 r-l 而非 r-l+1）

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
     * - 实现：利用了 map.put(k, v) 的返回值特性（若 k 已存在于 map 中则返回之前的 v，否则返回 null）来简化对 l 的更新。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring6(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0;
        Map<Character, Integer> indexMap = new HashMap<>();

        for (int r = 0; r < chars.length; r++) {
            Integer prevIndex = indexMap.put(chars[r], r);
            if (prevIndex != null)               // 判断字符是否已存在于窗口中
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
        Arrays.fill(indexes, -1);          // 将每个字符的索引初始化为-1（∵ 不能用默认值0）

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
        log(lengthOfLongestSubstring7("abbcaccb"));  // expects 3 ("bca")
        log(lengthOfLongestSubstring7("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring7("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring7("abba"));      // expects 2 ("ab" or "ba")
        log(lengthOfLongestSubstring7("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring7("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring7(""));          // expects 0
    }
}
