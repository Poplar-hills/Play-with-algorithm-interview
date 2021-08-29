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
     * - 思路：其实思路比较直接 —— 在递归去程上，每两个节点为一对进行交换，比如：
     *        1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *          → 1跟2交换：2.next = 1; 1.next = f(3);
     *                    → 3跟4交换：4.next = 3; 3.next = f(5);
     *                          ← f(5) = 5->N
     *                ← f(3) = 4->3->5->N
     *      ← f(1) = 2->1->4->3->5->N
     * -  技巧：在交换节点时，由于是递，所以无需提供 first 的上一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = swap(head);    // 交换 head 和 head.next，并将交换后的 head.next 赋给 first：
        ListNode second = first.next;
        if (second != null)
            second.next = swapPairs2(second.next);
        return first;
    }

    private static ListNode swap(ListNode first) {  // 交换 first 和 first.next 并返回交换后的第一个节点
        ListNode second = first.next;               // （∵ 是递归实现 ∴ 无需提供 first 的上一个节点）
        if (second == null) return first;
        ListNode temp = second.next;
        second.next = first;
        first.next = temp;
        return second;
    }

    /*
     * 解法3：递归 + 标记奇偶
     * - 思路：使用递归，在回程上 swap 节点。但若链表为奇数个节点，则最后一个节点不能与倒数第二个 swap ∴ 需要在去程时标记节点
     *   的奇偶，在奇数节点上进行 swap：
     *        1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *        o
     *             e
     *                  o
     *                       e
     *                            o
     *                          ← 5->NULL
     *                       ← 4->5->NULL
     *                  ← 4->3->5->NULL
     *             ← 2->4->3->5->NULL
     *        ← 2->1->4->3->5->NULL
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs3(ListNode head) {
        return helper3(head, true);
    }

    private static ListNode helper3(ListNode head, boolean shouldSwap) {
        if (head == null || head.next == null) return head;
        ListNode returned = helper3(head.next, !shouldSwap);
        if (shouldSwap) {
            ListNode next = returned.next;
            returned.next = head;
            head.next = next;
            return returned;
        } else {
            head.next = returned;
            return head;
        }
    }

    /*
     * 解法4：递归（最简单、直接的版本）
     * - 实现：不用想太多，直接写交换逻辑即可 —— 交换节点1和2，则1要链到2后面，1的下一个是3的递归结果：
     *        1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *          → 1跟2交换：2.next = 1; 1.next = f(3);
     *                    → 3跟4交换：4.next = 3; 3.next = f(5);
     *                          ← f(5) = 5->N
     *                ← f(3) = 4->3->5->N
     *      ← f(1) = 2->1->4->3->5->N
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs4(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode second = head.next;
        head.next = swapPairs3(second.next);
        second.next = head;
        return second;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(swapPairs(l1));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(swapPairs(l2));  // expects 2->1->4->3->5->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        printLinkedList(swapPairs(l3));  // expects 1->NULL
    }
}
