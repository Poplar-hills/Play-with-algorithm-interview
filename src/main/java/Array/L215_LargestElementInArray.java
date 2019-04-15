package Array;

import java.util.PriorityQueue;
import java.util.Random;

import static Utils.Helpers.log;
import static Utils.Helpers.swap;

public class L215_LargestElementInArray {
    /*
    * 解法1：堆排序
    * - 使用最大堆，extractMax n-k 次，总复杂度为 O((2n-k)logn)
    * */
    public static int kthLargest1(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        for (int n : nums) {
            pq.add(n);
            if (pq.size() > k)
                pq.poll();
        }
        return pq.poll();
    }

    /*
    * 解法2：快排思路
    * - 该快排是从大到小排序，这样才能满足"第几大"的需求（如果是从小到大排序，就只能找到"第几小"）
    * - 要从大到小排序，则需要：[v|--- > v ---|--- == v ---|...|--- < v ---]
    *                         l           gt             i   lt        r
    * */
    public static int kthLargest2(int[] nums, int k) {
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
        int v = nums[l];

        int lt = r + 1;
        int gt = l;
        int i = l + 1;

        while (i < lt) {
            if (nums[i] < v)
                swap(nums, i, --lt);
            else if (nums[i] > v)
                swap(nums, i++, ++gt);
            else
                i++;
        }
        swap(nums, l, gt);
        gt--;
        return new int[] {gt, lt};
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {3, 2, 1, 5, 6, 4};
        log(kthLargest1(arr1, 2));  // expects 5

        int[] arr2 = new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6};
        log(kthLargest1(arr2, 4));  // expects 4

        int[] arr3 = new int[] {3, 2, 1, 5, 6, 4};
        log(kthLargest2(arr3, 2));  // expects 5

        int[] arr4 = new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6};
        log(kthLargest2(arr4, 4));  // expects 4
    }
}
