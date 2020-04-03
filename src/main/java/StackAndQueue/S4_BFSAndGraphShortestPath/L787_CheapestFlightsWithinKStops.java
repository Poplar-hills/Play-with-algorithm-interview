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
     *     3. 在查找相邻顶点时，若到达某一相邻顶点的 price 已经超过之前找到的 cheapestPrice，则需要进行剪枝，这是最关键的
     *        性能优化点，若没有会超时。
     *     4. BFS 过程中不能对顶点使用类似 L127 中 visited/unvisited 的重复访问检查 ∵ 要找的是 price 最小的路径 ∴ 要
     * - 时间复杂度：O(V+E)，即 O(n+m)，其中 m 为航线条数（flights.length）：
     *     1. 构建 graph 需要遍历所有航线，即所有边 ∴ 是 O(E)，即 O(m)；
     *     2. ∵ graph 更类似邻接表 ∴ 在 graph 上进行 BFS 是 O(V+E)，即 O(n+m)。
     * - 空间复杂度 O(n+m)。
     * */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));  // 按 city 进行索引

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{src, 0, 0});  // q[i] 中的三个元素：[ciry, src->ciry 的 price, 当前线路上的 stop 个数]
                                         // 让 stop 个数从-1开始，这样经过1段航线后 stop 个数为0，经过2段航线后 stop 个数为1，符合题意
        int cheapestPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int city = curr[0], price = curr[1], numOfStop = curr[2];

            if (!graph.containsKey(city) || numOfStop > K) continue;  // 若该没有从该 city 出发的航线，或 stop 个数达到 K 则停止前进

            for (int[] f : graph.get(city)) {
                if (price + f[2] >= cheapestPrice) continue;      // 剪枝（Pruning）
                if (f[1] == dst)
                    cheapestPrice = Math.min(cheapestPrice, price + f[2]);
                q.offer(new int[]{f[1], price + f[2], numOfStop + 1});
            }
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /*
     * 解法2：BFS（解法1的一次一层版）
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
        int numOfStop = 0;
        int cheapestPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            for (int i = 0, qSize = q.size(); i < qSize; i++) {   // qSize is fixed
                int[] curr = q.poll();
                int city = curr[0], price = curr[1];

                if (!graph.containsKey(city) || numOfStop > K) continue;

                for (int[] f : graph.get(city)) {
                    if (price + f[1] >= cheapestPrice) continue;  // 剪枝（Pruning）
                    if (f[0] == dst)
                        cheapestPrice = Math.min(cheapestPrice, price + f[1]);
                    q.offer(new int[]{f[0], price + f[1]});
                }
            }
            if (++numOfStop > K) break;  // 在每层顶点遍历完之后再让 numOfStop++，并再与 K 比较一次（性能优化）
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /*
     * 解法3：DFS
     * - 思路：非常 straightforward，从起点开始对由城市和航线组成的图进行完整的 DFS，并找到所有到达终点的路径里的最小 price。
     * - 实现：与解法1的唯一区别就是用 Stack 代替 Queue 实现 DFS。
     * - 时间复杂度：在邻接表上进行 DFS 是 O(V+E)，即 O(n+m)；空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice3(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{src, 0, 0});
        int cheapestPrice = Integer.MAX_VALUE;

        while (!stack.isEmpty()) {
            int[] curr = stack.pop();
            int city = curr[0], price = curr[1], numOfStop = curr[2];

            if (!graph.containsKey(city) || numOfStop > K) continue;

            for (int[] f : graph.get(city)) {
                if (price + f[2] >= cheapestPrice) continue;      // 剪枝（Pruning）
                if (f[1] == dst)
                    cheapestPrice = Math.min(cheapestPrice, price + f[2]);
                stack.push(new int[]{f[1], price + f[2], numOfStop + 1});
            }
        }

        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    /*
     * 解法4：DFS（解法3的递归版）
     * - 思路：与解法3一致。
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    private static int cheapestPrice;

    public static int findCheapestPrice4(int n, int[][] flights, int src, int dst, int K) {
        cheapestPrice = Integer.MAX_VALUE;                      // 不在上面赋值是为了让 test case 之间不互相影响
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));
        helper(graph, src, dst, K + 1, 0);
        return cheapestPrice == Integer.MAX_VALUE ? -1 : cheapestPrice;
    }

    private static void helper(Map<Integer, List<int[]>> graph, int city, int dst, int K, int currPrice) {
        if (K < 0) return;
        if (city == dst) {
            cheapestPrice = currPrice;  // 每次找到到达终点的路径就直接赋值，下面的剪枝保证了这里最后取到的是最小 price
            return;
        }
        if (!graph.containsKey(city)) return;
        for (int[] f : graph.get(city)) {
            if (currPrice + f[2] >= cheapestPrice) continue;  // 剪枝（Pruning）
            helper(graph, f[1], dst, K - 1, currPrice + f[2]);
        }
    }

    /*
     * 解法5：Dijkstra
     * - 思路：本题是个典型的带权图，而 Dijkstra 算法正适用于计算带权图的最短路径树（即从起点到每个顶点的最短路径）。
     * - 实现：∵ 本题中需要的只是从起点到终点的最短路径 ∴ 无需对每个顶点进行松弛（relaxation），只要按权值（price）从小到大的
     *   顺序访问相邻顶点，则第一条到达终点的路径即是 cheapest price 路径。
     * - 💎 Dijkstra vs. BFS：
     *   - 本题中的 Dijkstra 实现其实就是采用了 PriorityQueue 的 BFS；
     *   - Dijkstra 算法依赖于图论的一个特性 —— 图上从 s → t 的最短路径同时也是从 s 到达该路径上任意一个顶点的最短路径。
     *     例如 test case 2 中，从 0 → 4 的最短路径同时也是 0 → 1、0 → 2 的最短路径。
     *   - 从另一个角度看，若图上所有边的权值都为1，则 Dijkstra 其实就是 BFS。
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice5(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));

        PriorityQueue<int[]> pq = new PriorityQueue<>((c1, c2) -> c1[1] - c2[1]);  // 基于 price 的最小堆
        pq.offer(new int[]{src, 0, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();    // ∵ pq 是基于 price 的最小堆 ∴ 每次 poll 到的都是 price 最小相邻 city
            int city = curr[0], price = curr[1], numOfStop = curr[2];

            if (city == dst) return price;  // 第一个到达终点的路径的 price 即是 cheapest price
            if (!graph.containsKey(city) || numOfStop > K) continue;

            for (int[] f : graph.get(city))
                pq.offer(new int[]{f[1], price + f[2], numOfStop + 1});
        }

        return -1;
    }

    /*
     * 解法6：Bellman-Ford
     * - 思路：用于
     * - 💎 应用：Bellman-Ford 适用于有负权边的图
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice6(int n, int[][] flights, int src, int dst, int K) {
        return -1;
    }

    /*
     * 解法7：DP
     * - 思路：
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice7(int n, int[][] flights, int src, int dst, int K) {
        return -1;
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
        log(findCheapestPrice5(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice5(3, flights1, 0, 2, 0));  // expects 500

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
        log(findCheapestPrice5(8, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice5(8, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice5(8, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice5(8, flights2, 2, 0, 10));  // expects -1
    }
}
