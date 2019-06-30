package DP;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;
import static Utils.Helpers.timeIt;

/*
* 通过求 Fibonacci Sequence 中的第 n 个数来理解 DP 的理念。
*
* - （看完解法1、2、3之后再看这个）总结：
*   - DP 定义: Dynamic programming is a method for solving a complex problem by breaking it down into simpler
*     subproblems, solving each of those subproblems just once, and storing their solutions – ideally, in an
*     memory-based data structure.
*   - DP vs. 递归问题，
* */

public class Fibonacci {
    /*
     * 解法1：自上而下（top-down）的 naive recursion 方式
     * - 时间复杂度 O(2^n)，因为每次调用方法都会产生2个分支再调用该方法，一共递归 n 次，所以总调用次数是 2^n；
     * - 空间复杂度 O(n)。
     * */
    public static int fib1(int n) {
        if (n < 2) return n;
        return fib1(n - 1) + fib1(n - 2);
    }

    /*
     * 解法1：自上而下（top-down）的 recursion + memoization（记忆化搜索，或叫缓存）
     * - 时间复杂度 O(n)，因为每次调用该方法时 fib(n-1) 和 fib(n-2) 已经被计算过了，不需要重复计算，因此该方法总调用次数为 n 次；
     * - 空间复杂度 O(n)。
     * */
    public static int fib2(int n) {
        return fib2(n, new HashMap<>());
    }

    private static int fib2(int n, Map<Integer, Integer> cache) {
        if (n < 2) return n;
        if (!cache.containsKey(n))
            cache.put(n, fib2(n - 1, cache) + fib2(n - 2, cache));
        return cache.get(n);
    }

    /*
     * 解法3：自下而上（bottom-up）方式
     * - 思路：先找到 n 为最小值时的解，再层层递推出 n 为大值时的解（这个过程就是 DP）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。相比解法2，该解法在时间、空间效率上的统计效率都更高，因为：
     *   1. 没有递归，所以没有系统栈空间的消耗；
     *   2. 自下而上求解，使得 cache 中的每一项都只被访问1次（解法2中会被访问多次）。
     * */
    public static int fib3(int n) {
        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(0, 0);
        cache.put(1, 1);
        for (int i = 2; i <= n; i++)
            cache.put(i, cache.get(i - 1) + cache.get(i - 2));
        return cache.get(n);
    }

    public static void main(String[] args) {
        log(fib1(40));
        log(fib2(40));
        log(fib3(40));

        timeIt(40, Fibonacci::fib1);
        timeIt(40, Fibonacci::fib2);
        timeIt(40, Fibonacci::fib3);
    }
}
