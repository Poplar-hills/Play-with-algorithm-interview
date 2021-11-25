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
     * 解法1：滑动窗口 + freq Map
     * - 思路：以 [l,r] 为窗口，用 Map 记录每个字符的频次。每次将 r 处字符添加到窗口中之后：
     *   - 若发现其频次 == 1，说明无重复元素，则获取最大长度；
     *   - 若发现其频次 > 1，说明 r 处字符重复，此时让 l 不断右移，收缩窗口，直到将第一个重复的字符从窗口中移出。
     *    "p   w   w   k   e   w"
     *     lr                      - 初始状态：map(p:1), max=1, r++
     *     l---r                   - map(p:1, w:1), max=2, r++
     *     l-------r               - map(p:1, w:2), foundDuplicate, max=2, l++
     *         l---r               - map(w:2), foundDuplicate, max=2, l++
     *             lr              - map(w:1), max=2, r++
     *             l---r           - map(w:1: k:1), max=2, r++
     *             l-------r       - map(w:1, k:1, e:1), max=3, r++
     *             l-----------r   - map(w:2, k:1, e:1), foundDuplicate, max=3, l++, r==arr.length, loop ends
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 1;
        boolean foundDuplicate = false;
        Map<Character, Integer> freq = new HashMap<>();
        freq.put(chars[0], 1);

        while (r < chars.length - 1) {
            if (foundDuplicate) {
                freq.merge(chars[l++], -1, Integer::sum);
                if (freq.get(chars[r]) == 1)  // 在 r 向右移动的过程中，只要发现重复，r 就会停止不动 ∴ 重复的元素就是 chars[r]
                    foundDuplicate = false;
                continue;
            }
            freq.merge(chars[++r], 1, Integer::sum);
            if (freq.get(chars[r]) > 1)  // 若发现重复元素，set flag
                foundDuplicate = true;
            else                         // 没有重复时取得最大长度
                maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口 + Set
     * - 思路：不同于解法1：
     *   1. 该解法以 Set 作为窗口，初始窗口中无字符，set.size() 即为窗口长度；
     *   2. r 指向待纳入窗口的下一个字符 ∴ 每次先检查 r 处的字符是否存在于窗口中：
     *      - 若不存在，则扩展窗口（将 r 出字符纳入窗口），并记录窗口最大长度；
     *      - 若存在，则收缩窗口（将 l 处字符从窗口中移出）。
     *     "p   w   w   k   e   w"
     *      lr                      - 初始状态：set(), no arr[r], add it to set, r++
     *      l---r                   - set(p), no arr[r], add it to set, r++
     *      l-------r               - set(p,w), has arr[r], remove arr[l], l++
     *          l---r               - set(w), has arr[r], remove arr[l], l++
     *              lr              - set(), no arr[r], add it to set, r++
     *              l---r           - set(w), no arr[r], add it to set, r++
     *              l-------r       - set(w,k), no arr[r], add it to set, r++
     *              l-----------r   - set(w,k,e), r == arr.length, loop ends, return set.size()
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0;
        Set<Character> window = new HashSet<>();  // 以 Set 为窗口

        for (int l = 0, r = 0; r < chars.length; ) {
            if (!window.contains(chars[r])) {  // 若判断窗口中无 r 处字符，再将其纳入窗口，并取最大长度
                window.add(chars[r++]);
                maxLen = Math.max(maxLen, window.size());
            } else {
                window.remove(chars[l++]);
            }
        }

        return maxLen;
    }

    /*
     * 解法3：滑动窗口 + Set
     * - 思路：与解法2一致。
     * - 实现：
     *   - 与解法2不同，不以 Set 为窗口，而是以 [l,r] 为窗口，r-l+1 为窗口长度；
     *   - 与解法1不同，不预先往窗口中添加元素，而是将 r 初始化为 -1，让初始窗口中没有元素；
     * - 👉 注意：滑动窗口的题目要定义好语义，如：
     *   1. 是以谁为窗口：set 还是 [l,r]；
     *   2. 窗口左右边界：r 是指向当前窗口中的最后一个元素，还是指向下一个待进入窗口的元素。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring3(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = -1, n = chars.length;   // 初始窗口中没有元素 ∴ r 指向-1
        Set<Character> set = new HashSet<>();

        while (l < n && r < n) {                           // 当 remove 元素后 l 可能 > r ∴ 比解法2多了 l < n 的条件
            if (r + 1 < n && !set.contains(chars[r + 1]))  // ∵ 要取 r+1 处的值 ∴ 要 r+1 < n 来保证不越界
                set.add(chars[++r]);                       // r 要先++ 才指向下一个待进入窗口的字符
            else
                set.remove(chars[l++]);
            maxLen = Math.max(maxLen, r - l + 1);          // 窗口长度发生变化时取最大长度
        }

        return maxLen;
    }

    /*
     * 解法4：解法3的 int[256] 版
     * - 思路：与解法2、3一致。
     * - 实现：使用 int[256] 而非 Set 来记录窗口中的元素（ASCII 全集有256个字符，其中前128个是最常用的，后128个属于扩展字符集
     *   ∴ 若题中说明字符集只是 0-9 或 a-z，则可只开辟 int[128] 大小）。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring4(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0, r = -1, n = s.length();
        int[] freq = new int[256];

        while (l < n && r < n) {        // 与解法3一样，防止 l 越界
            if (r < n - 1 && freq[s.charAt(r + 1)] == 0)
                freq[s.charAt(++r)]++;  // 这里隐含一个 freq[char] -> freq[int] 的 ASCII 转换
            else
                freq[s.charAt(l++)]--;
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法5：滑动窗口 + 双 while
     * - 思路：与解法2、3、4一致。
     * - 实现：以 Set 为窗口，内层使用两个 while 分别右移 r 直到重复元素进入窗口，以及右移 l 直到窗口内没有重复元素，
     *   而窗口长度的计算发生在这两个移动过程中间。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring5(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0, l = 0, r = 0, n = chars.length;  // 窗口初始长度为1，r 指向下一个要进入窗口的元素
        Set<Character> window = new HashSet<>();

        while (r < n) {
            while (r < n && !window.contains(chars[r]))  // 扩展窗口直到重复元素进入窗口（并停在重复元素上）
                window.add(chars[r++]);
            maxLen = Math.max(maxLen, window.size());    // 每当窗口长度增长后，取窗口最大长度
            if (r < n)                                   // 若 r 已到达末尾则不用再移动 l
                while (l < r && window.contains(chars[r]))  // 收缩窗口内没有重复元素（l 最后与 r 重合）
                    window.remove(chars[l++]);
        }

        return maxLen;
    }

    /*
     * 解法6：解法5的 int[256] 版
     * - 思路：与解法5一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring6(String s) {
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
     * 解法7：滑动窗口 + Map 记录字符索引
     * - 思路：以 [l,r] 为窗口，并用 Map 记录每个字符最新出现的位置。当重复元素进入窗口时（在 Map 中发现有之前记录的索引），
     *   此时不再让 l 一步一步右移来越过重复元素，而是直接从 Map 中取得该重复元素之前的索引，并直接跳到该索引+1处，从而快速
     *   去除了重复元素。该思路与前面解法的最大不同点是，l 是跳跃的，只有 r 在滑动。
     *     p   w   w   k   e   w
     *     lr                       - 初始状态：map(), max=0, r++
     *     lr                       - map(p:0), no arr[r], max=1, r++
     *     l---r                    - map(p:0, w:1), no arr[r], max=2, r++
     *     l-------r                - map(p:0, w:1), found arr[r], l jumps to map.get(w)+1, update map.put(w,r)
     *             lr               - map(p:0, w:2), found arr[r], but map.get(w)==l ∴ max=2, r++
     *             l---r            - map(p:0, w:2, k:3), no arr[r], max=2, r++
     *             l-------r        - map(p:0, w:2, k:3, e:4), no arr[r], max=3, r++
     *             l-----------r    - map(p:0, w:2, k:3, e:4), found arr[r], l jumps to map.get(w)+1, update map.put(w,r)
     *                 l-------r    - map(p:0, w:5, k:3, e:4), r==arr.length-1, loop ends
     *   注意：该解法中，Map 只会不断 put，而不会 remove（与解法1-6中的 Set 不同的地方）。
     * - 👉 实现：
     *   1. 利用了 map.put(k,v) 的返回值特性（若 k 已存在于 map 中则返回之前的 v，否则返回 null）来简化对 l 的更新。
     *   2. ∵ l 是跳跃向前移动的 ∴ indexMap 中的元素只能增不能删。若碰到 test case 4 的"abba"，在 r 移动到第2个 a 上时，l 指向
     *      第2个 b，此时在 indexMap 中能找到 a 的 prevIndex，但由于不能让 l 后退 ∴ 需要取 Math.max。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring7(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0;
        Map<Character, Integer> indexMap = new HashMap<>();  // 记录 <字符, 该字符最新的索引>

        for (int l = 0, r = 0; r < chars.length; r++) {
            Integer prevIndex = indexMap.put(chars[r], r);   // 让 r 处的字符进入窗口
            if (prevIndex != null)                 // 判断字符是否已存在于窗口中
                l = Math.max(l, prevIndex + 1);    // 取 Math.max，确保 l 不会后退
            maxLen = Math.max(maxLen, r - l + 1);  // 注意即使 prevIndex != null 也要比较一遍 maxLen（例如在 test case 1
        }                                          // 中，当遍历到的第二个 a 时，prevIndex=0，但 ∵ 此时 l 已经 > 0 ∴ 可以
                                                   // 重新计算 maxLen）
        return maxLen;
    }

    /*
     * 解法8：滑动窗口 + Map 记录字符索引（解法7的另一种写法，🥇最优解）
     * - 思路：与解法7一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring8(String s) {
        if (s == null || s.isEmpty()) return 0;
        int l = 0, r = 0, maxLen = 1;     // maxLen 从1开始（比解法7中从0开始更符合窗口语义）
        char[] chars = s.toCharArray();
        Map<Character, Integer> indexMap = new HashMap<>();  // Map<字符, 该字符的最新索引>
        indexMap.put(chars[0], 0);        // 预先放入第0个字符

        while (r < chars.length - 1) {    // r 最大只能到最后一个字符，否则下面 ++r 会越界
            Integer prevIndex = indexMap.put(chars[++r], r);
            if (prevIndex != null && prevIndex >= l)  // ∵ prevIndex 可能是 < l ∴ 这里要加上 prevIndex >= l 的条件
                l = prevIndex + 1;
            else                          // 只有在 r 右移之后窗口中仍无重复元素的时候才需要取最大长度
                maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法9：解法7的 int[256] 版
     * - 思路：与解法6一致。
     * - 实现：用 int[256] 代替 Map 来记录每个字符的出现位置。缺点是需要遍历 int[256] 来将每个字符的索引初始化为-1。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring9(String s) {
        if (s == null) return 0;
        int maxLen = 0, l = 0;
        int[] indexes = new int[256];
        Arrays.fill(indexes, -1);  // 将每个字符的索引初始化为-1（∵ 不能用默认值0）

        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            if (indexes[c] != -1)  // 若 r 处的字符存在于窗口中
                l = Math.max(l, indexes[c] + 1);
            indexes[c] = r;   // 在数组中记录 r 处字符的索引（∵ 数组没有 Map.put 返回旧值的功能 ∴ 只能在用完旧值之后再覆盖）
            maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring("abbcaccb"));  // expects 3 ("bca")
        log(lengthOfLongestSubstring("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring("abba"));      // expects 2 ("ab" or "ba")
        log(lengthOfLongestSubstring("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring(""));          // expects 0
    }
}
