package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Reverse Nodes in k-Group
*
* - 给定一个链表，每 k 个节点为一组，反转每一组节点，返回修改后的链表。
* */

public class L25_ReverseNodesInKGroup {
    public static ListNode reverseKGroup(ListNode head, int k) {
        return head;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup(l1, 2));  // expects 2->1->4->3->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup(l2, 3));  // expects 3->2->1->4->5->NULL
    }
}
