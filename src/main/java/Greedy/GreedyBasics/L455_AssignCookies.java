package Greedy.GreedyBasics;

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

        int count = 0;
        int i = 0, j = 0;

        while (j < s.length && i < g.length) {  // 若饼干或小朋友用尽则 return
            if (s[j] >= g[i]) {  // 若能满足第 i 个小朋友则 count++、i++
                count++;
                i++;
            }
            j++;                 // 不管能不能满足都移到下一块饼干上
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

        int count = 0;
        int i = g.length - 1, j = s.length - 1;

        while (j >= 0 && i >= 0) {
            if (s[j] >= g[i]) {
                count++;
                j--;
            }
            i--;  // 不管能不能满足都往下走 ∵ 饼干也是从大到小排列的，若当前饼干满足不了则后面饼干都无法满足 ∴ 跳过该小朋友
        }

        return count;
    }

    /*
     * 解法3：TreeMap
     * - 思路：要让最多的小朋友开心，只需为每个小朋友从 s 中找到 ≥ 且最接近其贪心指数的饼干即可。要实现这个逻辑可以使用 TreeMap
     *   中的 ceilingKey() 方法。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(m)。
     * */
    public static int findContentChildren3(int[] g, int[] s) {
        TreeMap<Integer, Integer> tree = new TreeMap<>();  // ∵ 要用到 ceilingKey() 方法 ∴ 接口和实现都得是 TreeMap
        int count = 0;

        for (int size : s)                           // 将所有饼干添加进 tree 并累计个数，O(m)
            tree.put(size, tree.getOrDefault(size, 0) + 1);

        for (int greed : g) {                        // O(nlogn)
            Integer gSize = tree.ceilingKey(greed);  // 从 tree 上找 ≥ greed 的所有 size 中最小的那个，O(logn)
            if (gSize != null) {
                count++;
                tree.put(gSize, tree.get(gSize) - 1);
                if (tree.get(gSize) == 0)
                    tree.remove(gSize);
            }
        }

        return count;
    }

    public static void main(String[] args) {
        log(findContentChildren3(
            new int[]{1, 2, 3},
            new int[]{1, 1}));        // expects 1. 最多让一个小朋友开心

        log(findContentChildren3(
            new int[]{1, 2},
            new int[]{1, 2, 3}));     // expects 2. 两个小朋友都能开心

        log(findContentChildren3(
            new int[]{10, 9, 8, 7},
            new int[]{5, 6, 7, 8}));  // expects 2. 小朋友7、8能开心
    }
}
