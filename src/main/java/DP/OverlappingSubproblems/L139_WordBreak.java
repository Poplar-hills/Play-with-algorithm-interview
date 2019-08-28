package DP.OverlappingSubproblems;

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
    * 超时解：Recursion
    * - 思路：类似 L343_IntegerBreak 的思路，将字符串递归地分成两段，直到找到解或无解。例如对 test case 1 来说：
    *     - "l" + f("eetcode")；
    *             - "e" + f("etcode")
    *                     - "e" + f("tcode")
    *                     - "et" + f("code")
    *                     - ...
    *             - "ee" + f("tcode")
    *             - "eet" + f("code")
    *             - ...
    *     - "le" + f("etcode")；
    *              - ...
    *     - "lee" + f("tcode")；
    *              - ...
    *     - "leet" + f("code")    → 此时前后两端同时存在于 wordDict 中，说明原问题有解
    *
    * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        return search(s, 0, new HashSet<>(wordDict));  // 为了高效查询 wordDict 是否包含某个字符串，将 wordDict 转为 set
    }

    private static boolean search(String s, int l, Set<String> set) {
        if (l == s.length()) return true;            // 递归到底时后半段为空，而前半段可能存在于 set 中 ∴ 也要返回 true 后面的 if 才能为 true
        for (int r = l + 1; r <= s.length(); r++)    // 注意遍历终止条件为 <= s.length() ∵ 下面 substring 时 r 是不包括在内的
            if (set.contains(s.substring(l, r)) && search(s, r, set))  // 若前半段和后半段都在 set 中，说明原问题有解
                return true;
        return false;
    }

    /*
     * 解法1：Recursion + Memoization
     * - 思路：用缓存记录重叠子问题的计算结果。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean wordBreak1(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        Boolean[] cache = new Boolean[s.length()];  // 此处使用 Boolean 而非 boolean，从而下面可以判断是否为 null
        return search1(s, 0, new HashSet<>(wordDict), cache);
    }

    private static boolean search1(String s, int l, HashSet<String> set, Boolean[] cache) {
        if (l == s.length()) return true;
        if (cache[l] != null) return cache[l];
        for (int r = l + 1; r <= s.length(); r++)
            if (set.contains(s.substring(l, r)) && search1(s, r, set, cache))
                return cache[l] = true;
        return cache[l] = false;
    }

    /*
     * 解法2：DP
     * - 思路：
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static boolean wordBreak2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;

        int n = s.length();
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[n + 1];  // dp[i] 表示子串 s[0..i) 是否能由 set 中的单词组成（也因此要开辟 n+1 的空间）
        dp[0] = true;                       // dp[0]，即 s[0,0)，即空字符串。空字符串不需要任何单词即可组成，因此设为 true

        for (int r = 1; r <= n; r++) {      // r ∈ [1..n]
            for (int l = 0; l < r; l++) {   // l ∈ [0..r)
                if (dp[l] && set.contains(s.substring(l, r))) {  // 若前、后两段字符串都在 set 中
                    dp[r] = true;
                    break;
                }
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {
        log(wordBreak2("leetcode", Arrays.asList("leet", "code")));       // true
        log(wordBreak2("applepenapple", Arrays.asList("apple", "pen")));  // true
        log(wordBreak2("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));  // false
    }
}