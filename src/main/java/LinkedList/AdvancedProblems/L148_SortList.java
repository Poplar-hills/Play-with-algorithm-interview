package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Sort List
*
* - Sort a linked list in O(nlogn) time using constant space complexity.
* */

public class L148_SortList {
    public static ListNode sortList(ListNode head) {

        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{4, 2, 1, 3});
        printLinkedList(sortList(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(sortList(l2));  // expects -1->0->3->4->5->NULL
    }
}
