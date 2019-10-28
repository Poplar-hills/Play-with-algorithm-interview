package Utils;

import javafx.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Helpers {
    public static void log(Object content) { System.out.println(content); }

    public static void log(int[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(char[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(boolean[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(Object[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(TreeNode node) { System.out.println(node == null ? null : node.getVal()); }

    public static <E> void swap(E[] arr, int i, int j) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length)
            throw new IllegalArgumentException("swap failed. Index is out of bounds.");
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(int[] arr, int i, int j) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length)
            throw new IllegalArgumentException("swap failed. Index is out of bounds.");
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static Integer[] generateRandomIntArr(int size) {
        Random r = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++)
            arr[i] = r.nextInt(size);
        return arr;
    }

    public static Character[] generateRandomCharArr(int size) {
        Random r = new Random();
        Character[] arr = new Character[size];
        for (int i = 0; i < size; i++)
            arr[i] = (char) (r.nextInt(26) + 'a');
        return arr;
    }

    public static void timeIt(Comparable[] arr, Consumer<Comparable[]> fn) {
        double startTime = System.nanoTime();
        fn.accept(arr);
        double endTime = System.nanoTime();
        log(String.format("Time consumed: %s", (endTime - startTime) / 1000000000.0));
    }

    public static void timeIt(Integer num, Function<Integer, Integer> fn) {
        double startTime = System.nanoTime();
        fn.apply(num);
        double endTime = System.nanoTime();
        log(String.format("Time consumed: %s", (endTime - startTime) / 1000000000.0));
    }

    public static Integer[] generateNearlyOrderedArr(int size, int numOfSwap) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++)
            arr[i] = i;

        Random r = new Random();
        for (int i = 0; i < numOfSwap; i++) {
            int index1 = r.nextInt(size);
            int index2 = index1;
            while (index1 == index2)
                index2 = r.nextInt(size);
            swap(arr, index1, index2);
        }

        return arr;
    }

    public static Integer[] generateRandomArrayFromRange(int size, int lowerBound, int upperBound) {
        Random r = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < arr.length; i++)
            arr[i] = r.nextInt(upperBound - lowerBound) + lowerBound;
        return arr;
    }

    public static boolean isSorted(Comparable[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0)
                return false;
        }
        return true;
    }

    // 链表节点类
    public static class ListNode {
        public int val;        // Have to make the attributes and the methods public
        public ListNode next;
        public ListNode(int x) { val = x; }
        public ListNode() { }
    }

    // 从数组生成链表
    public static ListNode createLinkedListFromArray(int[] arr) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        for (int n : arr) {
            curr.next = new ListNode(n);
            curr = curr.next;
        }
        return dummyHead.next;
    }

    // 打印链表
    public static void printLinkedList(ListNode head) {
        StringBuilder s = new StringBuilder();
        while (head != null) {
            s.append(head.val);
            s.append("->");
            head = head.next;
        }
        s.append("NULL");
        log(s.toString());
    }

    // 二叉树节点类
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int x) { val = x; }
        public int getVal() { return val; }
    }

    // 以深度优先的方式从数组生成二叉树
    public static TreeNode createBinaryTreeDepthFirst(Integer[] arr) {
        return arr.length == 0 ? null : createBinaryTreeDepthFirst(arr, 0).getValue();
    }

    private static Pair<Integer, TreeNode> createBinaryTreeDepthFirst(Integer[] arr, int i) {  // i 指向 arr 中下一个待访问的元素
        TreeNode node = new TreeNode(arr[i]);

        if (++i < arr.length && arr[i] != null) {
            Pair<Integer, TreeNode> p = createBinaryTreeDepthFirst(arr, i);
            i = p.getKey();
            node.left = p.getValue();
        }
        if (++i < arr.length && arr[i] != null) {
            Pair<Integer, TreeNode> p = createBinaryTreeDepthFirst(arr, i);
            i = p.getKey();
            node.right = p.getValue();
        }

        return new Pair<>(i, node);
    }

    // 以广度优先的方式从数组生成二叉树
    public static TreeNode createBinaryTreeBreadthFirst(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null)
            return null;

        Queue<TreeNode> q = new LinkedList<>();
        TreeNode tree = new TreeNode(arr[0]);
        q.offer(tree);

        for (int i = 1; i < arr.length && !q.isEmpty(); i += 2) {  // 遍历 arr，每次消费两个元素用于创建左右子节点
            TreeNode curr = q.poll();                        // 得到父节点
            if (arr[i] != null) {                            // 用 arr[i] 创建左子节点
                curr.left = new TreeNode(arr[i]);
                q.offer(curr.left);
            }
            if (i + 1 < arr.length && arr[i + 1] != null) {  // 用 arr[i+1] 创建右子节点
                curr.right = new TreeNode(arr[i + 1]);
                q.offer(curr.right);
            }
        }
        return tree;
    }

    // 以深度优先的方式打印二叉树
    public static void printBinaryTreeDepthFirst(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root != null) collectDepthFirst(root, list);
        log(list);
    }

    private static void collectDepthFirst(TreeNode node, List<Integer> list) {
        if (node == null) {
            list.add(null);
            return;
        }
        list.add(node.val);
        collectDepthFirst(node.left, list);
        collectDepthFirst(node.right, list);
    }

    // 以广度优先的方式打印二叉树
    public static void printBinaryTreeBreadthFirst(TreeNode root) {
        List<Integer> list = new ArrayList<>();

        if (root == null) {
            log(list);
            return;
        }

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null)
                list.add(null);      // 最终非叶子节点下的 null 节点会被打印出来（例：[1, null, 2]）
            else {
                list.add(node.val);
                if (node.left == null && node.right == null)  // 只有叶子节点的左右子节点（null）不进入队列
                    continue;
                q.offer(node.left);  // 只要左右子节点至少有一个不是 null，则左右子节点就都要进入队列
                q.offer(node.right);
            }
        }

        log(list);
    }
}
