package Greedy;

import static Utils.Helpers.log;

import java.util.Arrays;
import java.util.Collections;

/*
* Assign Cookies
*
* - 给小朋友分饼干，每个小朋友最多分到一块。每个小朋友想要的饼干大小用”贪心指数“ g(i) 表示，每块饼干的大小用 s(i) 表示。
*   若小朋友 i 分到了饼干 j，且 s(j) >= g(i) 则小朋友会很高兴。给定数组 s 和 g，问如何分饼干能让最多的小朋友高兴。
*
* - 贪心算法的题一般比较简单，因为代码会比较短。但难点在于判断一个问题是否可以使用贪心算法。
* */

public class L455_AssignCookies {
    /*
     * 解法1：Greedy
     * - 思路：Serve the least greedy children first.
     * - 时间复杂度 O(max(nlogn, mlogm))，空间复杂度 O(1)，其中 n=len(g), m=len(s)。
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
     * - 时间复杂度 O(max(nlogn, mlogm))，空间复杂度 O(1)，其中 n=len(g), m=len(s)。
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

    public static void main(String[] args) {
        log(findContentChildren(
            new int[]{1, 2, 3},
            new int[]{1, 1}));        // expects 1. 最多让一个小朋友开心

        log(findContentChildren(
            new int[]{1, 2},
            new int[]{1, 2, 3}));     // expects 2. 两个小朋友都能开心

        log(findContentChildren(
            new int[]{10, 9, 8, 7},
            new int[]{5, 6, 7, 8}));  // expects 2. 小朋友7、8能开心
    }
}
