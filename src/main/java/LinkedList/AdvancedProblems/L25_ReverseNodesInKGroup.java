package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Reverse Nodes in k-Group
*
* - 给定一个链表，每 k 个节点为一组，反转每一组节点，返回修改后的链表。
* */

public class L25_ReverseNodesInKGroup {
    /*
    * 解法1：不断局部反转节点
    * - 思路：题中要求反转按组进行，因此需要一个方法能够反转给定链表中的 k 个节点，并返回反转后的链表头结点以链回原链表中。
    *        当反转完一组之后，原链表中的索引向右移动到下一组的起点，继续反转。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = prev.next;
        int count = 0;

        while (curr != null) {
            prev.next = reverseNodes(curr, k);
            while (curr != null && count < k) {  // 移动到下一组待反转节点的起点上
                prev = prev.next;
                curr = curr.next;
            }
        }

        return dummyHead.next;
    }

    private static ListNode reverseNodes(ListNode head, int n) {
        if (head == null || head.next == null || n == 0) return head;
        ListNode prev = head, curr = head.next;

        while (curr != null && n > 1) {
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
            n--;
        }

        head.next = curr;
        return prev;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup(l1, 2));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup(l2, 3));  // expects 3->2->1->4->NULL
    }
}
