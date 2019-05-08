package LinkedList;

/*
* Reverse Linked List
*
* -
* */

import static Utils.Helpers.log;

public class L206_ReverseLinkedList {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    /*
    * 解法1：遍历
    * - 时间复杂度 O(n)，空间复杂度 O(1)
    * */
    public static ListNode reverseList(ListNode head) {
        if (head == null) return null;
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    /*
     * 解法2：递归
     * - 时间复杂度 O(n)，空间复杂度 O(n)
     * */
    public static ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode rhead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return rhead;
    }

    // 辅助工具方法
    public static void printLinkedList(ListNode head) {
        StringBuilder s = new StringBuilder();
        while (head != null) {
            s.append(head.val);
            s.append("->");
            head = head.next;
        }
        s.append("NULL");
        log(s.toString());
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        head.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        printLinkedList(head);  // 1->2->3->4->5->NULL

        printLinkedList(reverseList2(head));   // expects 5->4->3->2->1->NULL
    }
}
