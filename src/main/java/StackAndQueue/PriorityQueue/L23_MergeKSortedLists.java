package StackAndQueue.PriorityQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
* Merge k Sorted Lists
*
* - Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
* */

public class L23_MergeKSortedLists {
    /*
     * 解法1：PriorityQueue
     * - 思路：该题的本质是排序，因此可选方案有：
     *   1. 借助 PriorityQueue 进行堆排序；
     *   2. 借助 TreeSet 进行 BST 排序；
     *   3. 直接排序
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)，（其中 n 为 lists 中所有链表的节点数之和）。
     * */
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a  ->  a.val));  // 创建最小堆
        for (ListNode l : lists)
            for (ListNode curr = l; curr != null; curr = curr.next)
                pq.offer(curr);

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {
            curr.next = pq.poll();
            curr = curr.next;
        }
        curr.next = null;  // ∵ 是将 lists 中的各个链表的节点重新拼接 ∴ 需要将拼接后的链表的最后一个节点的 next 置空，否则可能成环

        return dummyHead.next;
    }

    /*
     * 解法2：merge sort
     * - 思路：另一种 intuitive 的排序方案就是 merge sort。因为 lists 可能有2个以上的链表，因此可采用 reduce 的思路，
     *   即将多个链表的归并分解成两两链表归并。
     * - todo: 时间复杂度怎么分析？？？时间复杂度 O(?)，空间复杂度 O(n)，（其中 n 为 lists 中所有链表的节点数之和）。
     * */
    public static ListNode mergeKLists2(ListNode[] lists) {
        if (lists.length == 0) return null;
        ListNode merged = lists[0];
        for (int i = 1; i < lists.length; i++)  // 将 lists 中的所有链表 reduce 成一个链表
            merged = merge2List(merged, lists[i]);
        return merged;
    }

    private static ListNode merge2List(ListNode l1, ListNode l2) {
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
     * 解法3：merge sort (改进版)
     * - 思路：解法2中，将多个链表 reduce 成一个的过程不是二分的，因此效率较低。该解法中对此进行改进。
     * - 时间复杂度 O(n)，空间复杂度 O(n)，（其中 n 为 lists 中所有链表的节点数之和）。
     * */
    public static ListNode mergeKLists3(ListNode[] lists) {
        int len = lists.length;
        if (len == 0) return null;

        while (len > 1) {
            for (int i = 0; i < len / 2; i++) {
                lists[i] = merge2List(lists[i], lists[len - 1 - i]);
                lists[len - 1 - i] = null;
            }
            len = (len + 1) / 2;
        }
        return lists[0];
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 5});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        ListNode l3 = createLinkedListFromArray(new int[]{2, 6});
        ListNode res = mergeKLists3(new ListNode[]{l1, l2, l3});
        printLinkedList(res);  // expects 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6

        ListNode l4 = createLinkedListFromArray(new int[]{-2, -1, -1, -1});
        ListNode l5 = createLinkedListFromArray(new int[]{});
        ListNode res2 = mergeKLists3(new ListNode[]{l4, l5});
        printLinkedList(res2);  // expects -2 -> -1 -> -1 -> -1
    }
}
