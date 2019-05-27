package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Reorder List
*
* - Given a singly linked list L0 -> L1 -> ... -> Ln-1 -> Ln, reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
* - You may not modify the values in the list's nodes, only nodes itself may be changed.
* */

public class L143_ReorderList {
    public static void reorderList(ListNode head) {
        return;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        reorderList(l1);
        printLinkedList(l1);  // expects 1->4->2->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        reorderList(l2);
        printLinkedList(l2);  // expects 1->5->2->4->3->NULL
    }
}
