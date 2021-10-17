package Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Helpers {
    public static void log(Object content) { System.out.println(content); }

    public static void log(int[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(char[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(boolean[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(Object[] arr) { System.out.println(Arrays.toString(arr)); }

    public static void log(char[][] twoDArr) {
        StringBuilder sb = new StringBuilder();
        for (char[] arr : twoDArr) {
            for (char c : arr)
                sb.append(c).append(" ");
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static void log(ListNode head) { System.out.println(head); }

    public static void log(TreeNode node) { System.out.println(node == null ? null : node.getVal()); }

    public static <E> void swap(E[] arr, int i, int j) {  // generic swap 方法（但不支持原始类型）
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length)
            throw new IllegalArgumentException("swap failed. Index is out of bounds.");
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(int[] arr, int i, int j) {  // int 类型的 swap 方法
        if (i < 0 || i >= arr.length || j < 0 || j >= arr.length)
            throw new IllegalArgumentException("swap failed. Index is out of bounds.");
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void swap(char[] arr, int i, int j) {  // char 类型的 swap 方法
        char temp = arr[i];
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

    /*
     * 链表节点类
     * */
    public static class ListNode {
        public int val;        // Have to make the attributes and the methods public
        public ListNode next;
        public ListNode(int x) { val = x; }
        public ListNode() { }

        public ListNode get(int i) {  // 获取并返回第 i 个节点
            ListNode curr = this;
            while (curr.next != null && i != 0) {
                curr = curr.next;
                i--;
            }
            return i == 0 ? curr : null;
        }

        public ListNode tail() {  // 找到并返回链表中的最后一个节点（SEE: L160_IntersectionOfTwoLinkedLists）
            ListNode curr = this;
            while (curr.next != null)
                curr = curr.next;
            return curr;
        }

        @Override
        public String toString() {  // 打印链表
            StringBuilder s = new StringBuilder();
            ListNode curr = this;
            while (curr != null) {
                s.append(curr.val);
                s.append("->");
                curr = curr.next;
            }
            s.append("NULL");
            return s.toString();
        }

    }

    /*
     * 从数组生成链表
     * */
    public static ListNode createLinkedList(int[] arr) {
        ListNode dummyHead = new ListNode();
        ListNode curr = dummyHead;
        for (int n : arr) {
            curr.next = new ListNode(n);
            curr = curr.next;
        }
        return dummyHead.next;
    }

    /*
     * 二叉树节点类
     * */
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int x) { val = x; }
        public int getVal() { return val; }

        public TreeNode get(int val) { return get(this, val); }  // 在二叉树中找到并返回第一个值为 val 的节点的引用（SEE: L236_LCAOfBinaryTree）

        private TreeNode get(TreeNode root, int val) {
            if (root == null) return null;
            if (root.val == val) return root;
            TreeNode left = get(root.left, val);
            if (left != null) return left;
            TreeNode right = get(root.right, val);
            if (right != null) return right;
            return null;
        }
    }

    /*
     * 以深度优先的方式从数组生成二叉树（实现1）
     * */
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

    /*
     * 以广度优先的方式从数组生成二叉树（实现2）
     * - 与👆实现1的差别就是把 i 作为类的成员变量来实现而已。
     * */
    private static int i = 0;

    public static TreeNode createBinaryTreeDepthFirst2(Integer[] arr) {
        if (arr == null || arr.length == 0) return null;
        return helper(arr, new TreeNode(arr[0]));
    }

    private static TreeNode helper(Integer[] arr, TreeNode root) {
        if (root == null) return null;
        if (++i < arr.length && arr[i] != null)
            root.left = helper(arr, new TreeNode(arr[i]));
        if (++i < arr.length && arr[i] != null)
            root.right = helper(arr, new TreeNode(arr[i]));
        return root;
    }

    /*
     * 以广度优先的方式从数组生成二叉树
     * */
    public static TreeNode createBinaryTreeBreadthFirst(Integer[] arr) {  // ∵ 树中节点可能为 null ∴ 使用 Integer 数组
        if (arr == null || arr.length == 0 || arr[0] == null)
            return null;

        Queue<TreeNode> q = new LinkedList<>();
        TreeNode tree = new TreeNode(arr[0]);
        q.offer(tree);

        for (int i = 1; i < arr.length && !q.isEmpty(); i += 2) {  // 遍历 arr，每次消费两个元素用于创建左右子节点
            TreeNode node = q.poll();               // 得到父节点
            if (arr[i] != null) {                   // 用 arr[i] 创建左子节点
                node.left = new TreeNode(arr[i]);
                q.offer(node.left);
            }
            if (i + 1 < arr.length && arr[i + 1] != null) {  // 用 arr[i+1] 创建右子节点
                node.right = new TreeNode(arr[i + 1]);
                q.offer(node.right);
            }
        }
        return tree;
    }

    /*
     * 以深度优先的方式打印二叉树
     * */
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

    /*
     * 以广度优先的方式打印二叉树
     * */
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
                list.add(null);
            else {
                list.add(node.val);
                q.offer(node.left);
                q.offer(node.right);
            }
        }

        int last = list.size() - 1;
        for (int i = list.size() - 1; i >= 0; i--) {  // 找到 list 中最后一个非 null 元素的索引
            if (list.get(i) != null) {
                last = i;
                break;
            }
        }

        log(list.subList(0, last + 1));
    }

    /*
     * Pair 类（copy from javafx.util）
     * */
    public static class Pair<K,V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }

        @Override
        public String toString() { return key + "=" + value; }

        @Override
        public int hashCode() {
            return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (key == null ? pair.key != null : !key.equals(pair.key)) return false;
                if (value == null ? pair.value != null : !value.equals(pair.value)) return false;
                return true;
            }
            return false;
        }
    }
}
