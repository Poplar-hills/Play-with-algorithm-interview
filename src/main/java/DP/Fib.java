package DP;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

public class Fib {
    /*
     * 解法1：自上而下的 naive 方式
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static int fib(int n) {
        if (n < 2) return n;
        return fib(n - 1) + fib(n - 2);
    }

    /*
     * 解法1：自上而下，应用缓存（或叫 memory search）
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
     * 解法3：自下而上的 DP 方式
     * -
     * */
    public static int fib3(int n) {
        Map<Integer, Integer> cache = new HashMap<>();
        if (n == 0) cache.put(0, 0);
        if (n == 1) cache.put(1, 1);
        for (int i = 2; i <= n; i++)
            cache.put(i, cache.get(i - 1) + cache.get(i - 2));
        return cache.get(n);
    }

    public static void main(String[] args) {
        log(fib3(40));
    }
}
