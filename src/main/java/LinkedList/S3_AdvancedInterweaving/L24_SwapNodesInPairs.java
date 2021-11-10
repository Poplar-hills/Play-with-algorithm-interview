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
     *        p    c    s    t                     - 交换节点1和2
     *        D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
     *        p    s    c    t                     - ∵ 交换完之后 s、c 的位置对调了 ∴ 向后移动两步只需让 p = c；c = c.next
     *        D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
     *                  p    c    s    t           - 交换节点3和4
     *        D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
     *                  p    s    c    t
     *        D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
     *                            p    c     s     - ∵ c.next == null ∴ 停止交换
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */

    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        ListNode prev = dummyHead, curr = head;

        while (curr != null && curr.next != null) {  // 内部不断交换 curr 和 curr.next
            ListNode second = curr.next;
            ListNode third = second.next;  // 交换之前先保存 third 节点的指针
            prev.next = second;
            second.next = curr;
            curr.next = third;

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
     * - 👉 技巧：在交换节点时，由于是递，所以无需提供 first 的上一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = swap(head);   // 交换 head 和 head.next，并将交换后的 head.next 赋给 first：
        ListNode second = first.next;
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
     *       odd
     *           even
     *                 odd
     *                      even
     *                           odd
     *                          ← 5->NULL（∵ 是最后一个节点 ∴ 无需看标志位，直接返回）
     *                       ← 4->5->NULL（标志位为 false，不交换）
     *                  ← 4->3->5->NULL（标志位为 true，交换）
     *             ← 2->4->3->5->NULL（标志位为 false，不交换）
     *        ← 2->1->4->3->5->NULL（标志位为 true，交换）
     * - 👉 技巧：本解法虽然不是最简洁的，但这种通过传参在去程时打标，回程时根据打标进行相应处理的技巧值得掌握。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs3(ListNode head) {
        return helper3(head, true);
    }

    private static ListNode helper3(ListNode head, boolean shouldSwap) {  // 奇数节点 swap，偶数节点则不
        if (head == null || head.next == null) return head;
        ListNode returned = helper3(head.next, !shouldSwap);  // 在去程中通过取反 shouldSwap 来标记奇偶
        if (shouldSwap) {
            ListNode next = returned.next;
            returned.next = head;  // 交换的是当前 head 节点与下层递归返回的头结点
            head.next = next;
            return returned;
        } else {
            head.next = returned;
            return head;
        }
    }

    /*
     * 解法4：递归（解法2的化简版，🥇最优解）
     * - 思路：与解法2一致，在去程时交换节点。
     * - 实现：不用想太多，直接写交换逻辑即可 —— 每层递归处理2个节点，例如第一层递归交换节点1和2，将1链到2后面，而1的下一个节点
     *   则是下一层递归（对节点3的递归）结果：
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
        ListNode second = head.next;          // 本层递归处理第1、2个节点
        head.next = swapPairs4(second.next);  // 下一层递归处理第3、4个节点
        second.next = head;                   // 反向两个节点间的链接
        return second;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        log(swapPairs2(l1));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(swapPairs2(l2));  // expects 2->1->4->3->5->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        log(swapPairs2(l3));  // expects 1->NULL
    }
}
