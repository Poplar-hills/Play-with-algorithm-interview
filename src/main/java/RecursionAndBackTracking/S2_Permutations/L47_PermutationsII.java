package RecursionAndBackTracking.S2_Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/*
 * Permutations II
 *
 * - Given a collection of numbers that might contain duplicates, return all possible unique permutations.
 *
 * - 区别：
 *   - L46_Permutations：nums 中的元素无重复，返回所有可能的排列；
 *   - L47_PermutationsII：nums 中的元素可能重复，返回所有 unique 的排列；
 *
 * - 💎 分析：
 *   - 排列问题（Permutation）通常可以转化为树形问题，并通过回溯法解决。
 *   - L46_Permutations：∵ 是排列而非组合 ∴ 往 [1] 里追加2与往 [2] 里追加1得到的结果不是重复解 ∴ 每层递归时都要从第1个元素开始。
 *   - L47_PermutationsII：在 L46_Permutations 的基础上 ∵ 元素可能重复，而选用重复元素的分支会产生重复的解 ∴ 要么在最后用 Set
 *     对解进行去重，要么在分支时通过避免选用重复元素达到剪枝的目的。
 * */

public class L47_PermutationsII {
    /*
     * 解法1：Recursion + Backtracking + Set
     * - 思路：结合 L46_Permutations 解法1、2，使用 Set 去重，同时最后复制 list。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Set<List<Integer>> set = new HashSet<>();
        backtrack(nums, new ArrayList<>(), new boolean[nums.length], set);
        return new ArrayList<>(set);
    }

    private static void backtrack(int[] nums, List<Integer> list, boolean[] used, Set<List<Integer>> set) {
        if (list.size() == nums.length) {
            set.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                list.add(nums[i]);
                used[i] = true;
                backtrack(nums, list, used, set);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法2：Iteration + Set
     * - 思路：在 L46_Permutations 解法5的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n * n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique2(int[] nums) {
        if (nums.length == 0) return new ArrayList<>();
        Queue<List<Integer>> q = new LinkedList<>();
        q.offer(new ArrayList<>());

        for (int i = 0; i < nums.length; i++) {
            while (q.peek().size() == i) {
                List<Integer> list = q.poll();
                for (int j = 0; j <= list.size(); j++) {
                    List<Integer> newList = new ArrayList<>(list);
                    newList.add(j, nums[i]);
                    q.offer(newList);
                }
            }
        }

        return new ArrayList<>(new HashSet<>(q));  // 最后 Set 去重
    }

    /*
     * 解法3：Recursion + In-place swap + Set
     * - 思路：在 L46_Permutations 解法3的基础上加入用于去重的 Set。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack3(nums, 0, res);
        return new ArrayList<>(new HashSet<>(res));  // 最后 Set 去重
    }

    private static void backtrack3(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {
            swap(nums, i, j);
            backtrack3(nums, i + 1, res);
            swap(nums, i, j);
        }
    }

    /*
     * 解法4：Recursion + Backtracking + Inner Set
     * - 思路：与解法1一致。
     * - 实现：不同于解法1、2、3使用 Set 对结果集进行去重，本解法对树进行剪枝，从而不产生重复解。例如对于 nums=[1,2,1] 来说：
     *                              []
     *                  1/          2|          1\                - [] 下的第二个1的分支可剪掉
     *              [1]             [2]             [1]
     *           2/     1\       1/    1\        1/    2\         - [2] 下的第二个1的分支可剪掉
     *         [1,2]   [1,1]   [2,1]   [2,1]   [1,1]   [1,2]
     *          1|      2|      1|      1|      2|      1|
     *        [1,2,1] [1,1,2] [2,1,1] [2,1,1] [1,1,2] [1,2,1]
     *                                   ×       ×       ×        - 剪掉这三个分支后不再有重复解出现
     *   在对 []、[2] 进行分支时，都出现了重复的分支，而剪掉重复分支的关键在于判断 nums 中的重复元素 —— 只要元素是重复的，
     *   则得到的分支一定重复 ∴ 可以在解法1的基础上使用 Set 进行判断。
     * - 💎 经验：拿到类似的题后要先画图，通过画图分析递归、遍历、剪枝的策略。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique4(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack4(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void backtrack4(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        Set<Integer> set = new HashSet<>();  // ∵ 去重的对象是同一层里遍历时的不同分支 ∴ 要将 Set 声明在这里
        for (int i = 0; i < nums.length; i++) {
            if (!used[i] && !set.contains(nums[i])) {  // 通过 Set 识别 nums 中未使用过的元素是否与之前使用过的重复
                set.add(nums[i]);
                list.add(nums[i]);
                used[i] = true;
                backtrack4(nums, list, used, res);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法5：Recursion + Backtracking + Sort
     * - 思路：与解法4一致。
     * - 实现：不同于解法4中采用 Set 识别 nums 中的重复元素，该解法先对 nums 排序，再通过判断前后两个元素是否相等的方式去重。
     * - 时间复杂度 O(nlogn + n!)，空间复杂度 O(n)。
     * - 👉 该解法中 "&& used[i - 1]" 条件较难理解，而且可以替换为 "&& !used[i - 1]"，不是很 straightforward ∴ 更推荐解法4。
     * */
    public static List<List<Integer>> permuteUnique5(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        Arrays.sort(nums);    // 这里要先排序，后面才能进行 nums[i] == nums[i-1] 的判断
        backtrack5(nums, new ArrayList<>(), new boolean[nums.length], res);
        return res;
    }

    private static void backtrack5(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1]) continue;  // 通过比较前后两个元素来识别重复，注意 used[i-1] 条件不可少
            list.add(nums[i]);
            used[i] = true;
            backtrack5(nums, list, used, res);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    /*
     * 解法6：Recursion + In-place swap + Inner Set
     * - 思路：与解法3一致。
     * - 实现：在解法3的基础上加入解法4中对树进行剪枝的优化。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permuteUnique6(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack6(nums, 0, res);
        return res;
    }

    private static void backtrack6(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length - 1) {
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        Set<Integer> set = new HashSet<>();
        for (int j = i; j < nums.length; j++) {
            if (!set.contains(nums[j])) {  // 若 nums[j] 已经在 set 中了（已经与其余元素 swap 过了）则跳过
                set.add(nums[j]);
                swap(nums, i, j);
                backtrack6(nums, i + 1, res);
                swap(nums, i, j);
            }
        }
    }

    public static void main(String[] args) {
        log(permuteUnique5(new int[]{1, 1, 2}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
//        log(permuteUnique5(new int[]{1, 2, 1}));     // expects [[1,1,2], [1,2,1], [2,1,1]]
//        log(permuteUnique5(new int[]{1, 1, 2, 1}));  // expects [[1,1,1,2], [1,1,2,1], [1,2,1,1], [2,1,1,1]]
//        log(permuteUnique5(new int[]{1, 2}));        // expects [[1,2], [2,1]]
//        log(permuteUnique5(new int[]{1}));           // expects [[1]]
//        log(permuteUnique5(new int[]{}));            // expects []
    }
}
