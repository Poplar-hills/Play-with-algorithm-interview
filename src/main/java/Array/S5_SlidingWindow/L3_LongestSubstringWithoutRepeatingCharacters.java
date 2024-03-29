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
 * - 💎 技巧：对于这种找连续子串的问题，滑动窗口是最常用的解法，即根据题中条件来不断改变窗口的左右界，从而找到所需子串。
 *
 * - Follow-up Question: Instead of finding the length, now we need to find the indexes of all the longest
 *   substring without repeating characters.
 * */

public class L3_LongestSubstringWithoutRepeatingCharacters {
    /*
     * 超时解：双指针遍历（Brute force）
     * - 💎 思路：首先，这类求 XXXsubstring、XXXsubarray 的题目通常有两种解法：
     *     1. 滑动窗口：如 L76_MinimumWindowSubstring、L438_FindAllAnagramsInString、L209_MinimumSizeSubarraySum
     *     2. 双指针遍历：如 L560_SubarraySumEqualsK、L1763_LongestNiceSubstring
     *     - 从复杂度看，双指针滑动至少是 O(n^2)，而左右伸缩滑动可以是 O(n)。
     *   其中，双指针遍历法最为 intuitive —— 通过改变 [l,r] 来遍历所有 substring，然后检查每个 substring 是否包含重复字符，
     *   若包含则直接放弃，否则记录其长度即可。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring0(String s) {
        if (s == null || s.isEmpty()) return 0;
        char[] chars = s.toCharArray();
        int maxLen = 0;

        for (int l = 0; l < s.length(); l++) {
            for (int r = l; r < s.length(); r++) {
                Set<Character> set = new HashSet<>();
                for (int i = l; i <= r; i++) {
                    if (set.contains(chars[i])) break;  // 检查该 substring 是否包含重复字符，若包含则直接放弃
                    set.add(chars[i]);
                }
                if (set.size() == r - l + 1 && r - l + 1 > maxLen)  // set.size() == r-l+1 表示不包含重复字符
                    maxLen = r - l + 1;
            }
        }

        return maxLen;
    }

    /*
     * 解法1：滑动窗口 + freq Map
     * - 思路：严格以 [l,r] 为窗口定义，用 Map 记录窗口内每个字符的频次。每次将 r 处字符纳入窗口和 Map 中之后：
     *   - 若其在 Map 中的频次为1，说明 r 处字符不重复，则可记录窗口最大长度；
     *   - 若其在 Map 中的频次 > 1，说明 r 处字符重复，此时需让 l 不断右移，收缩窗口，直到将第一个重复的字符从窗口中移出。
     *    "p   w   w   k   e   w"
     *     lr                      - 初始状态：map(p:1), max=1, r++
     *     l---r                   - map(p:1, w:1), max=2, r++
     *     l-------r               - map(p:1, w:2), foundDuplicate, max=2, l++
     *         l---r               - map(w:2), foundDuplicate, max=2, l++
     *             lr              - map(w:1), max=2, r++
     *             l---r           - map(w:1: k:1), max=2, r++
     *             l-------r       - map(w:1, k:1, e:1), max=3, r++
     *             l-----------r   - map(w:2, k:1, e:1), foundDuplicate, max=3, l++, r==arr.length, loop ends
     * - 实现：∵ 严格以 [l,r] 为窗口定义 ∴
     *   1. 若把 l,r 初始化为0，则窗口内元素个数为1 ∴ 也要把 maxLen 和 Map 初始化为包含第0个元素；
     *   2. 每次右移 r 后都要将 r 处字符纳入窗口中，无论该字符在窗口中是否重复。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 1;
        boolean foundDuplicate = false;
        Map<Character, Integer> map = new HashMap<>();
        map.put(chars[0], 1);

        while (r < chars.length - 1) {   // ∵ 下面要 ++r ∴ 这里要留出余地，不让 r 越界
            while (foundDuplicate) {     // 直到窗口内不再有重复元素时，才会跳出循环并再次右移 r
                map.merge(chars[l++], -1, Integer::sum);
                if (map.get(chars[r]) == 1)  // 在 r 向右移动的过程中，只要发现重复，r 就会停止不动 ∴ 重复的元素就是 chars[r]
                    foundDuplicate = false;
            }
            map.merge(chars[++r], 1, Integer::sum);  // ∵ 严格以 [l,r] 为窗口定义 ∴ 每次右移 r 后都要将 r 处字符纳入窗口中，无论是否重复
            if (map.get(chars[r]) > 1)   // 若发现重复元素，set flag
                foundDuplicate = true;
            else                         // 没有重复时取得最大长度
                maxLen = Math.max(maxLen, r - l + 1);
        }

        return maxLen;
    }

    /*
     * 解法2：滑动窗口 + Set
     * - 思路：不同于解法1，该解法：
     *   1. 以 Set 作为窗口；
     *   2. 不严格以 [l,r] 为窗口定义，初始窗口中无字符；
     *   3. r 指向待纳入窗口的下一个字符 ∴ 每次先检查 r 处的字符是否存在于窗口中：
     *      - 若不存在，则扩展窗口（将 r 处字符纳入窗口），并记录 maxLen（set.size()）；
     *      - 若存在，则收缩窗口（将 l 处字符从窗口中移出）。
     *     "p   w   w   k   e   w"
     *      lr                      - 初始状态：set(), arr[r] not found ∴ add to set, r++, max=1
     *      l---r                   - set(p), arr[r] not found ∴ add to set, r++, max=2
     *      l-------r               - set(p,w), found duplicate ∴ remove arr[l] from set, l++
     *          l---r               - set(w), found duplicate ∴ remove arr[l] from set, l++
     *              lr              - set(), arr[r] not found ∴ add to set, r++
     *              l---r           - set(w), arr[r] not found ∴ add to set, r++
     *              l-------r       - set(w,k), arr[r] not found ∴ add to set, r++, max=3
     *              l-----------r   - set(w,k,e), found duplicate ∴ remove arr[l] from set, l++
     *                  l-------r   - set(k,e), arr[r] not found ∴ add to set, r++
     *                            r - set(k,e,w), r == set.size() ∴ loop ends
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring2(String s) {
        if (s == null || s.isEmpty()) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 0;
        Set<Character> set = new HashSet<>();

        while (r < chars.length) {
            if (!set.contains(chars[r])) {  // 若判断窗口中无 r 处字符，再将其纳入 set，并取最大长度
                set.add(chars[r++]);
                maxLen = Math.max(maxLen, set.size());
            } else {
                set.remove(chars[l++]);
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
     * - 💎 注意：滑动窗口的题目要定义好语义：
     *   1. 是以谁为窗口：set 还是 [l,r]；
     *   2. 窗口左右边界：r 是指向当前窗口中的最后一个元素，还是指向下一个待进入窗口的元素。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring3(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = -1, maxLen = 0, n = chars.length;   // 初始窗口中没有元素 ∴ r 指向-1
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
     * - 👉🏻 实现：使用 int[256] 而非 Set 来记录窗口中的元素（ASCII 全集有256个字符，其中前128个是最常用的，后128个属于扩展字符集
     *   ∴ 若题中说明字符集只是 0-9 或 a-z，则可只开辟 int[128] 大小）。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static int lengthOfLongestSubstring4(String s) {
        if (s == null) return 0;
        int l = 0, r = -1, maxLen = 0, n = s.length();
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
     * 解法5：滑动窗口 + 双 while（🥇最优解之一）
     * - 思路：与解法2、3、4一致。
     * - 实现：以 Set 为窗口（初始长度为0），r 指向下一个要进入窗口的元素。遍历过程中使用两个内层 while 分别右移 r 直到重复元素
     *   进入窗口，以及右移 l 直到窗口内没有重复元素。窗口长度的计算发生在每次窗口扩展完毕的时候。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring5(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 0, n = chars.length;
        Set<Character> set = new HashSet<>();            // 窗口初始长度为0，r 指向下一个要进入窗口的元素

        while (r < n) {
            while (r < n && !set.contains(chars[r]))     // 扩展窗口直到发现重复元素（r 停在重复元素上，但不让其进入窗口）
                set.add(chars[r++]);
            maxLen = Math.max(maxLen, set.size());       // 每当窗口长度增长后，取窗口最大长度
            if (r < n)                                   // 最后 r 已越界时无需也不能再收缩，否则下一行的 chars[r] 会报错
                while (l < r && set.contains(chars[r]))  // 收缩窗口内没有重复元素（l 最后与 r 重合）
                    set.remove(chars[l++]);
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
     * 解法7：滑动窗口 + Map 记录字符索引（🥇最优解之一）
     * - 思路：以 [l,r] 为窗口，并用 Map 记录每个字符最新出现的位置。当重复元素进入窗口时（在 Map 中发现有之前记录的索引），
     *   此时不再让 l 一步一步右移来越过重复元素，而是直接从 Map 中取得该重复元素之前的索引，并直接跳到该索引+1处，从而快速
     *   去除了重复元素。该思路与前面解法的最大不同点是，l 是跳跃的，只有 r 在滑动。
     *     p   w   w   k   e   w    -
     *     lr                       - 初始状态：map(), max=0, no prev index for arr[r] ∴ map(p:0), max=1, r++
     *     l---r                    - no prev index for arr[r] ∴ map(p:0, w:1), max=2, r++
     *     l-------r                - found prev index w:1 ∴ l jumps to 1+1, update map to (p:0, w:2), r++
     *             l---r            - no prev index for arr[r] ∴ map(p:0, w:2, k:3), r++
     *             l-------r        - no prev index for arr[r] ∴ map(p:0, w:2, k:3, e:4), max=3, r++
     *             l-----------r    - found prev index w:2 ∴ l jumps to 2+1, update map to (p:0, w:3, k:3, e:4), r++
     *                 l---------r  - r==arr.length, loop ends
     * - 👉 实现：
     *   1. 利用了 map.put(k,v) 的返回值特性（若 k 已存在于 map 中则返回之前的 v，否则返回 null）来简化对 l 的更新。
     *   2. ∵ l 是跳跃向前移动的 ∴ indexMap 中的元素有增无减（与解法1-6中的 Set 不同的地方）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring7(String s) {
        if (s == null) return 0;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 0;
        Map<Character, Integer> indexMap = new HashMap<>();  // Map<字符, 该字符最新的索引>

        while (r < chars.length) {
            Integer prevIndex = indexMap.put(chars[r], r);   // 记录 r 处的字符的 index
            if (prevIndex != null && prevIndex >= l)         // 若之前已经记录过 index 且 >= l（确保 l 不会后退）
                l = prevIndex + 1;
            maxLen = Math.max(maxLen, r - l + 1);
            r++;
        }

        return maxLen;
    }

    /*
     * 解法8：滑动窗口 + Map 记录字符索引（解法7的另一种写法）
     * - 思路：与解法7一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int lengthOfLongestSubstring8(String s) {
        if (s == null || s.isEmpty()) return 0;
        int l = 0, r = 0, maxLen = 1;     // maxLen 从1开始
        char[] chars = s.toCharArray();
        Map<Character, Integer> indexMap = new HashMap<>();
        indexMap.put(chars[0], 0);        // 预先放入第0个元素

        while (r < chars.length - 1) {    // 为了确保下一行 ++r 不会越界 ∴ 这里的 r ∈ [0,n-1]
            Integer prevIndex = indexMap.put(chars[++r], r);  // ∵ 上面预先放入了第0个元素 ∴ 在开始循环之前要先 ++r
            if (prevIndex != null)
                l = Math.max(l, prevIndex + 1);    // 这里取 max ∴ 上一行无需再判断 prevIndex >= l
            maxLen = Math.max(maxLen, r - l + 1);  // 注意，这句不能放入 else 中，也就是说即使让 l 向后跳跃，也要重新计算 maxLen
        }                                          // 否则，当遍历到"abbcaccb"中的第2个"a"时，prevIndex=0，但 ∵ 此时l 已经 > 0，
                                                   // l 的取值不会被覆盖，即只有 r 右移（上面++r），l 没动 ∴ 仍需重新计算 maxLen。
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

    /*
     * Follow-up Question: Instead of finding the length, now we need to find the starting indexes of all the
     * longest substring without repeating characters.
     * - 👉🏻 思路：∵ 题目主体不变，仍然是找无重复的最大子串 ∴ 只需在上述解法的基础上对 maxLen 的取值语句进行改造即可。
     * - 实现：基于解法2。
     * */
    public static List<Integer> indexesOfLongestSubstring(String s) {
        List<Integer> indexes = new ArrayList<>();
        if (s == null) return indexes;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 0;
        Set<Character> set = new HashSet<>();  // 以 Set 为窗口

        while (r < chars.length) {
            if (!set.contains(chars[r])) {    // 若判断窗口中无 r 处字符，再将其纳入窗口，并取当前长度
                set.add(chars[r++]);
                int currLen = set.size();
                if (currLen > maxLen) {       // 若找到更大的窗口 length，则：
                    maxLen = currLen;         // 1. 更新 maxLen
                    indexes.clear();          // 2. 清空之前记录的 indexes（∵ 之前记录的都是 length 更小的 indexes）
                    indexes.add(l);           // 3. 重新记录 index
                } else if (currLen == maxLen) {  // 若找到一样大的 length 则直接记录 index
                    indexes.add(l);
                }
            } else {
                set.remove(chars[l++]);
            }
        }

        return indexes;
    }

    /**
     * - 实现：基于解法7。
     */
    public static List<Integer> indexesOfLongestSubstring2(String s) {
        List<Integer> indexes = new ArrayList<>();
        if (s == null) return indexes;
        char[] chars = s.toCharArray();
        int l = 0, r = 0, maxLen = 0;
        Map<Character, Integer> indexMap = new HashMap<>();

        while (r < chars.length) {
            Integer prevIndex = indexMap.put(chars[r], r);
            if (prevIndex != null && prevIndex >= l)
                l = prevIndex + 1;
            int currLen = r - l + 1;
            if (currLen > maxLen) {
                maxLen = currLen;
                indexes.clear();
                indexes.add(l);
            } else if (currLen == maxLen) {
                indexes.add(l);
            }
            r++;
        }

        return indexes;
    }

    public static void main(String[] args) {
        log(lengthOfLongestSubstring8("abbcaccb"));  // expects 3 ("bca")
        log(lengthOfLongestSubstring8("pwwkew"));    // expects 3 ("wke")
        log(lengthOfLongestSubstring8("cdd"));       // expects 2 ("cd")
        log(lengthOfLongestSubstring8("abba"));      // expects 2 ("ab" or "ba")
        log(lengthOfLongestSubstring8("bbbbba"));    // expects 2 ("ba")
        log(lengthOfLongestSubstring8("bbbbb"));     // expects 1 ("b")
        log(lengthOfLongestSubstring8(""));          // expects 0

        log(indexesOfLongestSubstring2("abba"));      // expects [0, 2]
        log(indexesOfLongestSubstring2("abcbaacb"));  // expects [0, 2, 5]
    }
}
