package Greedy.S1_GreedyBasics;

import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Is Subsequence
 *
 * - Given a string s and a string t, check if s is subsequence of t.
 *
 * - Assumptions:
 *   1. There is only lower case English letters in both s and t.
 *   2. t is potentially a very long (length ~= 500,000) string, and s is a short string (<=100).
 *
 * - Follow up:
 *   If there are lots of incoming S, say S1, S2, ... , Sk where k >= 1B, and you want to check one by one to
 *   see if T has its subsequence. In this scenario, how would you change your code?
 * */

public class L392_IsSubsequence {
    /*
     * 解法1：Greedy（双指针）
     * TODO: 为什么双指针也算是 greedy？？？
     * - 时间复杂度 O(n)，空间复杂度 O(1)，其中 n = len(s)。
     * */
    public static boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;
        while (i < s.length() && j < t.length()) {
            if (s.charAt(i) == t.charAt(j)) i++;
            j++;
        }
        return i == s.length();
    }

    /*
     * 解法2：Greedy
     * - 思路：与解法1一致。
     * - 实现：用 indexOf() 方法代替解法1中的双指针。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。该解法统计性能比解法1高很多，原因是解法1 while 中要调用2次 charAt()，而本解法中的
     *   indexOf() 在 for 中只调用一次（charAt() 和 indexOf() 都是遍历搜索）。
     * */
    public static boolean isSubsequence2(String s, String t) {
        int fromIdx = 0;
        for (char sChar : s.toCharArray()) {
            int i = t.indexOf(sChar, fromIdx);
            if (i == -1) return false;
            fromIdx = i + 1;
        }
        return true;
    }

    /*
     * 解法3：解法1的 Recursion 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSubsequence3(String s, String t) {
        if (s.length() == 0) return true;

        for (int i = 0; i < t.length(); i++)
            if (s.charAt(0) == t.charAt(i))
                return isSubsequence3(s.substring(1), t.substring(i + 1));

        return false;  // 若 t 遍历完了还 return 说明 s 中有 t 中没有的字符 ∴ return false
    }

    /*
     * 解法4：解法2的 Recursion 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isSubsequence4(String s, String t) {
        if (s.length() == 0) return true;
        int idx = t.indexOf(s.charAt(0));
        return (idx != -1)
            ? isSubsequence(s.substring(1), t.substring(idx + 1))
            : false;
    }

    /*
     * 解法5：DP
     * - 思路：采用 L1143_LongestCommonSubsequence 的方法 —— len(若最长公共子串) = len(s)，则说明 s 是 t 的子串。
     * - 优化：可继续采用 L1143 中解法4、5的方式优化空间复杂度。
     * - 时间复杂度 O(n*m)，空间复杂度 O(n*m)。
     * */
    public static boolean isSubsequence5(String s, String t) {
        int ls = s.length(), lt = t.length();
        int[][] dp = new int[ls + 1][lt + 1];  // 要多开辟一个空间用于递推最后一个元素

        for (int i = ls - 1; i >= 0; i--)      // 从后往前递推
            for (int j = lt - 1; j >= 0; j--)
                dp[i][j] = s.charAt(i) == t.charAt(j)
                    ? 1 + dp[i + 1][j + 1]
                    : Math.max(dp[i + 1][j], dp[i][j + 1]);

        return dp[0][0] == ls;
    }

    /*
     * 解法6：Map + Binary Search
     * - 同时也是 Follow up 的解法。
     * - 思路：s 是 t 的子序列需要满足2个条件：
     *   1. s 中的字符都存在于 t 中；
     *   2. s 中的字符的相对顺序与他们在 t 中的相对顺序一致。
     *   - 对于条件1：为 t 构建一个字符字典，看 s 中的字符是否在字典中；
     *   - 对于条件2：在遍历 s 的过程中，查找并记录当前字符在 t 中的索引，若索引值单调增大则说明条件2满足。
     * - 实现：要同时实现以上这两点，可以：
     *   1. 先为 t 构建一个 Map<字符, List<索引>>（∵ 是投建过程是顺序遍历 t ∴ 索引列表是有序的）；
     *   2. 设置索引指针 i，遍历 s：
     *      - 对于 s[0]，若其在 map 中，则获取对应索引列表中的第0个索引+1后赋给 i（后面搜索出的索引都得 > i 才能说明单调递增）；
     *      - 对于 s[1..]，同样先检查是否在 map 中，若在则在其对应的索引列表中二分搜索 i，若得到的插入点在索引列表右边，说明在
     *        t[i..] 中找不到该字符，说明原问题无解，否则说明找得到，继续下一轮循环。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(m)。
     * */
    public static boolean isSubsequence6(String s, String t) {
        Map<Character, List<Integer>> map = new HashMap<>();  // 为 t 构建 {字符 -> 索引列表} 的字典
        for (int i = 0; i < t.length(); i++) {
            List<Integer> list = map.getOrDefault(t.charAt(i), new ArrayList<>());
            list.add(i);
            map.put(t.charAt(i), list);
        }

        int i = -1;                                     // 用于记录 s 中的字符在 t 中的索引，初始化为-1
        for (char sChar : s.toCharArray()) {            // 遍历 s 中的字符
            if (!map.containsKey(sChar)) return false;  // 检查条件1
            List<Integer> list = map.get(sChar);        // 若满足则拿到 sChar 对应的索引列表
            int j = binarySearch(list, i);              // 在索引列表中搜索以检查条件2（∵ 索引列表有序 ∴ 可二分搜索）
            if (j < 0) j = -(j + 1);                    // 若没找到则将 j 转换成插入点（insertion point）
            if (j == list.size()) return false;         // 若插入点在最右边，说明 i > list 中的最大值，即 s 中该字符的个数多于 t 中该字符的个数
            i = list.get(j) + 1;
        }

        return true;
    }

    private static int binarySearch(List<Integer> list, int el) {  // 在 list 中二分搜索 el，输入输出与 Collections.binarySearch() 一致
        int l = 0, r = list.size() - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (el < list.get(mid)) r = mid - 1;
            else if (el > list.get(mid)) l = mid + 1;
            else return mid;
        }
        return -(l + 1);
    }

    /*
     * 解法7：Bucket 数组 + Binary Search
     * - 同时也是 Follow up 的解法。
     * - 思路：与解法6一致。
     * - 实现：与解法6的不同之处在于：
     *   1. 使用 bucket 形式的 List 数组代替解法6中的 Map；
     *   2. 使用内置的 Collections.binarySearch() 进行二分查找。
     * - 时/空间复杂度与解法6一致。
     * */
    public static boolean isSubsequence7(String s, String t) {
        List<Integer>[] buckets = new List[256];  // ASCII 中有256个字符（其实可以只开辟26的大小，但读写时需要减偏移量）
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (buckets[c] == null)
                buckets[c] = new ArrayList<>();
            buckets[c].add(i);
        }

        int i = 0;
        for (char c : s.toCharArray()) {
            List<Integer> list = buckets[c];
            if (list == null) return false;
            int j = Collections.binarySearch(list, i);
            if (j < 0) j = -(j + 1);
            if (j == list.size()) return false;
            i = list.get(j) + 1;
        }

        return true;
    }

    public static void main(String[] args) {
        log(isSubsequence5("abc", "ahbgdc"));   // expects true
        log(isSubsequence5("acc", "baaxcc"));   // expects true
        log(isSubsequence5("axc", "abcd"));     // expects false. (s 中存在 t 中没有的字符)
        log(isSubsequence5("aac", "abcd"));     // expects false. (s 中的字符存在于 t 中，但个数比 t 中多)
        log(isSubsequence5("abc", "axacxbb"));  // expects false. (s 与 t 字符相对顺序不匹配)
    }
}
