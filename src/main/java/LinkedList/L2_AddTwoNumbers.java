package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Add Two Numbers
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以逆序存储，返回这两个整数之和的逆序链表。
*   如 342 + 465 = 807，则给出 2->4->3、5->6->4，返回 7->0->8。
* */

public class L2_AddTwoNumbers {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        
    }

    public static void main(String[] args) {
        ListNode n1 = createLinkedListFromArray(new int[]{2, 4, 3});
        ListNode n2 = createLinkedListFromArray(new int[]{5, 6, 4});
        printLinkedList(addTwoNumbers(n1, n2));  // expects 7->0->8
    }
}

