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
     * 错误解（Memory Limit Exceeded）：PriorityQueue
     * */
    public static ListNode mergeKLists2(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
        for (ListNode l : lists)
            for (ListNode curr = l; curr != null; curr = curr.next)
                pq.offer(curr);

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {
            curr.next = pq.poll();
            curr = curr.next;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 5});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        ListNode l3 = createLinkedListFromArray(new int[]{2, 6});
        ListNode[] lists = new ListNode[]{l1, l2, l3};  // todo: 不需要 cast？？？
        ListNode res = mergeKLists2(lists);
        printLinkedList(res);  // expects 1->1->2->3->4->4->5->6
    }
}
