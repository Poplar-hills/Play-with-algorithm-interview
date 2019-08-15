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

    public static void log(Object[] arr) {
        System.out.println(Arrays.toString(arr));
    }

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

    public static class ListNode {  // The node class for linked list. Have to make the attributes and the method public
        public int val;
        public ListNode next;
        public ListNode(int x) { val = x; }
        public ListNode() { }
    }

    public static ListNode createLinkedListFromArray(int[] arr) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        for (int n : arr) {
            curr.next = new ListNode(n);
            curr = curr.next;
        }
        return dummyHead.next;
    }

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

    public static class Node {
        public Node left;
        public Node right;
        public int value;
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public static TreeNode createBinaryTreeDepthFirst(Integer[] arr) {  // 以深度优先的方式从数组生成二叉树
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

    public static TreeNode createBinaryTreeBreadthFirst(Integer[] arr) {  // 以广度优先的方式从数组生成二叉树
        if (arr == null || arr.length == 0 || arr[0] == null)
            return null;

        int i = 0;  // i 指向 arr 中下一个待访问的元素
        Queue<TreeNode> q = new LinkedList<>();
        TreeNode tree = new TreeNode(arr[i++]);
        q.offer(tree);

        while (i < arr.length && !q.isEmpty()) {
            TreeNode curr = q.poll();
            if (arr[i] != null) {
                curr.left = new TreeNode(arr[i]);
                q.offer(curr.left);
            }
            i++;
            if (arr[i] != null) {
                curr.right = new TreeNode(arr[i]);
                q.offer(curr.right);
            }
            i++;
        }
        return tree;
    }

    public static void printBinaryTree(TreeNode node) {
        ArrayList<Integer> list = new ArrayList<>();
        if (node != null) printBinaryTree(node, list);
        log(list);
    }

    private static void printBinaryTree(TreeNode node, List<Integer> list) {
        if (node == null) {
            list.add(null);
            return;
        }
        list.add(node.val);
        printBinaryTree(node.left, list);
        printBinaryTree(node.right, list);
    }

    public static int maxOfN(int ...nums) {
        return Arrays.stream(nums)
            .reduce(Math::max)
            .getAsInt();  // reduce 返回的是一个 OptionalInt，需要解包
    }
}
