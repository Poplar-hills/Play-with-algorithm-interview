package DP;

import java.util.*;

import static Utils.Helpers.log;

/*
* Triangle
*
* - Given a triangle, find the minimum path sum from top to bottom.
* - Note: Each step you may move to adjacent numbers on the row below (比如下面的 test case 2 中，第2行的2只能移动
*   到第3行中的1或-1上，而不能移动到-3上，因此不是从每行中找到最小值就行).
* - Bonus point if using only O(n) extra space, where n is the number of rows in the triangle.
* */

public class L120_Triangle {
    /*
    * 超时解：找到所有 path 后再计算最小的 sum。
    * - 思路：与 L70_ClimbingStairs 中的超时解一致。
    * */
    public static int minimumTotal(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;

        Queue<List<Integer>> q = new LinkedList<>();  // 队列中存放 index path，每条 path 中存放元素的 index 而非元素值本身
        List<Integer> initialIndexPath = new ArrayList<>();
        initialIndexPath.add(0);
        q.offer(initialIndexPath);

        while (!q.isEmpty()) {
            List<Integer> indexPath = q.poll();
            if (indexPath.size() == triangle.size()) {
                int sum = 0;
                for (int i = 0; i < indexPath.size(); i++)
                    sum += triangle.get(i).get(indexPath.get(i));    // 求该 path 的所有元素值之和    public static int minimumTotal(List<List<Integer>> triangle) {

                res = Math.min(res, sum);
                continue;
            }
            int currIndex = indexPath.get(indexPath.size() - 1);     // 获取当前元素在其 level 上的 index
            for (int i = 0; i < 2; i++) {                            // 遍历所有可能的下一步节点
                List<Integer> newPath = new ArrayList<>(indexPath);  // 复制当前的 path
                newPath.add(currIndex + i);                          // 将下一步节点的 index 放入 newPath 中
                q.offer(newPath);
            }
        }

        return res;
    }

    /*
    * 解法1：
    * - 思路：该题也可用图论建模：
    *            -1
    *           ↙  ↘
    *          2    3
    *        ↙  ↘  ↙  ↘
    *       1    -1   -3
    *   建模后该题转化为，求从顶层到底层顶点的路径中节点值之和最小的那条路径。
    * - 采用 BFS，
    * - 时间复杂度 O()，空间复杂度 O()。
    * */
    static class RouteSum {
        int level, index, sum;
        public RouteSum(int level, int index, int sum) {
            this.level = level;
            this.index = index;
            this.sum = sum;
        }
    }

    public static int minimumTotal1(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        if (triangle == null) return res;

        Queue<RouteSum> q = new LinkedList<>();
        q.offer(new RouteSum(0, 0, triangle.get(0).get(0)));  // q 中保存 <level, index, sum> 信息

        while (!q.isEmpty()) {
            RouteSum routeSum = q.poll();
            
            for (int i = 0; i < 2; i++) {
                int newLevel = routeSum.level + 1;
                int newIndex = routeSum.index + i;
                int newSum = routeSum.sum + triangle.get(newLevel).get(newIndex);  // 到下一层中取相邻顶点

                if (newLevel == triangle.size() - 1)  // 若已抵达 bottom level
                    res = Math.min(res, newSum);
                else
                    q.offer(new RouteSum(newLevel, newIndex, newSum));
            }
        }

        return res;
    }

    /*
     * 解法2：
     * - 思路：bottom-up DP
     * - 时间复杂度 O()，空间复杂度 O()。
    */
    public static int minimumTotal2(List<List<Integer>> triangle) {
        return 0;
    }

    public static void main(String[] args) {
        log(minimumTotal1(Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        )));  // expects 11 (2 + 3 + 5 + 1)

        log(minimumTotal1(Arrays.asList(
                Arrays.asList(-1),
                Arrays.asList(2, 3),
                Arrays.asList(1, -1, -3)
        )));  // expects -1 (-1 + 3 + -3) 注意不是从每行中找到最小值就行，如：(-1 + 2 + -3)
    }
}
