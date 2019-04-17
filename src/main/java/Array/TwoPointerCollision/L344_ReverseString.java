package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Reverse String
*
* - 指针对撞思路
* */

public class L344_ReverseString {
    public static void reverseString(char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }

    public static void main(String[] args) {
        char[] arr1 = new char[] {'h', 'e', 'l', 'l', 'o'};
        reverseString(arr1);
        log(arr1);       // expects ['o', 'l', 'l', 'e', 'h']

        char[] arr2 = new char[] {'H', 'a', 'n', 'n', 'a', 'h'};
        reverseString(arr2);
        log(arr2);       // expects ['h', 'a', 'n', 'n', 'a', 'H']
    }
}
