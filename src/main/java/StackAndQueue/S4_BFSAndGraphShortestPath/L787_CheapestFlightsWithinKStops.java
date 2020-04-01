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
     * 解法1：BFS
     * - 思路：非常 straightforward，从起点开始对由城市和航线组成的图进行完整的 BFS（遍历所有顶点，而不是到达了终点就提前结束），
     *   并找到所有到达终点的路径里的最小 price。
     * - 实现：
     *     1. 要进行 BFS，需要先构建 graph，而 ∵ graph 的作用是在 BFS 时能够快速找到从任一 city 出发的所有航线（找相邻顶点），
     *        即能按 city 进行查找 ∴ 其结构应该是 {city: List<flight>}；
     *     2. 在 BFS 过程中，将路径的 price 和 stop 个数带在每个节点上。
     * - 时间复杂度：构建 graph 需要遍历所有航线，即所有边 ∴ 是 O(E)；而完整的 BFS 过程是 O(V+E)；∴ 整体是 O(V+E)，
     *   即 O(n+m)，其中 m 为航线条数（flights.length）。
     * - 空间复杂度 O(n+m)。
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

            if (!graph.containsKey(city) || stop == K) continue;  // 若该没有从该 city 出发的航线，或 stop 个数达到 K 则停止前进

            for (int[] f : graph.get(city)) {
                if (price + f[2] >= cheapestPrice) continue;
                if (f[1] == dst)
                    cheapestPrice = Math.min(cheapestPrice, price + f[2]);
                q.offer(new int[]{f[1], price + f[2], stop + 1});
            }
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /*
     * 解法2：BFS 2
     * - 思路：与解法1一致。
     * - 实现：与解法1相比：
     *   1. graph 的生成采用普通遍历（putIfAbsent 方法），并且去掉了每条航线的起点，只保留终点和 price 两个元素；
     *   2. while 内部采用一次遍历完该层所有顶点的实现；
     *   3. ∵ 每轮 while 遍历完一层的所有顶点，而从起点到每层的各个顶点的步数是一样的 ∴ 可以在 while 外部记录经过的 stop
     *      个数（numOfStop），而不用将该信息带在 q 中的每个顶点上。
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice2(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] f : flights) {
            graph.putIfAbsent(f[0], new ArrayList<>());
            graph.get(f[0]).add(new int[]{f[1], f[2]});
        }

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{src, 0});
        int numOfStop = -1;
        int cheapestPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            for (int i = 0, qSize = q.size(); i < qSize; i++) {  // qSize is fixed
                int[] curr = q.poll();
                int city = curr[0], price = curr[1];

                if (!graph.containsKey(city) || numOfStop == K) continue;

                for (int[] f : graph.get(city)) {
                    if (f[0] == dst)
                        cheapestPrice = Math.min(cheapestPrice, price + f[1]);
                    if (price + f[1] >= cheapestPrice) continue;
                    q.offer(new int[]{f[0], price + f[1]});
                }
            }
            if (++numOfStop == K) break;  // 在每层顶点遍历完之后再让 numOfStop++，并再与 K 比较一次（性能优化）
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /*
     * 解法3：DFS
     * - 思路：非常 straightforward，从起点开始对由城市和航线组成的图进行完整的 DFS，并找到所有到达终点的路径里的最小 price。
     * - 实现：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static int findCheapestPrice3(int n, int[][] flights, int src, int dst, int K) {
        
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
        log(findCheapestPrice3(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice3(3, flights1, 0, 2, 0));  // expects 500

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
        log(findCheapestPrice3(8, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice3(8, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice3(8, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice3(8, flights2, 2, 0, 10));  // expects -1
    }
}
