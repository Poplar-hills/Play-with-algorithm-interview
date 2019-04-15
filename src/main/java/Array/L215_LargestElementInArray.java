package Array;

import java.util.PriorityQueue;

import static Utils.Helpers.log;

public class L215_LargestElementInArray {
    public static int kthLargest1(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        for (int n : nums) {
            pq.add(n);
            if (pq.size() > k)
                pq.poll();
        }
        return pq.poll();
    }

    public static void main(String[] args) {
        int[] arr1 = new int[] {3, 2, 1, 5, 6, 4};
        log(kthLargest1(arr1, 2));  // expects 5

        int[] arr2 = new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6};
        log(kthLargest1(arr2, 4));  // expects 4
    }
}
