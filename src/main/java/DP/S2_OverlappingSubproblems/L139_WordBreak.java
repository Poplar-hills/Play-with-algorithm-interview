package DP.S2_OverlappingSubproblems;

import static Utils.Helpers.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Word Break
 *
 * - Given a non-empty string s and a dictionary wordDict containing a list of non-empty words, determine
 *   if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * - Note:
 *   - The same word in the dictionary may be reused multiple times in the segmentation.
 *   - You may assume the dictionary does not contain duplicate words.
 * */

public class L139_WordBreak {
    /*
     * 超时解：DFS + Recursion
     * - 思路：总体思路是对 s 进行不同长度的截取，检查截取出来的部分是否存在于 wordDict 中，若存在则继续对剩余的部分进行截取。
     *   例如对于 s="cars", wordDict=["ca","car","ars","s"] 来说：
     *                    "cars"
     *       c/       ca/     car\     cars\      - 以不同的长度进行截取，若截出来的部分不存在于 wordDict 中，则终止
     *       ×        "rs"       "s"        ×     - ca、car 存在于 wordDict 中 ∴ 继续递归截取
     *              r/  rs\      s|               - r、rs 都不存在于 wordDict 中 ∴ 终止
     *              ×     ×       ""              - 当剩余部分为""时，说明找到解了，返回 true
     *   ∴ 可知：
     *     - 子问题定义：f(i) 表示“从索引 i 开始到末尾的字符串 s[i..n) 能否由 wordDict 中的单词拼接而成”；
     *     - 递推表达式：f(i) = any(s[i..j) && f(j))，其中 i ∈ [0,n)，j ∈ [i+1,n]。
     * - 说明：该解法其实就是回溯搜索。
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
   public static boolean wordBreak_1(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        return helper_1(s, 0, new HashSet<>(wordDict));    // 将 wordDict 转为 Set
    }

    private static boolean helper_1(String s, int i, Set<String> set) {
        if (i == s.length()) return true;           // f("") 的情况返回 true
        for (int j = i + 1; j <= s.length(); j++)   // 注意 j 可以等于 s.length() ∵ 下面 substring 时 j 是不包含的
            if (set.contains(s.substring(i, j)) && helper_1(s, j, set))  // 若前后两段都在 set 中，说明该问题有解
                return true;
        return false;
    }

    /*
     * 解法1：DFS + Recursion + Memoization
     * - 思路：对于 s="carsys", wordDict=["ca","car","r","sy","ys"] 来说：
     *                    "carsys"
     *       c/       ca/         car\     cars\
     *       ×       "rsys"         "sys"        ×
     *              r/   rs\        / | \
     *            "sys"     ×        ...
     *            / | \
     *             ...
     *   在自上而下的分解过程中的不同分支上出现了重叠子问题 f("sys") ∴ 可在超时解的基础上加入 Memoization 优化。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        Boolean[] cache = new Boolean[s.length()];   // 此处使用 Boolean 而非 boolean，未计算的位置上初值为 null
        return helper(s, 0, new HashSet<>(wordDict), cache);
    }

    private static boolean helper(String s, int i, HashSet<String> set, Boolean[] cache) {
        if (i == s.length()) return true;
        if (cache[i] != null) return cache[i];
        for (int j = i + 1; j <= s.length(); j++)
            if (set.contains(s.substring(i, j)) && helper(s, j, set, cache))
                return cache[i] = true;
        return cache[i] = false;
    }

    /*
     * 解法2：DP
     * - 思路：将解法1直接转换为 DP 的写法（其实本质思路与解法1是一样的 —— 都是自上而下分解任务），子问题定义和递推表达式不变：
     *   - f(i) 表示“从索引 i 开始到末尾的字符串 s[i..n) 是否能由 wordDict 中的单词拼接而成”；
     *   - f(i) = any(s[i..j) && f(j))，其中 i ∈ [0,n)，j ∈ [i+1,n]。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static boolean wordBreak2(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;

        Set<String> set = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[n] = true;                           // dp[n] 存储 f("") 的解

        for (int i = n - 1; i >= 0; i--) {      // 从后往前递推
            for (int j = i + 1; j <= n; j++) {  // 在 s[i..n) 中尝试不同的截取方式
                if (set.contains(s.substring(i, j)) && dp[j]) {  // 若 s[i..j)、s[j..n) 都在 set 中则说明 f(i) 有解
                    dp[i] = true;
                    break;                      // 若 f(i) 已经有解，则无需再尝试其他截取方式
                }
            }
        }

        return dp[0];
    }

    /*
     * 解法3：DP
     * - 思路：不同于解法2，该解法采用自下而上的正统 DP 思路，先解决基本问题，再递推出高层次问题的解：
     *   - 子问题定义：f(i) 表示“字符串 s[0..i) 是否能由 wordDict 中的单词拼接而成”；
     *   - 递推表达式：f(i) = any(f(j) && s[j..i])。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static boolean wordBreak3(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;

        Set<String> set = new HashSet<>(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];  // dp[i] 表示子串 s[0..i) 是否能由 set 中的单词组成
        dp[0] = true;                       // f("") 的情况

        for (int i = 1; i <= n; i++) {      // 从前往后遍历
            for (int j = 0; j < i; j++) {   // 在 s[0..i) 中尝试不同的截取方式
                if (dp[j] && set.contains(s.substring(j, i))) {  // 若 s[0..j)、s[j..n) 都在 set 中则说明 f(i) 有解
                    dp[i] = true;
                    break;                  // 若 f(i) 已经有解，则无需再尝试其他截取方式
                }
            }
        }

        return dp[n];
    }

    /*
     * 解法4：DFS + Recursion + 前缀匹配
     * - 实现：不同于解法1，本解法对 s 的分段方式不再是逐个字符分段检测，而是采用头部单词匹配。理由是，若 s 能由 wordDict 中的
     *   词组成，则一定是由其中某一个词开头的（反之若 s 不是由 wordDict 中的词开头的，则可直接返回 false），找到这个词之后再对
     *   其后半段继续这样的匹配检测，从而形成递归结构。
     * - 注意：startWith() 方法的第二个参数指定匹配的起始索引，好用。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)，统计性能是几种解法中最快的。
     * */
    public static boolean wordBreak4(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) return false;
        Boolean[] cache = new Boolean[s.length()];
        return dfs(s, 0, wordDict, cache);
    }

    private static boolean dfs(String s, int i, List<String> wordDict, Boolean[] cache) {
        if (i == s.length()) return true;
        if (cache[i] != null) return cache[i];

        for (String word : wordDict)
            if (s.startsWith(word, i) && dfs(s, i + word.length(), wordDict, cache))  // 若前、后两段都存在于 wordDict 中
                return cache[i] = true;
        return cache[i] = false;
    }

    public static void main(String[] args) {
        log(wordBreak2("leetcode", List.of("leet", "code")));              // expects true
        log(wordBreak2("applepenapple", List.of("apple", "pen")));         // expects true
        log(wordBreak2("cars", List.of("ca", "car", "ars", "s")));         // expects true
        log(wordBreak2("carsys", List.of("ca", "car", "r", "sy", "ys")));  // expects false
        log(wordBreak2("catsandog", List.of("cats", "dog", "sand", "and", "cat")));  // expects false
    }
}