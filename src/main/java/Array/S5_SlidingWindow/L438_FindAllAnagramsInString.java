package Array.S5_SlidingWindow;

import java.util.*;

import static Utils.Helpers.log;

/*
 * Find All Anagrams in a String
 *
 * - Given a string s and a non-empty string p, find all the start indices of p's anagrams (由颠倒字母顺序而构成的词) in s.
 *
 * - Note:
 *   - Strings consists of lowercase English letters only.
 *   - The order of output does not matter.
 * */

public class L438_FindAllAnagramsInString {
    /*
     * 解法1：定长滑动窗口
     * - 思路：类似 L76_MinimumWindowSubstring 的解法1 ∵ 是找连续子串的问题 ∴ 可尝试滑动窗口方法求解 —— 控制窗口左右边界
     *   的滑动来找到所需子串。而另一方面 ∵ 要找的是 p 的 anagrams（即找到的解的长度要 = p 的长度，不同于 L76 中的解只需包含
     *   p 中字符即可）∴ 可以采用定长滑动窗口：让窗口长度固定为 p 的长度，然后在 s 上滑动，在滑动过程中检查窗口中的字符是否刚好
     *   与 p 中的字符完全匹配。例如对于：s = eecbaebabacd，p = abc
     *         e e c b a e b a b a c d
     *         -
     *         ---
     *         -----                     - 在这之前窗口连续扩展，从这之后窗口开始以定长滑动
     *           -----
     *             -----                 - 找到解1
     *                   ...             - 省略中间滑动过程
     *                         -----     - 找到解2
     *                           -----
     * - 实现：在上面的该过程中，r 一直在右移，而 l 则是在窗口长度达到 p 的长度后才开始右移 —— 这里的"达到"在代码中表现为
     *   "当窗口长度 > p 的长度后，让 l++ 来收缩窗口"。
     * - 总结：该解法虽然代码结构上类似 L76 解法1，但滑动过程很不一样 —— 除了最开始连续扩展窗口（使其到达 p.lenght() + 1）
     *   之外，之后每次扩展一步就收缩一步（而非 L76 里连续扩展、连续收缩的过程），这样来模拟定长窗口的滑动过程。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        Map<Character, Integer> freq = new HashMap<>();  // p 的频谱（开始遍历之后还会加入属于 s 但不属于 p 的字符）
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);  // 相当于 freq.put(c, freq.getOrDefault(c,0) + 1);

        int matchCount = 0, l = 0, r = 0;
        char[] chars = s.toCharArray();

        while (r < s.length()) {                   // 当 r 抵达末尾，且 l 完成收缩时遍历结束
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)  // 若 r 处字符在频谱中且频次 >0，则表示匹配上了
                matchCount++;
            freq.merge(chars[r++], -1, Integer::sum);  // 让 chars[r] 的频次-1（即使 chars[r] 不在频谱中也将其加入并设置频率为-1）

            if (matchCount == p.length()) res.add(l);

            if (r - l + 1 > p.length()) {          // 每当窗口长度 > p.length()，就收缩一下 l
                if (freq.get(chars[l]) == 0) matchCount--;
                freq.merge(chars[l++], 1, Integer::sum);
            }
        }

        return res;
    }

    /*
     * 解法2：解法1的 int[256] 版
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static List<Integer> findAnagrams2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        int[] freq = new int[256];
        for (char c : p.toCharArray()) freq[c]++;  // 将 p 中每个字符的出现频次初始化为1

        int matchCount = 0, l = 0, r = 0;

        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) matchCount++;
            freq[s.charAt(r++)]--;

            if (matchCount == p.length()) res.add(l);

            if (r - l + 1 > p.length()) {
                if (freq[s.charAt(l)] == 0) matchCount--;
                freq[s.charAt(l++)]++;
            }
        }

        return res;
    }

    /*
     * 解法3：滑动窗口
     * - 思路：与 L76 解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams3(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0;
        char[] sChars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(sChars[r])) {
                if (freq.get(sChars[r]) > 0)
                    matchCount++;
                freq.merge(sChars[r], -1, Integer::sum);
            }
            r++;
            while (matchCount == p.length()) {
                if (r - l == p.length())  // ∵ 在判断 matchCount == p.length() 之前 r 已经自增 ∴ 这里不再是 r-l+1
                    res.add(l);
                if (freq.containsKey(sChars[l])) {
                    if (freq.get(sChars[l]) == 0)
                        matchCount--;
                    freq.merge(sChars[l], 1, Integer::sum);
                }
                l++;
            }
        }

        return res;
    }

    /*
     * 解法4：解法3的简化版
     * - 思路：与解法3一致。
     * - 实现：与 L76_MinimumWindowSubstring 解法2一致。
     * - 总结：比起解法1，该解法通用性更强，还可解决 L76 的"包含"类问题。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams4(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0;
        char[] sChars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(sChars[r]) && freq.get(sChars[r]) > 0)
                matchCount++;
            freq.merge(sChars[r++], -1, Integer::sum);

            while (matchCount == p.length()) {
                if (r - l == p.length())
                    res.add(l);
                if (freq.get(sChars[l]) == 0)
                    matchCount--;
                freq.merge(sChars[l++], 1, Integer::sum);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(findAnagrams("eecbaebabacd", "abc"));  // expects [2, 8] ("cba", "bac")
        log(findAnagrams("xxyxxy", "xxy"));        // expects [0, 1, 2, 3] ("xxy", "xyx", "yxx", "xxy")
        log(findAnagrams("cccddd", "dc"));         // expects [2] ("cd")
        log(findAnagrams("z", "zz"));              // expects []
        log(findAnagrams("hhh", "hhh"));           // expects [0]
    }
}
