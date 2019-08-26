package DP.KnapsackProblem;

import static Utils.Helpers.*;

import java.util.Arrays;
import java.util.Comparator;

/*
* Ones and Zeroes
*
* - 给定一个字符串数组，其中每个字符串都是由01组成的，问用 m 个0和 n 个1最多能组成数组中的多少个字符串。
* - Note: each 0 and 1 can be used at most once.
* */

public class L474_OnesAndZeroes {
    public static int findMaxForm(String[] strs, int m, int n) {
        // Arrays.sort(strs, (a, b) -> a.length() - b.length());
        Arrays.sort(strs, Comparator.comparingInt(String::length));
        int res = 0;
        for (String str : strs) {
            int i = 0;
            for ( ; i < str.length(); i++) {
                char c = str.charAt(i);
                if ((c == '0' && m == 0) || (c == '1' && n == 0)) break;
                if (c == '0') m--;
                if (c == '1') n--;
            }
            if (i == str.length()) res++;
            if (m == 0 && n == 0) break;
        }
        return res;
    }

    public static void main(String[] args) {
        log(findMaxForm(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3));  // expects 4. ("10", "0001", "1", "0")
        log(findMaxForm(new String[]{"10", "0001", "111001", "1", "0"}, 4, 3));  // expects 3. ("10", "1", "0")
        log(findMaxForm(new String[]{"10", "0", "1"}, 1, 1));                    // expects 2. ("0", "1")
        log(findMaxForm(new String[]{"111", "1000", "1000", "1000"}, 9, 3));     // expects 3. ("1000", "1000", "1000")
    }
}