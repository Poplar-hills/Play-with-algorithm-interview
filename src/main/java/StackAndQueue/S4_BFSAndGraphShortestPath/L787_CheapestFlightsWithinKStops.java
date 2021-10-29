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
 * */

public class L787_CheapestFlightsWithinKStops {
    /*
     * 解法1：BFS
     * - 思路：与 L126_WordLadderII 解法1类似，非常 straightforward，从起点 src 开始对由城市和航线组成的图进行完整的
     *    BFS（遍历所有顶点，而不是到达了终点就提前结束），找到所有到达终点 dst 的路径，并返回其中最小的 price。
     * - 💎 实现：本解法采用先构建 graph，再在 graph 上进行 BFS 的方式实现：
     *     1. ∵ graph 是用来在 BFS 时能根据任一节点找到所有相邻节点（即需根据任意一个起始城市，找到所有从该城市出发的航线）
     *        ∴ graph 要能支持对起始城市的随机访问 ∴ graph 的结构要么是：
     *        a). 邻接表（int[][] 或 List<List<Integer>>）：通常使用索引查询（如 L70_ClimbingStairs 解法3）；
     *        b). 哈希表（Map<city, List<flight>>）：使用城市名称查询（如本解法中的情况）。
     *     2. ∵ 最终要求的是 price，且 stop 个数是限制条件 ∴ 在 BFS 过程中要将路径的 price 和 stop 个数带在每个顶点上。
     *        在查找相邻顶点时，若到达某一相邻顶点的 price 已经超过之前找到的 minPrice，则进行剪枝。
     *     3. ∵ 要求的是不同路径的最小 price，而不同路径可能会经过相同的顶点（联想 Dijkstra 的松弛操作）∴ BFS 过程中不能
     *        对顶点使用 visited/unvisited 的重复访问检查。
     * - 💎 总结：本题与 L127_WordLadder 对照可发现：
     *   - 若是求无权图上的最短路径，则只需用 BFS 找到第一条到达终点的路径即可，分支时要对顶点做 vistied/unvisited 判断。
     *   - 若是求带权图上的最小权路径，若要使用 BFS/DFS，则需遍历所有到达终点的路径，且分支时不能做 vistied/unvisited 判断。
     * - 时间复杂度：O(V+E)，即 O(n+m)，其中 m 为航线条数（flights.length）：
     *     1. 构建 graph 需要遍历所有航线，即所有边 ∴ 是 O(E)，即 O(m)；
     *     2. ∵ graph 更类似邻接表 ∴ 在 graph 上进行 BFS 是 O(V+E)，即 O(n+m)。
     * - 空间复杂度 O(n+m)。
     * */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)  // Map<city, List<flight>>
            .collect(Collectors.groupingBy(f -> f[0]));          // 按起始城市进行分组

        Queue<int[]> q = new LinkedList<>();  // Queue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>
        q.offer(new int[]{src, 0, -1});       // 注意 stopCount 要从-1开始 ∵ K 表示的是不包含 src、dst 的步数

        int minPrice = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            int[] pathInfo = q.poll();
            int city = pathInfo[0], totalPrice = pathInfo[1], stopCount = pathInfo[2];

            if (city == dst)
                minPrice = Math.min(minPrice, totalPrice);   // ∵ 要找到所有 src->dst 的路径 ∴ 当到一条通路之后不能 return
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
     *   2. while 内部采用一次遍历完该层所有顶点的实现；
     *   3. ∵ 每轮 while 遍历完一层的所有顶点，而从起点到每层的各个顶点的步数是一样的 ∴ 可以在 while 外部记录经过的 stop
     *      个数（stopCount），而不用将该信息带在 q 中的每个顶点上。
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
                int[] pathInfo = q.poll();
                int city = pathInfo[0], totalPrice = pathInfo[1];

                if (city == dst)
                    minPrice = Math.min(minPrice, totalPrice);
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
            int[] pathInfo = stack.pop();
            int city = pathInfo[0], totalPrice = pathInfo[1], stopCount = pathInfo[2];

            if (city == dst)
                minPrice = Math.min(minPrice, totalPrice);
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
    private static int minPrice;

    public static int findCheapestPrice4(int n, int[][] flights, int src, int dst, int K) {
        minPrice = Integer.MAX_VALUE;     // 不在上面赋值是为了不让 test case 之间互相影响
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));
        dfs4(graph, src, dst, 0, K + 1);
        return minPrice == Integer.MAX_VALUE ? -1 : minPrice;
    }

    private static void dfs4(Map<Integer, List<int[]>> graph, int city, int dst, int totalPrice, int k) {
        if (city == dst) {
            minPrice = totalPrice;  // 每次找到通路后就直接赋值，下面的剪枝保证了这里最后取到的是最小 price（注意上
            return;                 // 面解法1-3不能这么写，TODO: why???）
        }
        if (!graph.containsKey(city) || k == 0) return;
        for (int[] flight : graph.get(city))
            if (totalPrice + flight[2] < minPrice)
                dfs4(graph, flight[1], dst, totalPrice + flight[2], k - 1);
    }

    /*
     * 解法5：简化版的 Dijkstra（性能优于解法1-3）
     * - 👉 前提：先看完 play-with-algorithms/ShortestPath/Dijkstra.java 中的介绍。
     * - 思路：本题是个典型的带权图，而 Dijkstra 算法正适用于计算带权图的单元最短路径树（即从一个起点到每个顶点的最小权路径）。
     *   ∵ 本题中需要的只是从起点到终点的最短路径，无需求出起点到每个顶点的最短路径（最小权路径）∴ 无需对每个顶点进行
     *   relaxation 操作（∴ 该解法是不标准的 Dijkstra），只要按边的权值（price）从小到大的顺序访问每个顶点的相邻顶点，
     *   则第一条到达终点的路径即是最短路径（cheapest price）。
     * - 💎 实现：
     *   1. 该解法中的 Dijkstra 实现本质上是采用了 PriorityQueue 的 BFS；
     *   2. 与 L126_WordLadderII 解法2一样，Dijkstra 算法也依赖于图的一个特性 —— 图上两点之间的最短路径，同时也是从起点
     *      到该路径上各顶点的最短路径。若反过来利用该特性，从起点开始通过 BFS 一层层的查找每个顶点的最短邻边（最小权边），
     *      这样找到的第一条到达终点的路径即是两点之间的最短路径。演算过程：
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
     * - 时间复杂度：标准的 Dijkstra 实现是 O(ElogV)，但该解法中：
     *   1. 构建 graph 需要遍历所有航线，即 O(m)，其中 m = flights.length；
     *   2. 堆中存放的元素数 = 航线数 ∴ 其 offer、poll 操作为 O(logm)，一共进行 m 次 ∴ 是 O(mlogm)；
     *   3. 在 graph 上为堆中每个元素查找相邻顶点是 O(m)；
     *   ∴ 总体时间复杂度 O(mlogm)，空间复杂度 O(n+m)。
     * */
    public static int findCheapestPrice5(int n, int[][] flights, int src, int dst, int K) {
        Map<Integer, List<int[]>> graph = Arrays.stream(flights)
            .collect(Collectors.groupingBy(f -> f[0]));

        PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> p1[1] - p2[1]);  // 基于 totalPrice 的最小堆
        pq.offer(new int[]{src, 0, -1});  // PriorityQueue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>

        while (!pq.isEmpty()) {
            int[] pathInfo = pq.poll();  // ∵ 每次 poll 出来的都是 pq 里从 src 出发 totalPrice 最小的路径
            int city = pathInfo[0], totalPrice = pathInfo[1], stopCount = pathInfo[2];

            if (city == dst) return totalPrice;  // 第一个到达终点的路径的 totalPrice 即是最低的 price
            if (!graph.containsKey(city) || stopCount == K) continue;

            for (int[] flight : graph.get(city))
                pq.offer(new int[]{flight[1], totalPrice + flight[2], stopCount + 1});
        }

        return -1;
    }

    /*
     * 解法6：完整版的 Dijkstra
     * - 思路：Dijkstra 算法用于为一副带权图生成最短路径树（即从起点到图中所有其他顶点的最短路径数组）。解法5中的 Dijkstra
     *   是化简后的版本，而本解法中采用的是完整的 Dijkstra 过程，基于 graph 生成 minPrices、minStops 数组：minPrices[i]
     *   表示从 src 到顶点 i 的最低费用；minStops[i] 表示从 src 到顶点 i 的最少中转站数量），最后返回 minPrices[dst] 即可。
     * - 实现：
     *   1. ∵ 航线图通常非常密集 ∴ 本解法的 graph 采用邻接矩阵（Adjacency Matrix）；
     *   2. 邻接矩阵是通过索引查询，该解法中假设了城市名就是城市索引（这点题中没有明确说明，但 test case 中就是这样）。
     *   3. Dijkstra 的过程：
     *      - 与解法5相同点：同样是使用优先队列，每次 poll 出最短路径；
     *      - 与解法5不同点：增加了松弛操作 —— 为每次 poll 出的路径中的顶点的邻边进行松弛，若松弛过程中找到了 price 更低
     *        或 stopCount 更少的路径，则将该顶点重新入队，再次进行松弛。
     * - 时间复杂度：即标准的 Dijkstra 的时间复杂度 O(ElogV)，也就是 O(mlogn)。实际在 Leetcode 上，该解法快于97%的解法
     *   （也是解法1-7中唯一不超时的解法）。
     * - 空间复杂度：O(n+m)。
     * */
    public static int findCheapestPrice6(int n, int[][] flights, int src, int dst, int K) {
        int[][] graph = new int[n][n];   // adjacency graph
        for (int[] f : flights)
            graph[f[0]][f[1]] = f[2];    // graph[src][dst] = price

        int[] minPrices = new int[n];     // min prices from src to each city
        int[] minStops = new int[n];      // min num of stops from src to each city
        Arrays.fill(minPrices, Integer.MAX_VALUE);
        Arrays.fill(minStops, Integer.MAX_VALUE);
        minPrices[src] = 0;
        minStops[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> p1[1] - p2[1]);
        pq.offer(new int[]{src, 0, -1});  // PriorityQueue<[city, 该路径的 totalPrice, 该路径上的 stopCount]>

        while (!pq.isEmpty()) {
            int[] pathInfo = pq.poll();
            int city = pathInfo[0], totalPrice = pathInfo[1], stopCount = pathInfo[2];

            if (city == dst) return totalPrice;       // 找到的第一条通路就是最短路径 ∴ 直接 return
            if (stopCount == K) continue;             // 剪枝

            for (int nei = 0; nei < n; nei++) {       // 松弛所有邻边（relax all neighboring edges）
                if (graph[city][nei] > 0) {           // price > 0 表示有从 city -> nei 的航线
                    int newPrice = totalPrice + graph[city][nei];
                    int newStopCount = stopCount + 1;

                    if (newPrice < minPrices[nei] || newStopCount < minStops[nei])  // 若松弛操作得到的 newPrice/newStopCount < 之前记录的 totalPrice/stopCount 则：
                        pq.offer(new int[]{nei, newPrice, newStopCount});  // 再次入队 nei 顶点，对其所有邻边重新进行松弛

                    minPrices[nei] = Math.min(minPrices[nei], newPrice);   // 更新记录
                    minStops[nei] = newStopCount;
                }
            }
        }

        return minPrices[dst] == Integer.MAX_VALUE ? -1 : minPrices[dst];
    }

    /*
     * 解法7：Bellman-Ford
     * - 前提：先理解 Bellman-Ford 的过程演示：https://www.youtube.com/watch?v=obWXjtg0L64&vl=en（0'35''）。
     * - 思路：虽然题中说了不会有负权边，但可以使用 Dijkstra 的场景就一定可以使用 Bellman-Ford（虽然算法复杂度大很多）。
     * - 原理：假设图中可能存在负权边，则经过更多顶点的路径可能总距离反而更短。这时 Dijkstra 的贪心策略就会失效，不再能保证
     *   第一条到达终点的路径就是最短的。解决方案是反复对所有边进行松弛操作，使得起点到每个顶点的距离逐步逼近其最短距离。
     * - 实现：
     *   1. 标准的 Bellman-Ford 算法最多会迭代 V-1 次，而本题中 ∵ V 与 K 的关系是 V = K+2 ∴ 应迭代 K+1 次；
     *   2. ∵ 只迭代 K+1 次 ∴ 最终得到的 prices 会是一个中间状态，不会包含起点到所有顶点的最短路径 ∴ 需要做到每次迭代之间
     *      互不影响 ∴ 需要在迭代开始之前先 copy 一份 prices，让迭代中的更新都发生在这份 copy 里，迭代结束之后再将其赋给
     *      prices（若是标准实现，迭代 V-1 次，则不需要这种处理，这一点通过 test case 1、2 可更好的理解）。
     * - 💎 Bellman-Ford vs. Dijkstra：
     *   1. 若图中存在负权边，则应使用 Bellman-Ford，否则使用 Dijkstra 效率更优；
     *   2. 在思想上，Dijkstra 是基于 BFS；而 Bellman-Ford 是基于 DP，在多次迭代中趋近最优解。
     *   3. 在实现上，Dijkstra 需先构建 graph，并用 PriorityQueue 找到最短路径；而 Bellman-Ford 无需构建 graph，
     *      而是在 V-1 次迭代中对所有已达到的顶点的邻边进行松弛。
     * - 时间复杂度为 O(EV)，即 O(mn)，空间复杂度 O(V)，即 O(n)。
     * */
    public static int findCheapestPrice7(int n, int[][] flights, int src, int dst, int K) {
        int[] minPrices = new int[n];   // Bellman-Ford 的基本形式是填充最短路径树数组（同标准版的 Dijkstra）
        Arrays.fill(minPrices, Integer.MAX_VALUE);
        minPrices[src] = 0;

        for (int i = 0; i <= K; i++) {  // 迭代 K+1 次
            int[] copy = Arrays.copyOf(minPrices, n);  // 先拷贝一份，保证下面 minPrices[from] 读到的值不是被 copy[to] 覆盖过的
            for (int[] flight : flights) {               // 无需提前构建 graph，只需在每次迭代中遍历所有邻边，对每条边进行松弛
                int from = flight[0], to = flight[1], price = flight[2];
                if (minPrices[from] == Integer.MAX_VALUE)  // 若该边的起点还没被访问过，则跳过（∵ 无法进行松弛操作）
                    continue;
                copy[to] = Math.min(copy[to], minPrices[from] + price);  // 松弛
            }
            minPrices = copy;  // 迭代结束时更新 minPrices
        }

        return minPrices[dst] == Integer.MAX_VALUE ? -1 : minPrices[dst];  // 通过 minPrices[dst] 取得最短路径
    }

    /*
     * 解法8：DP
     * - 思路：// TODO 补充状态转移方程
     *   - 子问题定义：f(k, c) 表示“在 k-1 个 stop 之内从起点 src 到达城市 c 的最小 price”；
     *         k\c |  0   1   2   3   4
     *        -----+---------------------
     *          0  |  0   ∞   ∞   ∞   ∞
     *          1  |  0   50  20  60  ∞       - 在0个 stop 之内从 src → c 的最小 price
     *          2  |  0   30  20  50  60      - f(2,1) 会被更新两次：∞→50→30；f(2,3) 也会被更新两次：∞→60→50
     *          3  |  0   30  20  50  40
     * - 实现：
     *   1. ∵ 在 Math.min 时可能发生 Integer.MAX_VALUE + price，超过 int 的上限 ∴ dp 需要声明为 long[][]；
     *   2. 在解法6的 Bellman-Ford 中会跳过源顶点还未被访问过的边，而本解法中则仍会访问，而 ∵ 未被访问过的顶点的最短路径是 ∞，
     *      ∴ 基于 ∞ 去更新目标顶点的最短路径仍会是 ∞。
     * - 时间复杂度 O(EV)，空间复杂度 O(nm)，空间复杂度 O(n^2)。
     * */
    public static int findCheapestPrice8(int n, int[][] flights, int src, int dst, int K) {
        long[][] dp = new long[K + 2][n];    // dp[k][c] 表示在 k-1 个 stop 内从 src 到达城市 c 的最小 price
        for (long[] row : dp)
            Arrays.fill(row, Integer.MAX_VALUE);
        dp[0][src] = 0;

        for (int k = 1; k < K + 2; k++) {    // 迭代 K+1 次
            dp[k][src] = 0;
            for (int[] f : flights) {        // 每次迭代都遍历所有邻边
                int sCity = f[0], tCity = f[1], price = f[2];
                dp[k][tCity] = Math.min(dp[k][tCity], dp[k - 1][sCity] + price);  // f(k,tCity) 取决于 f(k-1,sCity) + sCity→tCity 的 price
            }
        }

        return dp[K + 1][dst] == Integer.MAX_VALUE ? -1 : (int)dp[K + 1][dst];
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
        log(findCheapestPrice6(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice6(3, flights1, 0, 2, 0));  // expects 500

        int[][] flights2 = {
                {0, 1, 50}, {0, 2, 20}, {0, 3, 60}, {1, 4, 10},
                {2, 1, 10}, {2, 4, 50}, {2, 3, 30}, {3, 4, 20}
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
        log(findCheapestPrice6(5, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice6(5, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice6(5, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice6(5, flights2, 2, 0, 4));   // expects -1

        int[][] flights3 = {{0, 1, 5}, {1, 2, 5}, {0, 3, 2}, {3, 1, 2}, {1, 4, 1}, {4, 2, 1}};
        log(findCheapestPrice6(5, flights3, 0, 2, 2));   // expects 7
        log(findCheapestPrice6(5, flights3, 0, 2, 3));   // expects 6
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
        log(findCheapestPrice6(8, flights4, 0, 6, 6));   // expects 112

    }
}
