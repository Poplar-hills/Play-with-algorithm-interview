package LinkedList.S1_BasicInterweaving;

import java.util.Stack;

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
        return prev;  // 注意最后返回的是 prev 而非 curr
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
        if (head.next == null) return head;
        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /*
     * 解法3：Stack
     * - 思路：说起"反向"就应该能联想到 Stack 这种数据结构。BST 的前序、中序遍历就是同样的思路。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseList3(ListNode head) {
        Stack<ListNode> stack = new Stack<>();

        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        ListNode dummyHead = new ListNode(), prev = dummyHead;
        while (!stack.isEmpty()) {
            prev.next = stack.pop();
            prev = prev.next;
        }
        prev.next = null;  // 注意要把最后一个节点的 next 置空（否则会与前一个节点形成双向链接）

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(reverseList3(l));    // expects 5->4->3->2->1->NULL
    }
}
