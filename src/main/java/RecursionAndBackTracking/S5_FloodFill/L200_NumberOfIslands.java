
package RecursionAndBackTracking.S5_FloodFill;

import static Utils.Helpers.*;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Number of Islands
 *
 * - Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. An island is surrounded
 *   by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four
 *   edges of the grid are all surrounded by water.
 * */

public class L200_NumberOfIslands {
    /*
     * 解法1：Flood Fill (DFS, Recursion)
     * - 思路：该题是经典的 Flood Fill 场景。Flood Fill 算法其实非常简单，就是从一个格子开始往各个方向填充，直到各个方向都
     *   走到头为止 ∴ Flood Fill 本质上就是搜索。例如 test case 1：尝试对 grid 上的每个格子进行 Flood Fill，其中从 [0,0]
     *   开使的 Flood Fill 如下：
     *              1
     *           ↓/  →\     - 只有 ↓、→ 两个方向可以填充
     *           1     1
     *          →|          - 已填充过的格子不能再次填充 ∴ 左边的1只有往 → 填充，右边的1无路可走
     *           1
     *   可见，当所有分支都走到头时相当于找到了一个完整的 island，此时一次 Flood Fill 结束，再继续在 grid 上搜索下一个
     *   还未填充过的1，并从那里开始新一轮 Flood Fill。
     *
     * - 👉 Flood Fill 本质上是 DFS，而它是否是回溯法则见仁见智（可以算也可以不算），不用太纠结。
     *
     * - 时间复杂度 O(m*n)：可以用3个极端情况来估算：
     *     1. 所有格子都是'1'：此时外层循环 m*n 次，floodFill 方法执行1次，耗时 m*n ∴ 总时间复杂度 O(2*m*n)，即 O(m*n)；
     *     2. 所有格子都是'0'：此时只有外层遍历耗时 m*n ∴ 总时间复杂度 O(m*n)；
     *     3. 整个 grid 由'1'和'0'相间：与情况1相同，也是 O(m*n)。
     * - 空间复杂度 O(m*n)。
     * */

    private static int m, n;
    private static boolean[][] filled;
    private static final int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};  // 顺序：上右下左

    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        m = grid.length;
        n = grid[0].length;
        filled = new boolean[m][n];  // 用于记录哪些格子已经填充过
        int count = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1' && !filled[r][c]) {  // 找到下一个还未填充过的 island
                    count++;
                    floodFill(grid, r, c);
                }
            }
        }

        return count;
    }

    private static void floodFill(char[][] grid, int r, int c) {
        filled[r][c] = true;                  // 将该格子标记为填充过
        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (isValidPos(newR, newC) && grid[newR][newC] == '1' && !filled[newR][newC])
                floodFill(grid, newR, newC);  // 当 Flood Fill 的所有分支都走到头时递归会自然结束 ∴ 不需要显示的递归终止条件
        }
    }

    private static boolean isValidPos(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }

    /*
     * 解法2：Flood Fill (BFS, Iteration)
     * - 思路：与解法1一致。
     * - 实现：与解法1有两处不同：
     *   1. 采用基于 BFS 实现 Flood Fill；
     *   2. 解法1中单独创建了 boolean[][] 用于记录哪些格子已被填充，而该解法中采用 in-place modification，即每到达一个
     *      '1'，就将其标记为'0'，从而也能达到不重复填充的目的。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    public static int numIslands2(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        m = grid.length;
        n = grid[0].length;
        int count = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    floodFill2(grid, r, c);
                }
            }
        }

        return count;
    }

    private static void floodFill2(char[][] grid, int r, int c) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{r, c});
        grid[r][c] = '0';      // ∵ 后面只会将相邻的格子置'0' ∴ 这里要先将起始格子置'0'

        while (!q.isEmpty()) {
            int[] pair = q.poll();
            for (int[] d : directions) {
                int newR = pair[0] + d[0], newC = pair[1] + d[1];
                if (isValidPos(newR, newC) && grid[newR][newC] == '1') {
                    q.offer(new int[]{newR, newC});
                    grid[newR][newC] = '0';  // 先将四周相邻的格子入队，而不是马上访问（BFS 与 DFS 的关键区别）
                }
            }
        }
    }

    /*
     * 解法3：Flood Fill + Union Find
     * - 思路：该问题是一个联通性问题，即 grid 中的所有 land 是否两两联通，若联通则属于同一个 island。而 Union Find 是专门
     *   解决连通性问题的数据结构 ∴ 整体思路可以是：
     *     1. 对 grid 上的所有 '1' 进行 Flood Fill，并在并查集中将相邻的 '1' 连通；
     *     2. 并查集返回连通区域的个数。
     * - 实现：为了能让并查集返回连通区域的个数，需要对并查集进行设计：
     *     1. 初始化时，grid 中的每个 '1' 都被认为是一个独立的 island ∴ 每个 '1' 在并查集中都有自己的集合编号；而所有 '0'
     *        则在并查集中共享同一个集合编号；
     *     2. 并查集维护 island 的个数。初始化时 island 个数为 '1' 的个数；之后 Flood Filled 中每次将相邻的 island 在
     *        并查集中连通时都让 island 个数 -1；
     * - 👉 改进：UnionFind 有很多优化策略：基于树大小、基于树高、基于路径压缩等方式，具体 SEE: Play-with-data-structure/UnionFind
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */
    private static class UnionFind {
        private final int[] parents;
        private int count;                     // 并查集中维护联通区域（即 island）的个数

        UnionFind(char[][] grid) {
            int m = grid.length, n = grid[0].length;
            parents = new int[m * n];          // parents 的大小即 grid 的大小

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (grid[r][c] == '1') {   // 初始化时给每个 land 格子一个唯一的 id
                        int id = r * n + c;    // 将二维坐标映射到一维
                        parents[id] = id;
                        count++;               // 初始化时每个 land 格子都是一个 island（之后再把相邻的 land 不断 unify 起来）
                    }
                }
            }
        }

        public void unify(int p, int q) {  // 无 rank 和路径压缩优化的并查集实现（优化版 SEE: L130_SurroundedRegions）
            int pRoot = find(p);
            int qRoot = find(q);
            if (pRoot == qRoot) return;
            parents[pRoot] = qRoot;
            count--;                 // 若成功 unify 两块 land 之后，island 的个数要 -1
        }

        private int find(int p) {
            return p == parents[p] ? p : find(parents[p]);
        }
    }

    public static int numIslands3(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;

        m = grid.length;
        n = grid[0].length;
        UnionFind uf = new UnionFind(grid);  // 初始化并查集

        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++)
                if (grid[r][c] == '1')
                    floodFill3(grid, r, c, uf);

        return uf.count;  // 最后返回并查集中的 count
    }

    private static void floodFill3(char[][] grid, int r, int c, UnionFind uf) {
        for (int[] d : directions) {                   // 将每个 land 格子与其相邻的 land 格子进行 union
            int newR = r + d[0], newC = c + d[1];
            if (isValidPos(newR, newC) && grid[newR][newC] == '1')
                uf.unify(r * n + c, newR * n + newC);  // 将 [r,c]、[newR,newC] 进行一维化编码
        }
    }

    private static boolean isValidPos0(int r, int c) {
        return r >= 0 && r < m && c >=0 && c < n;
    }

    public static void main(String[] args) {
        log(numIslands2(new char[][] {  // expects 3
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'},
        }));

        log(numIslands2(new char[][] {  // expects 1
            {'1', '1', '1', '1', '0'},
            {'1', '1', '0', '1', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '0', '0', '0'},
        }));

        log(numIslands2(new char[][] {  // expects 2
            {'0', '0', '0'},
            {'0', '1', '1'},
            {'1', '0', '0'},
        }));

        log(numIslands2(new char[][] {  // expects 1
            {'1'},
        }));
    }
}
