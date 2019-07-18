package LinkedList.ClassicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Duplicates from Sorted List
*
* - 为有序链表去重。
* */

public class L83_RemoveDuplicatesFromSortedList {
    /*
     * 解法1：
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                curr.next = curr.next.next;  // 跳过 curr.next 节点，将 curr 和 curr.next.next 相连
            } else {
                curr = curr.next;
            }
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 1, 2});
        printLinkedList(deleteDuplicates(l1));  // expects 1->2->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{0, 1, 1, 1, 2, 3, 3});
        printLinkedList(deleteDuplicates(l2));  // expects 0->1->2->3->NULL
    }
}
