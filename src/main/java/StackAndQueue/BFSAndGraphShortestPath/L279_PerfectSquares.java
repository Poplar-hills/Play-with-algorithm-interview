package StackAndQueue.BFSAndGraphShortestPath;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Utils.Helpers.log;

/*
* Perfect Squares
*
* - Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
* */

public class L279_PerfectSquares {
    /*
    * 解法1：BFS
    * - 思路：将该问题建模成一个图论问题：从 n 到 0，每个数字表示一个顶点；若两顶点之间相差一个完全平方数，则链接两顶点，由此得到一个
    *   无权图，并且该问题转化为了求该无权图中从 n 到 0 的最短路径的步数。例如：
    *                                    0 -- 1 -- 2                 0 -- 1 ---- 2
    *          0 -- 1 -- 2               |    |    |                 |    |    / |
    *          |         |               |    5    |                 |    5 - 6  |
    *          4 ------- 3               |    |    |                 |    |      |
    *      n = 4 时最短路径为1步           +--- 4 -- 3                 +--- 4 ---- 3
    *                                n = 5 时最短路径为2步          n = 6 时最短路径为3步
    * - 实现过程：要求图中两点的最短路径，可使用广度优先遍历（BFS）实现 —— 使用队列作为辅助数据结构，从起点开始，不断将所有相邻顶点
    *   加入队列，直到到达终点为止。∵ 最终要返回找到的最短路径的步数，因此队列中除了保存顶点之外还要有步数信息。
    * */
    public static int numSquares(int n) {
        assert n > 0;

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();  // Pair<顶点, 从起点到该顶点所走过的步数>
        q.offer(new Pair<>(n, 0));  // 顶点 n 作为 BFS 的起点，因此经过0步路径到达

        boolean[] visited = new boolean[n + 1];  // 从 n 到 0 需要开 n+1 的空间
        visited[n] = true;

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int num = pair.getKey();
            int step = pair.getValue();

            if (num == 0) return step;  // 若到达终点则返回路径步数
            for (int i = 1; num - i * i >= 0; i++) {  // 若还未到达终点则将所有与当前顶点相差一个完全平方数的顶点入队（这里相当于 BFS 中将所有相邻顶点入队）
                int next = num - i * i;
                if (!visited[next]) {   // 已访问过的节点不入队
                    q.offer(new Pair<>(next, step + 1));
                    visited[next] = true;
                }
            }
        }
        throw new IllegalStateException("No Solution.");  // 只要输入参数正确则不会到达这行 ∵ 所有正整数最终都可以用1相加得到
    }

    public static void main(String[] args) {
        log(numSquares(12));  // expects 3. (12 = 4 + 4 + 4)
        log(numSquares(13));  // expects 2. (13 = 4 + 9)
    }
}
