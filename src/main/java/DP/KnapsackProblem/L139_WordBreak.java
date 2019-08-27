package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* Word Break
*
* - 给定一个字符串 s 和一个字符串数组 wordDict（无重复字符串），问能否能使用 wordDict 中的字符串拼接成 s（每个字符串只能用一次）。
* */

public class L139_WordBreak {
    /*
    * 解法1：Recursion
    * - 思路：类似 L343_IntegerBreak 的思路不断将字符串分段，然后递归，直到找到解或无解。例如对 test case 1 来说：
    *   第1次递归："l" + f("eetcode")；第2次递归："le" + f("etcode")；第3次递归："lee" + f("tcode")；... 直到找到字符串的前半段
    *   "leet"、后半段"code"同时存在于 wordDict 中，说明原问题有解。
    * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null) return false;
        return wordBreak(s, 0, new HashSet<>(wordDict));  // 为了高效查询 wordDict 是否包含某个字符串，将 wordDict 转为 set
    }

    private static boolean wordBreak(String s, int start, Set<String> set) {
        if (start == s.length()) return true;                  // 递归到底时后半段为空，而前半段可能存在于 set 中 ∴ 也要返回 true 后面的 if 才能为 true
        for (int end = start + 1; end <= s.length(); end++)    // 注意遍历终止条件为 <= s.length() ∵ 下面 substring 时 end 是不包括在内的
            if (set.contains(s.substring(start, end)) && wordBreak(s, end, set))  // 若前半段和后半段都在 set 中，说明原问题有解
                return true;
        return false;
    }

    public static void main(String[] args) {
        log(wordBreak("leetcode", Arrays.asList("leet", "code")));       // true
        log(wordBreak("applepenapple", Arrays.asList("apple", "pen")));  // true
        log(wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));  // false
    }
}