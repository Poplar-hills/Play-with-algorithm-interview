package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Swap Nodes in Pairs
 *
 * - Given a linked list, swap every two adjacent nodes and return its head.（注意不能交换值）。
 * */

public class L24_SwapNodesInPairs {
    /*
     * 解法1：遍历
     * - 思路：交换两个节点实际上需要4个节点的参与：两个节点 + 这两个节点之前、之后的节点，这样交换完之后才能再将后续链表链接回去。
     * - 演示：D -> 1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *        p    c    n    t                     - 交换节点1和2
     *        D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
     *                  p    c    n    t           - 交换节点3和4
     *        D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
     *                            p    c     n     - ∵ c.next == null ∴ 停止交换
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead, curr = head;

        while (curr != null && curr.next != null) {  // 内部不断交换 curr 和 curr.next
            ListNode next = curr.next;
            ListNode temp = next.next;
            prev.next = next;
            next.next = curr;
            curr.next = temp;

            prev = curr;       // 交换完成后让 prev、curr 都向后移动两步
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：递归
     * - 思路：1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *                          ← 5->N
     *                ← 4->3->5->N
     *      ← 2->1->4->3->5->N
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = swapNodes2(head), second = first.next;
        if (second != null)
            second.next = swapPairs(second.next);
        return first;
    }

    private static ListNode swapNodes2(ListNode first) {  // 交换 first 和 second 并返回交换后的第一个节点（∵ 是用于递归 ∴ 不需要提供 first 的上一个节点）
        ListNode second = first.next;
        if (second == null) return first;
        ListNode temp = second.next;
        second.next = first;
        first.next = temp;
        return second;
    }

    /*
     * 解法3：解法2的精简版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs3(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode second = head.next;
        head.next = swapPairs3(second.next);
        second.next = head;
        return second;
    }



    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(swapPairs0(l1));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(swapPairs0(l2));  // expects 2->1->4->3->5->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        printLinkedList(swapPairs0(l3));  // expects 1->NULL
    }
}
