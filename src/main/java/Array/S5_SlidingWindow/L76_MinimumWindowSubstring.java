package Array.S5_SlidingWindow;

import static Utils.Helpers.log;

import java.util.*;

/*
 * Minimum Window Substring
 *
 * - Given two strings S and T, find the minimum substring in S which contain all the characters in T.
 * - If there is no such window in S that covers all characters in T, return the empty string "".
 * - If there is such window, you are guaranteed that there will always be only one unique minimum window in S.
 * */

public class L76_MinimumWindowSubstring {
    /*
     * 超时解：双指针遍历
     * - 💎 思路：与 L3_LongestSubstringWithoutRepeatingCharacters 超时解类似，使用双指针遍历所有 substring，然后检查每个
     *   substring 是否包含 t 中的所有字符（即 brute force）。
     * - 时间复杂度 O(n^3)，空间复杂度 O(n)。
     * */
    public static String minWindow0(String s, String t) {
        char[] sChars = s.toCharArray(), tChars = t.toCharArray();
        int start = -1, minLen = Integer.MAX_VALUE;

        for (int l = 0; l < sChars.length; l++) {
            for (int r = l; r < sChars.length; r++) {
                int len = r - l + 1;
                if (containAllChars(sChars, l, r, tChars) && len < minLen) {
                    start = l;
                    minLen = len;
                }
            }
        }

        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    private static boolean containAllChars(char[] sChars, int l, int r, char[] tChars) {
        Map<Character, Integer> tFreq = new HashMap<>();
        for (char c : tChars)
            tFreq.merge(c, 1, Integer::sum);
        for (int i = l; i <= r; i++) {
            if (tFreq.containsKey(sChars[i])) {
                tFreq.merge(sChars[i], -1, Integer::sum);
                if (tFreq.get(sChars[i]) == 0)
                    tFreq.remove(sChars[i]);
            }
        }
        return tFreq.isEmpty();
    }

    /*
     * 解法1：滑动窗口
     * - 💎 思路：∵ 是找连续子串的问题 ∴ 可尝试滑动窗口方法求解 —— 控制窗口左右边界的滑动来找到所需子串。通过观察 test case 1
     *   可知要求的最小子串需要包含 t 中所有字符，且尽量少的包含重复字符 ∴ 可得到窗口滑动控制方式：先右移 r 扩展窗口，直到 t 中
     *   所有字符进入窗口。之后右移 l 收缩窗口，直到窗口中不再包含 t 中所有字符，此时记录窗口长度，重复该过程直到 r 抵达 s 末尾。
     *   例如对于 s="ABAACBAB", t="ABC" 来说：
     *       A  B  A  A  C  B  A  B
     *       lr                       - 未包含 t 中全部字符 ∴ 继续扩展
     *       l--r                     - 未包含 t 中全部字符 ∴ 继续扩展
     *       l-----r                  - 未包含 t 中全部字符 ∴ 继续扩展
     *       l--------r               - 未包含 t 中全部字符 ∴ 继续扩展
     *       l-----------r            - 窗口已包含 t 中全部字符 ∴ 开始收缩
     *          l--------r            - 仍然包含 t 中全部字符，继续收缩
     *             l-----r            - 不再包含 t 中全部字符 ∴ 取 s[l-1,r] 与 minLen 比较，并开始扩展
     *             l--------r         - 包含了 t 中全部字符 ∴ 开始收缩
     *                l-----r         - 仍然包含 t 中全部字符 ∴ 继续收缩
     *                   l--r         - 不再包含 t 中全部字符 ∴ 取 s[l-1,r] 与 minLen 比较，并开始扩展
     *                   l-----r      - 包含了 t 中全部字符 ∴ 开始收缩
     *                      l--r      - 不再包含 t 中全部字符 ∴ 取 s[l-1,r] 与 minLen 比较，并开始扩展
     *                      l-----r   - 未包含 t 中全部字符；又 ∵ r 已抵达 s 末尾 ∴ 无法继续扩展，循环结束
     * - 实现：该过程中需知道，当 l 或 r 任意一边右移一步后：
     *     1. 边界上的字符是否是 t 中的字符；
     *     2. 此时窗口内是否包含 t 中的所有字符。
     *   这需要两个结构来实现：
     *     - Map<Character, Integer> freq 记录在窗口内同时又在 t 中的字符的频次；
     *     - int matchCount 记录已经匹配上的 t 中的字符的个数，若 matchCount == t.size() 说明 t 中所有字符已在窗口内。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String minWindow(String s, String t) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : t.toCharArray())              // 先构建 t 中字符的频谱
            freq.merge(c, 1, Integer::sum);   // 相当于 freq.put(c, freq.getDefault(c) + 1);

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;    // minLen 记录匹配上的子串的最小长度，start 记录其起始索引，用于最后截取
        char[] chars = s.toCharArray();
        boolean shrinked = false;                   // 需要一个标志位标识窗口是否进行过收缩（比如👆前四步没有进行过收缩 ∴ 不能取 minLen）

        while (r < s.length()) {                    // ∵ 每扩展一个字符后都会进行充分收缩 ∴ r 到达最后一个字符，且 l 进行充分收缩后整个滑动过程结束
            if (freq.containsKey(chars[r])) {       // 先扩展窗口（减小 r 处字符在频谱中的频次）
                shrinked = false;
                if (freq.get(chars[r]) > 0) matchCount++;  // 若频次 >0 表示 r 处字符匹配上了
                freq.merge(chars[r], -1, Integer::sum);
            }
            r++;                                    // 扩展窗口

            while (matchCount == t.length()) {      // 只要窗口中包含了 t 的所有字符，就持续收缩窗口（增大 l 处字符在频谱中的频次）
                shrinked = true;                    // 只要开始收缩就将标志位置为 true
                if (freq.containsKey(chars[l])) {
                    if (freq.get(chars[l]) == 0)    // l 处字符频次为0说明 t 中所有的该字符已经刚好被匹配完了，此时要记录 minLen
                        matchCount--;               // （若匹配过多则频次会 <0，说明是冗余字符，可以不需记录 minLen 而直接收缩窗口）
                    freq.merge(chars[l], 1, Integer::sum);
                }
                l++;                                // 收缩窗口
            }

            if (shrinked) {                         // 当扩展和收缩过程都完成后，再取 minLen
                if ((r-1) - (l-1) + 1 < minLen) {   // ∵ 此时 r、l 都已经++过了 ∴ 都要-1
                    minLen = (r-1) - (l-1) + 1;
                    start = l-1;
                }
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /*
     * 解法2：滑动窗口（解法1的简化版，🥇最优解）
     * - 思路：与解法1一致。
     * - 实现：与解法1区别在于：
     *   1. 取 minLen 的时机改到了 while 内部（即每次收缩时取 minLen）∴ 不再需要 shrinked 标志位标识是否收缩过。
     *   2. 在扩展、收缩窗口时不再需要判断 r、l 处的字符是否在 freq 中（即是否为 t 中字符），即使是非 t 中字符也可以添加进去。
     * - 💎 套路：扩展/收缩滑动窗口类型的题目都可以采用该模式
     *   1. 大 while 循环内 r 每扩展一个字符后都让 l 进行充分收缩（r 用 if 扩展，l 用 while 收缩）；
     *   2. 扩展/收缩时检测 r/l 处的字符在频谱 freq 中的值来增/减 matchCount；
     *   3. 在每次收缩后比较并记录子串长度；
     *   4. 大 while 结束条件为 r < n。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String minWindow2(String s, String t) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : t.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;
        char[] chars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)
                matchCount++;
            freq.merge(chars[r++], -1, Integer::sum);  // 不同点：非 t 中的字符也会被添加到 freq 中，但值总是 < 0
                                                             // 只有 t 中的字符才有可能频率 >= 0。
            while (matchCount == t.length()) {  // 只要窗口中包含了 t 的所有字符，就持续收缩窗口（增加 l 处字符的频次）。
                if (r - l < minLen) {           // 当所有该字符都已匹配上，且窗口宽度比之前更小时，更新 minLen、start。
                    minLen = r - l;             // ∵ r 在上面已经++过了，指向下一个待处理的字符 ∴ 这里窗口长度为 r-l，
                    start = l;                  // 而非 r-l+1。
                }
                if (freq.get(chars[l]) == 0)    // ∵ 只有 t 中字符才可能频率 >= 0 ∴ 若这里的字符频率为0，就意味着一定是
                    matchCount--;               // t 中字符，且频次也已匹配上了 ∴ 从窗口移出时需要 matchCount--。
                freq.merge(chars[l++], 1, Integer::sum);
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    /*
     * 解法3：滑动窗口（解法2的 int[256] 版）
     * - 思路：与解法1、2一致。
     * - 实现：采用 int[256] 代替解法1中的 Map，从而得以简化语句（这种类型的题目中，int[256] 的解法通常都能比 Map 更简洁）。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static String minWindow3(String s, String t) {
        int[] freq = new int[256];
        for (char c : t.toCharArray()) freq[c]++;

        int l = 0, r = 0, matchCount = 0;
        int minLen = s.length() + 1, start = -1;

        while (r < s.length()) {
            if (freq[s.charAt(r++)]-- > 0)  // 这一个条件在解法1中需要两个条件才能实现 ∵ int[256] 为所有字符都设了初值0
                matchCount++;
            while (matchCount == t.length()) {
                if (r - l < minLen)
                    minLen = r - (start = l);    // 2 assignments in 1 line
                if (freq[s.charAt(l++)]++ == 0)
                    matchCount--;
            }
        }
        return start == -1 ? "" : s.substring(start, start + minLen);
    }

    public static void main(String[] args) {
        log(minWindow0("ABAACBAB", "ABC"));  // expects "ACB"
        log(minWindow0("BCAACBAB", "BBC"));  // expects "CBAB" (t 中也可能存在重复字符)
        log(minWindow0("TT", "TT"));         // expects "TT"
        log(minWindow0("S", "SS"));          // expects ""
        log(minWindow0("YYZ", "ZY"));        // expects "YZ"
    }
}
