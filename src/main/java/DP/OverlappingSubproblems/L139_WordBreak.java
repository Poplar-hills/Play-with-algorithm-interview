package DP.OverlappingSubproblems;

import static Utils.Helpers.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* Word Break
*
* - 给定一个字符串 s 和一个字符串数组 wordDict，问能否能使用 wordDict 中的字符串拼接成 s。
* - 注：1. wordDict 中的元素可以重复使用；2. wordDict 中不存在重复元素。
* */

public class L139_WordBreak {
    /*
    * 超时解：DFS（没有 cache 的 DFS 就是 Brute force，同时也是 Back-tracking)
    * - 思路：类似 L343_IntegerBreak，将字符串递归地截成两段，直到有解或到底：
    *   - 定义子问题：f(i) 表示“从索引 i 开始的字符串 s[i,len) 是否能由 wordDict 中的单词拼接而成”；
    *   - 状态转移方程：f(i) = any(s[i,j) && f(j))，其中 i ∈ [0,len)，j ∈ [i+1,len]。
    *   例如对 s="sunisfo", wordDict=["sun","is","fo"] 来说：
    *      f("sunisfo")
    *        - "s" && f("unisfo")
    *        - "su" && f("nisfo")
    *        - "sun" && f("isfo")                  → "sun" 在 wordDict 中，继续递归分解后半段
    *                   - "i" && f("sfo")
    *                   - "is" && f("fo")          → "is" 在 wordDict 中，继续递归分解后半段
    *                             - "f" && f("o")
    *                             - "fo" && f("")  → 此时返回 true 到上层，上层也返回 true...直到原问题返回 true
    * - 实现：根据递归结构可知需要双重循环：一重用于在 s 上移动 i，另一重用于在 i 固定的情况下移动 j，将字符串截成两段。
    * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        return helper(s, 0, new HashSet<>(wordDict));  // 为了高效查询 wordDict 是否包含某个字符串，将 wordDict 转为 set
    }

    private static boolean helper(String s, int i, Set<String> set) {
        if (i == s.length()) return true;              // f("") 的情况要返回 true
        for (int j = i + 1; j <= s.length(); j++)      // 注意 j 可以等于 s.length() ∵ 下面 substring 时 j 是不包含的
            if (set.contains(s.substring(i, j)) && helper(s, j, set))  // 若前后两段都在 set 中，说明该问题有解
                return true;
        return false;
    }

    /*
    * 解法1：Recursion + Memoization (DFS with cache)
    * - 思路：用缓存记录重叠子问题的计算结果。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak1(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        Boolean[] cache = new Boolean[s.length()];  // 此处使用 Boolean 而非 boolean，从而未计算状态可以设为 null
        return helper1(s, 0, new HashSet<>(wordDict), cache);
    }

    private static boolean helper1(String s, int i, HashSet<String> set, Boolean[] cache) {
        if (i == s.length()) return true;
        if (cache[i] != null) return cache[i];
        for (int j = i + 1; j <= s.length(); j++)
            if (set.contains(s.substring(i, j)) && helper1(s, j, set, cache))
                return cache[i] = true;
        return cache[i] = false;
    }

    /*
    * 解法2：DP
    * - 思路：子问题定义和状态转移方程不变：
    *   - f(i) 表示“从索引 i 开始的字符串 s[i,len) 是否能由 wordDict 中的单词拼接而成”；
    *   - f(i) = any(s[i,j) && f(j))，其中 i ∈ [0,len)，j ∈ [i+1,len]。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;

        Set<String> set = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[n] = true;

        for (int i = n - 1; i >= 0; i--) {
            for (int j = n; j >= i + 1; j--) {
                if (set.contains(s.substring(i, j)) && dp[j]) {
                    dp[i] = true;
                    break;
                }
            }
        }

        return dp[0];
    }

    /*
    * 解法3：DP
    * - 思路：
    *   - 定义子问题：dp[i] 表示子串 s[0..i) 是否能由 wordDict 中的单词组成；
    *   - 状态转移方程：对于任意 j ∈ [0,i) 有 dp[i] = dp[j] && wordDict.contains(s[j+1, i])，即前后两段都是 wordDict 中的单词。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    // public static boolean wordBreak3(String s, List<String> wordDict) {
    //     if (s == null || s.length() == 0) return false;

    //     int n = s.length();
    //     Set<String> set = new HashSet<>(wordDict);
    //     boolean[] dp = new boolean[n + 1];  // ∵ dp[i] 表示子串 s[0..i) 是否能由 set 中的单词组成 ∴ 要开辟 n+1 的空间
    //     dp[0] = true;                       // dp[0]，即 s[0,0)，即空字符串。空字符串不需要任何单词即可组成，因此设为 true

    //     for (int i = 1; i <= n; i++) {      // i ∈ [1..n]
    //         for (int j = 0; j < i; j++) {   // j ∈ [0..i)
    //             if (dp[j] && set.contains(s.substring(j, i))) {  // 若前、后两段字符串都在 set 中
    //                 dp[i] = true;
    //                 break;
    //             }
    //         }
    //     }
    //     return dp[n];
    // }

    /*
    * 解法3：DFS
    * - 实现：不同于解法1，本解法对 s 的分段方式不再是逐个字符分段，而是采用头部单词匹配（s.startWith(word, start)）。理由是若 s 能
    *   由 wordDict 中的词组成，则一定是由其中某一个词开头的，因此。
    * - 注意：s.startWith() 方法的第二个参数指定匹配的起始索引，很好用。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)，该解法是几种解法中最快的。
    * */
    public static boolean wordBreak3(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        Boolean[] cache = new Boolean[s.length()];
        return dfs(s, 0, wordDict, cache);
    }

    private static boolean dfs(String s, int start, List<String> wordDict, Boolean[] cache) {
        if (start == s.length()) return true;  // 只有找到解（s 完全有 wordDict 中的词组成）时才会递归到底
        if (cache[start] != null) return cache[start];

        for (String word : wordDict)
            if (s.startsWith(word, start) && dfs(s, start + word.length(), wordDict, cache))  // 若前、后两段都存在于 wordDict 中则说明有解
                return cache[start] = true;
        return cache[start] = false;
    }

    public static void main(String[] args) {
        log(wordBreak2("leetcode", Arrays.asList("leet", "code")));       // expects true
        log(wordBreak2("applepenapple", Arrays.asList("apple", "pen")));  // expects true
        log(wordBreak2("cars", Arrays.asList("car", "ca", "rs")));        // expects true
        log(wordBreak2("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));  // expects false
    }
}