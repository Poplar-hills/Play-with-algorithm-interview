package RecursionAndBackTracking.S5_FloodFill;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
 * Surrounded Regions
 *
 * - Given a 2D board containing 'X' and 'O' (the letter O), capture all regions surrounded by 'X'.
 * - A region is captured by flipping all 'O's into 'X's in that surrounded region.
 * */

public class L130_SurroundedRegions {
    /*
     * 解法1：Inside-out Flood Fill (DFS, Recursion)
     * - 思路：首先一眼可知该题属于连通性问题，可用 Flood Fill 或 Union Find 求解。只是不同于 L200_NumberOfIslands，
     *   该题中对有效 region 的定义是四周都是 'X' 的 'O'，而与边界相邻的 'O' 则是无效的 region。
     *   -> 如此一来，程序的主体仍然可以是 Flood Fill，只需要在遍历 'O' 的邻居时加入对边界的判断 —— 若该 'O' 与边界相邻，
     *      则整个 region 无效，只有当 Flood Fill 在没有碰到边界的情况下正常结束时才算找到了有效的 region，进而再将其中的
     *      所有 'O' 都 flip 成 'X'。
     *      -> ∵ 要先遍历整个 region 后才能知道是否有效 ∴ 需要一个列表来暂存当前 region 中所有坐标，若遍历之后 region
     *         有效则 flip 其中的所有坐标，否则直接丢弃即可。
     *
     * - 💎 实现：在遍历一个 region 时：
     *   1. 已遍历过的 'O' 要被标记为 filled，以避免重复遍历。当遍历结束，无论遍历区域是否是 valid region，都无需将已标记为
     *      filled 的 'O' 恢复为 unfilled。这样在之后的 flood fill 遍历中就可以忽略这些 'O'，避免重复计算（相当于剪枝）。
     *   2. 一旦发现某个 'O' 的邻居越界（意味着该 region 无效），此时我们有两种方案：
     *     1. 立即退出当前 Flood Fill，不再继续遍历该 region，转而在 board 上搜索下一个 region；
     *     2. 继续当前 Flood Fill，遍历完该 region，并将其中的所有 'O' 标记为 filled。
     *   若采用方案1，只将该 region 中遍历过的 '0' 标记为 filled，同时其他联通但未遍历到的 'O' 还是 unfilled，这种方式会导致
     *   test case 3 出错：
     *        O O O O      O O O O    - ∵ 第1排与边界相邻 ∴ 从 [0,0] 开始后会马上因越界而退出遍历（但会被标记为 filled）。
     *        X O X O  ->  X X X O      同理，第1排的 'O' 都会被标记为 filled。
     *        X O O X      X X X X    - 而当遍历到 [1,1] 时 ∵ [0,1] 在刚才已经被标记为已填充 ∴ 不会再访问。从而会误认为
     *        X X X O      X X X O      [1,1]、[2,1]、[2,2] 是一个有效的 region 而将他们 flip。
     *   ∴ 应该采用方案1，一次性遍历完整个 region，即使发现该 region 无效也先不退出，而是等所有 'O' 都被标记为 filled 之后再
     *   继续安心的在 board 上搜索下一个 region。
     *
     * - 💎 关键点：
     *   1. 在 board 上搜索 flood fill 起点时，只有当一个格子里既是 'O'，又没有被 fill 过，才是有效的 flood fill 起点。
     *   2. 在 flood fill 过程中，即使最后发现一个 region 无效（不能 flip），也要将其中所有格子标记为 filled。只有这样在继续
     *      搜索 flood fill 起点时才能跳过无效 region 的 'O'。
     *   - ∴ 可见在 flood fill 过程中，boolean[][] filled 是不可逆的。
     *
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)。
     * */

    private static int m, n;
    private static boolean[][] filled;
    private static final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  // 顺序：上右下左

    public static void solve(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;

        m = board.length;     // 行数
        n = board[0].length;  // 列数
        filled = new boolean[m][n];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r][c] == 'O' && !filled[r][c]) {    // 关键：必须既是 'O' 又没 fill 过的格子才能 flood fill
                    List<int[]> region = new ArrayList<>();  // 用于暂存当前 region 的所有坐标
                    if (floodFill(board, r, c, region))       // 若该 region 有效，则 flip 该其中的所有 'O'
                        for (int[] p : region)
                            board[p[0]][p[1]] = 'X';
                }
            }
        }
    }

    private static boolean floodFill(char[][] board, int r, int c, List<int[]> region) {
        boolean isValid = true;  // ∵ 要一次性 fill 整个 region，不能中途发现无效就 return ∴ 采用变量记录该 region 是否有效
        filled[r][c] = true;      // 每个格子 fill 之后无需恢复原状
        region.add(new int[]{r, c});

        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (!isValidPos(newR, newC))
                isValid = false;    // 若任一邻格越界，则说明该格子在边界上，则整个 region 无效（注意不能中途 return）
            else if (board[newR][newC] == 'O' && !filled[newR][newC])
                if (!floodFill(board, newR, newC, region))
                    isValid = false;
        }

        return isValid;  // 遍历完后再该 region 是否有效的信息返回
    }

    private static boolean isValidPos(int r, int c) {
        return r >= 0 && r < m && c >= 0 && c < n;
    }

    /*
     * 超时解（Time Limit Exceeded）：Inside-out Flood Fill (BFS, Iteration)
     * - 思路：与解法1一致。
     * - 实现：解法1采用的是基于 DFS recursion 的回溯，而该解法中采用基于 BFS iteration 的回溯。
     * - 时间复杂度 O(m*n)，与解法1一致，但实际当中会超时，目前原因未知（？？？）。
     * - 空间复杂度 O(m*n)。
     * */
    public static void solve2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;

        m = board.length;
        n = board[0].length;
        filled = new boolean[m][n];
        List<int[]> region = new ArrayList<>();

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r][c] == 'O' && !filled[r][c]) {
                    region.clear();  // ∵ region 提前定义 ∴ 每次使用前需先清空
                    if (floodFill2(board, r, c, region))
                        for (int[] p : region)
                            board[p[0]][p[1]] = 'X';
                }
            }
        }
    }

    private static boolean floodFill2(char[][] board, int initR, int initC, List<int[]> region) {
        boolean isValid = true;
        Queue<int[]> q = new LinkedList<>();  // 用 Queue 实现基于 BFS 的 flood fill
        q.offer(new int[]{initR, initC});

        while (!q.isEmpty()) {
            int[] pos = q.poll();
            int r = pos[0], c = pos[1];

            region.add(new int[]{r, c});
            filled[r][c] = true;

            for (int[] d : directions) {
                int newR = r + d[0], newC = c + d[1];
                if (!isValidPos(newR, newC))
                    isValid = false;
                else if (board[newR][newC] == 'O' && !filled[newR][newC])
                    q.offer(new int[]{newR, newC});
            }
        }

        return isValid;
    }

    /*
     * 解法3：Outside-in Flood Fill (DFS, Recursion)
     * - 思路：另一种聪明的思路是从 board 边界上的 'O' 开始 Flood Fill，将这些无效的 region 用特殊符号 '*' 填充。当所有
     *   的无效 region 被填充完之后，board 上剩余的 'O' 就都是有效的 region 了 ∴ 最后只需再将所有的 'O' flip 成 'X'、将
     *   所有的 '*' 替换回 'O' 即可。
     * - 实现：相比解法1、2，该解法更加简洁，原因是：
     *     1. 先处理无效的 'O' ∴ 只需使用标准的 Flood Fill 即可，无需任何修改；
     *     2. 将遍历过的 'O' 替换成了 '*' ∴ 有 '*' 的格子即是被访问过的，无需再单独开辟 boolean[][] filled；
     * - 👉 总结：与解法1、2对比，该解法其实是从边界开始向内陆进行 Flood Fill，即 outside-in，而解法1、2是 inside-out。
     * - 时间复杂度 O(m*n)，空间复杂度 O(m*n)，时空复杂度也比解法1、2更优。
     * */
    public static void solve3(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;
        m = board.length;
        n = board[0].length;

        for (int r = 0; r < m; r++) {
            if (board[r][0] == 'O') floodFill3(board, r, 0);          // 遍历左边界
            if (board[r][n - 1] == 'O') floodFill3(board, r, n - 1);  // 遍历右边界
        }
        for (int c = 0; c < n; c++) {
            if (board[0][c] == 'O') floodFill3(board, 0, c);          // 遍历上边界
            if (board[m - 1][c] == 'O') floodFill3(board, m - 1, c);  // 遍历下边界
        }
        for (int r = 0; r < m; r++) {                                   // 最后完成替换
            for (int c = 0; c < n; c++) {
                if (board[r][c] == 'O') board[r][c] = 'X';
                if (board[r][c] == '*') board[r][c] = 'O';
            }
        }
    }

    private static void floodFill3(char[][] board, int r, int c) {  // 标准的 Flood Fill，用 '*' 填充遍历到的格子
        board[r][c] = '*';
        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (isValidPos(newR, newC) && board[newR][newC] == 'O')  // 若是 'O' 说明没有访问过（访问过的都置为 '*' 了）
                floodFill3(board, newR, newC);
        }
    }

    /*
     * 解法4：Flood Fill + Union Find
     * - 思路：
     *     1. ∵ 该问题是连通性问题 ∴ 可使用并查集（Union-Find）求解；
     *     2. 总体思路借鉴解法3，即先标记出无效（与边界联通）的 'O'，剩下的 'O' 就都是有效的、需要被 flip 的了。
     *   结合1、2得到具体思路：
     *     1. 先在并查集中将所有无效的 'O' 连接到一个虚拟节点上；
     *     2. 之后遍历 board 上的所有 'O'，若它与虚拟节点不联通，则说明是有效的 'O'，从而进行 flip。
     * - 实现：
     *     1. 并查集的实现比较标准，没有过多改变，需要的修改（如二维坐标到一维的映射）都放到主逻辑中，从而让并查集保持纯粹；
     *     2. 并查集若不做优化则会 Time Limit Exceeded ∴ 加入 path compression 和基于 rank 的优化。
     * - 👉 理解：该解法是真正理解并查集（及其优化方式）的极好题目，一定要下断点跟踪 parents 每一步的变化来加深理解。
     * - 时间复杂度 O(m*n)：基于 path-compression + rank 的并查集的效率接近 O(1)；
     * - 空间复杂度 O(m*n)。
     * */
    private static class UnionFind {
        private final int [] parents;
        private final int [] ranks;

        public UnionFind(int size) {  // 对比 L200 中的并查集，该并查集的构造方法无需传入整个 board，只使用其 size 即可（∵ unify
            parents = new int[size];  // 的时候会将节点坐标编码后传入 ∴ 就无需在并查集内编码了）
            ranks = new int[size];
            for (int i = 0; i < size; i++) {
                parents[i] = i;
                ranks[i] = 1;
            }
        }

        public void unify(int p, int q) {
            int pRoot = find(p), qRoot = find(q);
            if (pRoot == qRoot) return;

            if (ranks[pRoot] < ranks[qRoot])  // rank-based（基于树高）优化，每次将 rank 小的 root 连接到 rank 大的 root 上
                parents[pRoot] = qRoot;
            else if (ranks[pRoot] > ranks[qRoot])
                parents[qRoot] = pRoot;
            else {
                parents[pRoot] = qRoot;
                ranks[qRoot]++;
            }
        }

        public int find(int p) {
            while (parents[p] != p) {
                parents[p] = parents[parents[p]];  // path compression 优化，不断将 p 连接到祖父节点上（与父节点同层）
                p = parents[p];                    // 跳过 p 原本的父节点，直接从爷爷节点继续遍历
            }
            return p;
        }

        public boolean isConnected(int p, int q) {
            return find(p) == find(q);
        }
    }

    public static void solve4(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return;

        m = board.length;
        n = board[0].length;
        UnionFind uf = new UnionFind(m * n + 1);  // 最后多开辟1的空间存放虚拟节点
        int DUMMY_NODE = m * n;

        for (int r = 0; r < m; r++) {  // 遍历 board 上所有的 'O'
            for (int c = 0; c < n; c++) {
                if (board[r][c] != 'O') continue;
                if (r == 0 || r == m - 1 || c == 0 || c == n - 1)  // 若 'O' 在边界上，则将其与虚拟节点连通
                    uf.unify(node(r, c), DUMMY_NODE);
                else {  // 关键：将不在边界上的 'O' 与其四周相邻的 'O' 连通，从而让有效的跟有效的连通，无效的跟无效的连通
                    for (int[] d : directions) {
                        int newR = r + d[0], newC = c + d[1];
                        if (board[newR][newC] == 'O')
                            uf.unify(node(r, c), node(newR, newC));
                    }
                }
            }
        }

        for (int r = 1; r < m - 1; r++)      // 最后对有效的 'O'（即不与虚拟节点连通的 'O'）进行替换
            for (int c = 1; c < n - 1; c++)
                if (!uf.isConnected(node(r, c), DUMMY_NODE))
                    board[r][c] = 'X';
    }

    private static int node(int r, int c) {  // 将二维坐标映射到一维数组索引上
        return r * n + c;
    }

    public static void main(String[] args) {
        char[][] board1 = {          // expects:
            {'X', 'X', 'X', 'X'},    //   X X X X
            {'X', 'O', 'O', 'X'},    //   X X X X
            {'X', 'X', 'O', 'X'},    //   X X X X
            {'X', 'O', 'X', 'X'}     //   X O X X
        };
        solve4(board1);
        log(board1);

        char[][] board2 = {          // expects: (nothing changes)
            {'O', 'O', 'O', 'O'},    //   O O O O
            {'X', 'O', 'X', 'O'},    //   X O X O
            {'X', 'O', 'O', 'X'},    //   X O O X
            {'X', 'O', 'X', 'O'}     //   X O X O
        };
        solve4(board2);
        log(board2);

        char[][] board3 = {          // expects: (nothing changes)
            {'O', 'O', 'O', 'O'},    //   O O O O
            {'X', 'O', 'X', 'O'},    //   X O X O
            {'X', 'O', 'O', 'X'},    //   X O O X
            {'X', 'X', 'X', 'O'}     //   X X X O     // 该行第2个元素与上个 test case 不同
        };
        solve4(board3);
        log(board3);
    }
}
