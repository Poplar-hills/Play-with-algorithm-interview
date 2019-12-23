package RecursionAndBackTracking.S6_TraditionalAi;

import static Utils.Helpers.*;

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
 * - 👉 总结：回溯搜索是传统人工智能的基础 —— 在机器学习流行之前，人工智能算法很大程度上就是回溯搜索（当然搜索过程中有很多技巧）。
 * */

public class L51_NQueens {
    /*
     * 解法1：
     * - 思路：以4皇后为例，在4*4的棋盘上放置4个皇后，其中的隐含线索是每行都要放一个皇后（若某行没有皇后，则说明一定有一行里会
     *   同时出现2个皇后，从而违背了题意）。若每行都要放一个皇后，那么我们只需对每行进行遍历，逐一来看每个格子是否能放皇后即可。
     *   例如第一行中在 [0,0] 处放皇后，那么第二行中，[1,0]、[1,1] 都不能放，而 [1,2] 处可以放，那么第三行中就哪都不能放了，
     *   ∴ 返回第二行，将皇后放在 [1,3] 处，此时第三行可以放在 [2,1] 处，而此时第四行哪里都不能放 ∴ 返回第三行，而第三行没有
     *   其他能放的地方 ∴ 再返回第二行，而第二行已经到头 ∴ 返回第一行，再从 [0,1] 处开始尝试……
     *            ["Q...",     ["Q...",
     *             "..Q.",      "...Q",
     *             ""    ]      ".Q..",
     *                          ""    ]
     *   可见，每次操作都是在某一行中尝试放置皇后，若不能放则返回上一行，重新放置上一行中的皇后然后继续尝试，直到四行中都找到了
     *   能放皇后的位置时就找到了一个解。可见这是一个回溯搜索的过程（也就是 DFS 过程）。该过程若用树形结构来描述：
     *                        4 Queens
     *                    /     /   \     \
     *         row 1:    0     1     2     3
     *                  / \    |     |    / \
     *         row 2:  2   3   3     0   0   1
     *                     |   |     |   |
     *         row 3:      2   0     3   2
     *                         |     |
     *         row 4:          2     1
     *
     *   至此总体思路已经形成，剩下要解决的问题是如何能快速对树进行剪枝，即判断出一个格子是否可以放置皇后，即在一个格子的横、竖、斜
     *   四个方向上判断是否还有其他皇后存在：
     *     - 横：∵ 每行只会放一个皇后，放置之后就进入下一行 ∴ 无需额外逻辑进行判断；
     *     - 竖：可使用一个4位数组 col[i] 来记录第 i 列是否已有皇后；
     *     - 斜：若也想用数组 dia1[i]、dia2[i] 来分别记录两个对角方向上第 i 条对角线上是否有皇后，则需知道：
     *            1. 共有多少条对角线（即 dia 数组大小）；
     *            2. 如何标记一条对角线（即如何访问 dia 中元素）。
     *          通过找规律可知：
     *            - n*n 的棋盘共有 2*n-1 条对角线 ∴ dia1、dia2 的大小都应为 2*n-1；
     *            - 对于 / 方向，每条对角线经过的格子的横纵坐标之和都相同，分别是0，1，2，3，4，5，6 ∴ 可将 i+j 作为索引使用；
     *            - 对于 \ 方向，每条对角线经过的格子的横纵坐标之差都相同，分别是-3，-2，-1，0，1，2，3 ∴ 可将 i-j 加上偏移量 n-1 作为索引使用。
     *
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<String>> solveNQueens(int n) {
        return null;
    }

    public static void main(String[] args) {
        log(solveNQueens(4));
        /*
         * expects [   // 4皇后问题有两个解
         *   [".Q..",
         *    "...Q"
         *    "Q..."
         *    "..Q."],
         *
         *   ["..Q.",
         *    "Q..."
         *    "...Q"
         *    ".Q.."]
         * ]
         * */
    }
}
