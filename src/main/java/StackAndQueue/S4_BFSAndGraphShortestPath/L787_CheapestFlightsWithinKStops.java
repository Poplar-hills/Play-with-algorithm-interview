package StackAndQueue.S4_BFSAndGraphShortestPath;

import static Utils.Helpers.log;

/*
 * Cheapest Flights Within K Stops
 *
 * - There are "n" cities connected by "m" flights. Each flight starts from city "u" and arrives at "v" with
 *   a price "w". Given all the cities, flights, starting city "src" and the destination "dst", find the
 *   cheapest price from "src" to "dst" with up to "k" stops in the middle. Output -1 if there is no such route.
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
        int[][] flights1 = new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        /*
         *               ⓪
         *             ↙   ↘
         *       100 ↙       ↘ 500
         *         ↙           ↘
         *       ①  →  →  →  →  ②
         *              100
         * */
        log(findCheapestPrice(3, flights1, 0, 2, 1));  // expects 200
        log(findCheapestPrice(3, flights1, 0, 2, 0));  // expects 500

        int[][] flights2 = new int[][]{
            {0, 1, 50}, {0, 2, 20}, {0, 3, 60}, {1, 4, 10},
            {2, 1, 10}, {2, 4, 50}, {2, 3, 30}, {3, 4, 20}};
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
        log(findCheapestPrice(8, flights2, 0, 4, 2));   // expects 40.（→ ↑ ↘）
        log(findCheapestPrice(8, flights2, 0, 4, 1));   // expects 60.（↗ ↘）
        log(findCheapestPrice(8, flights2, 0, 4, 0));   // expects -1
        log(findCheapestPrice(8, flights2, 2, 1, 10));  // expects -1
    }
}
