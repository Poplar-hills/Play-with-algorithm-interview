package StackAndQueue.S5_PriorityQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
 * Merge k Sorted Lists
 *
 * - Merge k sorted linked lists and return it as one sorted list.
 * */

public class L23_MergeKSortedLists {
    /*
     * 解法1：PriorityQueue 全排序
     * - 思路：该题的本质是排序 ∴ 容易想到将 lists 中的所有链表的所有节点放到一起进行排序。可行的排序方式有：
     *   1. 借助 PriorityQueue 进行堆排序；
     *   2. 借助 TreeSet 进行 BST 排序（但不适用该题 ∵ BST 不允许重复节点，且无法像 L347 解法3那样在 value 相同的情况下可再按 key 排序）；
     *   3. Merge sort（SEE：解法2、3、4）。
     *   该解法采用方案1，使用 PriorityQueue 对所有节点进行堆排序。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。其中 n 为所有节点数。
     * */
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);  // 基于 val 的最小堆
        for (ListNode head : lists)  // 将所有链表的所有节点添加到 pq 中，O(nlogn) 操作
            for (ListNode curr = head; curr != null; curr = curr.next)
                pq.offer(curr);

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {
            curr.next = pq.poll();
            curr = curr.next;
        }
        curr.next = null;  // 在将各个链表的节点重新链接后，需要将最后一个节点的 next 置空，否则可能成环（如 test case 2）

        return dummyHead.next;
    }

    /*
     * 解法2：Merge one by one
     * - 思路：另一种方案是对 lists 中的链表进行 reduce，即将 merge 多个链表的过程 reduce 成两两链表 merge 的过程
     *   （或者说是 generalize merge sort to sort k arrays）。
     * - 实现：若使用 Stream.reduce 则 test case 3 会报错 ∵ Java 中 reduce 的 accumulator 不能返回 null ∴ 最好直接
     *   使用命令式的 for 循环。
     * - 时间复杂度 O(k*n)，空间复杂度 O(1)。其中 k = len(lists)，n 为所有节点数。
     *   （分析过程 SEE: https://coding.imooc.com/learn/questiondetail/133949.html）
     * */
    public static ListNode mergeKLists2(ListNode[] lists) {
        if (lists.length == 0) return null;
        for (int i = 1; i < lists.length; i++)  // 将 lists 中的所有链表 reduce 成一个链表
            lists[0] = merge2Lists(lists[0], lists[i]);
        return lists[0];
    }

    private static ListNode merge2Lists(ListNode l1, ListNode l2) {  // 即 L21_MergeTwoSortedLists 的解法3，O(m+n)
        ListNode dummyHead = new ListNode(), curr = dummyHead;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr = curr.next = l1;  // 从右往左赋值，相当于 curr.next = l1; curr = curr.next;
                l1 = l1.next;
            } else {
                curr = curr.next = l2;
                l2 = l2.next;
            }
        }
        if (l1 != null) curr.next = l1;
        if (l2 != null) curr.next = l2;

        return dummyHead.next;
    }

    /*
     * 解法3：Merge by pairing up
     * - 思路：解法2中，将多个链表 reduce 成一个的过程不是二分的，即不是将 lists 中的链表两两 merge，而是每个链表都和第一个
     *   链表 merge，这样会重复遍历第一个链表 k 次，效率较低。该解法中 pair up lists and merge them without handling
     *   any list more than once。例如对于 lists = [l1,l2,l3,l4]：
     *     - 解法2所需遍历的节点个数：                         - 而该解法所需遍历节点个数：
     *         第1次 merge：l1 + l2                            第1次 merge：l1 + l4
     *         第2次 merge：(l1 + l2) + l3                     第2次 merge：l2 + l3
     *         第3次 merge：(l1 + l2 + l3) + l4                第3次 merge：(l1 + l4) + (l2 + l3)
     *         共计遍历节点个数：3*l1 + 3*l2 + 2*l3 + l4         共计遍历节点个数：l1 + l4 + l2 + l3
     * - 实现：要将 lists 中的链表两两 pair，最终合成一个的过程：
     *           +-----+
     *           ↓     |
     *       [a, b, c, d, e]  ->  [ae, bd, c, d, e]  ->  [ace, bd, c, d, e]  ->  [abcde, bd, c, d, e]
     *        ↑           |        ↑       |               ↑    |
     *        +-----------+        +-------+               +----+
     *   该实现需要两层循环，内层循环负责配对链表（一头一尾），并将尾部的链表 merge 进头部链表中。这样的配对方式在每次循环中只能
     *   merge 一半的链表 ∴ 需要外层循环控制，在没有将所有链表 merge 之前持续运行内存循环。
     * - 💎 技巧：该思路👆和实现👇是数组合并的经典处理方式，可以跟解法2中的 reduce 方式一起比较记忆。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(1)。其中 k = len(lists)，n 为所有节点数。
     *   O(nlogk) 的原因与归并排序的复杂度的原因一致，SEE: play-with-algorithms/MergeSort.java - 归并排序的思想。
     * */
    public static ListNode mergeKLists3(ListNode[] lists) {
        int len = lists.length;
        if (len == 0) return null;

        while (len > 1) {                      // len == 1 时说明已经 merge 了所有链表
            for (int i = 0; i < len / 2; i++)  // 遍历 lists 中前一半的链表
                lists[i] = merge2Lists(lists[i], lists[len - 1 - i]);  // 将倒数第 i 个链表 merge 到第 i 个链表里
            len = (len + 1) / 2;               // merge 完后将 len 砍半（注意 ∵ len 可能为奇数 ∴ lists 中间的链表
        }                                      // 可能没有配对 ∴ 需要放到下一轮中继续 merge

        return lists[0];
    }

    /*
     * 解法4：Merge by pairing up (merge sort)
     * - 思路：总体思路与解法3一致，也是使用二分的形式将 lists 中的链表两两配对进行 merge。
     * - 实现：与解法3不同，该解法使用标准归并排序思路，先对 lists 中的链表不断进行二分（partition），最后再两两归并（merge）。
     * - 参考：L75_SortColors 解法2（数组的 merge sort）。
     * - 💎 反思：换一个角度思考，其实链表数组就是在一维的普通数组之上，每个元素延伸出了多个节点形成链表而已：
     *           [1, 1, 2]  ->  [1, 1, 2]
     *                           ↓  ↓  ↓
     *                           4  3  6
     *                           ↓  ↓
     *                           5  4
     *   不管是对数组还是对链表数组进行 merge sort，都是在水平维度上进行二分（partition）和归并（merge），只是在归并时，
     *   普通数组的归并对象是元素，而链表数组的归并对象是链表。
     * - 时间复杂度 O(nlogk)。
     * - 空间复杂度 O(nlogk)，原因是两个方法都采用了递归，若 merge 用迭代则整体空间复杂度为 O(logk)。
     * */
    public static ListNode mergeKLists4(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeSort(lists, 0, lists.length - 1);
    }

    private static ListNode mergeSort(ListNode[] lists, int l, int r) {  // 对 lists 中的链表进行二分，然后两两归并
        if (l == r) return lists[l];
        int mid = (r - l) / 2 + l;
        ListNode l1 = mergeSort(lists, l, mid);  // 二分操作（partition）
        ListNode l2 = mergeSort(lists, mid + 1, r);
        return merge(l1, l2);                    // 二分到底后开始两两归并（merge）
    }

    private static ListNode merge(ListNode l1, ListNode l2) {  // merge2Lists 的递归版（即 L21_MergeTwoSortedLists 的解法4）
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val < l2.val) {
            l1.next = merge(l1.next, l2);  // 对链表递归大多是这种模式：l.next = recursiveFn(...)
            return l1;
        } else {
            l2.next = merge(l1, l2.next);
            return l2;
        }
    }

    /*
     * 解法5：使用 PriorityQueue 模拟 merge sort
     * - 思路：与解法1不同，该解法不一次性将所有节点放入 PriorityQueue 中排序，而是先将 k 个链表的头节点放入比较，之后不断将
     *   堆中最小的节点出队链到结果链表上，之后再将其下一个节点入队，以此方式模拟 merge sort 过程。
     * - 实现：与解法1不同，该解法不需要在最后让 curr.next = null ∵ 该解法不是一次性将整个链表的所有节点都放入 pq 中排序，
     *   而是每次只放入下一个节点 ∴ 不会出现把同一个链表后面的节点放到前面的情况（例如解法1运行 test case 2 则会将得到：
     *   -2 -----> -1 -----> -1 -----> -1，导致如果不让 curr.next = null，则第二个-1还连着最后一个-1导致成环）。
     *              |         |         |
     *          最后一个-1  第一个-1   第二个-1
     * - 时间复杂度 O(klogk + nlogk)，空间复杂度 O(k)。其中 k 为链表个数，n 为所有节点数。
     * */
    public static ListNode mergeKLists5(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));  // 相当于 (a, b) -> a.val - b.val
        for (ListNode head : lists)   // 把 k 个链表的头节点装入最小堆中，复杂度为 O(klogk)
            if (head != null)         // 跳过空链表
                pq.offer(head);       // 链表头节点进入堆中后会进行排序

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {       // 该循环会遍历所有节点，且每次会有出队、入队操作 ∴ 是 O(nlogk)
            ListNode node = pq.poll();
            curr.next = node;
            if (node.next != null) pq.offer(node.next);
            curr = curr.next;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 4, 5});
        ListNode l2 = createLinkedList(new int[]{1, 3, 4});
        ListNode l3 = createLinkedList(new int[]{2, 6});
        ListNode res = mergeKLists2(new ListNode[]{l1, l2, l3});
        printLinkedList(res);   // expects 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6

        ListNode l4 = createLinkedList(new int[]{-2, -1, -1, -1});
        ListNode l5 = createLinkedList(new int[]{});
        ListNode res2 = mergeKLists2(new ListNode[]{l4, l5});
        printLinkedList(res2);  // expects -2 -> -1 -> -1 -> -1

        ListNode l6 = createLinkedList(new int[]{});
        ListNode l7 = createLinkedList(new int[]{});
        ListNode res3 = mergeKLists2(new ListNode[]{l6, l7});
        printLinkedList(res3);  // expects null
    }
}
