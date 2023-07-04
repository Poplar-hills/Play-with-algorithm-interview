package DP.S1_Basics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

import static Utils.Helpers.log;
import static Utils.Helpers.timeIt;

/*
 * Fibonacci Number
 *
 * - The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such that
 *   each number is the sum of the two preceding ones, starting from 0 and 1. That is,
 *       F(0) = 0,  F(1) = 1,  F(N) = F(N-1) + F(N-2), for N > 1.
 *   Given N, calculate F(N).
 *
 * - DP 定义：Dynamic programming is a method for solving a complex problem by breaking it down into simpler
 *   sub-problems, solving each of those sub-problems just once, and storing their solutions – ideally, in a
 *   memory-based data structure.
 *
 * ️- 看完本题后再看 L279_PerfectSquares、L64_MinimumPathSum，它们都采用了2种 DP 解法，是非常好的例子。
 * */

public class L509_FibonacciNumber {
    /*
     * 超时解：top-down naive recursion 方式
     * - 时间复杂度 O(2^n) ∵ 每次调用方法都会产生2个调用该方法的分支，递归 n 次 ∴ 总调用次数是 2^n；
     * - 空间复杂度 O(n)。
     * */
    public static int fib(int n) {
        if (n < 2) return n;
        return fib(n - 1) + fib(n - 2);
    }

    /*
     * 解法1：Recursion + Memoization（记忆化搜索）
     * - 思路：自上而下，层层递归分解，并在过程中缓存每个子问题的解。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int fib1(int n) {
        int[] cache = new int[n + 1];
        return fib1(n, cache);
    }

    private static int fib1(int n, int[] cache) {
        if (n < 2) return n;
        if (cache[n] != 0) return cache[n];
        return cache[n] = fib1(n - 1, cache) + fib1(n - 2, cache);
    }

    /*
     * 解法2：解法1的 Map 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
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
     * 解法3：DP
     * - 思路：自下而上，先解决最基本问题，再从最基本问题层层递推出 n 为更大值时的解。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。相比解法1、2，该解法在时间、空间效率上的统计效率都更高，因为：
     *   1. 没有递归，所以没有系统栈空间的消耗；
     *   2. 自下而上求解，使得 cache 中的每一项都只被访问1次（解法1、2中会计算一次但被访问多次）。
     * */
    public static int fib3(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++)
            dp[i] = dp[i - 1] + dp[i - 2];
        return dp[n];
    }

    /*
     * 解法4：解法3的 Map 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int fib4(int n) {
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 0);
        dp.put(1, 1);
        for (int i = 2; i <= n; i++)  // dp 中放入 fib(0), fib(1) 后再从小到大逐个计算更大的 n 值
            dp.put(i, dp.get(i - 1) + dp.get(i - 2));
        return dp.get(n);
    }

    /*
     * 解法5：DP（解法3的空间优化版）
     * - 思路：与解法3一致。
     * - 实现：从递推表达式和代码都可以看出，f(n) 的解只取决于 f(n-1)、f(n-2) 的解，即当前状态只和之前的两个状态有关 ∴ 并不
     *   需要维护解法3中的整个 dp 数组，而是只需记录之前的两个状态即可。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int fib5(int n) {
        if (n <= 1) return n;
        int prev2 = 0, prev1 = 1;  // prev2、prev1 分别初始化为 f(0)、f(1) 的解

        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }

    /*
     * 解法6：fork/join 多线程
     * - 思路：对于每个需要计算的数字都创建一个线程单独执行。
     * - 💎 实现：
     *   1. fork/join 框架的核心类是 ForkJoinPool（线程和任务管理）、ForkJoinTask（实现 fork/join 操作）这2个类，
     *      但在实际开发中（尤其是通过递归进行任务拆分/合并计算），通常用 ForkJoinTask 的子类 RecursiveTask（有返回结果）、
     *      RecursiveAction（无返回结果）来替代 ForkJoinTask。
     *   2. Cache 基于 ConcurrentHashMap 实现。
     *   - SEE: https://juejin.cn/post/6992178673730191397
     * - 时间复杂度：使用多线程的本意是为了将大型任务 divide and rule，并行计算多个子任务。但该解法中由于计算量太小、创建的线程
     *   数又多 ∴ 耗时反而更长，且有 OOM 的风险。
     * */
    private static class FibTask extends RecursiveTask<Integer> {  // 继承 RecursiveTask
        private final int n;
        private final Map<Integer, Integer> cache;

        public FibTask(int n) {
            this.n = n;
            this.cache = new ConcurrentHashMap<>();
        }

        @Override
        public Integer compute() {  // RecursiveTask 接口方法
            if (n < 2) return n;
            if (cache.containsKey(n)) return cache.get(n);
            FibTask f1 = new FibTask(n - 1);
            f1.fork();              // 分支出一个线程计算任务 f1
            FibTask f2 = new FibTask(n - 2);
            int res = f2.compute() + f1.join();  // 主线程计算任务 f2，等待 f1 的结果，并加到一起返回
            cache.put(n, res);
            return res;
        }
    }

    public static void main(String[] args) {
        log(fib(40));
        log(fib1(40));
        log(fib2(40));
        log(fib3(40));
        log(fib4(40));
        log(fib5(40));
        log(new FibTask(40).compute());

        timeIt(40, L509_FibonacciNumber::fib);
        timeIt(40, L509_FibonacciNumber::fib1);
        timeIt(40, L509_FibonacciNumber::fib2);
        timeIt(40, L509_FibonacciNumber::fib3);  // 第二快
        timeIt(40, L509_FibonacciNumber::fib4);
        timeIt(40, L509_FibonacciNumber::fib5);  // 最快
        timeIt(40, (n) -> new FibTask(n).compute());  // 最慢（慢了好几个数量级 ∵ 任务太小，而线程创建和切换成本高）
    }
}
