package Array.S4_TwoPointerCollision;

import static Utils.Helpers.swap;
import static Utils.Helpers.log;

/*
 * Reverse String
 *
 * - Reverses a string. The input string is given as an array of characters.
 * - You must do this by modifying the input array in-place with O(1) extra memory.
 * - Assume all the characters are ASCII characters.
 *
 * - 👉Java 语法：虽然 Utils/Helpers.java 中有 generic swap() 方法，但 ∵ 本题中的输入是原始类型 char[]，而泛型不支持
 *   原始类型 ∴ 无法使用。Java 中的泛型只存在于编译期，编译到字节码之后泛型会被擦除，即泛型不会进入到运行时阶段。而在进行泛型
 *   擦除时，泛型值需要被转换成 Object ∴ 能被用作泛型的必须是能转成 Object 的类型，而原始类型是不支持这一点的。
 * */

public class L344_ReverseString {
    /*
     * 解法1：指针对撞 + swap
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static void reverseString(char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--)
            swap(s, i, j);
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
