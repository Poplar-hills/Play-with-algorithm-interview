package RecursionAndBackTracking.FloodFill;

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
     * 解法1：Inside-out Flood Fill + Recursion (DFS)
     * - 思路：首先一眼可知该题属于连通性问题，而连通性问题可用 Flood Fill 或 Union Find 求解。而不同于 L130 和 L200，
     *   该题中对”联通”的限制为：
     *     1. 联通的标志是要能到达左/上边界以及右/下边界；
     *     2. 联通的过程是只能由高水位到达同级或更低水位。
     *   第二点很容易判断，只需在 Flood Fill 遍历每个格子的邻居时对水位进行判断即可；而第一点的判断时机则是在 Flood Fill 的
     *   某个分支抵达边界时进行，若有单个分支能同时抵达左/上以及右/下，过多个分支分别抵达左/上以及右/下则说明该 Flood Fill 的
     *   起始格子是一个有效解。
     *
     * - 实现：根据以上思路可设计程序：
     *     1. 主程序遍历 matrix，对每个格进行 Flood Fill；
     *     2. Flood Fill 中的递归函数定义为：f(m,n) 表示从格子 [m,n] 开始进行 Flood Fill，若能到达左/上以及右/下则返回
     *   Set(1,0)，若只到达左/上则返回 Set(1)，若只到达右/下则返回 Set(0)；
     *     3. 若某分支走到某格已经获得 Set(1,0)，则可以提前返回，而若 Set 中只有一个数字则还需继续走下去；
     *     4. 若某分支已经返回 Set(1,0)，则上层递归无需在走完其他分支，可以提前返回；
     *     5. 每次 Flood Fill 过程中一个格子只能经过一次（否则可能产生无限循环），而不同的 Flood Fill 过程之间互不相干。
     *
     * - 时间复杂度 O(l*w * l*w)，空间复杂度 O(l*w)。
     * */

    private static int l, w;
    private static int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static List<List<Integer>> pacificAtlantic(int[][] matrix) {
        List<List<Integer>> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return res;
        l = matrix.length;
        w = matrix[0].length;

        for (int m = 0; m < l; m++) {
            for (int n = 0; n < w; n++) {
                Set<Integer> set = new HashSet<>();
                boolean[][] filled = new boolean[l][w];  // 在每次 Flood Fill 过程中记录经过的格子
                reachSeas(matrix, m, n, set, filled);
                if (set.size() == 2)
                    res.add(Arrays.asList(m, n));
            }
        }
        return res;
    }

    private static void reachSeas(int[][] matrix, int m, int n, Set<Integer> set, boolean[][] filled) {
        filled[m][n] = true;
        if (m == 0 || n == 0) set.add(1);           // 1代表抵达了左/上界
        if (m == l - 1 || n == w - 1) set.add(0);   // 0代表抵达了右/下界
        if (set.size() == 2) return;                // 当1和0都有时说明本次 Flood Fill 已经找到解了

        for (int[] d : directions) {
            int newM = m + d[0], newN = n + d[1];
            if (validPos(matrix, newM, newN) && matrix[m][n] >= matrix[newM][newN] && !filled[newM][newN]) {
                reachSeas(matrix, newM, newN, set, filled);
                if (set.size() == 2) return;        // 若已经找到解则提前退出遍历
            }
        }
    }

    private static boolean validPos(int[][] matrix, int m, int n) {
        return m >= 0 && m < l && n >= 0 && n < w;
    }

    /*
     * 解法2：Outside-in Flood Fill + Recursion (DFS)
     * - 思路：类似 L130_SurroundedRegions 解法3的思路，从边界向内陆进行 Flood Fill。具体来说，从 Pacific 的两边进行
     *   Flood Fill，将与 Pacific 连通的格子进行标记，然后再从 Atlantic 的两边进行 Flood Fill，同样将与 Atlantic 连通
     *   的格子进行标记，若同一个格子被标记过两次，则说明该格子是一个解。
     * - 时间复杂度 O(l*w)：∵ reachPacific 和 reachAtlantic 充当了 filled 数组 ∴ 相当于只遍历了 matrix 两遍，相比解法1有极大性能提升；
     * - 空间复杂度 O(l*w)。
     * */
    public static List<List<Integer>> pacificAtlantic2(int[][] matrix) {
        List<List<Integer>> res = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return res;
        l = matrix.length;
        w = matrix[0].length;
        boolean[][] reachPacific = new boolean[l][w];
        boolean[][] reachAtlantic = new boolean[l][w];

        for (int m = 0; m < l; m++) {
            floodFill(matrix, m, 0, reachPacific);       // 第一列
            floodFill(matrix, m, w - 1, reachAtlantic);  // 最后一列
        }

        for (int n = 0; n < w; n++) {
            floodFill(matrix, 0, n, reachPacific);       // 第一行
            floodFill(matrix, l - 1, n, reachAtlantic);  // 最后一行
        }

        for (int m = 0; m < l; m++)
            for (int n = 0; n < w; n++)
                if (reachPacific[m][n] && reachAtlantic[m][n])
                    res.add(Arrays.asList(m, n));

        return res;
    }

    private static void floodFill(int[][] matrix, int m, int n, boolean[][] filled) {
        filled[m][n] = true;
        for (int[] d : directions) {
            int newM = m + d[0], newN = n + d[1];
            if (validPos(matrix, newM, newN) && matrix[m][n] <= matrix[newM][newN] && !filled[newM][newN])
                floodFill(matrix, newM, newN, filled);  // 注意上面判断条件是由低水位流向高水位 ∵ 是从边界向内陆 Flood Fill
        }
    }

    public static void main(String[] args) {
        log(pacificAtlantic2(new int[][] {
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

        log(pacificAtlantic2(new int[][] {
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
