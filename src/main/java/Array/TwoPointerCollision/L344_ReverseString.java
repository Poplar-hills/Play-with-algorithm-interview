package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Reverse String
*
* - Reverses a string. The input string is given as an array of characters.
* - You must do this by modifying the input array in-place with O(1) extra memory.
* - Assume all the characters are ASCII characters.
*
* - 不相关的注释：
*   1. Java 中将 char[] 连接成一个 String 的方式：String str = new String(chars);
*   2. Java 中 reverse 一个 String 的最快方式：new StringBuilder(str).reverse().toString();
* */

public class L344_ReverseString {
    /*
    * 解法1：指针对撞，不断交换
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static void reverseString(char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }

    public static void main(String[] args) {
        char[] arr1 = new char[]{'h', 'e', 'l', 'l', 'o'};
        reverseString(arr1);
        log(arr1);  // expects ['o', 'l', 'l', 'e', 'h']

        char[] arr2 = new char[]{'H', 'a', 'n', 'n', 'a', 'h'};
        reverseString(arr2);
        log(arr2);  // expects ['h', 'a', 'n', 'n', 'a', 'H']
    }
}
