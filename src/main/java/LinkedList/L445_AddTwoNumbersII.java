package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Add Two Numbers II
*
* - 给出两个非空链表，代表两个非负整数。其中每个整数的各个位上的数字以顺序存储，返回这两个整数之和的顺序链表。
*   如 7243 + 564 = 7807，则给出 7—>2->4->3、5->6->4，返回 7->8->0->7。
* */

public class L445_AddTwoNumbersII {
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{7, 2, 4, 3});
        ListNode l2 = createLinkedListFromArray(new int[]{5, 6, 4});
        printLinkedList(addTwoNumbers(l1, l2));   // expects 7->8->0->7->NULL
    }
}
