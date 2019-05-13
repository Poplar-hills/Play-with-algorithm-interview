package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Odd Even Linked List
*
* -
* */

public class L328_OddEvenLinkedList {
    public static ListNode oddEvenList(ListNode head) {
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(oddEvenList(l1));  // expects 1->3->5->2->4->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{2, 1, 3, 5, 6, 4, 7});
        printLinkedList(oddEvenList(l2));  // expects 2->3->6->7->1->5->4->NULL
    }
}
