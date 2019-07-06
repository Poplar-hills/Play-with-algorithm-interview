package Array.TwoPointerCollision;

import static Utils.Helpers.log;

/*
* Valid Palindrome
*
* - Given a string, determine if it is a palindrome, considering only alphanumeric characters (即只考虑字母和数字，不考
*   虑空格、标点符号) and ignoring cases. Also, empty string is a valid palindrome.
* */

public class L125_ValidPalindrome {
    /*
    * 解法1：指针对撞
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    private static boolean validPalindrome(String s) {
        s = s.toLowerCase();
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {             // 指针对撞
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) i++;  // 跳过无效字符
            while (j > i && !Character.isLetterOrDigit(s.charAt(j))) j--;
            if (s.charAt(i) != s.charAt(j)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        log(validPalindrome("A man, a plan, a canal: Panama"));  // expects true
        log(validPalindrome("race a car"));  // expects false
    }
}
