package RecursionAndBackTracking.S1_Basics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.Helpers.log;

/*
 * Palindrome Partitioning
 *
 * - Given a string s, partition s such that every substring of the partition is a palindrome. Return all
 *   possible palindrome partitioning of s (找出能组成 s 的所有回文子串列表).
 * */

public class L131_PalindromePartitioning {
    /*
     * 解法1：Backtracking
     * - 思路：该题是一个组合问题 ∴ 可以转化为树形问题，采用回溯进行搜索。例如对于 s="aabb" 来说：
     *                                     []
     *                   a/           aa/    aab\   aabb\
     *                  [a]          [aa]       x       x
     *            a/  ab| abb\     b/   bb\
     *          [a,a]   x    x  [aa,b] [aa,bb]               - 得到解 [aa,bb]
     *         b/  bb\            b|
     *    [a,a,b] [a,a,bb]     [aa,b,b]                      - 得到解 [a,a,bb]、[aa,b,b]
     *      b|
     *   [a,a,b,b]                                           - 得到解 [a,a,b,b]
     *
     * - 时间复杂度 O(n * n^2)：长度为 n 的字符串有 n-1 个间隔，每个间隔上都有切或不切2种选择 ∴ 共有 2^(n-1) 种切分方式，
     *   ∴ 是 O(2^n) 量级的复杂度。而每次递归需执行 O(n) 的 isPalindrome() ∴ 总复杂度为 O(n * 2^n)。
     * - 空间复杂度 O(n)：从树中最左侧的分支可见，最差情况下每个字符都要单独切分出来，即 n-1 个间隔都做切分 ∴ O(n)。
     * */
    public static List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;
        backtrack(s, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(String s, int i, List<String> list, List<List<String>> res) {
        if (i == s.length()) {
            res.add(new ArrayList<>(list));  // ∵ list 在回溯过程中是复用的 ∴ 只需在最后将 list 复制进 res 中即可
            return;
        }
        for (int j = i; j < s.length(); j++) {  // j 的语义是截取 s 时的右边界 s[i..j]
            String sub = s.substring(i, j + 1);
            if (isPalindrome(sub)) {
                list.add(sub);
                backtrack(s, j + 1, list, res);  // 下一层的递归起点为 j+1
                list.remove(list.size() - 1);  // 恢复上一层中 list 的状态，以便继续回溯查找
            }
        }
    }

    private static boolean isPalindrome(String str) {
        for (int i = 0; i < str.length() / 2; i++)
            if (str.charAt(i) != str.charAt(str.length() - 1 - i))
                return false;
        return true;
    }

    /**
     * 解法2：Backtracking + Memoization (🥇最优解)
     * - 思路：观察解法1中的树：
     *                                     []
     *                   a/           aa/    aab\   aabb\
     *                  [a]          [aa]       x       x   - 得到 [aa] 之后，要对剩下的"bb"进行切分
     *            a/  ab| abb\     b/   bb\
     *          [a,a]   x    x  [aa,b] [aa,bb]              - 得到 [a,a] 之后，要对剩下的"bb"进行切分
     *         b/  bb\            b|
     *    [a,a,b] [a,a,bb]     [aa,b,b]
     *      b|
     *   [a,a,b,b]
     *
     *   可见，对"bb"的切分出现了2次，且结果都是 ["b","b"]、["bb"]。而且在切分出结果之后又都要执行 isPalindrome()。
     *   ∴ 可以采用 memoization 的思路进行优化，避免重复计算。
     * - 实现：
     *   1. 最简单的方式是只给 isPalindrome() 方法添加 memoization：每次进入方法时检查参数 comp 是否已存在与 Map 当中，
     *      若存在则直接返回结果（true/false）；
     *   2. 更彻底的方式是给切分添加 memoization：缓存对子串 s[i..n) 切分的结果，但由于 backtracking 中每层递归并不返回结果，
     *      而只是往结果集中 add 解 ∴ 无法实现 memoization。
     *   ∴ 只能采用方式1 —— 给 isPalindrome() 方法添加 memoization。
     * - 时间复杂度 O(n * 2^n)：分析同上；
     * - 空间复杂度 O(2^n)：memoization Map 中最多会存储 2^n 个子串（可理解为用2层 for(i: 0->n) + for(j: i+1->n) 获得
     *   的所有子串）。
     */
    public static List<List<String>> partition2(String s) {
        List<List<String>> res = new ArrayList<>();
        if (s == null || s.isEmpty()) return res;
        backtrack2(s, 0, new ArrayList<>(), res, new HashMap<>());
        return res;
    }

    private static void backtrack2(String s, int i, List<String> list, List<List<String>> res,
                                   Map<String, Boolean> map) {
        if (i == s.length()) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < s.length(); j++) {
            String sub = s.substring(i, j + 1);
            if (isPalindrome2(sub, map)) {
                list.add(sub);
                backtrack2(s, j + 1, list, res, map);
                list.remove(list.size() - 1);
            }
        }
    }

    private static boolean isPalindrome2(String comp, Map<String, Boolean> map) {
        if (map.containsKey(comp)) return map.get(comp);
        char[] arr = comp.toCharArray();  // 解法1中的 charAt(i) 底层要遍历才能找到第 i 个字符 ∴ 这里 toCharArray 能提高统计性能
        for (int l = 0, r = arr.length - 1; l < r; l++, r--) {
            if (arr[l] != arr[r]) {
                map.put(comp, false);
                return false;
            }
        }
        map.put(comp, true);
        return true;
    }

    /*
     * 解法4：Double DP // TODO: 复习完 dp 之后重做
     * - 思路：观察解法1中的树，可见：
     *   1. 对于"bb"的切分出现了两次，结果都是 ["b","b"]、["bb"] ∴ 可以采用 dp 的思路进行优化，避免重复计算；
     *   2. 相同的切分也都要执行 isPalindrome() ∴ 同样可以使用 dp 的思路进行优化，避免重复检查。
     * - 实现：
     *   1. dp[r] 表示 s[0..r) 上的切分结果（即 s 中前 r 个字符上的解），其状态转移方程为：// TODO: ????
     *          0 -      - []
     *          1 - a    - [a]
     *          2 - aa   - [[a,a], [aa]]
     *          3 - aab  - [[a,a,b], [aa,b], [a,ab], [aab]]
     *          4 - aabb - [[a,a,b,b], [a,a,bb], [aa,b,b], [aa,bb]]
     *   2. palChecks[l][r] 表示 s[l..r] 是否是 palindrome（通过 palChecks[l+1][r-1] 递推出来），其状态转移方程为：
     *      g(l, r) = (s[l] == s[r]) && (r-l<=1 || g(l+1, r-1))（例："aa" -> "baab"）。
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<String>> partition4(String s) {
        if (s == null || s.isEmpty()) return new ArrayList<>();
        int len = s.length();
		List<List<String>>[] dp = new List[len + 1];     // dp[r] 记录 s[0..r) 上的解（注意创建列表数组的语法）
        boolean[][] palChecks = new boolean[len][len];   // palChecks[l][r] 记录 s[l..r] 是否是一个回文串

		dp[0] = new ArrayList<>();
		dp[0].add(new ArrayList<>());

		for (int r = 0; r < s.length(); r++) {   // r 为子串的右边界
			dp[r + 1] = new ArrayList<>();       // 初始化 dp[r+1] 处的列表（即 s[0..r] 上的解列表）
			for (int l = 0; l <= r; l++) {       // l 为子串的左边界
				if ((s.charAt(l) == s.charAt(r)) && (r - l <= 1 || palChecks[l + 1][r - 1])) { // 递推 s[l..r] 是否是回文串
					palChecks[l][r] = true;
					String comp = s.substring(l, r + 1);  // 获得回文串 s[l..r]
					for (List<String> list : dp[l]) {    // dp[l] 中每个列表都是 s[0..l) 上的一个解
						List<String> newList = new ArrayList<>(list);
						newList.add(comp);
						dp[r + 1].add(newList);  // 复制 s[0..l) 上的每一个解，追加上面获得的回文子串后再放入 dp[r+1] 中
					}
				}
			}
		}
		return dp[len];  // ∵ dp[r] 记录的是 s 的前 r 个字符 s[0..r) 上的解 ∴ 这里返回的是 dp[len]
    }

    public static void main(String[] args) {
        log(partition2("aab"));   // expects [["aa","b"], ["a","a","b"]]
        log(partition2("aabb"));  // expects [["a","a","b","b"], ["a","a","bb"], ["aa","b","b"], ["aa","bb"]]
        log(partition2("zz"));    // expects [["z","z"], ["zz"]]
        log(partition2("zzz"));   // expects [["z","z","z"], ["z","zz"], ["zz","z"], ["zzz"]]
        log(partition2(""));      // expects []
    }
}
