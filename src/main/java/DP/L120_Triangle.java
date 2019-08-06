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
    * 超时解1：
    * - 思路：与 L70_ClimbingStairs 中的超时解一致，用图论建模：
    *            -1
    *           ↙  ↘
    *          2    3
    *        ↙  ↘  ↙  ↘
    *       1    -1   -3
    *   建模后该题转化为求从顶层到底层顶点的路径中最小的顶点值之和，因此要用 BFS 找到每一条路径，同时求其中最小的顶点值之和。
    * - 时间复杂度 O(2^n)：∵ 每个顶点都会产生2个分支 ∴ 复杂度与 Fibonacci.java 的解法1一致。
    * - 空间复杂度 O(nlogn)：queue 中同一时间最多存储 n/2 条路径（完美二叉树最底层节点个数为 n/2），而每条路径中有 logn（树高）个顶点。
    * */
    public static int minimumTotal(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        Queue<List<Integer>> q = new LinkedList<>();  // 队列中存放 path，每条 path 中存放每个顶点在其 level 上的 index
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(0);
        q.offer(initialPath);

        while (!q.isEmpty()) {
            List<Integer> path = q.poll();

            if (path.size() == triangle.size()) {     // 若一个 path 中的顶点个数 == triangle 的高度，则说明已经到底
                int sum = 0;                          // 开始求该 path 的所有顶点值之和
                for (int i = 0; i < path.size(); i++)
                    sum += triangle.get(i).get(path.get(i));
                res = Math.min(res, sum);
                continue;                             // 退出循环
            }

            int currIndex = path.get(path.size() - 1);  // 若该 path 还未到底，则获取其最新经过的顶点在其 level 上的 index
            for (int i = 0; i < 2; i++) {               // 寻找相邻顶点
                List<Integer> newPath = new ArrayList<>(path);   // 复制当前的 path（这个技巧很有用）
                newPath.add(currIndex + i);                      // 将下一步节点的 index 放入 newPath 中
                q.offer(newPath);
            }
        }

        return res;
    }

    /*
    * 超时解2：
    * - 思路：本解法本质上和超时解1的思路是一样的，同样也是采用 BFS 遍历每一条路径。区别在于本解法中 queue 里存的不是代表路径的顶点列表，
    *   而是由 level, index, sum 三者确定的一条路径的最新状态，level, index 记录路径经过的最新顶点，sum 记录路径当前的节点值之和。
    * - 时间复杂度 O(2^n)：解释同超时解1。
    * - 空间复杂度 O(n)：queue 中每个元素只是 Path 对象，而非如超时解1中的整个顶点列表，因此不需要乘以 logn。
    * */
    static class Path {
        final int level, index, sum;  // immutable memebers
        public Path(int level, int index, int sum) {
            this.level = level;
            this.index = index;
            this.sum = sum;
        }
    }

    public static int minimumTotal0(List<List<Integer>> triangle) {
        int res = Integer.MAX_VALUE;
        Queue<Path> q = new LinkedList<>();
        q.offer(new Path(0, 0, triangle.get(0).get(0)));  // 队列中保存 <level, index, sum> 信息，sum 初始化为根顶点值

        while (!q.isEmpty()) {
            Path path = q.poll();
            int level = path.level;
            int index = path.index;
            int sum = path.sum;

            if (level == triangle.size() - 1) {  // 若已抵达 bottom level 则不再入队，只比较 sum
                res = Math.min(res, sum);
                continue;
            }

            for (int i = 0; i < 2; i++) {
                int adj = triangle.get(level + 1).get(index + i);    // 到下一层中取相邻顶点
                q.offer(new Path(level + 1, index + i, sum + adj));  // 每个相邻顶点都是一个分支，即产生一条新的路径
            }
        }

        return res;
    }

    /*
    * 解法1：bottom-up DP
    * - 思路：横向顶点直接进行比较，纵向层与层之间进行 reduce：
    *            -1                -1              -1
    *           /  \              /  \
    *          2    3     --->   1    0    --->
    *        /  \  /  \
    *       1    -1   -3
    *   在第三层中从 (1, -1) 中选出-1加到第二层的2上；(-1, -3) 中选出-3加到第二层的3上。在第二层中从 (1, 0) 中选出0加到
    *   第一层的-1上，得到最终结果-1。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
    * */
    public static int minimumTotal1(List<List<Integer>> triangle) {
        for (int i = triangle.size() - 1; i > 0; i--) {    // 从下到上遍历 triangle 中除了顶层以外的每一层。O(logn)（树高）
            List<Integer> upperLevel = triangle.get(i - 1);
            List<Integer> currLevel = triangle.get(i);

            for (int j = 0; j < upperLevel.size(); j++) {  // 合并 upperLevel 和 currLevel。O(n)（每层最多有 n/2 个节点）
                int min = Math.min(currLevel.get(j), currLevel.get(j + 1));
                upperLevel.set(j, upperLevel.get(j) + min);
            }
        }

        return triangle.get(0).get(0);
    }

    /*
    * 解法2：bottom-up DP
    * - 思路：与解法1一样。
    * - 时间复杂度 O(TODO: ????)，空间复杂度 O(n)。
    * */
    public static int minimumTotal2(List<List<Integer>> triangle) {
        int n = triangle.size();

        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = triangle.get(n - 1).get(i);  // 用 bottom level 初始化 arr

        for (int i = n - 2; i >= 0; i--)          // 从倒数第2层开始往上遍历
            for (int j = 0; j <= i; j++)          // 遍历每一层（第 i 层共有 i 个元素）
                arr[j] = Math.min(arr[j], arr[j + 1]) + triangle.get(i).get(j);

        return arr[0];
    }
    public static void main(String[] args) {
        log(minimumTotal2(Arrays.asList(
                Arrays.asList(2),
                Arrays.asList(3, 4),
                Arrays.asList(6, 5, 7),
                Arrays.asList(4, 1, 8, 3)
        )));  // expects 11 (2 + 3 + 5 + 1)

        log(minimumTotal2(Arrays.asList(
                Arrays.asList(-1),
                Arrays.asList(2, 3),
                Arrays.asList(1, -1, -3)
        )));  // expects -1 (-1 + 3 + -3) 注意不是从每行中找到最小值就行，如：(-1 + 2 + -3)

        log(minimumTotal2(Arrays.asList(
                Arrays.asList(-10)
        )));  // expects -10
    }
}
