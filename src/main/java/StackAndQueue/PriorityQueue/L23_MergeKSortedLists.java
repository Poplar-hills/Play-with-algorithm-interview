package StackAndQueue.PriorityQueue;

import java.util.Comparator;
import java.util.PriorityQueue;

import static Utils.Helpers.*;

/*
* Merge k Sorted Lists
*
* - Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
* */

public class L23_MergeKSortedLists {
    /*
    * 解法1：merge sort
    * */
    public static ListNode mergeKLists(ListNode[] lists) {

        return lists[0];
    }

    /*
     * 解法1：PriorityQueue
     * - 思路：把 lists 中的所有链表的所有节点都放到 PriorityQueue 中进行排序。
     * */
    public static ListNode mergeKLists2(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a  ->  a.val));
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

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 5});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        ListNode l3 = createLinkedListFromArray(new int[]{2, 6});
        ListNode res = mergeKLists2(new ListNode[]{l1, l2, l3});
        printLinkedList(res);  // expects 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6

        ListNode l4 = createLinkedListFromArray(new int[]{-2, -1, -1, -1});
        ListNode l5 = createLinkedListFromArray(new int[]{});
        ListNode res2 = mergeKLists2(new ListNode[]{l4, l5});
        printLinkedList(res2);  // expects -2 -> -1 -> -1 -> -1
    }
}
