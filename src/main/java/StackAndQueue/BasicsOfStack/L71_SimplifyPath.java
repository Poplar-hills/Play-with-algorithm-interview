package StackAndQueue.BasicsOfStack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.stream.Stream;

import static Utils.Helpers.*;

/*
* Simplify Path
*
* - Given an absolute path for a file (Unix-style), simplify it. Or in other words, convert it to the canonical path.
*   1. The returned canonical path must always begin with a slash /
*   2. There must be only a single slash / between two directory names.
*   3. The last directory name (if it exists) must not end with a trailing /.
*   4. The canonical path must be the shortest string representing the absolute path.
* */

public class L71_SimplifyPath {
    /*
     * 解法1：Stack
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static String simplifyPath(String path) {
        if (path == null) return null;
        Stack<String> stack = new Stack<>();  // 注意：如果使用 Deque 代替 Stack，则最后一句输出会是反的，因为他们 have reverse iteration orders（SEE: https://stackoverflow.com/questions/12524826/why-should-i-use-deque-over-stack）

        for (String s : path.split("/")) {
            if (s.isEmpty() || s.equals(".")) continue;
            if (!s.equals("..")) stack.push(s);
            else if (!stack.isEmpty()) stack.pop();
        }

        return "/" + String.join("/", stack);
    }

    /*
     * 解法2：解法1的 stream 版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
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
        log(simplifyPath("/home/"));  // expects "/home"

        log(simplifyPath("/../"));  // expects "/". You cannot go one level up from the root directory, as it's already the highest level

        log(simplifyPath("/home//foo/"));  // expects "/home/foo".

        log(simplifyPath("/a/./b/../../c/"));  // expects "/c"

        log(simplifyPath("/a/../../b/../c//.//"));  // expects "/c"

        log(simplifyPath("/a//b////c/d//././/.."));  // expects "/a/b/c"
    }
}
