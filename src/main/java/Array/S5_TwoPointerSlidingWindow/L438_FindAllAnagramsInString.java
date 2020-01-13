package Array.S5_TwoPointerSlidingWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * Find All Anagrams in a String
 *
 * - Given a string s and a non-empty string p, find all the start indices of p's anagrams (由颠倒字母顺序而构成的词) in s.
 * - Strings consists of lowercase English letters only.
 * - The order of output does not matter.
 * */

public class L438_FindAllAnagramsInString {
    /*
     * 解法1：滑动窗口
     * - 思路：类似 L76_MinimumWindowSubstring 的解法1。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        Map<Character, Integer> freq = new HashMap<>();  // 频谱中初始只有 p 中的字符（但开始遍历之后还会加入属于 s 但不属于 p 的字符）
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);  // 将 p 中每个字符的出现频次初始化为1，相当于 freq.put(c, freq.get(c)+1);

        int matchCount = 0, l = 0, r = 0;
        char[] chars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)
                matchCount++;
            freq.merge(chars[r++], -1, Integer::sum);

            if (matchCount == p.length()) res.add(l);

            if (r - l == p.length()) {  // 若窗口中的元素个数比 p 的长度多1，则开始让左界右滑，缩窄窗口
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

            if (r - l == p.length()) {  // 若窗口中的元素个数比 p 的长度多1，则开始让左界右滑，缩窄窗口
                if (freq[s.charAt(l)] == 0) matchCount--;
                freq[s.charAt(l++)]++;
            }
        }

        return res;
    }

    /*
     * 解法3：滑动窗口（比解法1、2更简洁但也更费解）
     * - 思路：∵ 是求最小子串 ∴ 可尝试滑动窗口方法求解 —— 控制窗口左右边界的滑动来找到所需子串。通过观察 test case 可得到窗口
     *   滑动的控制方式：在遍历过程中第一次遇到 p 中的字符时将 l 指向该位置，若后面的字符依然是 p 中字符，且出现次数还未到达其在
     *   p 中的出现次数，则继续扩展窗口
     *        "e  e  c  b  a  e  b  a  b  a  c  d"
     *         lr                                   - {a:1, b:1, c:1}, matchCount=0
     *         r  l                                 - {a:1, b:1, c:1, e:1}, matchCount=-1
     *            lr                                - {a:1, b:1, c:1, e:0}, matchCount=0
     *            r  l                              - {a:1, b:1, c:1, e:1}, matchCount=-1
     *               lr                             - {a:1, b:1, c:1, e:0}, matchCount=0
     *               l  r                           - {a:1, b:1, c:0, e:0}, matchCount=1
     *               l     r                        - {a:1, b:0, c:0, e:0}, matchCount=2
     *               l        r                     - {a:0, b:0, c:0, e:0}, matchCount=3
     *                  l     r                     - {a:0, b:0, c:1, e:0}, matchCount=2
     *                     l  r                     - {a:0, b:1, c:1, e:0}, matchCount=1
     *                        lr                    - {a:1, b:1, c:1, e:0}, matchCount=0
     *                        r  l                  - {a:1, b:1, c:1, e:1}, matchCount=-1
     *
     * - 注：该方法中 matchCount 可能为负、l 可能会大于 r（见 test case 1 中的 "ee"）因此会有点反直觉（但其实很巧妙），需要
     *   单步调试才能更好理解。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams3(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() == 0) return res;

        Map<Character, Integer> freq = new HashMap<>();  // 频谱中初始只有 p 中的字符（但开始遍历之后还会加入属于 s 但不属于 p 的字符）
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);  // 将 p 中每个字符的出现频次初始化为1，相当于 freq.put(c, freq.get(c)+1);

        int matchCount = 0, l = 0, r = 0;
        char[] chars = s.toCharArray();

        while (r < s.length()) {                          // 当 r 抵达 s 末尾时遍历结束
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0) {  // 若 r 处字符在频谱中且频次 > 0
                freq.merge(chars[r++], -1, Integer::sum);                // 则将其频率+1，并向右扩展窗口
                matchCount++;
            } else {                                      // 若 r 处字符不在频谱中
                freq.merge(chars[l++], 1, Integer::sum);  // 则将其加入频谱，并向右收缩窗口
                matchCount--;
            }
            if (matchCount == p.length())                 // 匹配的字符数 == p 中的字符数，即找到了一个 anagram
                res.add(l);
        }

        return res;
    }

    /*
     * 解法4：解法3的 int[256] 版
     * - 思路：与解法3一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static List<Integer> findAnagrams4(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        int[] freq = new int[256];
        for (char c : p.toCharArray()) freq[c]++;

        int matchCount = 0, l = 0, r = 0;

        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) {
                freq[s.charAt(r++)]--;
                matchCount++;
            } else {
                freq[s.charAt(l++)]++;
                matchCount--;
            }
            if (matchCount == p.length())
                res.add(l);
        }

        return res;
    }

    public static void main(String[] args) {
        log(findAnagrams("eecbaebabacd", "abc"));  // expects [2, 8] ("cba", "bac")
        log(findAnagrams("abab", "ab"));           // expects [0, 1, 2] ("ab", "ba", "ab")
        log(findAnagrams("cccddd", "dc"));         // expects [2] ("cd")
        log(findAnagrams("xxyxxy", "xxy"));        // expects [0, 1, 2, 3] ("xxy", "xyx", "yxx", "xxy")
        log(findAnagrams("z", "zz"));              // expects []
    }
}
