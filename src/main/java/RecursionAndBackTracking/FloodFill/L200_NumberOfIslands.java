
package RecursionAndBackTracking.FloodFill;

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
     * 解法1：Recursion + Backtracking (DFS)
     * - 思路：该题是经典的 Flood Fill 场景，而 Flood Fill 算法其实非常简单，就是从单一的一个格子开始往各个方向填充（fill），
     *   直到各个方向都走到头为止 ∴ Flood Fill 本质上就是 DFS ∴ 该解整体思路与 L79 类似。例如 test case 1：尝试对 grid
     *   上的每个格子进行 Flood Fill，其中从 [0, 0] 开使的 Flood Fill 如下：
     *              1
     *           ↓/  →\     - 只有 ↓、→ 两个方向可以填充
     *           1     1
     *          →|          - 已填充过的格子不能再次填充 ∴ 左边的1只有往 → 填充，右边的1无路可走
     *           1
     *   可见，当所有分支都走到头时相当于找到了一个完整的 island，此时一次 Flood Fill 结束，再继续在 grid 上搜索下一个
     *   还未填充过的1，并从那里开始新一轮 Flood Fill。
     *
     * - Flood Fill 本质上是 DFS，而它是否是回溯法则见仁见智（可以算也可以不算），不用太纠结。
     *
     * - 时间复杂度 O(l*w)：时间复杂度可以用3个极端情况来估算：
     *     1. 所有格子都是'1'：此时外层遍历耗时 l*w，floodFill 方法耗时 l*w ∴ 总时间复杂度 O(2*l*w)，即 O(l*w)；
     *     2. 所有格子都是'0'：此时只有外层遍历耗时 l*w ∴ 总时间复杂度 O(l*w)；
     *     3. 整个 grid 由'1'和'0'相间：与情况1相同，也是 O(l*w)。
     * - 空间复杂度 O(l*w)。
     * */

    private static int l, w;
    private static boolean[][] filled;
    private static int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};

    public static int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        l = grid.length;
        w = grid[0].length;
        filled = new boolean[l][w];  // 用于记录哪些格子已经填充过
        int count = 0;

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                if (grid[m][n] == '1' && !filled[m][n]) {  // 找到下一个还未填充过的 land
                    count++;                               // 此时就让 island 个数 +1（或放到下一句后面，即 Flood Fill 结束的地方也一样）
                    floodFill(grid, m, n);                 // 再从这里开始 Flood Fill
                }
            }
        }

        return count;
    }

    private static void floodFill(char[][] grid, int m, int n) {
        filled[m][n] = true;              // 将该格子标记为填充过
        for (int[] d : directions) {
            int newM = m + d[0], newN = n + d[1];
            if (validPos(newM, newN) && grid[newM][newN] == '1' && !filled[newM][newN])  // 当 Flood Fill 的所有分支都走到头时递归
                floodFill(grid, newM, newN);                                             // 会自然结束 ∴ 不需要显示的递归终止条件
        }
    }

    private static boolean validPos(int m, int n) {
        return m >= 0 && m < l && n >= 0 && n < w;
    }

    /*
     * 解法2：Iteration (BFS)
     * - 思路：与解法1一致。
     * - 实现：与解法1有两处不同：
     *     1. 解法1中的 floodFill 方法采用的回溯本质上是 DFS，而该解法中 floodFill2 方法采用 BFS 实现；
     *     2. 解法1中单独创建了 boolean[][] 用于记录哪些格子已被填充，而该解法中采用 in-place modification，即每到达一个
     *        格子，就在 grid 中将这个格子标记为'0'，从而也能达到不重复填充的目的。
     * - 时间复杂度 O(l*w)，空间复杂度 O(l*w)。
     * */
    public static int numIslands2(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        l = grid.length;
        w = grid[0].length;
        int count = 0;

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                if (grid[m][n] == '1') {
                    count++;
                    floodFill2(grid, m, n);
                }
            }
        }

        return count;
    }

    private static void floodFill2(char[][] grid, int m, int n) {
        grid[m][n] = '0';                      // ∵ 后面只会将相邻的格子置'0' ∴ 这里要先将起始格子置'0'
        Queue<Pair<Integer, Integer>> q = new LinkedList<>();
        q.offer(new Pair<>(m, n));

        while (!q.isEmpty()) {
            Pair<Integer, Integer> pair = q.poll();
            int oldM = pair.getKey(), oldN = pair.getValue();
            for (int[] d : directions) {
                int newM = oldM + d[0], newN = oldN + d[1];
                if (validPos(newM, newN) && grid[newM][newN] == '1') {
                    q.offer(new Pair<>(newM, newN));
                    grid[newM][newN] = '0';    // 先将四周相邻的格子入队，而不是马上访问（BFS 与 DFS 的关键区别）
                }
            }
        }
    }

    /*
     * 解法3：Union Find（并查集）
     * - 思路：该问题可以建模为一个联通性问题，即 grid 中的所有 land 是否两两联通，若联通则属于同一个 island。由此问题转化为
     *   求 grid 上任意两个是 land 的格子是否联通，而 Union Find 是专门解决连通性问题的数据结构 ∴ 在该思路下可设计程序：
     *     1. 初始化并查集；
     *     2. 遍历 grid 上的每个 land 格子；
     *     3. 将每个 land 格子与其相邻的 land 格子进行 union 操作（使它们在并查集中共享一个 island 编号）
     * - 时间复杂度 O(l*w)，空间复杂度 O(l*w)。
     * */
    private static class UnionFind {
        private int[] parents;
        private int count;                     //

        UnionFind(char[][] grid) {
            int l = grid.length, w = grid[0].length;
            parents = new int[l * w];          //

            for (int m = 0; m < l; m++) {
                for (int n = 0; n < w; n++) {
                    if (grid[m][n] == '1') {
                        int id = m * w + n;    //
                        parents[id] = id;
                        count++;
                    }
                }
            }
        }

        public void union(int p, int q) {
            int pRoot = find(p);
            int qRoot = find(q);
            if (pRoot == qRoot) return;
            parents[pRoot] = qRoot;
            count--;                 //
        }

        private int find(int p) {
            return p == parents[p] ? p : find(parents[p]);
        }
    }

    public static int numIslands3(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        l = grid.length;
        w = grid[0].length;
        UnionFind uf = new UnionFind(grid);       // 初始化并查集

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                if (grid[m][n] == '1') {          // 遍历 grid 上的每个 land 格子
                    for (int[] d : directions) {  // 将每个 land 格子与其相邻的 land 格子进行 union
                        int newM = m + d[0], newN = n + d[1];
                        if (validPos(newM, newN) && grid[newM][newN] == '1')
                            uf.union(m * w + n, newM * w + newN);  // 对格子 [m,n] 和 [newM,newN] 进行编码
                    }
                }
            }
        }

        return uf.count;
    }

    public static void main(String[] args) {
        log(numIslands3(new char[][] {  // expects 3
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'},
        }));

        log(numIslands3(new char[][] {  // expects 1
            {'1', '1', '1', '1', '0'},
            {'1', '1', '0', '1', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '0', '0', '0'},
        }));

        log(numIslands3(new char[][] {  // expects 2
            {'0', '0', '0'},
            {'0', '1', '1'},
            {'1', '0', '0'},
        }));

        log(numIslands3(new char[][] {  // expects 1
            {'1'},
        }));
    }
}
