package Array.S5_SlidingWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *   与 p 中的字符完全匹配。
     * - 总结：该解法虽然代码结构上类似 L76 解法1，但滑动过程很不一样 —— 除了最开始连续扩展窗口（使其到达 p.lenght()+1）之外，
     *   之后每次扩展一步就收缩一步（而非 L76 里连续扩展、连续收缩的过程），这样来模拟定长窗口的滑动过程：
     *     例如对于：s = eecbaebabacd，p = abc
     *         e e c b a e b a b a c d
     *         -
     *         ---
     *         -----
     *           -----
     *             -----                 - 找到解1
     *                   ...             - 省略中间滑动过程
     *                         -----     - 找到解2
     *                           -----
     *
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

        while (r < s.length()) {                       // 当 r 抵达末尾时遍历结束
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0)  // 在频谱中且频次 >0 表示 r 处字符匹配上了
                matchCount++;
            freq.merge(chars[r++], -1, Integer::sum);  // 让 chars[r] 的频次-1（即使 chars[r] 不在频谱中也将其加入并设置频率为-1）

            if (matchCount == p.length()) res.add(l);

            if (r - l + 1 > p.length()) {              // 每当窗口长度 >p 的长度，收缩一下窗口（收缩一步就不再 >p 了）
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
     * 解法3：滑动窗口（比解法1、2更简洁但也更费解）
     * - 思路：与解法1、2不同，该解法中的窗口控制方式是：只要 r 处字符没有匹配上，就让 l 右滑（∴ l 不总是在 r 的左侧，即窗口
     *   长度可能为-1），并且一旦没匹配上就让该字符加入频谱使得下再出现时一定能匹配上。若匹配上了，则让 r 右滑：
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
     * - 注：该方法中 matchCount 可能为负、l 可能会大于 r（见 test case 1 中的 "ee"）因此会有点反直觉（但其实很巧妙），
     *   需要单步调试才能更好理解。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams3(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() == 0) return res;

        Map<Character, Integer> freq = new HashMap<>();  // 频谱中初始只有 p 中的字符（但开始遍历之后还会加入属于 s 但不属于 p 的字符）
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCount = 0, l = 0, r = 0;
        char[] chars = s.toCharArray();

        while (r < s.length()) {
            if (freq.containsKey(chars[r]) && freq.get(chars[r]) > 0) {  // 在频谱中且频次 >0 表示 r 处字符匹配上了
                freq.merge(chars[r++], -1, Integer::sum);                // 这里与解法1、2不同，只有当匹配上了才让 r 右滑
                matchCount++;
            } else {                                      // 若 r 处字符没有匹配上
                freq.merge(chars[l++], 1, Integer::sum);  // 则将该字符其加入频谱，并向右收缩窗口
                matchCount--;
            }
            if (matchCount == p.length())                 // 匹配的字符数 == p 中的字符数，即找到了一个 anagram
                res.add(l);
        }

        return res;
    }

    /*
     * 解法4：
     * - 思路：
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<Integer> findAnagrams4(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : p.toCharArray())
            freq.merge(c, 1, Integer::sum);

        int matchCnt = 0, l = 0, r = 0;
        char[] sChars = s.toCharArray();
        while (r < s.length()) {
            if (freq.containsKey(sChars[r])) {
                if (freq.get(sChars[r]) > 0)
                    matchCnt++;
                freq.merge(sChars[r], -1, Integer::sum);
            }
            r++;
            while (matchCnt == p.length()) {
                if (r - l == p.length())
                    res.add(l);
                if (freq.containsKey(sChars[l])) {
                    if (freq.get(sChars[l]) == 0)
                        matchCnt--;
                    freq.merge(sChars[l], 1, Integer::sum);
                }
                l++;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        log(findAnagrams4("eecbaebabacd", "abc"));  // expects [2, 8] ("cba", "bac")
        log(findAnagrams4("xxyxxy", "xxy"));        // expects [0, 1, 2, 3] ("xxy", "xyx", "yxx", "xxy")
        log(findAnagrams4("cccddd", "dc"));         // expects [2] ("cd")
        log(findAnagrams4("z", "zz"));              // expects []
    }
}
