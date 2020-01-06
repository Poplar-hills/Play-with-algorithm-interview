package Array.S3_QuickSort;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

/*
 * Kth Largest Element in an Array
 *
 * - Find the kth largest element in an unsorted array.
 *
 * - 分析：该问题也是个排序问题 ∴ 思路可以有：
 *   1. 先对数组进行整体排序，再取 k 个元素，若使用快排 or 归并 or 堆排序。
 *   2. 不对数组进行整体排序，而是使用最小堆，在往堆中添加元素的过程中，让堆大小一直保持在 k，只要大小一超过 k 就让最小的元素出堆，
 *      而让大的元素保留在堆中。这样当遍历结束后，堆中保留的就是数组中最大的 k 个元素，而堆顶元素就是第 k 大的元素。
 *   3. 采用三路快排的思路对数组不断进行分组（每次只需对 k 所在的分组进行递归），直到分组索引 == k 时即找到了第 k 大元素。
 *   更多解法 SEE：SortingAdvanced - Exercise_KthSmallestElement（注意是 Smallest 不是本题中的 Largest）
 * */

public class L215_LargestElementInArray {
    /*
     * 解法1：Heap sort（堆排序）
     * - 思路：上面分析中的思路1。
     * - 时间复杂度 O((n+k)logn)，空间复杂度 O(n)。
     * */
    public static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());  // 最大堆
        for (int n : nums) pq.add(n);            // ∵ PriorityQueue 没有 heapify 方法 ∴ 需要手动添加
        for (int i = 1; i < k; i++) pq.poll();   // 将最大的 k-1 个元素从堆中移除
        return pq.poll();
    }

    /*
     * 解法2：Heap sort（堆排序）
     * - 思路：上面分析中的思路2。
     * - 时间复杂度 O((2n-k)logn)，若 k 很大则比解法1快，若 k 小则比解法1慢；
     * - 空间复杂度 O(k)。
     * */
    public static int kthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);  // 最小堆（∵ 后面要判断堆大小是否超过 k ∴ 这里开辟 k+1 的空间）
        for (int n : nums) {
            pq.add(n);
            if (pq.size() > k)  // 在往堆中添加元素过程中，一旦堆大小超过 k 就移除堆中最小的元素
                pq.poll();
        }
        return pq.poll();
    }

    /*
     * 解法3：快排思路
     * - 该快排是从大到小排序，这样才能满足"第几大"的需求（如果是从小到大排序，就只能找到"第几小"）
     * - 要从大到小排序，则需要：[v|--- > v ---|--- == v ---|...|--- < v ---]
     *                         l           gt             i   lt        r
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int kthLargest3(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, k - 1);  // k-1 是为了让语义更自然（"第1小"就是最小，"第2小"就是第2小，没有"第0小"）
    }

    private static int quickSelect(int[] nums, int l, int r, int k) {
        if (l == r) return nums[l];
        int[] ps = partition(nums, l, r);
        if (k <= ps[0])
            return quickSelect(nums, l, ps[0], k);
        if (k >= ps[1])
            return quickSelect(nums, ps[1], r, k);
        return nums[k];
    }

    private static int[] partition(int[] nums, int l, int r) {
        int vIndex = new Random().nextInt(r - l + 1) + l;
        swap(nums, l, vIndex);
        int v = nums[l], lt = r + 1, gt = l;

        for (int i = l + 1; i < lt; ) {
            if (nums[i] < v)
                swap(nums, i, --lt);
            else if (nums[i] > v)
                swap(nums, i++, ++gt);
            else
                i++;
        }
        swap(nums, l, gt);
        gt--;
        return new int[]{gt, lt};
    }

    public static void main(String[] args) {
        int[] arr1 = new int[]{3, 2, 1, 5, 6, 4};
        log(kthLargest(arr1, 2));  // expects 5

        int[] arr2 = new int[]{6, -2, 8, 10, -4, 0};
        log(kthLargest(arr2, 4));  // expects 0
    }
}
