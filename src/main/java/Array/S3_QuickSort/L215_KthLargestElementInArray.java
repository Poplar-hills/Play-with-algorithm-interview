package Array.S3_QuickSort;

import java.util.*;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Kth Largest Element in an Array
 *
 * - Find the kth largest element in an unsorted array（注意 k 取值从1开始，例如 k=1 时就是求数组中的最大元素）.
 *
 * - 分析：该问题本质上也是个排序问题 ∴ 思路可以有：
 *   1. 先对数组进行整体排序，再取第 k 个元素 -> O(nlogn) 。
 *   2. 使用大小为 k 的最小堆，用于获得数组中前 k 大的元素，最终堆顶的就是第 k 大元素 -> O(nlogk)。
 *   3. 基于快排思路的 Quick Select：不断对 k 所在的分区进行 partition，直到第 k 大的元素成为 pivot，并被移动到 k-1 的位置
 *      上，此时 nums[k-1] 即是第 k 大元素 -> O(n)。
 * */

public class L215_KthLargestElementInArray {
    /*
     * 解法1：Heap sort（全排序）
     * - 思路：基于上面分析中的思路1，先对数组进行整体排序，再取 k 个元素，该解法中选择堆排序作为具体的排序算法。
     * - 实现：∵ 要取的是第 k 大的元素 ∴ 使用最大堆。
     * - 时间复杂度 O((n+k)logn)，空间复杂度 O(n)。
     * */
    public static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());  // 最大堆（PriorityQueue 默认是最小堆）
        for (int n : nums) pq.offer(n);          // ∵ PriorityQueue 没有 heapify 方法 ∴ 需要手动添加
        for (int i = 1; i < k; i++) pq.poll();   // 将最大的 k-1 个元素从堆中移除
        return pq.poll();
    }

    /*
     * 解法2：Min heap（最小堆排序 k+1 个元素）
     * - 思路：上面分析中的思路2。不对数组进行整体排序，而是利用最小堆的排序能力 —— 在往堆中添加元素的过程中，让堆中的元素个数保持在 k 个，
     *   只要元素个数一超过 k 就将堆中最小的元素移除 ∴ 当全部遍历完之后，留在堆中的就是数组中最大的 k 个元素，而堆顶元素就是第 k 大的。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(k)。
     * */
    public static int kthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);  // 最小堆（∵ 后面要判断堆大小是否超过 k ∴ 这里开辟 k+1 的空间）
        for (int n : nums) {
            pq.offer(n);
            if (pq.size() == k + 1)  // 在往堆中添加元素过程中，一旦堆大小超过 k 就移除堆中最小的元素
                pq.poll();
        }
        return pq.poll();
    }

    /*
     * 解法3：Quick Select
     * - 思路：上面分析的思路3。∵ 快排的核心逻辑（partition 方法）就是每次从数组中选择一个元素作为 pivot 并把它移动到正确的
     *   位置上（即将第 n 大的元素移动到 n-1 位置上，-1是因为索引从0开始）。这个逻辑刚好符合该题需求 ∴ 只要将第 k 大元素移动到
     *   k-1 位置上，那 nums[k-1] 就是第 k 大元素 ∴ 我们只需不断对 k 所在的分区进行 partition，直到第 k 大的元素成为 pivot，
     *   并被移动到 k-1 的位置上即可。
     * - 注意：∵ 题中要求的是"第几大"（而非"第几小"）∴ 要从大到小排序排序，即分区 (l,gt] 中都是 >v 的；[lt,r] 中都是 <v 的；
     *   (gt,i) 中都是 ==v 的：[v|--- >v ---|--- ==v ---|.....|--- <v ---]
     *                        l          gt            i     lt       r
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int kthLargest3(int[] nums, int k) {
        assert (k >= 1 && k <= nums.length);
        return quickSelect(nums, 0, nums.length - 1, k - 1);  // k-1 是为了让 k 的取值从0开始，与索引从0开始相一致
    }

    private static int quickSelect(int[] nums, int l, int r, int k) {  // Quick Select 就是基于快排，选择出第 k 大/小的元素
        if (l == r) return nums[l];
        int[] ps = partition(nums, l, r);  // partition 的返回值 {gl,lt} 表示 {大于 pivot 的最后一个位置, 小于 pivot 的第一个位置}
        if (k <= ps[0])                    // 若 k ∈ [l,gt]，则继续对该分区进行快排（∵ 该分区内的元素还是无序的，第 k 大元素还不在 k 位置上）
            return quickSelect(nums, l, ps[0], k);
        if (k >= ps[1])                    // 若 k ∈ [lt,r]，则继续对该分区进行快排（同上）
            return quickSelect(nums, ps[1], r, k);
        return nums[k];                    // 若 k ∈ (gt,i)，说明刚才 partition 中的 pivot 就是第 k 大元素，并被移到了 k 位上 ∴ 直接返回即可
    }

    private static int[] partition(int[] nums, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;  // 对 nums 近乎有序的情况进行优化，防止算法退化成 O(n^2)
        swap(nums, l, vIndex);
        int v = nums[l], lt = r + 1, gt = l;

        for (int i = l + 1; i < lt; ) {  // 遍历 [l+1, lt)，注意 lt 是动态的
            if (nums[i] < v)
                swap(nums, i, --lt);
            else if (nums[i] > v)
                swap(nums, i++, ++gt);
            else
                i++;
        }
        swap(nums, l, gt);  // 将 pivot 放到正确的位置上
        gt--;
        return new int[]{gt, lt};
    }

    /*
     * 解法4：Quick Select（解法3的简化版）
     * - 思路：与解法3一致。
     * - 实现：基于两路快排的 partition，同样是从大到小排序：
     *   [v|--- >v ---|--- ≤v ---|.....]
     *    l            lt         i   r      - [l,lt) 位置上的元素 >v，[lt,i) 位置上的元素 <v
     * - 时间复杂度 O(n)，空间复杂度 O(logn)。
     * */
    public static int kthLargest4(int[] nums, int k) {
        assert (k >= 1 && k <= nums.length);
        return quickSelect4(nums, 0, nums.length - 1, k - 1);
    }

    private static int quickSelect4(int[] nums, int l, int r, int k) {
        if (l == r) return nums[l];
        int p = partition4(nums, l, r);
        if (k < p) return quickSelect4(nums, l, p - 1, k);
        if (k > p) return quickSelect4(nums, p + 1, r, k);
        return nums[p];  // 若 k == p，说明刚才 partition 中的 pivot 就是第 k 大元素，并被移到了 k 位上 ∴ 直接返回即可
    }

    private static int partition4(int[] nums, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(nums, l, vIndex);
        int v = nums[l], lt = l + 1;

        for (int i = l + 1; i <= r; i++)  // 遍历 [l+1, r]
            if (nums[i] > v)
                swap(nums, i, lt++);

        swap(nums, l, lt - 1);  // 将 pivot 放到正确的位置上
        return lt - 1;
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{3, 2, 1, 5, 6, 4};
        log(kthLargest(arr1, 2));  // expects 5

        int[] arr2 = new int[]{0, -2, 4, 4 -2, 0};
        log(kthLargest(arr2, 3));  // expects 0

        int[] arr3 = new int[]{0, -2, 4, 4 -2, 0};
        log(kthLargest(arr3, 4));  // expects 0
    }
}
