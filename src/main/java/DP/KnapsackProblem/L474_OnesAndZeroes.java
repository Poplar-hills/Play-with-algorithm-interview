package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;

/*
* Ones and Zeroes
*
* - 给定一个字符串数组，其中每个字符串都是由01组成的，问用 m 个0和 n 个1最多能组成数组中的多少个字符串。
* */

public class L474_OnesAndZeroes {
    public static int findMaxForm(String[] strs, int m, int n) {
        return 0;
    }

    public static void main(String[] args) {
        log(findMaxForm(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3));  // expects 4. ("10", "0001", "1", "0")
        log(findMaxForm(new String[]{"10", "0", "1"}, 1, 1));                    // expects 2. ("0", "1")
    }
}