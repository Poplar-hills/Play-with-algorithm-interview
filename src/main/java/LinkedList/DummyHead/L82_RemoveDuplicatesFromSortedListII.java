package LinkedList.DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Duplicates from Sorted List II
*
* - 删除一个有序链表中所有的重复节点。
* */

public class L82_RemoveDuplicatesFromSortedListII {
    public static ListNode deleteDuplicates(ListNode head) {

    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 3, 4, 4, 5});
        printLinkedList(deleteDuplicates(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 1, 1, 2, 3});
        printLinkedList(deleteDuplicates(l2));  // expects 2->3->NULL
    }
}
