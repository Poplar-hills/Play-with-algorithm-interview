package RecursionAndBackTracking.S3_Combinations;

import static Utils.Helpers.*;

import java.util.*;

/*
 * Combination Sum
 *
 * - Given a set of unique candidate numbers and a target number, find all the unique combinations in the
 *   candidate where the candidates sums to the target. (注意不同于 L40，candidates 中的所有元素都是唯一的)
 *
 * - Notes:
 *   1. The same repeated number may be chosen from candidates unlimited number of times.
 *   2. All numbers (including target) will be positive integers.
 *   3. The solution set must not contain duplicate combinations.
 * */

public class L39_CombinationSum {
    /*
     * 解法1：Recursion + Backtracking + Set
     * - 思路：尝试用 nums 中的每一个元素对 target 进行递归分解，其中：
     *     1. 若 target 减去一个 num 之后等于0则找到一个解；
     *     2. candidate 必须 <= target 才可以相减。
     *                              8
     *               2/            3|            5\
     *               6              5              3
     *         2/   3|   5\    2/  3|  5\        2/ 3\
     *         4     3    1    3    2    0       1   0     - 找到 [3,5]、[5,3]
     *       2/ 3\ 2/ 3\     2/ 3\ 2|
     *       2   1 1   0     1   0  0                      - 找到 [2,3,3]、[3,2,3]、[3,3,2]
     *      2|
     *       0                                             - 找到 [2,2,2,2]
     *   但 ∵ 组合不关注顺序（如 [3,5] 和 [5,3] 等价）∴ 需对找到的解进行去重，在找到解时先对其中的元素进行排序，再用 Set 去重。
     * - 时间复杂度 O(n^n)，空间复杂度 O(???)。
     * */
    public static List<List<Integer>> combinationSum(int[] nums, int target) {
        Set<List<Integer>> set = new HashSet<>();
        if (nums == null || nums.length == 0) return new ArrayList<>();
        backtrack(nums, target, new ArrayList<>(), set);
        return new ArrayList<>(set);
    }

    private static void backtrack(int[] candidates, int target, List<Integer> list, Set<List<Integer>> set) {
        if (target == 0) {
            List<Integer> newList = new ArrayList<>(list);  // 先复制
            newList.sort((a, b) -> a - b);  // 再排序
            set.add(newList);               // 最后 Set 去重
            return;
        }
        for (int n : candidates) {
            if (target >= n) {               // Pruning
                list.add(n);
                backtrack(candidates, target - n, list, set);
                list.remove(list.size() - 1);
            }
        }
    }

    /*
     * 解法2：Recursion + Backtracking
     * - 思路：与解法1一致。
     * - 实现：解法1是在找到解之后再去重，而更优的做法是根本不产生重复解，即对树进行更进一步的剪枝以避免进入产生重复解的分支。
     *   ∴ 可让每个节点在遍历 nums 时不回头，只遍历 >= 当前节点的 nums ∴ 解法1中的树会被剪成这样：
     *                            8
     *               2/          3|          5\
     *               6            5            3    - 5节点不再考虑分支2，只考虑 >= 3 的分支；3节点不再考虑分支2、3，且5的分支无效 ∴ 无解
     *         2/   3|   5\    3/  5\
     *         4     3    1    2    0               - 3节点不再考虑分支2，只考虑 >= 3 的分支；2节点不在的考虑分支2；找到解 [3,5]
     *       2/ 3\  3|
     *       2   1   0                              - 找到解 [2,3,3]
     *      2|
     *       0                                      - 找到解 [2,2,2,2]
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum2(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        backtrack2(nums, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack2(int[] nums, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < nums.length; j++) {  // 在遍历 nums 时不回头，只遍历 [i..) 范围的
            if (target >= nums[j]) {
                list.add(nums[j]);
                backtrack2(nums, target - nums[j], j, list, res);  // 因此要在递归函数里多传一个索引 j
                list.remove(list.size() - 1);
            }
        }
    }

    /*
     * 解法3：Recursion + Backtracking + sort + 剪枝
     * - 思路：与解法1、2一致。
     * - 实现：解法2通过不回头的遍历 nums 进行了进一步剪枝，而该解法更进一步，再添加一种剪枝的情况，进一步提升算法效率：
     *   例如对于 target=3, nums=[2,3,5,6] 来说：
     *                   3
     *         2/    3|    5|   6\
     *        ...     √     ×    ×
     *   可见，当遍历到 n=3 时 target - n 就已经等于0了 ∴ 无需再遍历5、6了（即对5、6进行剪枝）。但这种剪枝方式依赖于
     *   nums 有序 ∴ 可以在回溯开始前对 nums 进行从小到大排序。
     * - 时间复杂度 << O(n^n)，空间复杂度 O(target)。
     * */
    public static List<List<Integer>> combinationSum3(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) return res;
        Arrays.sort(nums);
        backtrack3(nums, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack3(int[] nums, int target, int i, List<Integer> list, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int j = i; j < nums.length && nums[j] <= target; j++) {  // 若 n > target 则可直接退出循循环
            list.add(nums[j]);
            backtrack3(nums, target - nums[j], j, list, res);
            list.remove(list.size() - 1);
        }
    }

    /*
     * 解法4：DP
     * - 思路：// TODO: ????
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> combinationSum4(int[] nums, int target) {
        Arrays.sort(nums);
        List<List<Integer>>[] dp = new List[target + 1];  // dp[i] 保存所有和为 i 的组合（∴ 最后返回 dp[target] 即可）

        for (int i = 0; i <= target; i++) {
            List<List<Integer>> comboList = new ArrayList<>();  // 和为 i 的组合

            for (int j = 0; j < nums.length && nums[j] <= i; j++) {  // 找到所有和为 i 的组合 j ∈ [0,i]
                int n = nums[j];
                if (n == i) {
                    comboList.add(Arrays.asList(n));     // n == i 的情况需特殊处理
                    continue;
                }
                for (List<Integer> list : dp[i - n]) {      // 遍历 dp[i-n] 中的每一个组合
                    if (n >= list.get(list.size() - 1)) {
                        List<Integer> newList = new ArrayList<>(list);
                        newList.add(n);
                        comboList.add(newList);
                    }
                }
            }
            dp[i] = comboList;
        }

        return dp[target];
    }

    public static void main(String[] args) {
        log(combinationSum4(new int[]{2, 7, 3, 6}, 7));  // expects [[7], [2,2,3]]
        log(combinationSum4(new int[]{2, 3, 5}, 8));     // expects [[2,2,2,2], [2,3,3], [3,5]]
        log(combinationSum4(new int[]{2, 3, 5}, 5));     // expects [[2,3], [5]]
        log(combinationSum4(new int[]{3}, 8));           // expects []
    }
}
