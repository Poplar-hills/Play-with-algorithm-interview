package LinkedList;

import static Utils.Helpers.*;

/*
* Reverse Linked List
* */

public class L206_ReverseLinkedList {
    /*
    * 解法1：遍历过程中不断将两个节点间的链接反向
    * - 时间复杂度 O(n)，空间复杂度 O(1)
    * */
    public static ListNode reverseList(ListNode head) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
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
        ListNode newHead = reverseList2(head.next);  // 这一句是递归的关键，要求 head 的 reverse，先求 head.next 的 reverse
        head.next.next = head;  // 把 head 节点放在了尾部
        head.next = null;       // 将尾部 head 节点前面的节点置为 null（这样完成递归后第一个节点就是 null 了，其他节点值会被上一句覆盖）
        return newHead;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(l);                 // 1->2->3->4->5->NULL
        printLinkedList(reverseList2(l));   // expects 5->4->3->2->1->NULL
    }
}
