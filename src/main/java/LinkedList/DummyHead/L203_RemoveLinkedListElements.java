package LinkedList.DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Linked List Elements
*
* - Remove all elements from a linked list of integers that have value val.
* */

public class L203_RemoveLinkedListElements {
    /*
    * 解法1：创建新链表
    * */
    public static ListNode removeElements(ListNode head, int val) {
        ListNode dummyHead = new ListNode();

        for (ListNode curr = head, curr2 = dummyHead; curr != null; curr = curr.next) {
            if (curr.val == val) continue;
            curr2.next = new ListNode(curr.val);
            curr2 = curr2.next;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedListFromArray(new int[]{1, 2, 6, 3, 4, 5, 6});
        printLinkedList(removeElements(l, 6));
    }
}
