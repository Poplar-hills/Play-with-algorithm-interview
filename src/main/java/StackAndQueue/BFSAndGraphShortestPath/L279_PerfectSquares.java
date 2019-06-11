package StackAndQueue.BFSAndGraphShortestPath;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

import static Utils.Helpers.log;

/*
* Perfect Squares
*
* - Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.
* */

public class L279_PerfectSquares {
    /*
    * 超时解：BFS
    * - 思路：将该问题建模成一个图论问题：从 n 到 0，每个数字表示一个节点；若两节点之间相差一个完全平方数，则链接两节点，由此得到一个
    *   无权图，并且该问题转化为了求该无权图中从 n 到 0 的最短路径。例如：
    *      n = 4:  0 -- 1 -- 2        n = 5:  0 -- 1 -- 2       n = 6:  0 -- 1 ---- 2
    *              |         |                |    |    |               |    |    / |
    *              4 ------- 3                |    5    |               |    5 - 6  |
    *                                         |    |    |               |    |      |
    *                                         +--- 4 -- 3               +--- 4 ---- 3
    * - 
    * */
    public static int numSquares(int n) {
        assert n > 0;
        Queue<Pair<Integer, Integer>> q = new LinkedList<>();  // Pair 中的 key 是图中的节点，value 表示在图中走过了几段路径到达该节点
        q.offer(new Pair<>(n, 0));  // 节点 n 作为 BFS 的起点，因此经过0段路径到达

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int num = pair.getKey();
            int step = pair.getValue();

            if (num == 0) return step;
            for (int i = 1; num - i * i >= 0; i++)
                q.offer(new Pair<>(num - i * i, step + 1));
        }
        throw new IllegalStateException("No Solution.");
    }

    public static void main(String[] args) {
        log(numSquares(12));  // expects 3. (12 = 4 + 4 + 4)
        log(numSquares(13));  // expects 2. (13 = 4 + 9)
    }
}
