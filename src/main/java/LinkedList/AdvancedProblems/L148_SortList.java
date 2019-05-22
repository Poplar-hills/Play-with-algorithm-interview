package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Sort List
*
* - Sort a linked list in O(nlogn) time using constant space complexity.
* - 思路：因为 Quick sort 非常依赖于数组的随机访问能力，因此不适用于链表，而 Merge Sort 适用于链表，因此此题应采用 Merge Sort 思路。
* */

public class L148_SortList {
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode slow = head, fast = head, prevOfSlow = null;
        while (fast != null && fast.next != null) {
            prevOfSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prevOfSlow.next = null;

        return merge(sortList(head), sortList(slow));
    }

    private static ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), prev = dummyHead;
        ListNode n1 = l1, n2 = l2;

        while (n1 != null || n2 != null) {
            if (n1 == null) {
                prev.next = n2;
                n2 = n2.next;
            } else if (n2 == null) {
                prev.next = n1;
                n1 = n1.next;
            } else if (n1.val < n2.val) {
                prev.next = n1;
                n1 = n1.next;
            } else {
                prev.next = n2;
                n2 = n2.next;
            }
            prev = prev.next;
        }
        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l0 = createLinkedListFromArray(new int[]{4, 1, 6, 5, 2, 8, 3, 7});
        printLinkedList(sortList(l0));  // expects 1->2->3->4->5->6->7->8->NULL

        ListNode l1 = createLinkedListFromArray(new int[]{4, 2, 1, 3});
        printLinkedList(sortList(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(sortList(l2));  // expects -1->0->3->4->5->NULL
    }
}
