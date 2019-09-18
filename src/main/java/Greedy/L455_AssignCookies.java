package Greedy;

import static Utils.Helpers.log;

/*
* Assign Cookies
*
* - 给小朋友分饼干，每个小朋友最多分到一块。每个小朋友想要的饼干大小用”贪心指数“ g(i) 表示，每块饼干的大小用 s(i) 表示。
*   若小朋友 i 分到了饼干 j，且 s(j) >= g(i) 则小朋友会很高兴。给定数组 s 和 g，问如何分饼干能让最多的小朋友高兴。
* */

public class L455_AssignCookies {
    public static int findContentChildren(int[] g, int[] s) {
        return 0;
    }

    public static void main(String[] args) {
        log(findContentChildren(new int[]{1, 2, 3}, new int[]{1, 1}));  // expects 1. 最多让一个小朋友开心
        log(findContentChildren(new int[]{1, 2}, new int[]{1, 2, 3}));  // expects 2. 两个小朋友都能开心
    }
}
