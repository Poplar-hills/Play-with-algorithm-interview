package DP;

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
    * 解法1：Recursion + Memoization (也是一种 DFS)
    * - 思路：该题目与 L70 爬楼梯非常类似，都是在做某事时每次有2种选择，求一共有多少种不同的选择组合能最终做成该事。对本题来说：
    *   1. 在对一个字符串解码时，每次都有2种选择：解码1个字母 or 解码2个字母；
    *   2. 全局解 = 局部解之和，例如：num("213") = num("13") + num("3") = 2 + 1 = 3；
    *   3. 两个特例：让 num("") = 1，num("0...") = 0，则整个分解过程就可以以递归的方式进行：
    *             num("102213")                              5
    *              ↙         ↘                             ↗   ↖
    *      num("02213")   num("2213")                    0       5
    *                     ↙        ↘                           ↗   ↖
    *              num("213")  →  num("13")                  3   ←   2
    *                    ↘        ↙       ↘                    ↖   ↗   ↖
    *                     num("3")      num("")                  1       1
    *                        ↓                                   ↑
    *                     num("")                                1
    *   4. ∵ 分解过程中有多个重叠子问题 ∴ 可以采用 memoization 优化。需要记忆的内容是 {索引: num}。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static int numDecodings(String s) {
        if (s == null || s.length() == 0) return 0;
        return dfs(s, 0, new HashMap<>());  // 第2个参数是指向待解码的字符，从第0个字符开始
    }

    public static int dfs(String s, int i, HashMap<Integer, Integer> map) {
        if (i == s.length()) return 1;      // num("") 的情况
        if (s.charAt(i) == '0') return 0;   // num("0...") 的情况
        if (map.containsKey(i)) return map.get(i);

        int res = dfs(s, i + 1, map);
        if (i + 1 < s.length() && Integer.parseInt(s.substring(i, i + 2)) < 27)
            res += dfs(s, i + 2, map);

        map.put(i, res);       // 全局解 = 局部解之和: num(s, i) = num(s, i+1) + num(s, i+2)
        return res;
    }

    /*
    * 解法2：DP
    *
    *
    * */
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        int[] cache = new int[n + 1];
        cache[n] = 1;

        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') cache[i] = 0;
            else {
                cache[i] = cache[i + 1];
                if (i + 1 < n && (s.charAt(i) == '1' || (s.charAt(i) == '2' && s.charAt(i + 1) < '7')))
                    cache[i] += cache[i + 2];
            }
        }

        return cache[0];
    }

    public static void main(String[] args) {
        log(numDecodings("12"));     // expects 2. "AB" (1 2) or "L" (12)
        log(numDecodings("227"));    // expects 2. "VG" (22 7), or "BBG" (2 2 7)
        log(numDecodings("226"));    // expects 3. "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6)
        log(numDecodings("102213")); // expects 5. ...
    }
}
