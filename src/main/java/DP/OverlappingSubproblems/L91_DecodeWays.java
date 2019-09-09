package DP.OverlappingSubproblems;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
* Decode Ways
*
* - A message containing letters from A-Z is being encoded to numbers using the following mapping: 'A' -> 1,
*   'B' -> 2, ..., 'Z' -> 26. Given a non-empty string containing only digits, determine the total number
*   of ways to decode it.
* */

public class L91_DecodeWays {
    /*
    * 超时解：DFS
    * - 思路：该题目与 L70 爬楼梯类似，都是在做某事时每次有2种选择，求一共有多少种不同的选择组合能最终做成该事。对本题来说：
    *   1. 在解码一个字符串时，每次都有2种选择：解码1个字母 or 解码2个字母；
    *   2. 全局解 = 局部解之和，例如：f("213") = f("13") + f("3") = 2 + 1 = 3；
    *   3. 两个特例：设 f("") = 1；f("0...") = 0；
    *   则整个解码过程就可以用递归的方式进行：
    *               f("102213")                            5
    *                ↙       ↘                           ↗   ↖
    *          f("02213")   f("2213")                  0       5
    *                       ↙       ↘                        ↗   ↖
    *                  f("213")  →  f("13")                3   ←   2
    *                        ↘      ↙     ↘                  ↖   ↗   ↖
    *                         f("3")      f("")                1       1
    *                           ↓                              ↑
    *                         f("")                            1
    *  - 定义子问题：f(i) 表示“从 i 开始的字符串的解法方式数”；
    *  - 状态转移方程：f(i) = f(i + 1) + f(i + 2)，其中 i ∈ [0, len-3]。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) return 0;
        return helper(s, 0);  // 0指向待解码字符串的第0个字母
    }

    private static int helper(String s, int i) {
        if (i == s.length()) return 1;                       // f("") 的情况
        if (i > s.length() || s.charAt(i) == '0') return 0;  // 越界以及 f("0...") 的情况
        return helper(s, i + 1) + helper(s, i + 2);
    }

    /*
    * 解法1：Recursion + Memoization (DFS with cache)
    * - 思路：在超时解的基础上加入 Memoization 优化。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numDecodings1(String s) {
        if (s == null || s.length() == 0) return 0;
        return dfs(s, 0, new HashMap<>());
    }

    public static int dfs(String s, int i, HashMap<Integer, Integer> map) {
        if (i == s.length()) return 1;
        if (s.charAt(i) == '0') return 0;
        if (map.containsKey(i)) return map.get(i);

        int res = dfs(s, i + 1, map);
        if (i + 1 < s.length() && Integer.parseInt(s.substring(i, i + 2)) < 27)
            res += dfs(s, i + 2, map);

        map.put(i, res);
        return res;
    }

    /*
    * 解法2：DP
    * - 思路：DP 与解法1中 recursion 的思路一致 —— 每个以指针 i 为起点的字符串的解码方法数 f(s, i) = f(s, i+1) + f(s, i+2)；
    *   只是实现方式相反 —— recursion 是从前往后递归，而 DP 是从后往前递推，前一个问题的解是建立在后面问题的解的基础上。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        int[] cache = new int[n + 1];   // ∵ cache 大小是已知的 ∴ 可以采用数组（否则只能采用 Map）
        cache[n] = 1;                   // 先解答最小问题，即 f("") 的情况

        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '0')
                cache[i] = 0;
            else {
                cache[i] = cache[i + 1];
                if (i + 1 < n && Integer.parseInt(s.substring(i, i + 2)) < 27)
                    cache[i] += cache[i + 2];
            }
        }

        return cache[0];
    }

    public static void main(String[] args) {
        log(numDecodings("12"));     // expects 2. "AB" (1,2) or "L" (12)
        log(numDecodings("227"));    // expects 2. "VG" (22,7), or "BBG" (2,2,7)
        log(numDecodings("226"));    // expects 3. "BZ" (2,26), "VF" (22,6), or "BBF" (2,2,6)
        log(numDecodings("102213")); // expects 5. ...
    }
}
