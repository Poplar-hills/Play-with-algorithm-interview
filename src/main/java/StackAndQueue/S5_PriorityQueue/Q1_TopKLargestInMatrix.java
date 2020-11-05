package StackAndQueue.S5_PriorityQueue;

import static Utils.Helpers.log;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/*
 * Find Top K Largest Elements In A Matrix
 *
 * - Given a m*n int matrix, find the k largest elements.
 * */

public class Q1_TopKLargestInMatrix {

    /*
     * 解法1：Max heap
     * - 时间复杂度 O(m + klogn)，空间复杂 O(k)
     * */
    public static List<Integer> findTopK(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length;
        PriorityQueue<int[]> q = new PriorityQueue<>(k, (arr1, arr2) -> arr2[0] - arr1[0]);

        for (int i = 0; i < m; i++) {
            int val = matrix[i][n - 1];
            q.offer(new int[]{val, i, n-1});
        }

        List<Integer> res = new ArrayList<>();
        while (res.size() < k) {
            int[] top = q.poll();
            res.add(top[0]);
            int rowNo = top[1], colNo = top[2];
            int nextColNo = colNo - 1;

            if (nextColNo >= 0) {
                int nextVal = matrix[rowNo][nextColNo];
                q.offer(new int[] {nextVal, rowNo, nextColNo});
            }
        }

        return res;
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][] {
            {2, 4, 4, 5, 9, 10},
            {1, 3, 4, 5, 7, 9},
            {0, 3, 5, 6, 7, 8},
            {4, 6, 8, 11, 12, 13}
        };

        log(findTopK(matrix, 7));
    }
}
