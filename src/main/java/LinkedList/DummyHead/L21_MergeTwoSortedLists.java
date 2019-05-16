package LinkedList.DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Merge Two Sorted Lists
*
* - merge 两个有序链表（返回一个新链表）。
* */

public class L21_MergeTwoSortedLists {
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        return l1;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 4});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        printLinkedList(mergeTwoLists(l1, l2));  // expects 1->1->2->3->4->4->NULL
    }
}
