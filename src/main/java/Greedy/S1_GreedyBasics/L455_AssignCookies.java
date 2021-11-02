package Greedy.S1_GreedyBasics;

import static Utils.Helpers.log;

import java.util.Arrays;
import java.util.TreeMap;

/*
 * Assign Cookies
 *
 * - 给小朋友分饼干，每个小朋友最多分到一块。每个小朋友想要的饼干大小用“贪心指数” g(i) 表示，每块饼干的大小用 s(i) 表示。
 *   若小朋友 i 分到了饼干 j，且 s(j) >= g(i) 则小朋友会很高兴。给定数组 s 和 g，问如何分饼干能让最多的小朋友高兴。
 *
 * - 贪心算法的题一般比较简单，因为代码会比较短。但难点在于判断一个问题是否可以使用贪心算法。
 * - 💎 动态规划 vs. 贪心算法：
 *   - 两者的区别还是很明显的。贪心是使用一个单一的方法前进，而动态规划则是尝试所有的可能，只不过中间使用“表格”进行记忆
 *     （所以要满足重叠子问题和最优子结构的性质）。背包问题是最好的例子。每次都放剩下的物品中单位价值最高的物品，这种策略
 *     是贪心（单一的策略前进）；而动态规划其实每次尝试了拿去所有的物品。另一个例子是 Dijkstra vs. Bellman-Ford 算法，
 *     见 L787_CheapestFlightsWithinKStops。
 *   - 没有准确的原则能用于判断什么时候应该使用贪心，什么时候用动态规划，什么时候只能回溯（需要培养对算法的深刻理解和在遇
 *     到具体问题时敏锐的直觉，这也是算法难的地方）。如果一定要的话，一个“应试”的方法是看时间复杂度要求。由于贪心算法的
 *     复杂度通常都会很低 O(n) 或 O(nlogn)，而动态规划算法通常都是 O(n^2) 的（但并不一定），所以，如果问题的时间复杂度
 *     要求是 O(n^2)级别的算法无法承受的话，通常可以想想是否可以贪心。
 *
 * - Follow Up:
 *   What if each child can get more than 1 cookies?
 *   If each child can get more than one cookie, this is only helpful for cases where some cookies are left
 *   over prior to the assignment. For example:
 *      G = {10, 15}
 *      S = {2, 3, 6, 7, 8, 10}
 *   If you assume that only 1 cookie can be assigned to each child, then only one child can be satisfied
 *   (this is, 10) and there will be 5 cookies left over. If each child can get more than one cookie, then
 *   you would keep a running sum of S[i...j] until S[j] is greater than G[k]. In this example, you will
 *   assign 10 = 2 + 3 + 6 and 15 = 7 + 8.
 * */

public class L455_AssignCookies {
    /*
     * 解法1：Greedy
     * - 思路：Serve the least greedy children first.
     * - 时间复杂度 O(max(nlogn, mlogm))，空间复杂度 O(1)，其中 n = len(g), m = len(s)。
     * */
    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);

        int i = 0, j = 0, count = 0;
        while (i < g.length && j < s.length) {  // 若饼干或小朋友任一用尽则 return count
            if (s[j] >= g[i]) {  // 若能满足第 i 个小朋友则 count++、i++
                count++;
                i++;
            }
            j++;  // ∵ 小朋友的贪心指数是升序排列，若 s[j] 满足不了该小朋友则一定也无法满足后面的小朋友 ∴ 跳过该饼干
        }

        return count;
    }

    /*
     * 解法2：Greedy
     * - 思路：Serve the most greedy children first.
     * - 时间复杂度 O(max(nlogn, mlogm))，空间复杂度 O(1)，其中 n = len(g), m = len(s)。
     * */
    public static int findContentChildren2(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);  // Arrays.sort() 无法降序排列，只能升序排列后从后往前遍历

        int i = g.length - 1, j = s.length - 1, count = 0;
        while (j >= 0 && i >= 0) {
            if (s[j] >= g[i]) {
                count++;
                j--;
            }
            i--;  // ∵ 饼干是降序排列，若 s[j] 满足不了该小朋友则后面饼干一定也无法满足 ∴ 跳过该小朋友
        }

        return count;
    }

    /*
     * 解法3：TreeMap
     * - 思路：要让最多的小朋友开心，只需为每个小朋友从饼干中找到 ≥ 且最接近其贪心指数的饼干（即 >= g[i] 的最小 s[j]），这可以
     *   联想到 BST 上的 ceiling 操作（在树上查找大于某个值的最小节点，即 sTree.ceiling(g[i])，而 ∵ 可能有多块 size 相同的
     *   饼干 ∴ 需要记录不同 size 的饼干的个数 ∴ 需采用 TreeMap 为饼干构建 frequency map。
     * - 语法：TreeMap 上的是 .ceilingKey() 方法；TreeSet 上的是 .ceiling() 方法（SEE: L220_ContainsDuplicateIII）。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(m)，其中 n = len(g), m = len(s)。
     * */
    public static int findContentChildren3(int[] g, int[] s) {
        TreeMap<Integer, Integer> freqTree = new TreeMap<>();  // ∵ 要用到 .ceilingKey() 方法 ∴ 接口和实现都得是 TreeMap
        int count = 0;

        for (int size : s)     // 为饼干数组用 TreeMap 构建 frequency map，O(mlogm)
            freqTree.put(size, freqTree.getOrDefault(size, 0) + 1);

        for (int greed : g) {  // O(nlogn)
            Integer sCeilCount = freqTree.ceilingKey(greed);  // 从 freqTree 上找 ≥ greed 的所有 size 中最小的那个，O(logn)
            if (sCeilCount != null) {                         // 注意 .ceilingKey() 返回类型是 Integer ∵ 可能为 null
                count++;
                freqTree.put(sCeilCount, freqTree.get(sCeilCount) - 1);
                if (freqTree.get(sCeilCount) == 0)
                    freqTree.remove(sCeilCount);
            }
        }

        return count;
    }

    public static void main(String[] args) {
        log(findContentChildren(
            new int[]{1, 2, 3},
            new int[]{1, 1}));        // expects 1. 最多让一个小朋友开心

        log(findContentChildren(
            new int[]{1, 2},
            new int[]{1, 2, 3}));     // expects 2. 两个小朋友都能开心

        log(findContentChildren(
            new int[]{10, 9, 8, 7},
            new int[]{5, 6, 7, 8}));  // expects 2. 小朋友8、7能开心
    }
}
