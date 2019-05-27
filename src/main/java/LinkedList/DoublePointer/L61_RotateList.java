package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Rotate List
*
* - Given a linked list, rotate the list to the right by k places, where k is non-negative.
* */

public class L61_RotateList {
    public static ListNode rotateRight(ListNode head, int k) {
        
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(rotateRight(l1, 2));  // expects 4->5->1->2->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{0, 1, 2});
        printLinkedList(rotateRight(l2, 4));  // expects 2->0->1->NULL
    }
}
