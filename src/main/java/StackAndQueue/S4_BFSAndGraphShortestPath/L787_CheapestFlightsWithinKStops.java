package StackAndQueue.S4_BFSAndGraphShortestPath;

import static Utils.Helpers.log;

/*
 * Cheapest Flights Within K Stops
 *
 * - There are "n" cities connected by "m" flights. Each flight starts from city "u" and arrives at "v" with
 *   a price "w". Given all the cities, flights, starting city "src" and the destination "dst", find the
 *   cheapest price from "src" to "dst" with up to "k" stops. If there is no such route, output -1.
 *
 * - Note there will not be any duplicated flights or self cycles.
 * */

public class L787_CheapestFlightsWithinKStops {
    /*
     * 解法1：
     * - 思路：
     * - 实现：
     * - 时间复杂度 O()
     * */
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        return 0;
    }

    public static void main(String[] args) {
        int[][] flights1 = new int[][]{{0, 1, 100}, {1, 2, 100},{0, 2, 500}};
        int n1 = 3, src1 = 0, dst1 = 2, k1 = 1;
        log(findCheapestPrice(n1, flights1, src1, dst1, k1));
        /*
         * expects 200.
         *               0
         *             ↙   ↘
         *       100 ↙       ↘ 500
         *         ↙           ↘
         *        1 →  →  →  →  2
         *              100
         * */

        int[][] flights2 = new int[][]{{0, 1, 100}, {1, 2, 100},{0, 2, 500}};
        int n2 = 3, src2 = 0, dst2 = 2, k2 = 0;
        log(findCheapestPrice(n2, flights2, src2, dst2, k2));
        /*
         * expects 500.
         *               0
         *             ↙   ↘
         *       100 ↙       ↘ 500
         *         ↙           ↘
         *        1 →  →  →  →  2
         *              100
         * */
    }
}
