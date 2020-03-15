package StackAndQueue.S1_BasicsOfStack;

import java.util.*;
import java.util.stream.Stream;

import static Utils.Helpers.*;

/*
 * Simplify Path
 *
 * - Given an absolute path for a file (Unix-style), simplify it by converting it to the canonical path.
 *   1. The returned canonical path must always begin with a slash /
 *   2. There must be only a single slash / between two directory names.
 *   3. The last directory name (if it exists) must not end with a trailing /.
 *   4. The canonical path must be the shortest string representing the absolute path.
 * */

public class L71_SimplifyPath {
    /*
     * 解法1：Stack
     * - 思路：首先分析题中的不同情况：
     *     1. 正常路径（"/home/"）
     *     2. 空路径（"//"）
     *     3. 停在当前目录（"/./"）
     *     4. 回到上级目录（"/../"）
     *   若根据对目录的操作方式分类，情况1是进入下一级目录，情况2、3是停在当前目录，情况3是回到上一级目录。而且这三种操作都发生
     *   在尾部 ∴ 可以想到使用 Stack 作为辅助数据结构。
     * - 实现：注意若使用 Deque 来实现 Stack，则最终输出是反的 ∵ they have reverse iteration orders，SEE:
     *   1. DequeTest.java
     *   2. https://stackoverflow.com/questions/12524826/why-should-i-use-deque-over-stack
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String simplifyPath(String path) {
        if (path == null) return null;
        Stack<String> stack = new Stack<>();

        for (String s : path.split("/")) {
            if (s.isEmpty() || s.equals(".")) continue;
            if (!s.equals("..")) stack.push(s);
            else if (!stack.isEmpty()) stack.pop();
        }

        return "/" + String.join("/", stack);
    }

    /*
     * 解法2：解法1的 stream 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)，实际运行效率比解法1低一些。
     * */
    public static String simplifyPath2(String path) {
        if (path == null) return null;
        Stack<String> stack = new Stack<>();

        Stream.of(path.split("/"))
            .filter(s -> !s.isEmpty() && !s.equals("."))
            .forEach(s -> {
                if (!s.equals("..")) stack.push(s);
                else if (!stack.isEmpty()) stack.pop();
            });

        return "/" + String.join("/", stack);
    }

    public static void main(String[] args) {
        log(simplifyPath("/home/"));                 // expects "/home"
        log(simplifyPath("/../"));                   // expects "/". You cannot go one level up from the root directory
        log(simplifyPath("/home//foo/"));            // expects "/home/foo".
        log(simplifyPath("/a/./b/../../c/"));        // expects "/c"
        log(simplifyPath("/a/../../b/../c//.//"));   // expects "/c"
        log(simplifyPath("/a//b////c/d//././/.."));  // expects "/a/b/c"
    }
}
