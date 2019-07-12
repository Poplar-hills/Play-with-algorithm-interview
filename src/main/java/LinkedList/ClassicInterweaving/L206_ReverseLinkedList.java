package LinkedList.ClassicInterweaving;

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
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
        }
        return prev;
    }

    /*
     * 解法2：递归
     * - 思路：当使用递归反向链表时，我们期望的过程是：
     *        1 -> 2 -> 3 -> 4
     *                     ← 4
     *                ← 4->3
     *           ← 4->3->2
     *      ← 4->3->2->1
     *   要实现这个过程：
     *   1. 递归终止条件：head == null || head.next == null（这两种情况只需返回 head 即可，不需要改变节点顺序）
     *   2. 递归单元逻辑：例如当 reserveList(head.next) 返回 4->3 时，此时链表为：1->2  4->3。此时需要让2链到3后面，
     *      得到 1  4->3->2，再将4返回给上一层。这就需要：                          |_____↑
     *           |________↑
     *      a. 让 3.next 指向 2 —— ∵ 此时 head 是2 ∴ head.next.next = head，此时链表为：1  4->3<->2
     *      b. 再移除 2->3 的链接（仅保留 3->2）—— head.next = null                    |_________↑
     * - 时间复杂度 O(n)，空间复杂度 O(n)
     * */
    public static ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(reverseList2(l));    // expects 5->4->3->2->1->NULL
    }
}
