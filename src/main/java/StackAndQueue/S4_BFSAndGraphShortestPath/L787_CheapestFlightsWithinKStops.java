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
 * - Note:
 *   - k ∈ [0, n-1]；
 *   - The price of each flight will be in the range [1, 10000] -- 即不会有负权边；
 *   - There will not be any duplicated flights or self cycles -- 即没有平行边或自环边。
 *
 * - 💎 与 L126_WordLadderII 比较：
 *   ∵ L126_WordLadderII 中的图是无权图 ∴ 可以采用解法2的"构建邻接表 + BFS + DFS"的方式搜索所有最短路径；而本题中的图是
 *   带权图 ∴ 通过 BFS 生成 int[] minStep 数组对搜索最小权路径没有帮助（∵ 要求的是最小权路径而非最短路径）∴ 只能采用朴素
 *   BFS、DFS、Dijkstra、Bellman-Ford。
 *
 * - 💎 不同图下最短路径问题的最优解法：
 *   - 无权图：BFS
 *   - 有权图（无负权边）：Dijkstra
 *   - 有权图（有负权边）：BellmanFord
 * */

public class L787_CheapestFlightsWithinKStops {
    /*
     * 解法1：BFS
     * - 思路：与 L126_WordLadderII 解法1类似，非常 straightforward，从起点开始对由城市和航线组成的图进行完整的 BFS
     *   （遍历所有顶点，而不是到达了终点就提前结束），找到所有到达终点的路径，并返回其中最小的 price。
     * - 💎 实现：本解法采用先构建 graph，再在 graph 上进行 BFS 的方式实现：
     *     1. ∵ graph 是用来在 BFS 时能根据任一顶点找到所有相邻顶点（即需根据任意一个起始城市，找到所有从该城市出发的航线）
     *        ∴ graph 要能支持对起始城市的随机访问 ∴ graph 的结构要么是：
     *        a). 邻接矩阵/邻接表：int[][] 或 List<List<Integer>>（通常使用顶点的索引进行查询）；
     *        b). 哈希表：Map<city, List<flight>>。
     *     2. ∵ 最终要求的是 price，且 stop 个数是限制条件 ∴ 在 BFS 过程中要将路径的 price 和 stop 个数带在每个顶点上。
     *        在查找相邻顶点时，若到达某一相邻顶点的 price 已经超过之前找到的 minPrice，则进行剪枝。
     *     3. ∵ 要求的是不同路径的最小 price，而不同路径可能会经过相同的顶点（联想 Dijkstra 的松弛操作）∴ BFS 过程中不能
     *        对顶点使用 visited/unvisited 的重复访问检查。
     * - 💎 总结：本题与 L127_WordLadder 解法1对照可发现：
     *   - 若求无权图上的最短路径，则用 BFS 找到第一条到达终点的路径即可，分支时要对顶点做 visited/unvisited 判断。
     *   - 若求带权图上的最小权路径，则用 BFS/DFS 遍历所有到达终点的路径，且分支时不能做 visited/unvisited 判断（∵ 可能
     *     会出现经过更多顶点，但整体权值更小的情况，即需要进行松弛的情况）∴ 需允许重复访问顶点。
     * - 时间复杂度：O(V+E)，即 O(n+m)，其中 m 为航线条数（flights.length）：
     *     1. 构建 graph 需要遍历所有航线，即所有边 ∴ 是 O(E)，即 O(m)；
     *     2. ∵ graph 更类似邻接表 ∴ 在 graph 上进行 BFS 是 O(V+E)，即 O(n+m)。
     * - 空间复杂度 O(n+m)。
     * */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)  // Map<city, List<flight>>
            .collect(Collectors.groupingBy(f -> f[0]));          // 按起始城市进行分组
//        int[][] graph = new int[n][n];  // 也可以用传统方式构建邻接矩阵/邻接表
//        for (int[] f : flights)
//            graph[f[0]][f[1]] = f[2];  // 注意该问题中的图是有向图 ∴ 不能 graph[i][j] = graph[j][i] = xxx;

        Queue<int[]> q = new LinkedList<>();  // Queue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>
        q.offer(new int[]{src, 0, -1});       // 注意 stopCount 要从-1开始 ∵ K 表示的是不包含 src、dst 的步数

        int minPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] path = q.poll();
            int city = path[0], totalPrice = path[1], stopCount = path[2];

            if (city == dst) {
                minPrice = Math.min(minPrice, totalPrice);   // ∵ 要找到所有 src->dst 的路径 ∴ 当到一条通路之后不能 return
                continue;
            }
            if (!graph.containsKey(city) || stopCount == K)  // 剪枝（Pruning）
                continue;

            for (int[] flight : graph.get(city))        // 遍历从 city 出发的所有航线
                if (totalPrice + flight[2] < minPrice)  // 剪枝
                    q.offer(new int[]{flight[1], totalPrice + flight[2], stopCount + 1});
        }

        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    /*
     * 解法2：BFS（解法1的一次一层版）
     * - 思路：与解法1一致。
     * - 实现：与解法1相比：
     *   1. graph 的生成采用普通遍历（putIfAbsent 方法），并且去掉了每条航线的起点，只保留终点和 price 两个元素；
     *   2. while 内部一次遍历完一层所有顶点 ∴ 起点到每层的各个顶点的步数是一样的 ∴ 可以在 while 外部使用变量 stopCount 记录
     *      经过的 stop 个数，而不用将该信息带在 q 中的每个顶点上。
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice2(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] f : flights) {
            graph.putIfAbsent(f[0], new ArrayList<>());
            graph.get(f[0]).add(new int[]{f[1], f[2]});  // List 中的每个元素只保留目的地、价格信息
        }

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{src, 0});
        int stopCount = -1;                // 注意 stopCount 要从-1开始
        int minPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            for (int i = 0, qSize = q.size(); i < qSize; i++) {   // qSize is fixed
                int[] path = q.poll();
                int city = path[0], totalPrice = path[1];

                if (city == dst) {
                    minPrice = Math.min(minPrice, totalPrice);
                    continue;
                }
                if (!graph.containsKey(city))
                    continue;

                for (int[] flight : graph.get(city))
                    if (totalPrice + flight[1] < minPrice)  // 剪枝（Pruning）
                        q.offer(new int[]{flight[0], totalPrice + flight[1]});
            }
            if (++stopCount > K) break;  // 在每层顶点遍历完之后再让 stopCount++，再与 K 比较
        }

        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    /*
     * 解法3：DFS
     * - 思路：即解法1、2的 DFS 版，从 src 开始对图进行完整的 DFS，找到所有到达 dst 的路径里的最小 price。
     * - 实现：与解法1的唯一区别就是用 Stack 代替 Queue 来实现 DFS 遍历。
     * - 时间复杂度：在邻接表上进行 DFS 是 O(V+E)，即 O(n+m)；空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice3(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));

        Stack<int[]> stack = new Stack<>();  // Stack<[city, 该路径的 totalPrice, 该路径上的 stopCount]>
        stack.push(new int[]{src, 0, -1});   // 注意 stopCount 要从-1开始 ∵ K 表示的是不包含 src、dst 的步数
        int minPrice = Integer.MAX_VALUE;

        while (!stack.isEmpty()) {
            int[] path = stack.pop();
            int city = path[0], totalPrice = path[1], stopCount = path[2];

            if (city == dst) {
                minPrice = Math.min(minPrice, totalPrice);
                continue;
            }
            if (!graph.containsKey(city) || stopCount == K)
                continue;

            for (int[] flight : graph.get(city))
                if (totalPrice + flight[2] < minPrice)
                    stack.push(new int[]{flight[1], totalPrice + flight[2], stopCount + 1});
        }

        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    /*
     * 解法4：DFS（解法3的递归版）
     * - 思路：与解法3一致。
     * - 时间复杂度 O(n+m)，空间复杂度 O(n+m)，其中 m 为航线条数（flights.length）。
     * */
    public static int findCheapestPrice4(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
                .collect(Collectors.groupingBy(f -> f[0]));
        int minPrice = dfs4(graph, src, dst, 0, K + 1);
        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    private static int dfs4(Map<Integer, List<int[]>> graph, int src, int dst, int totalPrice, int k) {
        if (src == dst) return totalPrice;

        int minPrice = Integer.MAX_VALUE;
        if (!graph.containsKey(src) || k == 0) return minPrice;

        for (int[] f : graph.get(src))
            minPrice = Math.min(minPrice, dfs4(graph, f[1], dst, totalPrice + f[2], k - 1));

        return minPrice;
    }

    /*
     * 解法5：简化版的 Dijkstra（即 BFS + Priority Queue，性能优于解法1-3）
     * - 👉 前提：先看完 Play-with-algorithms/src/main/java/ShortestPath/Dijkstra.java 中的介绍。
     * - 💎 思路：不同于解法1、2中使用 BFS/DFS 搜索两点间的所有路径 + 比较路径权值的思路，该解法采用化简版的 Dijkstra 算法：
     *   - 标准版的 Dijkstra 要通过对所有顶点进行 relaxation 操作来生成带权图的最短路径树（即从一个起点到每个顶点的最小权路径）；
     *   - 但 ∵ 本题中需要的只是从起点到终点的最短路径，无需求出起点到每个顶点的最短路径 ∴ 只要按边的权值从小到大的顺序访问每个
     *     顶点的相邻顶点，则第一条到达终点的路径即是最短路径（无需进行 relaxation）。
     * - 💎 实现：
     *   1. 该解法中的 Dijkstra 本质上是用 PriorityQueue 替换了普通 BFS 中的 Queue；
     *   2. 与 L126_WordLadderII 解法2一样，Dijkstra 算法也依赖于图的一个特性 —— 图上两点之间的最短路径，同时也是从起点
     *      到该路径上各顶点的最短路径。若反过来利用该特性，从起点开始用 BFS 一层层的查找每个顶点的最短邻边（最小权边），这样
     *      找到的第一条到达终点的路径即是两点之间的最短路径。演算过程：
     *                    ①
     *                 ↗  ↑  ↘          - 求从 ⓪ 到 ④ 的最短路径，K=2：每次都找最小权边，于是有 ⓪->②->①->④。
     *            50↗   10↑     ↘10     - 求从 ⓪ 到 ④ 的最短路径，K=1：
     *           ↗        ↑        ↘      1. 先沿着找最小权边走 ⓪->②->①，发现无法在 K 个中间点之内到达 ④ ∴ 回到
     *         ⓪ →  →  → ② →  →  → ④       上一顶点 ②；
     *           ↘   20   ↓   50   ↗      2. 从 ② 沿着权值倒数第二小的边走 ⓪->②->③，仍然无法在 K 个中间点之内到
     *            60↘   30↓     ↗20          达 ④ ∴ 回到上一顶点 ②；
     *                 ↘  ↓  ↗            3. 从 ② 沿着权值倒数第三小的边走 ⓪->②->④，可到达 ④ ∴ 得到最低价格60。
     *                    ③
     *   3. 从另一个角度看，当图上所有边的权值都相等时，Dijkstra 算法就退化成了 BFS。
     * - 时间复杂度：标准的 Dijkstra 实现是 O(ElogV)，但 ∵ 该解法中没有进行松弛操作，只是沿着最短路径前进直到 dst，因此：
     *   - 从 src 到达 dst 最多经过 V-1 个顶点；
     *   - 对每个顶点使用 min heap 从 V-1 条路径中找到最小的一条，O(logV)；
     *   ∴ 总体时间复杂度 O(VlogV)，即 O(nlogn)，空间复杂度 O(n+m)。
     * */
    public static int findCheapestPrice5(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);  // 基于 totalPrice 的最小堆
        pq.offer(new int[]{src, 0, -1});  // PriorityQueue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>

        while (!pq.isEmpty()) {
            int[] path = pq.poll();  // 👉🏻 ∵ 每次 poll 出来的都是从上一顶点出发 price 最小的相邻顶点 ∴ 也就是最短路径上的下一顶点
            int city = path[0], totalPrice = path[1], stopCount = path[2];

            if (city == dst) return totalPrice;  // 第一个到达终点的路径的 totalPrice 即是 minPrice
            if (!graph.containsKey(city) || stopCount == K) continue;

            for (int[] flight : graph.get(city))
                pq.offer(new int[]{flight[1], totalPrice + flight[2], stopCount + 1});
        }

        return -1;
    }

    /*
     * 解法6：简化版的 Dijkstra（解法5的性能优化版）
     * - 思路：在解法5的基础上通过剪枝进一步优化 —— ∵ 在 Dijkstra 的过程中，同一个顶点可能被多次访问，但若该顶点在之前已经
     *   沿着 PriorityQueue 中的最短路径被访问过（即已找到了 src 到达该顶点的最短路径），则再次访问时的 total price 一定
     *   更大 ∴ 则无需再次再次访问。
     * - 例：在上例中增加一条 ①->②，price=5 的路径：
     *                    ①
     *                 ↗  ↑↓  ↘          - 若求从 ⓪ 到 ④ 的最短路径，K=2：每次都找最小权边，于是会：
     *            50↗   10↑↓5    ↘10       1. 从 ⓪->①、⓪->②、⓪->③ 中选出最短路径 ⓪->②，继续访问 ② 的邻边；
     *           ↗        ↑↓        ↘      2. 从 ②->①、②->④、②->③ 中选出最短路径 ②->①，继续访问 ① 的邻边；
     *         ⓪ →  →  → ② →  →  → ④     3. 从 ①->②、①->④ 中选出最短路径 ①->②，但由于 ② 已被访问过，且之前的
     *           ↘   20   ↓   50   ↗          stopCount 为1 < 当前的 stopCount 3，其 total price 必定比之前更大
     *            60↘   30↓     ↗20           ∴ 剪枝该路径，转而选取第二最短路径 ①->④，并得到解。
     *                 ↘  ↓  ↗
     *                    ③
     * - 实现：
     *   - 维护一个 stopCounts 数组，stopCounts[i] 表示从 src 到顶点 i 的最少中间顶点数量；
     *   - 与解法5一样，该解法同样没有进行松弛操作，只是沿着最短路径前进直到 dst，但相比解法5多了剪枝条件以提高效率。
     * - 时间复杂度：O(VlogV)，即 O(nlogn)，空间复杂度 O(n+m)。实际在 Leetcode 上，该解法是解法1-8中唯一不超时的。
     * */
    public static int findCheapestPrice6(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
                .collect(Collectors.groupingBy(f -> f[0]));

        int[] stopCounts = new int[n];
        Arrays.fill(stopCounts, Integer.MAX_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{src, 0, -1});  // PriorityQueue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>

        while (!pq.isEmpty()) {
            int[] path = pq.poll();
            int city = path[0], totalPrice = path[1], stopCount = path[2];

            if (city == dst) return totalPrice;

            if (!graph.containsKey(city) || stopCount == K || stopCount > stopCounts[city])  // 新加了第三个剪枝条件
                continue;
            stopCounts[city] = Math.min(stopCounts[city], stopCount);  // 通过 PriorityQueue 第一次访问某一顶点的路径已经是最短路径

            for (int[] flight : graph.get(city))
                pq.offer(new int[]{flight[1], totalPrice + flight[2], stopCount + 1});
        }

        return -1;
    }

    /*
     * 解法7：标准版的 Dijkstra
     * - 思路：标准的 Dijkstra 算法会对所有节点进行 relaxation 操作，最终为带权图生成一棵完整的最短路径树，即从起点到
     *   所有顶点的最短路径数组 minPrices，其中 minPrices[i] 表示从 src 到顶点 i 的最小 price ∴ 最后只需返回
     *   minPrices[dst] 即可。
     * - 基础：松弛操作 SEE: https://coding.imooc.com/lesson/71.html#mid=1495 (6'24'')
     * - 实现：
     *   1. ∵ 航线图通常非常密集 ∴ 本解法的 graph 采用邻接矩阵（Adjacency Matrix）；
     *   2. 邻接矩阵是通过索引查询，该解法中假设了城市名就是城市索引（这点题中没有明确说明，但 test case 中就是这样）。
     *   3. Dijkstra 的过程：
     *      - 与解法5相同点：使用优先队列，每次 poll 出最短路径上的下一个顶点；
     *      - 与解法5不同点：多了松弛操作 —— 为每次 poll 出的路径中的顶点的邻边进行松弛，若松弛过程中找到了 price 更低
     *        或 stopCount 更少的路径，则将该顶点重新入队，再次进行松弛；
     *   4. 维护一个 stopCounts 数组用于记录从 src 到各个顶点 i 的中间顶点数；
     *   5. 不再在第一次到达 dst 时返回结果，而是对所有顶点进行 relaxation 并生成了完整的最短路径树后再返回 minPrices[dst]。
     * - 时间复杂度：即标准的 Dijkstra 的时间复杂度 O(ElogV)，也就是 O(mlogn)。
     * - 空间复杂度：O(n+m)。
     * */
    public static int findCheapestPrice7(int n, int[][] flights, int src, int dst, int K) {
        int[][] graph = new int[n][n];   // adjacency matrix
        for (int[] f : flights)
            graph[f[0]][f[1]] = f[2];    // graph[src][dst] = price

        int[] minPrices = new int[n];    // 从 src 出发到各顶点的 min price
        int[] minStops = new int[n];   // 从 src 出发到各顶点的 min stop count
        Arrays.fill(minPrices, Integer.MAX_VALUE);
        Arrays.fill(minStops, Integer.MAX_VALUE);
        minPrices[src] = 0;
        minStops[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{src, 0, -1});  // PriorityQueue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>

        while (!pq.isEmpty()) {
            int[] path = pq.poll();
            int city = path[0], totalPrice = path[1], stopCount = path[2];

            if (stopCount == K) continue;

            for (int adj = 0; adj < n; adj++) {  // 遍历所有顶点
                if (graph[city][adj] > 0) {      // 找到 city 的所有相邻顶点，开始松弛操作（relax all neighboring edges）
                    int newPrice = totalPrice + graph[city][adj];
                    int newStopCount = stopCount + 1;

                    if (newPrice < minPrices[adj] || newStopCount < minStops[adj])  // 若经过 adj 得到了更小的 price/stopCount，则再次入队 adj
                        pq.offer(new int[]{adj, newPrice, newStopCount});  // 基于新的 price/stopCount 对 adj 的所有邻边进行松弛

                    minPrices[adj] = Math.min(minPrices[adj], newPrice);   // 更新记录
                    minStops[adj] = Math.min(minStops[adj], newStopCount);  // 也可以直接覆盖而不比较大小
                }
            }
        }

        return minPrices[dst] == Integer.MAX_VALUE ? -1 : minPrices[dst];
    }

    /*
     * 解法8：Bellman-Ford
     * - 前提：先理解 Bellman-Ford 的过程演示：https://www.youtube.com/watch?v=obWXjtg0L64&vl=en（0'35''）。
     * - 思路：虽然题中说了不会有负权边，但可以使用 Dijkstra 的场景就一定可以使用 Bellman-Ford（虽然算法复杂度大很多）。
     * - 原理：假设图中可能存在负权边，则经过更多顶点的路径可能总距离反而更短。这时 Dijkstra 的贪心策略就会失效，不再能保证
     *   第一条到达终点的路径就是最短的。解决方案是反复对所有边进行松弛操作，使得起点到每个顶点的距离逐步逼近其最短距离（这也
     *   是 Bellman-Ford 的核心原理）。
     * - 实现：
     *   1. 标准的 Bellman-Ford 算法最多会迭代 V-1 次，而本题中 ∵ 题意限制了中间顶点个数 <= K，而 K <= V-2（顶点数
     *      V-2 即是最大的中间顶点数）∴ V-1 >= K+1 ∴ 最多迭代 K+1 次；
     *   2. ∵ 只迭代 K+1 次，而非 V-1 次 ∴ 最终得到的 prices 不会包含起点到所有顶点的最短路径 ∴ 需要做到每次迭代之间
     *      互不影响 ∴ 需要在迭代开始之前先 copy 一份 prices，让迭代中的更新都发生在这份 copy 里，迭代结束之后再将其赋给
     *      prices（若是标准实现，迭代 V-1 次，则不需要这种处理，这一点通过 test case 1、2 可更好的理解）。
     * - 💎 Bellman-Ford vs. Dijkstra：
     *   1. 若图中存在负权边，则应使用 Bellman-Ford，否则使用 Dijkstra 效率更优；
     *   2. 在思想上，Dijkstra 是基于 BFS + 贪心；而 Bellman-Ford 是基于 DP，在多次迭代中趋近最优解。
     *   3. 在实现上，Dijkstra 需先构建 graph，并用 PriorityQueue 找到最短路径；而 Bellman-Ford 无需构建 graph，
     *      而是在 V-1 次迭代中对所有已达到的顶点的邻边进行松弛。
     * - 时间复杂度为 O(EV)，即 O(mn)，空间复杂度 O(V)，即 O(n)。
     * */
    public static int findCheapestPrice8(int n, int[][] flights, int src, int dst, int K) {
        int[] minPrices = new int[n];   // Bellman-Ford 的基本形式是填充最短路径树数组（同标准版的 Dijkstra）
        Arrays.fill(minPrices, Integer.MAX_VALUE);
        minPrices[src] = 0;

        for (int i = 0; i <= K; i++) {                 // 迭代 K+1 次
            int[] copy = Arrays.copyOf(minPrices, n);  // 先拷贝一份，保证下面 minPrices[s] 读到的值不是被 copy[d] 覆盖过的
            for (int[] f : flights) {                   // 无需提前构建 graph，只需在每次迭代中遍历所有边，对每条边进行松弛
                int s = f[0], d = f[1], price = f[2];
                if (minPrices[s] == Integer.MAX_VALUE) continue;  // 跳过起点未被访问过的边（∵ 无法进行松弛操作）
                copy[d] = Math.min(copy[d], minPrices[s] + price);  // 松弛
            }
            minPrices = copy;                          // 迭代结束时更新 minPrices
        }

        return minPrices[dst] == Integer.MAX_VALUE ? -1 : minPrices[dst];  // 通过 minPrices[dst] 取得最短路径
    }

    /*
     * 解法9：DP
     * - 思路：与解法8 Bellman-Ford 算法的原理完全一致（其实还是要基于 Bellman-Ford 的原理去理解，否则不能 make sense）
     *   - 子问题定义：f(k,c) 表示“在 k-1 个中间 stop 之内从起点到达城市 c 的最小 price”
     *   - 状态转移方程：f(k,c) = min(f(k-1,c), f(k-1,s) + price)
     *         k\c |  0   1   2   3   4
     *        -----+---------------------
     *          0  |  0   ∞   ∞   ∞   ∞
     *          1  |  0   50  20  60  ∞     - 在0个 stop 之内从 src → c 的最小 price
     *          2  |  0   30  20  50  60    - f(2,1) 会被更新两次：∞→50→30；f(2,3) 也会被更新两次：∞→60→50
     *          3  |  0   30  20  50  40
     * - 时间复杂度 O(EV)，空间复杂度 O(nm)，空间复杂度 O(n^2)。该解法的统计性能在解法1-9中最高。
     * */
    public static int findCheapestPrice9(int n, int[][] flights, int src, int dst, int K) {
        int[][] dp = new int[K + 2][n];  // dp[k][c] 表示在 k-1 个 stop 内从 src 到达城市 c 的最小 price
        for (int[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE);
        dp[0][src] = 0;

        for (int k = 1; k < K + 2; k++) {  // 迭代 K+1 次
            dp[k][src] = 0;
            for (int[] f : flights) {       // 每次迭代都遍历所有邻边（相当于 Bellman-Ford 中的松弛操作）
                int s = f[0], d = f[1], price = f[2];
                if (dp[k - 1][s] == Integer.MAX_VALUE) continue;  // 跳过起点未被访问过的边（∵ 无法进行松弛操作）
                dp[k][d] = Math.min(dp[k][d], dp[k - 1][s] + price);  // f(k,d) 取决于 f(k-1,s) + s→d 的 price
            }
        }

        return dp[K + 1][dst] == Integer.MAX_VALUE ? -1 : (int)dp[K + 1][dst];
    }

    /**
     * 解法10：DP（解法9的空间优化版）
     * - 实现：∵ 解法9的状态转移过程中只用到了 dp 数组中的最后两行 ∴ 可以将 dp 数组初始化为 int[2][n]。
     * - 时间复杂度 O(EV)，空间复杂度 O(nm)，空间复杂度 O(n)。
     */
    public static int findCheapestPrice10(int n, int[][] flights, int src, int dst, int K) {
        int[][] dp = new int[2][n];
        for (int[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE);
        dp[0][src] = 0;
        int row = 0;  // the row in use

        for (int k = 1; k < K + 2; k++) {
            row = k % 2;
            dp[row][src] = 0;
            for (int[] f : flights) {
                int s = f[0], d = f[1], price = f[2];
                int prevRow = Math.abs(row - 1);
                if (dp[prevRow][s] == Integer.MAX_VALUE) continue;
                dp[row][d] = Math.min(dp[row][d], dp[prevRow][s] + price);  // Math.abs(row-1) 即 the row not in use
            }
        }

        return dp[row][dst] == Integer.MAX_VALUE ? -1 : (int)dp[row][dst];
    }

    public static void main(String[] args) {
        int[][] flights1 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        /*
         *               ⓪
         *             ↙   ↘
         *       100 ↙       ↘ 500
         *         ↙           ↘
         *       ①  →  →  →  →  ②
         *              100
         * */
        log(findCheapestPrice10(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice10(3, flights1, 0, 2, 0));  // expects 500

        int[][] flights2 = {
                {0, 1, 50}, {0, 2, 20}, {0, 3, 60}, {1, 4, 10},
                {2, 1, 10}, {1, 2, 5}, {2, 4, 50}, {2, 3, 30}, {3, 4, 20}
        };
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
        log(findCheapestPrice10(5, flights2, 0, 4, 3));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice10(5, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice10(5, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice10(5, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice10(5, flights2, 2, 0, 4));   // expects -1

        int[][] flights3 = {{0, 1, 5}, {1, 2, 5}, {0, 3, 2}, {3, 1, 2}, {1, 4, 1}, {4, 2, 1}};
        log(findCheapestPrice10(5, flights3, 0, 2, 2));   // expects 7
        log(findCheapestPrice10(5, flights3, 0, 2, 3));   // expects 6
        /*
         *      ⓪ → → → 5 → → → ① → → → 1 → → → ④
         *        ↘            ↗  ↘             ↙
         *          ↘ 2    2 ↗      ↘ 5     1 ↙
         *            ↘    ↗          ↘     ↙
         *              ③               ②
         * */

        int[][] flights4 = {
                {7, 5, 20}, {7, 6, 59}, {3, 1, 95}, {7, 0, 85}, {4, 7, 84}, {0, 7, 90},
                {1, 0, 19}, {2, 5, 74}, {2, 3, 81}, {2, 0, 56}, {5, 1, 25}, {4, 0, 89},
                {3, 6, 18}, {5, 2, 1},  {7, 1, 43}, {3, 2, 66}, {7, 3, 4}
        };
        log(findCheapestPrice10(8, flights4, 0, 6, 6));   // expects 112
    }
}
