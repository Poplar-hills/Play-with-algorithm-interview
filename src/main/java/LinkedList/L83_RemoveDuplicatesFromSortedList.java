package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Duplicates from Sorted List
*
* - 为有序链表去重。
* - 时间复杂度 O(n)，空间复杂度 O(1)。
* */

public class L83_RemoveDuplicatesFromSortedList {
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                curr.next = curr.next.next;
            } else {
                curr = curr.next;
            }
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 1, 2});
        printLinkedList(l1);
        printLinkedList(deleteDuplicates(l1));  // expects 1->2

        ListNode l2 = createLinkedListFromArray(new int[]{1, 1, 2, 3, 3});
        printLinkedList(l2);
        printLinkedList(deleteDuplicates(l2));  // expects 1->2->3
    }
}
