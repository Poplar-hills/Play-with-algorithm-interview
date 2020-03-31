package StackAndQueue.S4_BFSAndGraphShortestPath;

import java.util.*;
import java.util.stream.Collectors;

import static Utils.Helpers.log;

/*
 * Cheapest Flights Within K Stops
 *
 * - There are "n" cities connected by "m" flights. Each flight starts from city "u" and arrives at "v" with
 *   a price "w". Given all the cities, flights, starting city "src" and the destination "dst", find the
 *   cheapest price from "src" to "dst" with up to "k" stops in the middle. Output -1 if there is no such route.
 *
 * - Note there will not be any duplicated flights or self cycles.
 * */

public class L787_CheapestFlightsWithinKStops {
    /*
     * 超时解：BFS
     * - 思路：先构建 graph，在 graph 上进行完整的 BFS（遍历所有顶点，而不是到达了终点就提前结束），并在 BFS 过程中不断比较，
     *   找到 cheapest price。
     * - 实现：∵ graph 的作用是在 BFS 时能够快速查询从任一 city 出发的所有航线，即能按 city 进行查找 ∴ 其结构应该是
     *   {city: List<flight>}。
     * - 时间复杂度：构建 graph 需要遍历所有航线，即所有边 ∴ 是 O(E)；而完整的 BFS 过程是 O(V+E)；∴ 整体是 O(V+E)，
     *   即 O(n+m)，其中 m 为 flights.length。
     * */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));  // 按 city 进行索引

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{src, 0, -1});  // q[i] 中的三个元素：[ciry, src->ciry 的 price, 当前线路上的 stop 个数]
                                         // 让 stop 个数从-1开始，这样经过1段航线后 stop 个数为0，经过2段航线后 stop 个数为1，符合题意
        int cheapestPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int city = curr[0], price = curr[1], stop = curr[2];

            if (!graph.containsKey(city) || stop == K) continue;
            for (int[] f : graph.get(city)) {
                if (f[1] == dst)
                    cheapestPrice = Math.min(cheapestPrice, price + f[2]);
                q.offer(new int[]{f[1], price + f[2], stop + 1});
            }
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    public static void main(String[] args) {
        int[][] flights1 = new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        /*
         *               ⓪
         *             ↙   ↘
         *       100 ↙       ↘ 500
         *         ↙           ↘
         *       ①  →  →  →  →  ②
         *              100
         * */
        log(findCheapestPrice(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice(3, flights1, 0, 2, 0));  // expects 500

        int[][] flights2 = new int[][]{
            {0, 1, 50}, {0, 2, 20}, {0, 3, 60}, {1, 4, 10},
            {2, 1, 10}, {2, 4, 50}, {2, 3, 30}, {3, 4, 20}};
        /*
         *                 ①
         *              ↗  ↑  ↘
         *         50↗   10↑     ↘10
         *        ↗        ↑        ↘
         *      ⓪ →  →  → ② →  →  → ④
         *        ↘   20   ↓   50   ↗
         *         60↘   30↓     ↗20
         *              ↘  ↓  ↗
         *                 ③
         * */
        log(findCheapestPrice(8, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice(8, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice(8, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice(8, flights2, 2, 0, 10));  // expects -1
    }
}
