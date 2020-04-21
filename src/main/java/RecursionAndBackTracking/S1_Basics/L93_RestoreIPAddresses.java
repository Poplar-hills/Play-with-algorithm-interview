package RecursionAndBackTracking.S1_Basics;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Restore IP Addresses
 *
 * - Given a string containing only digits, restore it by returning all possible valid IP address combinations.
 * */

public class L93_RestoreIPAddresses {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：该题是一个组合问题 ∴ 可转化为树形问题求解，具体来说可采用类似 L17 解法1的回溯法。设置指针 i 在 s 上滑动 ∵ 每次
     *   滑动可以有3种不同情况（滑动1~3位）∴ 分别对这3种情况进行分支，递归下去检查是否能得到有效 ip：
     *                                  ""
     *                    /             |            \
     *              "1"                "12"            "123"
     *           /   |   \            /    \          /    \
     *      "1.2" "1.23" "1.234"  "12.3" "12.34"  "123.4" "123.45"
     *       / \    / \     |       / \     |       |        |
     *       ...    ...    ...      ...    ...     ...       ×
     * - 实现：
     *   1. 其中通过判断 ip 的有效性来为回溯进行剪枝（Pruning）：
     *     - 若一个 ip 已有4个 component，但 i 还未到达 s 末尾（tooMuchDigits 情况），则该 ip 无效；
     *     - 若一个 ip 中的 component 个数 < 3，但 i 已到达 s 末尾（notEnoughDigits 情况），则该 ip 无效；
     *   2. 还要判断 ip component 的有效性：
     *     - 若一个 component 位数 > 1 但以"0"开头，则该 component 无效；
     *     - 若一个 component 大于255，则该 component 无效。
     * - 时间复杂度 O(2^n)：一个长度为 n 的字符串有 n-1 个间隔，而每个间隔上都有2种选择：切分或不切分 ∴ 该字符串共有 2^(n-1)
     *   种切分方式，即需要 2^(n-1) 次递归 ∴ 总体是 O(2^n) 量级的复杂度。
     * - 空间复杂度 O(n)。
     * */
    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() == 0) return res;
        backtrack(s, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(String s, int i, List<String> ipList, List<String> res) {
        if (ipList.size() == 4 && i == s.length()) {  // Found solution
            res.add(String.join(".", ipList));
            return;
        }
        boolean notEnoughDigits = ipList.size() < 4 && i == s.length();  // 通过验证 ip 有效性进行剪枝
        boolean tooManyDigits = ipList.size() == 4 && i < s.length();
        if (notEnoughDigits || tooManyDigits) return;

        for (int j = 0; j < 3; j++) {                 // 尝试生成 ip component（最多3位 ∴ j ∈ [0,3)）
            if (i + j >= s.length()) break;           // 注意不要越界
            String comp = s.substring(i, i + j + 1);
            if (isValidIpComp(comp)) {                // 验证 ip component 的有效性
                ipList.add(comp);
                backtrack(s, i + j + 1, ipList, res);
                ipList.remove(ipList.size() - 1);
            }
        }
    }

    private static boolean isValidIpComp(String comp) {
        boolean startWith0 = comp.length() > 1 && comp.startsWith("0");
        return !startWith0 && Integer.parseInt(comp) <= 255;
    }

    /*
     * 解法2：Recursion + Backtracking
     * - 思路：与解法1一致。
     * - 实现：
     * - 时间复杂度 O(2^n)，空间复杂度 O(n)。
     * */
    public static List<String> restoreIpAddresses2(String s) {
        List<String> res = new ArrayList<>();
        backtrack2(s, 0, "", 0, res);
        return res;
    }

    private static void backtrack2(String s, int i, String restoredIp, int count, List<String> res) {
        if (count > 4) return;
        if (count == 4 && i == s.length()) res.add(restoredIp);

        for (int n = 1; n <= 3; n++) {
            if (i + n > s.length()) break;

            String comp = s.substring(i, i + n);
            if ((comp.startsWith("0") && comp.length() > 1) || Integer.parseInt(comp) > 255) continue;

            String newIp = restoredIp + comp + (count == 3 ? "" : ".");
            backtrack2(s, i + n, newIp, count + 1, res);
        }
    }

    /*
     * 解法3：Iteration
     * - 思路：∵ IP 地址只有四段 ∴ 可以使用三重循环遍历所有将 s 分割成四段的可能，若每段都是 valid 的，则整个 IP 地址 valid：
     *        "1  2  3  4  5  6"
     *            i  j  k           - "1.2.3.456" - s4 is invalid
     *            i  j     k        - "1.2.34.56" - valid
     *            i  j        k     - "1.2.345.6" - s3 is invalid
     *            i     j  k        - "1.23.4.56" - valid
     *            i     j     k     - "1.23.45.6" - valid
     *            i        j  k     - "1.234.5.6" - valid
     *               i  j  k        - "12.3.4.56" - valid
     *               i  j     k     - "12.3.45.6" - valid
     *               i     j  k     - "12.34.5.6" - valid
     *                  i  j  k     - "123.4.5.6" - valid
     *
     * - 时间复杂度 O(n^3)，空间复杂度 O(1)，其中 n 为 s 的长度（注：O(2^n) 只是量级，并不精确）。
     * */
    public static List<String> restoreIpAddresses3(String s) {
        List<String> res = new ArrayList<String>();
        int len = s.length();

        for (int i = 1; i < 4 && i < len - 2; i++) {  // i、j、k 的活动范围都要 <4，即每段地址最多3位；同时 i < len-2，给 j、k 留出余地
            for (int j = i + 1; j < i + 4 && j < len - 1; j++) {    // 同样 j < len-1，即 k 留出余地
                for (int k = j + 1; k < j + 4 && k < len; k++) {    // k 是最内层循环 ∴ 可以指向最后一个元素，只要 < len 即可
                    String s1 = s.substring(0, i), s2 = s.substring(i, j);
                    String s3 = s.substring(j, k), s4 = s.substring(k, len);
                    if (isValid3(s1) && isValid3(s2) && isValid3(s3) && isValid3(s4))
                        res.add(String.join(".", s1, s2, s3, s4));  // String.join 的第2个参数即可以是一个数组，也可以是多个字符串
                }
            }
        }
        return res;
    }

    private static boolean isValid3(String comp) {
        return !(comp.length() > 3
            || (comp.length() > 1 && comp.startsWith("0"))
            || Integer.parseInt(comp) > 255);
    }

    public static void main(String[] args) {
        log(restoreIpAddresses("25525511135"));  // expects ["255.255.11.135", "255.255.111.35"]
        log(restoreIpAddresses("123456789"));    // expects ["123.45.67.89"]
        log(restoreIpAddresses("12345"));        // expects ["1.2.3.45", "1.2.34.5", "1.23.4.5", "12.3.4.5"]
        log(restoreIpAddresses("02095"));        // expects ["0.2.0.95", "0.20.9.5"]
    }
}
