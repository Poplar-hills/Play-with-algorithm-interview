package RecursionAndBackTracking.S2_Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
 * Permutations
 *
 * - Given a collection of distinct integers, return all possible permutations (求 n 个不重复数字的全排列).
 *
 * - 注：The difference between combinations and permutations is ordering. With permutations we care about
 *   the order of the elements, whereas with combinations we don’t.
 * - Permutation: 从 n 个元素中任取 m 个按照一定的顺序排成一列，所能得到的排列个数为：A(n,m) = n(n-1)(n-2)...(n-m+1)；
 * - Combination: 从 n 个元素中任取 m 个组成一组，所能得到的组合个数为：C(n,m) = A(n,m) / m!；
 *
 * - 💎 总结：排列问题（Permutation）通常可以转化为树形问题，并通过回溯法解决。
 * */

public class L46_Permutations {
    /*
     * 解法1：Recursion + Backtracking
     * - 思路：将排列问题转化为树形问题，再使用回溯搜索解。具体来说，每次往列表中添加元素时选择 nums 中的不同元素。例如：
     *                             []
     *                 1/          2|           3\
     *              [1]            [2]            [3]
     *           2/    3\       1/    3\        1/    2\
     *        [1,2]   [1,3]   [2,1]   [2,3]   [3,1]   [3,2]
     *         3|      2|      3|      1|      2|      1|
     *       [1,2,3] [1,3,2] [2,1,3] [2,3,1] [3,1,2] [3,2,1]
     *
     *   该过程的形式化表达：permute([1,2,3]) = [1] + permute([2,3]), [2] + permute([1,3]), [3] + permute([1,2])。
     *   但这种方式需要在添加元素时判断待添加的元素是否已经用过了 ∴ 需要一个辅助数据结构来进行高效查询。
     * - 时间复杂度 O(n!)，即 n 个元素进行全排列；空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack(nums, new ArrayList<>(), new boolean[nums.length], res);  // 布尔数组用于记录哪些元素已经用过了
        return res;
    }

    private static void backtrack(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                list.add(nums[i]);
                used[i] = true;
                backtrack(nums, list, used, res);
                list.remove(list.size() - 1);
                used[i] = false;
            }
        }
    }

    /*
     * 解法2：Recursion + Backtracking + In-place swap（解法1的性能优化版）
     * - 思路：与解法1一致。
     * - 实现：但每次递归中采用原地交换元素的方式获得新的排列：
     *                            [1,2,3]
     *                    /          |           \             - 将0号元素交换到 [0..n) 号位置上
     *              [1,2,3]       [2,1,3]        [2,3,1]
     *              /    \        /    \         /     \       - 将1号元素交换到 [1..n) 号位置上
     *        [1,2,3] [1,3,2] [2,1,3] [2,3,1] [2,3,1] [2,1,3]
     *
     * - 👉 技巧：将 int[] 转为 List<Integer> 的另一种写法是：
     *   Arrays.stream(nums).boxed().collect(Collectors.toList())。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permute2(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack2(nums, 0, res);
        return res;
    }

    private static void backtrack2(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length - 1) {                  // 递归到底
            List<Integer> list = new ArrayList<>();  // 将数组转化为列表后再放入 res
            for (int n : nums) list.add(n);
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {  // j ∈ [i..n)，即让第 i 个元素分别与 [i..n) 个元素 swap
            swap(nums, i, j);
            backtrack2(nums, i + 1, res);
            swap(nums, i, j);                    // 在回到上一层之前将 nums 恢复原状
        }
    }

    /*
     * 解法3：Iteration
     * - 思路：采用类似 L17_LetterCombinationsOfPhoneNumber 解法2的思路，对于 nums 中的每个元素，都放到 res 中的每个列表
     *   里的每个插入点上，生成一个新的排列。例如，对于 [1,2,3] 来说：
     *                           /--> [3,2,1]
     *                         3/
     *                 /-> [2,1] -3-> [2,3,1]
     *                /        3\
     *         1    2/           \--> [2,1,3]
     *     [] --> [1]
     *              2\           /--> [3,1,2]
     *                \        3/
     *                 \-> [1,2] -3-> [1,3,2]
     *                         3\
     *                           \--> [1,2,3]
     *
     * - 时间复杂度 O(n * n!)：n 个元素的全排列有 n! 种结果，而每个结果中又有 n 个元素。
     * - 空间复杂度 O(1)。
     * */
    public static List<List<Integer>> permute3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        res.add(new ArrayList<>());        // 需要一个 trigger 元素

        for (int n : nums) {
            List<List<Integer>> newRes = new ArrayList<>();  // ∵ 下面要遍历 res 中的元素，不能一边遍历一遍添加 ∴ 这里要创建一个新的
            for (List<Integer> list : res) {
                for (int i = 0; i <= list.size(); i++) {     // 遍历 list 的每一个插入点（包括尾部的插入点）
                    List<Integer> newList = new ArrayList<>(list);
                    newList.add(i, n);                       // 往 i 处插入元素 n
                    newRes.add(newList);
                }
            }
            res = newRes;
        }

        return res;
    }

    /*
     * 解法4：Iteration (解法3的简化版)
     * - 思路：采用 L17_LetterCombinationsOfPhoneNumber 解法3的思路，用 Queue 简化解法1中对 res 中元素加工和添加的过程。
     * - 时间复杂度 O(n * n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permute4(int[] nums) {
        Queue<List<Integer>> q = new LinkedList<>();
        if (nums.length == 0) return new ArrayList<>();
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

        return new ArrayList<>(q);
    }

    public static void main(String[] args) {
        log(permute2(new int[]{1, 2, 3}));  // expects [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
        log(permute2(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permute2(new int[]{1}));        // expects [[1]]
        log(permute2(new int[]{}));         // expects []
    }
}
