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
     * - 💎 思路：以4皇后为例，在4*4的棋盘上放置4个皇后，即每行都要放一个皇后才行。而皇后在一行中放置的位置会影响后面所有行中皇后
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
     * - 💎 实现：该解法中采用 boolean[][] attackable 来标记哪些格子处在已放置皇后的攻击范围内。每当放置一个皇后之后，都更新
     *   attackable，将其可攻击到的格子标记为 true。而每当要返回上层递归时，先要将 attackable 的状态恢复原状。但注意在恢复
     *   原状时不能直接将格子标记为 false，这是 ∵ 该格子在被置为 true 之前可能就是已经是 true 了（即当前皇后与之前皇后的攻击
     *   范围有所重叠）∴ 需要在将格子置为 true 之前先用 tmp 记录 attackable 原来的状态，以便在返回上层前能进行恢复，又不会
     *   破坏之前皇后的攻击范围。
     *
     * - 时间复杂度 O(n^n)；空间复杂度 O(n^2)。
     * */
    private static boolean[][] attackable;

    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        if (n <= 0) return res;
        attackable = new boolean[n][n];
        putQueen(0, n, new ArrayList<>(), res);  // 从第0行开始尝试放置皇后，并将放置的纵坐标记录在 pos 中（索引即是横坐标）
        return res;
    }

    private static void putQueen(int r, int n, List<Integer> pos, List<List<String>> res) {
        if (r == n) {                  // 已经超过了最后一行（说明找到了解，若某行无法放置皇后则会提前返回，不会走到这）
            res.add(generateSolution(pos));
            return;
        }
        for (int c = 0; c < n; c++) {  // 遍历该行的每个格子，尝试放置皇后
            if (!attackable[r][c]) {
                pos.add(c);            // 先记录放置皇后的纵坐标（索引即是横坐标）
                Boolean[][] tmp = markAttackable(r, c, n);  // 在 boolean[][] 上标记该皇后的攻击范围，并将原状态记录在 tmp 中
                putQueen(r + 1, n, pos, res);
                restoreAttackable(r, c, n, tmp);  // 返回上层递归之前从 tmp 中恢复原来的状态
                pos.remove(pos.size() - 1);
            }
        }
    }

    private static Boolean[][] markAttackable(int r, int c, int n) {
        Boolean[][] tmp = new Boolean[n][n];  // 注意这里要用包装类

        for (int nr = r + 1; nr < n; nr++) {  // 遍历 (r,n) 行
            int delta = nr - r;
            tmp[nr][c] = attackable[nr][c];   // 在标记之前先保存原来该坐标的状态
            attackable[nr][c] = true;

            if (c - delta >= 0) {
                tmp[nr][c - delta] = attackable[nr][c - delta];
                attackable[nr][c - delta] = true;  // 标记 / 对角线上的格子
            }
            if (c + delta < n) {
                tmp[nr][c + delta] = attackable[nr][c + delta];
                attackable[nr][c + delta] = true;  // 标记 \ 对角线上的格子
            }
        }
        return tmp;
    }

    private static void restoreAttackable(int r, int c, int n, Boolean[][] tmp) {
        for (int nr = r + 1; nr < n; nr++) {
            int delta = nr - r;
            attackable[nr][c] = tmp[nr][c];
            if (c - delta >= 0)
                attackable[nr][c - delta] = tmp[nr][c - delta];
            if (c + delta < n)
                attackable[nr][c + delta] = tmp[nr][c + delta];
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
     * 解法2：Backtracking (Recursion, DFS) (解法1的化简版)
     * - 思路：与解法1一致。
     * - 分析：在判断一个格子是否能放置皇后时，解法1采用 boolean[][] attackable 来标记已放置的皇后的攻击范围。而 ∵ 新放置的
     *   皇后的攻击范围可能与之前皇后的攻击范围有所重叠 ∴ 需要在为新皇后更新 attackable 时先记录原有状态，以便在返回上层前能
     *   进行恢复，又不会破坏之前皇后的攻击范围 —— 这就导致代码比较臃肿，且空间效率较低。
     * - 实现：该解法中不再采用 boolean[][] 标记攻击范围，而是采用3个数组来标记每个格子在 |、/、\ 3个方向上是否有皇后，而又
     *   ∵ 已有皇后的方向上无法再放置皇后 ∴ 同一方向上不会被重复标记 ∴ 也就不存破坏之前标记的问题 ∴ 也就避免了解法1中先记录原有
     *   状态，之后再用它来恢复的麻烦。3个数组的定义：
     *     - | 方向：用数组 col[i] 来标记第 i 列中是否已有皇后；
     *     - /、\ 方向：若也想用数组 dia1[i]、dia2[i] 来标记两个对角方向上第 i 条对角线上是否有皇后，则需知道：
     *       1. 共有多少条对角线（即 dia 数组大小）；
     *       2. 如何访问 dia[] 中的元素。通过找规律可知：
     *          - n*n 的棋盘共有 2*n-1 条对角线 ∴ dia1[]、dia2[] 的大小都应为 2*n-1；
     *          - 对于 / 方向，每条对角线经过的格子的横纵坐标之和相同，分别是0，1，2，3，4，5，6 ∴ 可将 i+j 作为索引使用；
     *          - 对于 \ 方向，每条对角线经过的格子的横纵坐标之差相同，分别是-3，-2，-1，0，1，2，3 ∴ 可将 i-j 加上偏移量
     *            n-1 作为索引使用。
     *
     * - 时间复杂度 O(n^n)，空间复杂度 O(n)。
     * */
    private static boolean[] col, dia1, dia2;

    public static List<List<String>> solveNQueens2(int n) {
        List<List<String>> res = new ArrayList<>();
        if (n == 0) return res;

        col = new boolean[n];           // col[i] 表示第 i 列是否已有皇后
        dia1 = new boolean[2 * n - 1];  // dia1[i] 表示第 i 条 / 对角线上是否已有皇后
        dia2 = new boolean[2 * n - 1];  // dia2[i] 表示第 i 条 \ 对角线上是否已有皇后

        putQueen2(n, 0, new ArrayList<>(), res);
        return res;
    }

    private static void putQueen2(int n, int r, List<Integer> pos, List<List<String>> res) {
        if (r == n) {
            res.add(generateSolution(pos));
            return;
        }
        for (int c = 0; c < n; c++) {
            if (!col[c] && !dia1[r + c] && !dia2[r - c + n - 1]) {   // 若3个方向上都没有皇后则可以放置
                pos.add(c);
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = true;
                putQueen2(n, r + 1, pos, res);
                col[c] = dia1[r + c] = dia2[r - c + n - 1] = false;  // 返回上一层递归之前先恢复原状态
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
