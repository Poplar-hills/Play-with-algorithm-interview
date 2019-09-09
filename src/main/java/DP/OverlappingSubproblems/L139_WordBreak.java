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
    * - 思路：类似 L343_IntegerBreak，将字符串递归地截成两段，直到有解或到底。例如对 s="sunisfo", wordDict=["sun","is","fo"]：
    *   - f("sunisfo")
    *     - "s" && f("unisfo")
    *     - "su" && f("nisfo")
    *     - "sun" && f("isfo")                  → "sun" 在 wordDict 中，继续递归分解后半段
    *                - "i" && f("sfo")
    *                - "is" && f("fo")          → "is" 在 wordDict 中，继续递归分解后半段
    *                          - "f" && f("o")
    *                          - "fo" && f("")  → 此时返回 true 到上层，上层也返回 true...直到原问题返回 true
    * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
    * */
    public static boolean search2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        return search(s, 0, new HashSet<>(wordDict));  // 为了高效查询 wordDict 是否包含某个字符串，将 wordDict 转为 set
    }

    private static boolean search(String s, int l, Set<String> set) {
        if (l == s.length()) return true;          // 递归到底时后半段为空，而前半段可能存在于 set 中 ∴ 也要返回 true 这样下面的 if 才能为 true
        for (int r = l + 1; r <= s.length(); r++)  // 注意遍历终止条件为 <= s.length() ∵ 下面 substring 时 r 是不包括在内的
            if (set.contains(s.substring(l, r)) && search(s, r, set))  // 若前半段和后半段都在 set 中，说明原问题有解
                return true;
        return false;
    }

    /*
    * 解法1：Recursion + Memoization
    * - 思路：用缓存记录重叠子问题的计算结果。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
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
    *   - 定义子问题：dp[i] 表示子串 s[0..i) 是否能由 wordDict 中的单词组成；
    *   - 状态转移方程：对于任意 j ∈ [0,i) 有 dp[i] = dp[j] && wordDict.contains(s[j+1, i])，即前后两段都是 wordDict 中的单词。
    * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
    * */
    public static boolean wordBreak2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;

        int n = s.length();
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[n + 1];  // ∵ dp[i] 表示子串 s[0..i) 是否能由 set 中的单词组成 ∴ 要开辟 n+1 的空间
        dp[0] = true;                       // dp[0]，即 s[0,0)，即空字符串。空字符串不需要任何单词即可组成，因此设为 true

        for (int i = 1; i <= n; i++) {      // i ∈ [1..n]
            for (int j = 0; j < i; j++) {   // j ∈ [0..i)
                if (dp[j] && set.contains(s.substring(j, i))) {  // 若前、后两段字符串都在 set 中
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }

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
        log(wordBreak3("leetcode", Arrays.asList("leet", "code")));       // expects true
        log(wordBreak3("applepenapple", Arrays.asList("apple", "pen")));  // expects true
        log(wordBreak3("cars", Arrays.asList("car", "ca", "rs")));        // expects true
        log(wordBreak3("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));  // expects false
    }
}