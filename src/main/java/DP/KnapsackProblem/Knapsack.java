package DP.KnapsackProblem

import static Utils.Helpers.*;

/*
* Knapsack Problem
*
* - 有个背包，容量为 C, 现有 n 个不同种物品，编号为 0..n-1，其中每件物品的重量为 w(i)，价值为 v(i)。求可以像这个背包中放入哪些
*   物品，使得在不超过容量的基础上，背包中的物品价值最大。
*
* - 分析：该问题本质上是个最优组合问题。
*
* - Brute Force：
*   1. n 个不同种物品，每种物品都可以选择放入和不放入背包，因此共有 2^n 种组合；
*   2. 计算每种组合的总重，并 filter 掉所有总重超过 c 的组合；
*   3. 再计算剩下每种组合的总价值，并选出最大者。
*   - 总时间复杂度为 O((2^n)*n)。
*
* - Greedy Algorithm：
*
* */

public class Knapsack {

}
