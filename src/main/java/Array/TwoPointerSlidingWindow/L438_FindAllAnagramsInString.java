package Array.TwoPointerSlidingWindow;

import java.util.ArrayList;
import java.util.List;

import static Utils.Helpers.log;

/*
* Find All Anagrams in a String
*
* - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
* - 思路：滑动窗口
* */

public class L438_FindAllAnagramsInString {
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || p == null) return res;

        int[] freq = new int[128];         // 记录字符的出现频次（不光是 p 中的字符，p 中没有的也会记录，见 "xxyz" 用例）（也可以用 hash map）
        for (char c : p.toCharArray()) freq[c]++;  // 初始化为 p 中每个字符的出现频次

        int l = 0, r = 0, matchCount = 0;  // matchCount 是匹配计数器，用于记录窗口中出现了多少个 p 中的字符
        while (r < s.length()) {
            if (freq[s.charAt(r)] > 0) {   // 若 r 处的字符匹配上了（即存在于 p 中）
                freq[s.charAt(r++)]--;     // 该字符频次-1；r 向右滑动，指向新字符
                matchCount++;
            } else {                       // 若 r 处的字符没有匹配上或已经匹配过了
                freq[s.charAt(l++)]++;     // l 向右滑动，即将 l 处的字符从窗口中移除，频次+1
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
