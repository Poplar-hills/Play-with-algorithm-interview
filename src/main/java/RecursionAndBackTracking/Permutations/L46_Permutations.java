package RecursionAndBackTracking.Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
 * Permutations
 *
 * - Given a collection of distinct integers, return all possible permutations (求 n 个数字的全排列).
 * */

public class L46_Permutations {
    /*
     * 解法1：Iteration
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
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        res.add(new ArrayList<>());                        // 需要一个 trigger 元素

        for (int n : nums) {
            List<List<Integer>> temp = new ArrayList<>();
            for (List<Integer> list : res) {
                for (int j = 0; j <= list.size(); j++) {   // 遍历 list 的每一个插入点（包括尾部的插入点）
                    List<Integer> newList = new ArrayList<>(list);
                    newList.add(j, n);                     // 往复制出来的 list 的插入点 j 处放入元素 n
                    temp.add(newList);
                }
            }
            res = temp;                                    // 替换原 res
        }

        return res;
    }

    /*
     * 解法2：Iteration (解法1的简化版)
     * - 思路：采用 L17_LetterCombinationsOfPhoneNumber 解法3的思路，用 Queue 简化解法1中对 res 中元素加工和添加的过程。
     * - 时间复杂度 O(n * n!)，空间复杂度 O(n * n!)。
     * */
    public static List<List<Integer>> permute2(int[] nums) {
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

    /*
     * 解法3：Recursion + Backtracking
     * - 思路：不同于解法1、2，另一种更自然的思路是每次往列表中添加元素时选择 nums 中的不同元素。例如对于 [1,2,3] 来说：
     *                             []
     *                 1/          2|           3\
     *              [1]            [2]            [3]
     *           2/    3\       1/    3\        1/    2\
     *        [1,2]   [1,3]   [2,1]   [2,3]   [3,1]   [3,2]
     *         3|      2|      3|      1|      2|      1|
     *       [1,2,3] [1,3,2] [2,1,3] [2,3,1] [3,1,2] [3,2,1]
     *
     *   形式化表达：permute([1,2,3]) = [1] + permute([2,3]), [2] + permute([1,3]), [3] + permute([1,2])。
     *   但这种方式需要在添加时判断待添加的元素是否已经在列表中 ∴ 需要一个辅助数据结构来对此进行高效查询。
     * - 时间复杂度 O(n^n)：
     *   - 若不考虑 if (!used[i]) 的判断，则该解法相当于遍历一棵 n 叉树（n-ary）∵ 二叉树的节点个数为 2^h，三叉树为 3^h，
     *     n 叉树为 n^h ∴ 遍历 n 叉树的复杂度为 O(n^h)，而该题中树高 h 又与叉数 n 相等 ∴ 总体复杂度为 O(n^n)。
     *   - 若考虑 if (!used[i]) 的判断，则每递归一层都会减少一个分支，TODO: 如何计算该复杂度？？？？
     * - 空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permute3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        backtrack3(nums, new ArrayList<>(), new boolean[nums.length], res);  // 布尔数组用于记录哪些元素已经在列表中
        return res;
    }

    private static void backtrack3(int[] nums, List<Integer> list, boolean[] used, List<List<Integer>> res) {
        if (list.size() == nums.length) {    // 通过 list.size 即可判断是否递归到底 ∴ 不需要在参数中传递当前的 nums 索引
            res.add(new ArrayList<>(list));  // 递归到底时 list 才是一个完整的排列 ∴ 此时再将其加入 res
            return;
        }
        for (int i = 0; i < nums.length; i++) {     // 每次递归中都要遍历 nums 以找到可以添加进 list 的元素
            if (!used[i]) {                         // list 中没有的元素是可以添加的
                list.add(nums[i]);
                used[i] = true;
                backtrack3(nums, list, used, res);  // 继续往下递归
                list.remove(list.size() - 1);       // 在返回上一层递归前将 list 恢复原状（这是回溯的特征之一，但若每次复制 list，则无需恢复）
                used[i] = false;                    // used[i] 也要回复原状
            }
        }
    }

    /*
     * 解法4：Recursion + Backtracking + In-place swap
     * - 思路：与解法3类似，但每次递归中采用原地交换元素的方式获得新的排列：
     *                                   [1,2,3]
     *                           /          |           \
     *                     [1,2,3]       [2,1,3]        [2,3,1]
     *                     /    \        /    \         /     \
     *               [1,2,3] [1,3,2] [2,1,3] [2,3,1] [2,3,1] [2,1,3]
     *
     * - 实现：将 int[] 转为 List<Integer> 的另一种写法是：Arrays.stream(nums).boxed().collect(Collectors.toList())。
     * - 时间复杂度 O(n!)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> permute4(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        helper4(nums, 0, res);
        return res;
    }

    private static void helper4(int[] nums, int i, List<List<Integer>> res) {
        if (i == nums.length) {                  // 递归到底
            List<Integer> list = new ArrayList<>();
            for (int n : nums) list.add(n);      // 将 int[] 转化为 List<Integer> 后才能放入 res
            res.add(list);
            return;
        }
        for (int j = i; j < nums.length; j++) {  // 注意 j 要从 i 开始（∵ 最终只将最后一层的排列放入结果集）
            swap(nums, i, j);
            helper4(nums, i + 1, res);
            swap(nums, i, j);                    // 在回到上一层之前将 nums 恢复原状
        }
    }

    public static void main(String[] args) {
        log(permute4(new int[]{1, 2, 3}));  // expects [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
        log(permute4(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permute4(new int[]{1}));        // expects [[1]]
        log(permute4(new int[]{}));         // expects []
    }
}
