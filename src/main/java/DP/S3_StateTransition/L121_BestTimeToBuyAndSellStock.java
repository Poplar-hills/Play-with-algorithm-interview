package DP.S3_StateTransition;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - If you were only permitted to complete at most 1 transaction (i.e., buy one and sell one share of the stock)
 *
 * - Note that you cannot sell a stock before you buy one.
 * */

public class L121_BestTimeToBuyAndSellStock {
    /*
     * 解法1：Brute Force
     * - 思路：遍历比较。
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int maxProfit = 0;

        for (int b = 0; b < prices.length; b++)          // 固定 buy 的时机
            for (int s = b + 1; s < prices.length; s++)  // 尝试不同的 sell 时机
                if (prices[s] > prices[b])
                    maxProfit = Math.max(maxProfit, prices[s] - prices[b]);

        return maxProfit;
    }

    /*
     * 解法2：DP（two-pass）
     * - 💎 思路：首先定义好子问题：f(i) 表示“在 [i,n) 范围内交易所能得到的最大收益”。之后从最基本问题（最后一个问题）开始，
     *   思考如何能从后一个子问题的解递推出前一个子问题的解。例如：
     *        [7,  1,  5,  3,  6,  4]
     *                           f(5) = 0；
     *                        f(4) = 0；
     *                    f(3) = max(6-3, 4-3) = 3；
     *   此时思考如何从 f(3) 递推出 f(2)，即如何通过 [3..n) 内的最大收益得到 [2..n) 内的最大收益 —— 这两个子问题的区别就是
     *   是否考虑 prices[2] ∴ 这是个典型的“选/不选”问题：
     *     - 若在 prices[2] 时买入：max profit = [3..n) 中的最大元素 - prices[2]；
     *     - 若不在 prices[2] 时买入：max profit = [3..n) 中的最大收益，即 f(3) 的解。
     *   ∴ 可得：f(2) = max(max(prices[3..n)) - prices[2], f(3))。
     *   进而可得递推表达式：f(i) = max(max(prices[i+1..n)) - prices[i], f(i+1))。
     * - 实现：递推表达式中要求任意 [i..n) 内的最大值，这同样可以通过递推求得 —— 从最后往前依次递推出 [i..n) 内的最大值。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int n = prices.length;

        int[] maxPrices = new int[n];      // maxPrices[i] 表示 prices[i..n) 内的最大值
        maxPrices[n - 1] = prices[n - 1];  // 最基本问题

        for (int i = n - 2; i >= 0; i--)   // 从后往前递推
            maxPrices[i] = Math.max(prices[i], maxPrices[i + 1]);

        int[] dp = new int[n + 1];         // dp[i] 表示在 [i..n) 范围内交易所能得到的最大收益
        dp[n] = 0;                         // ∵ 要计算 f(n-1) 需要先知道 f(n) ∴ 设其为0

        for (int i = n - 1; i >= 0; i--)   // 从后往前递推
            dp[i] = Math.max(maxPrices[i] - prices[i], dp[i + 1]);

        return dp[0];
    }

    /**
     * 解法3：DP（解法2的简化版，one-pass）
     * - 思路：与解法2一致。
     * - 实现：无需单独构建 maxPrices 数组，而是在一次遍历中同时计算 max(prices[n+1..n)) 以及 maxProfit。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    public static int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int n = prices.length, prevMaxPrice = 0;
        int[] dp = new int[n + 1];
        dp[n - 1] = 0;

        for (int i = n - 2; i >= 0; i--) {
            prevMaxPrice = Math.max(prevMaxPrice, prices[i + 1]);
            dp[i] = Math.max(prevMaxPrice - prices[i], dp[i + 1]);
        }

        return dp[0];
    }

    /*
     * 解法4：Peak Valley
     * - 💎 思路：∵ 最多交易1次 ∴ 该问题可以转换为在整个区间内寻找极大值与极小值之差（注意必须极小值在前，极大值在后）
     *   ∴ 思路是在让指针 i 在数组上滑动的过程中：
     *   1. 不断寻找 [0,i] 内的 minPrice；
     *   2. 借助当前的 minPrice 计算 [0,i] 内的最大最小极值之差，并找到最大的差，即 maxProfit。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxProfit4(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int minPrice = prices[0], maxProfit = 0;

        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }

        return maxProfit;
    }

    /*
     * 解法5：DP
     * - 思路：与 L123_BestTimeToBuyAndSellStockIII 一致（注意该解法与👆解法2的问题定义不同）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit5(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int k = 1;
        int n = prices.length;

        int[][][] dp = new int[n][k+1][2];
        dp[0][0][1] = dp[0][1][1] = -prices[0];

        for (int d = 1; d < n; d++) {
            for (int t = 1; t < k + 1; t++) {
                dp[d][t][0] = Math.max(dp[d-1][t][0], dp[d-1][t][1] + prices[d]);
                dp[d][t][1] = Math.max(dp[d-1][t][1], dp[d-1][t-1][0] - prices[d]);
            }
        }

        return dp[n-1][k][0];
    }

    public static void main(String[] args) {
        log(maxProfit2(new int[]{7, 1, 5, 3, 6, 4}));  // expects 5. [-, buy, -, -, sell, -]
        log(maxProfit2(new int[]{7, 6, 4, 3, 1}));     // expects 0. no transaction.
    }
}