package RecursionAndBackTracking.S5_FloodFill;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Pacific Atlantic Water Flow
 *
 * - Given an m x n matrix of non-negative integers representing the height of each unit cell in a continent,
 *   the "Pacific ocean" touches the left and top edges of the matrix and the "Atlantic ocean" touches the
 *   right and bottom edges.
 * - Water can only flow in four directions (up, down, left, or right) from a cell to another one with height
 *   equal or lower.
 * - Find the list of grid coordinates where water can flow to both the Pacific and Atlantic ocean.
 *
 * - Note:
 *   1. The order of returned grid coordinates does not matter.
 *   2. Both m and n are less than 150.
 * */

public class L417_PacificAtlanticWaterFlow {
    /*
     * 解法1：Inside-out Flood Fill (DFS, Recursion)
     * - 思路：首先一眼可知该题属于连通性问题，而连通性问题可用 Flood Fill 或 Union Find 求解。而不同于 L130 和 L200，
     *   该题中对”联通”的限制为：
     *     1. 联通的标志是要能到达左/上边界、右/下边界；
     *     2. 联通的过程是只能由高水位到达同级或更低水位。
     *   第二点很容易判断，只需在 Flood Fill 遍历相邻格子时对水位进行判断即可；而第一点的判断时机则是在 Flood Fill 的某个
     *   分支抵达边界时进行，若有单个分支能同时抵或多个分支分别抵达左/上、右/下，则说明该 Flood Fill 的起始格子是一个有效解。
     *
     * - 实现：根据以上思路可设计程序：
     *     1. 主程序遍历 matrix，对每个格进行 Flood Fill；
     *     2. 在 Flood Fill 之前创建2个标志位，到达了一个海边就将一个标志位置为 true；
     *     3. 若某分支走到某格时两个标志位都为 true，则可以提前返回；若只有一个标志位为 true 则还需继续走下去；
     *     4. 若某分支的两个标志位都已为 true，则上层递归无需在走完其他分支，可以提前返回；
     *     5. 每次 Flood Fill 过程中一个格子只能经过一次（否则可能产生无限循环），而不同的 Flood Fill 过程之间互不相干。
     *
     * - 改进：∵ 该解法要通过 Flood Fill 检查每一格子最终是否到达了海边，并且过程中没有缓存中间计算结果 ∴ 存在很多重复计算。
     * - 时间复杂度 O(l*w * l*w)，空间复杂度 O(l*w)。
     * */

    private static int w, l;
    private static int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static List<List<Integer>> pacificAtlantic(int[][] matrix) {
        List<List<Integer>> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return res;
        w = matrix.length;
        l = matrix[0].length;

        for (int r = 0; r < w; r++) {
            for (int c = 0; c < l; c++) {
                boolean[] reached = new boolean[2];
                reachSeas(matrix, r, c, reached, new boolean[w][l]);
                if (reached[0] && reached[1])
                    res.add(Arrays.asList(r, c));
            }
        }
        return res;
    }

    private static void reachSeas(int[][] matrix, int r, int c, boolean[] reached, boolean[][] filled) {
        filled[r][c] = true;
        if (r == 0 || c == 0) reached[0] = true;          // 抵达了左/上界
        if (r == w - 1 || c == l - 1) reached[1] = true;  // 抵达了右/下界
        if (reached[0] && reached[1]) return;             // 当都为 true 时说明已找到解

        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (isValidPos(newR, newC) && !filled[newR][newC] && matrix[newR][newC] <= matrix[r][c]) {
                reachSeas(matrix, newR, newC, reached, filled);
                if (reached[0] && reached[1]) return;     // 若已经找到解则提前退出遍历
            }
        }
    }

    private static boolean isValidPos(int r, int c) {
        return r >= 0 && r < w && c >= 0 && c < l;
    }

    /*
     * 解法2：Outside-in Flood Fill (DFS, Recursion)
     * - 思路：类似 L130_SurroundedRegions 解法3的思路，从边界向内陆进行 Flood Fill。具体来说，从 Pacific 的两边进行
     *   Flood Fill，将与 Pacific 连通的格子进行标记，然后再从 Atlantic 的两边进行 Flood Fill，同样将与 Atlantic 连通
     *   的格子进行标记，若同一个格子被标记过两次，则说明该格子是一个解。
     * - 时间复杂度 O(l*w)：∵ reachPacific 和 reachAtlantic 充当了 filled 数组 ∴ 相当于遍历 matrix 2遍，比解法1有极大性能提升。
     * - 空间复杂度 O(l*w)。
     * */
    public static List<List<Integer>> pacificAtlantic2(int[][] matrix) {
        List<List<Integer>> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return res;
        w = matrix.length;
        l = matrix[0].length;
        boolean[][] reachPacific = new boolean[w][l];
        boolean[][] reachAtlantic = new boolean[w][l];

        for (int r = 0; r < w; r++) {
            floodFill(matrix, r, 0, reachPacific);       // 第一列
            floodFill(matrix, r, l - 1, reachAtlantic);  // 最后一列
        }

        for (int c = 0; c < l; c++) {
            floodFill(matrix, 0, c, reachPacific);       // 第一行
            floodFill(matrix, w - 1, c, reachAtlantic);  // 最后一行
        }

        for (int r = 0; r < w; r++)
            for (int c = 0; c < l; c++)
                if (reachPacific[r][c] && reachAtlantic[r][c])
                    res.add(Arrays.asList(r, c));

        return res;
    }

    private static void floodFill(int[][] matrix, int r, int c, boolean[][] filled) {
        filled[r][c] = true;
        for (int[] d : directions) {
            int newR = r + d[0], newC = c + d[1];
            if (isValidPos(newR, newC) && !filled[newR][newC] && matrix[newR][newC] >= matrix[r][c])
                floodFill(matrix, newR, newC, filled);  // 注意上面判断条件是由低水位流向高水位 ∵ 是从边界向内陆 Flood Fill
        }
    }

    public static void main(String[] args) {
        log(pacificAtlantic(new int[][] {
          {1, 2, 2, 3, 5},
          {3, 2, 3, 4, 4},
          {2, 4, 5, 3, 1},
          {6, 7, 1, 4, 5},
          {5, 1, 1, 2, 4}
        }));
        /*
         * expects [[0,4], [1,3], [1,4], [2,2], [3,0], [3,1], [4,0]] (positions with parentheses in below matrix)
         *
         *  Pacific ~   ~   ~   ~   ~
         *       ~  1   2   2   3  (5) |
         *       ~  3   2   3  (4) (4) |
         *       ~  2   4  (5)  3   1  |
         *       ~ (6) (7)  1   4   5  |
         *       ~ (5)  1   1   2   4  |
         *          -   -   -   -   - Atlantic
         * */

        log(pacificAtlantic(new int[][] {
            {2, 9, 5, 3, 7},
            {5, 7, 2, 4, 3},
            {6, 6, 6, 8, 4}
        }));
        /*
         * expects [[0,1], [0,4], [1,1], [1,3], [2,0], [2,1], [2,2], [2,3]]
         *
         *  Pacific ~   ~   ~   ~   ~
         *       ~  2  (9)  5   3  (7) |
         *       ~  5  (7)  2  (4)  3  |
         *       ~ (6) (6) (6) (8)  4  |
         *          -   -   -   -   - Atlantic
         * */
    }
}
