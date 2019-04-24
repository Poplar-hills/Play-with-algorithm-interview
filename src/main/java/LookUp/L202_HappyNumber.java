package LookUp;

import java.util.HashMap;
import java.util.Map;

import static Utils.Helpers.log;

/*
* Happy Number
*
* - 将一个数字替换为其各位数字的平方和，重复该过程，如果最终能得到1，则是 happer number，若陷入循环则不是。
*   例如：19 -> 1^2 + 9^2 = 82
*           -> 8^2 + 2^2 = 68
*           -> 6^2 + 8^2 = 100
*           -> 1^2 + 0^2 + 0^2 = 1，因此19是 happy number
* */

public class L202_HappyNumber {
    /*
    * 解法1 - 使用 map
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
     * 解法3 - Floyd Cycle detection
     * */
    public static boolean isHappy2(int n) {
        int slow = n, fast = n;
        do {
            slow = sumOfDigitSquare(slow);
            fast = sumOfDigitSquare(fast);
            fast = sumOfDigitSquare(fast);
        } while (slow != fast);
        return slow == 1;
    }

    public static void main(String[] args) {
        log(isHappy(19));  // true
        log(isHappy2(19));  // true
    }
}