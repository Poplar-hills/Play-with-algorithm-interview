package LinkedList;

import static Utils.Helpers.*;

/*
* Reverse Linked List II
*
* - Reverse a linked list from position m to n. Do it in one-pass. Note 1 ≤ m ≤ n ≤ length of list.
* */

public class L92_ReverseLinkedListII {
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        return head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        head.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        printLinkedList(head);  // 1->2->3->4->5->NULL

        printLinkedList(reverseBetween(head, 2, 4));  // 1->4->3->2->5->NULL

    }
}
