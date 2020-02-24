package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Duplicates from Sorted List
 *
 * - 为有序链表去重。
 * */

public class L83_RemoveDuplicatesFromSortedList {
    /*
     * 解法1：迭代
     * - 思路：原地跳过重复节点
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val)
                curr.next = curr.next.next;  // 跳过 curr.next 节点，将 curr 和 curr.next.next 相连
            else
                curr = curr.next;
        }
        return head;
    }

    /*
     * 解法2：递归
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) return head;
        head.next = deleteDuplicates2(head.next);
        return head.val == head.next.val ? head.next : head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 1, 2});
        printLinkedList(deleteDuplicates2(l1));  // expects 1->2->NULL

        ListNode l2 = createLinkedList(new int[]{0, 1, 1, 1, 2, 3, 3});
        printLinkedList(deleteDuplicates2(l2));  // expects 0->1->2->3->NULL
    }
}
