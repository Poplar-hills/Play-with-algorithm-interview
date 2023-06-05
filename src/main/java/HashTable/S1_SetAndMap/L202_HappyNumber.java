package HashTable.S1_SetAndMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Utils.Helpers.log;

/*
 * Happy Number
 *
 * - 将一个数字替换为其各位数字的平方和，重复该过程，如果最终能得到1，则是 happy number，若陷入循环则不是。
 *   例如：19 -> 1^2 + 9^2 = 82
 *           -> 8^2 + 2^2 = 68
 *           -> 6^2 + 8^2 = 100
 *           -> 1^2 + 0^2 + 0^2 = 1 ∴ 19是 happy number
 *
 * - 💎 经验：不要一上来就开始编码，要先用测试数据在纸上演算，分别走通返回不同结果的情况（如 test 1、3）之后就能得到解题思路。
 * */

public class L202_HappyNumber {
    /*
     * 解法1：Map + Recursion
     * - 思路：从题意可知，本题的主体结构是递归；从纸上演算可知，递归的退出条件是 n=1 或检测到循环 ∴ 问题转化为了如何检测循环。
     *   若没有循环，则所有数字应该只出现一次，不会重复 ∴ 只需要使用 hash table 检测是否出现重复数字即可。
     * - 实现：使用 Map 记录 n 的出现频次。
     * - 时间复杂度为 O(n)，空间复杂度为 O(n)。
     * */
    public static boolean isHappy(int n) {
        return helper(n, new HashMap<>());
    }

    private static boolean helper(int n, Map<Integer, Integer> map) {
        if (n == 1) return true;
        int freq = map.merge(n, 1, Integer::sum);  // 相当于 map.put(n, map.getOrDefault(n, 0) + 1);
        if (freq > 1) return false;     // ∵ 上面刚刚给 n 的频率 +1 ∴ 这里检测频率是否 >1

        int squareSum = 0;
        while (n != 0) {
            int rightMostDigit = n % 10;
            squareSum += Math.pow(rightMostDigit, 2);
            n /= 10;
        }

        return helper(squareSum, map);
    }

    /*
     * 解法2：Set + Recursion
     * - 思路：与解法1一致。
     * - 实现：∵ 只需要检查数字是否出现过 ∴ 可以采用 Set。
     * - 时间复杂度为 O(n)，空间复杂度为 O(n)。
     * */
    public static boolean isHappy2(int n) {
        return helper2(n, new HashSet<>());
    }

    public static boolean helper2(int n, Set<Integer> set) {
        if (n == 1) return true;
        if (set.contains(n)) return false;
        set.add(n);
        int squareSum = sumOfDigitSquare(n);
        return helper2(squareSum, set);
    }

    private static int sumOfDigitSquare(int n) {
        int squareSum = 0;
        while (n != 0) {
            int rightMostDigit = n % 10;
            squareSum += Math.pow(rightMostDigit, 2);
            n /= 10;
        }
        return squareSum;
    }

    /*
     * 解法3：Set + Iteration
     * - 思路：与解法1、2一致。
     * - 时间复杂度为 O(n)，空间复杂度为 O(n)。
     * */
    public static boolean isHappy3(int n) {
        Set<Integer> set = new HashSet<>();
        set.add(n);

        while (n != 1) {  // 循环终止条件是抵达1
            int squareSum = sumOfDigitSquare(n);
            if (set.contains(squareSum)) return false;
            set.add(squareSum);
            n = squareSum;
        }

        return true;
    }

    /*
     * 解法4：Floyd Cycle detection
     * - 思路：检测循环的一个经典方式是使用 Floyd Cycle detection（佛洛依德判圈算法，又称龟兔赛跑算法 Tortoise and hare）：
     *   - 它是一个可以在有限状态机、迭代函数或者链表上判断是否存在环，以及判断环的起点与长度的算法。
     *   - 它的基本思路是若一个链上存在环，则在其上以不同速度前进的2个指针必定会在某个时刻相遇，而相遇点就是环的入口。
     *   - 时间复杂度为 O(n)，空间复杂度为 O(1)。
     * */
    public static boolean isHappy4(int n) {
        int slow = n, fast = n;
        do {
            slow = sumOfDigitSquare(slow);
            fast = sumOfDigitSquare(sumOfDigitSquare(fast));  // fast 每次执行两遍，slow 执行一遍
        } while (slow != fast);                               // 若中途 fast == slow 则说明有环
        return slow == 1;
    }

    public static void main(String[] args) {
        log(isHappy(19));   // true.  19 → 82 → 68 → 100 → 1
        log(isHappy(100));  // true.  100 → 1
        log(isHappy(18));   // false. 18 → 65 → 61 → 37 → 58 → 89 → 145 → 42 → 20 → 4 → 16 → 37 → ...
        log(isHappy(0));    // false. 0 → 0 → ...
    }
}