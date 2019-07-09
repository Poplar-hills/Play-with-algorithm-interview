package HashTable.SetAndMapBasics;

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
*           -> 1^2 + 0^2 + 0^2 = 1，因此19是 happy number
* */

public class L202_HappyNumber {
    /*
    * 解法1 - Map + 递归
    * - 思路：本题的关键是如何检测循环 -- 若没有循环则所有数字应该只出现一次，不会重复，因此可以使用 Map 或 Set。
    * */
    public static boolean isHappy(int n) {
        Map<Integer, Integer> map = new HashMap<>();
        return isHappy(n, map);
    }

    private static boolean isHappy(int n, Map<Integer, Integer> map) {
        map.put(n, map.getOrDefault(n, 0) + 1);
        if (map.get(n) > 1) return false;
        int s = sumOfDigitSquare(n);
        return s == 1 || isHappy(s, map);
    }

    private static int sumOfDigitSquare(int n) {
        int sum = 0;
        while (n > 0) {
            sum += (n % 10) * (n % 10);
            n /= 10;
        }
        return sum;
    }

    /*
     * 解法2 - Set + 迭代
     * */
    public static boolean isHappy2(int n) {
        Set<Integer> set = new HashSet<>();
        set.add(n);
        while (n != 1) {  // 循环终止条件是抵达1
            int sum = sumOfDigitSquare(n);
            if (set.contains(sum)) return false;
            set.add(sum);
            n = sum;
        }
        return true;
    }

    /*
     * 解法3 - Floyd Cycle detection
     * */
    public static boolean isHappy3(int n) {
        int slow = n, fast = n;
        do {
            slow = sumOfDigitSquare(slow);
            fast = sumOfDigitSquare(sumOfDigitSquare(fast));  // fast 每次执行两遍，slow 执行一遍
        } while (slow != fast);                               // 若中途 fast == slow 则说明有环
        return slow == 1;
    }

    public static void main(String[] args) {
        log(isHappy(19));   // true
        log(isHappy2(19));  // true
        log(isHappy3(19));  // true
    }
}