package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * N Queens
 *
 * - The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that no two queens
 *   attack each other (in horizontal, vertical or diagonal direction).
 *
 * - Given an integer n, return all distinct solutions to the n-queens puzzle. Each solution contains a
 *   distinct board configuration of the n-queens' placement, where 'Q' and '.' both indicate a queen and an
 *   empty space respectively.
 *
 * - 👉 总结：回溯搜索是传统人工智能的基础 —— 在机器学习流行之前，人工智能算法很大程度上就是回溯搜索（当然搜索过程中有很多技巧），
 *   很多类似的智力游戏（如 L37 的数独）都可以采用递归 + 回溯的方式求解。
 * */

public class L51_NQueens {
    /*
     * 解法1：Backtracking (Recursion, DFS)
     * - 思路：以4皇后为例，在4*4的棋盘上放置4个皇后，即每行都要放一个皇后才行。而皇后在一行中放置的位置会影响后面所有行中皇后
     *   放置的位置 ∴ 程序的主题逻辑应该是：
     *     1. 对一行中的格子进行遍历，尝试放置皇后；
     *     2. 当遍历到可以放皇后的格子时，暂停当前行的遍历，转到下一行进行遍历，并根据上面皇后的位置来判断每个格子是否能放置皇后；
     *     3. 若所有行都放置了皇后（找到解）或某一行中所有格子都无法放置皇后（无解），此时返回上一行，继续之前暂停的遍历，对下一
     *        个格尝试放置皇后；
     *   例如第一行中在 [0,0] 处放皇后，那么第二行中 [1,0]、[1,1] 都不能放，而 [1,2] 处可以放，但这样的话第三行的所有格子就
     *   都不能放了 ∴ 返回第二行，继续之前暂停的遍历，将皇后放在 [1,3] 处，此时第三行可以放在 [2,1] 处，但此时第四行的所有格子
     *   都不能放了 ∴ 返回第三行，而第三行没有其他能放的位置 ∴ 再返回第二行，而第二行已经到头 ∴ 返回第一行，再从 [0,1] 处开始尝试……
     *       Q . . .        Q . . .        Q . . .        . Q . .
     *       × × Q .   ->   × × . Q   ->   × × . Q   ->   × × × .
     *       × × × ×        × . × ×        × Q × ×        . × . ×
     *       × . × ×        × × . ×        × × × ×        . × . .
     *   可见总体思路就是回溯搜索，剩下要解决的问题是如何剪枝，即如何判断一个格子是否可以放置皇后，即一个格子的水平、竖直、对角4个
     *   方向中是否还有其他皇后存在。
     *
     * - 实现：该解法中采用 boolean[][] attackable 来标记哪些格子处在已放置皇后的攻击范围内。每当放置一个皇后之后，都更新
     *   attackable，将其可攻击到的格子标记为 true。而每当要返回上层递归时，先将 attackable 的状态恢复
     * */

    private static boolean[][] attackable;

    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        if (n <= 0) return res;
        attackable = new boolean[n][n];
        putQueen0(0, n, new ArrayList<>(), res);
        return res;
    }

    private static void putQueen0(int r, int n, List<Integer> pos, List<List<String>> res) {
        if (r == n) {        // 若每行都放置了皇后（找到解）
            res.add(generateSolution(pos));
            return;
        }

        for (int c = 0; c < n; c++) {    // 遍历该行的每个格子，尝试放置皇后
            if (!attackable[r][c]) {
                pos.add(c);                   // 先记录放置皇后的纵坐标（横坐标就是 c 在 pos 中的索引 ∴ 不用记录）
                Boolean[][] oldStates = markAttackable(r, c, n);   // 在放置皇后之后，更新 boolean[][]，将该皇后的各个方向上格子标记为 attackable
                putQueen0(r + 1, n, pos, res);
                unmarkAttackable(r, c, n, oldStates);  // 返回上层递归之前要 undo 上面对 boolean[][] 的更新，恢复原来的状态
                pos.remove(pos.size() - 1);
            }
        }
    }

    private static Boolean[][] markAttackable(int r, int c, int n) {
        Boolean[][] oldStates = new Boolean[n][n];

        for (int nr = r + 1; nr < n; nr++) {          // 遍历 initR 之后的所有行
            oldStates[nr][c] = attackable[nr][c];
            attackable[nr][c] = true;       // 标记竖直方向上的格子
            int delta = nr - r;

            if (c - delta >= 0) {
                oldStates[nr][c - delta] = attackable[nr][c - delta];
                attackable[nr][c - delta] = true;  // 标记左下对角线方向上的格子
            }
            if (c + delta < n) {
                oldStates[nr][c + delta] = attackable[nr][c + delta];
                attackable[nr][c + delta] = true;  // 标记右下对角线方向上的格子
            }
        }
        return oldStates;
    }

    private static void unmarkAttackable(int r, int c, int n, Boolean[][] oldStates) {
        for (int nr = r + 1; nr < n; nr++) {  // nr means next row
            int delta = nr - r;
            attackable[nr][c] = oldStates[nr][c];
            if (c - delta >= 0)
                attackable[nr][c - delta] = oldStates[nr][c - delta];
            if (c + delta < n)
                attackable[nr][c + delta] = oldStates[nr][c + delta];
        }
    }

    private static List<String> generateSolution(List<Integer> pos) {  // 将 pos 转为真正的解
        List<String> solution = new ArrayList<>();
        for (int p : pos) {
            char[] row = new char[pos.size()];  // Java 中用相同字符生成字符串的方式
            Arrays.fill(row, '.');
            row[p] = 'Q';                       // 用 'Q' 标记皇后的位置
            solution.add(new String(row));
        }
        return solution;
    }

    /*
     * 解法1：Backtracking
     * - 思路：
     *     - 横：∵ 每行只会放一个皇后，放置之后就进入下一行 ∴ 无需额外逻辑进行判断；
     *     - 竖：可使用一个 n 位数组 col[i] 来记录第 i 列是否已有皇后；
     *     - 斜：若也想用数组 dia1[i]、dia2[i] 来分别记录两个对角方向上第 i 条对角线上是否有皇后，则需知道：
     *            1. 共有多少条对角线（即 dia 数组大小）；
     *            2. 如何标记一条对角线（即如何访问 dia 中元素）。
     *          通过找规律可知：
     *            - n*n 的棋盘共有 2*n-1 条对角线 ∴ dia1、dia2 的大小都应为 2*n-1；
     *            - 对于 / 方向，每条对角线经过的格子的横纵坐标之和都相同，分别是0，1，2，3，4，5，6 ∴ 可将 i+j 作为索引使用；
     *            - 对于 \ 方向，每条对角线经过的格子的横纵坐标之差都相同，分别是-3，-2，-1，0，1，2，3 ∴ 可将 i-j 加上偏移量 n-1 作为索引使用。
     *
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    private static boolean[] col, dia1, dia2;

    public static List<List<String>> solveNQueens2(int n) {
        List<List<String>> res = new ArrayList<>();
        if (n == 0) return res;

        col = new boolean[n];           // col[i] 表示第 i 列是否已有皇后
        dia1 = new boolean[2 * n - 1];  // dia1[i] 表示第 i 根 / 对角线上是否已有皇后
        dia2 = new boolean[2 * n - 1];  // dia2[i] 表示第 i 根 \ 对角线上是否已有皇后

        putQueen(n, 0, new ArrayList<>(), res);
        return res;
    }

    private static void putQueen(int n, int r, List<Integer> pos, List<List<String>> res) {  // 尝试在第 r 行中放置皇后，pos 记录放置的位置
        if (r == n) {                                                // （pos[r]=k 表示第 r 行的皇后放在了第 k 列上）
            res.add(generateSolution(pos));  // r == n 说明 0 ~ n-1 行都成功放置了皇后，即找到了一个有效解
            return;
        }
        for (int c = 0; c < n; c++) {                                // 遍历该行中的每一列
            if (!col[c] && !dia1[r + c] && !dia2[r - c + n - 1]) {   // 若3个方向上都没有皇后则可以放置
                pos.add(c);                                          // 将列索引放到 pos[r] 上以表示皇后位置为 [r,c]
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = true;
                putQueen(n, r + 1, pos, res);
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = false;  // 返回上一层递归之前要恢复原状态
                pos.remove(pos.size() - 1);
            }
        }
    }

    public static void main(String[] args) {
        log(solveNQueens(4));
        /*
         * expects [   // 在4×4的棋牌上，4皇后问题有2个解
         *   [". Q . .",
         *    ". . . Q"
         *    "Q . . ."
         *    ". . Q ."],
         *
         *   [". . Q .",
         *    "Q . . ."
         *    ". . . Q"
         *    ". Q . ."]
         * ]
         * */

        log(solveNQueens(5));
        /*
         * expects [   // 在5×5的棋牌上，5皇后问题有10个解
         *   ["Q....", "..Q..", "....Q", ".Q...", "...Q."],
         *   ["Q....", "...Q.", ".Q...", "....Q", "..Q.."],
         *   [".Q...", "...Q.", "Q....", "..Q..", "....Q"],
         *   [".Q...", "....Q", "..Q..", "Q....", "...Q."],
         *   ["..Q..", "Q....", "...Q.", ".Q...", "....Q"],
         *   ["..Q..", "....Q", ".Q...", "...Q.", "Q...."],
         *   ["...Q.", "Q....", "..Q..", "....Q", ".Q..."],
         *   ["...Q.", ".Q...", "....Q", "..Q..", "Q...."],
         *   ["....Q", ".Q...", "...Q.", "Q....", "..Q.."],
         *   ["....Q", "..Q..", "Q....", "...Q.", ".Q..."]
         * ]
         * */
    }
}
