package StackAndQueue.BasicsOfStack;

import java.util.Stack;

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
    public static String simplifyPath(String path) {

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
