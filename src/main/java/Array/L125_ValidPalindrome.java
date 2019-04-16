package Array;

import static Utils.Helpers.log;

/*
* Valid Palindrome
*
* - 只考虑字符串和数字，不考虑空格、标点符号
* - 大小写不敏感
* - 使用指针对撞思路
* */

public class L125_ValidPalindrome {
    private static boolean validPalindrome(String s) {
        s = s.toLowerCase();
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) i++;
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
