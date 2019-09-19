package Array.TwoPointerSlidingWindow;

import java.util.ArrayList;
import java.util.List;

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

        int[] freq = new int[256];
        for (char c : p.toCharArray()) freq[c]++;  // 将 p 中每个字符的出现频次初始化为1

        int l = 0, r = 0;
        int matchCount = 0;  // 记录窗口中出现了多少个 p 中的字符

        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) matchCount++;
            freq[s.charAt(r)]--;
            r++;

            if (matchCount == p.length()) res.add(l);

            if (r - l == p.length()) {  // 若窗口中的元素个数比 p 的长度多1，则开始让左界右滑，缩窄窗口
                if (freq[s.charAt(l)] == 0) matchCount--;
                freq[s.charAt(l)]++;
                l++;
            }
        }

        return res;
    }

    /*
    * 解法2：滑动窗口（更简洁但更费解一点）
    * - 注：该方法中 l 可能会大于 r，这也是该解法的费解之处。总的来说解法1更加可读。
    * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
    * */
    public static List<Integer> findAnagrams2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        int[] freq = new int[256];
        for (char c : p.toCharArray()) freq[c]++;  // 将 p 中每个字符的出现频次初始化为1

        int l = 0, r = 0;
        int matchCount = 0;                // 记录窗口中出现了多少个 p 中的字符

        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) {   // 若 r 处的字符匹配上了（即存在于 p 中）
                freq[s.charAt(r++)]--;     // 该字符频次-1；r 向右滑动
                matchCount++;
            } else {                       // 若 r 处的字符没有匹配上或已经匹配过了
                freq[s.charAt(l++)]++;     // l 向右滑动，将 l 处的字符从窗口中移除，并使其频次+1
                matchCount--;
            }
            if (matchCount == p.length())  // 此时窗口中的字符完全匹配 p 中的字符（但不一定按顺序），即找到了一个 anagram
                res.add(l);
        }

        return res;
    }

    public static void main(String[] args) {
        log(findAnagrams("cbaebabacd", "abc"));  // expects [0, 6] ("cba", "bac")
        log(findAnagrams("abab", "ab"));         // expects [0, 1, 2] ("ab", "ba", "ab")
        log(findAnagrams("cccddd", "dc"));       // expects [2] ("cd")
        log(findAnagrams("xxyz", "zy"));         // expects [2] ("yz")
    }
}
