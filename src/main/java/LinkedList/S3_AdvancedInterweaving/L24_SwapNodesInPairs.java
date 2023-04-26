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
     * - 例：D -> 1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *      p    c    s    t                     - 交换节点1和2
     *      D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
     *      p    s    c    t                     - ∵ 交换之后 second、curr 的位置对调 ∴ p 向后移动两步而 c 移动一步：p=c, c=c.next
     *      D -> 2 -> 1 -> 3 -> 4 -> 5 -> NULL
     *                p    c    s    t           - 交换节点3和4
     *      D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
     *                p    s    c    t
     *      D -> 2 -> 1 -> 4 -> 3 -> 5 -> NULL
     *                          p    c     s     - ∵ c.next == null ∴ 停止交换
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

            prev = curr;  // 交换完成后让 prev 向后移动两步，而 curr 移动一步
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：递归
     * - 思路：其实思路比较直接 —— 在递归去程上，每两个节点为一对进行交换，比如：
     * - 例：1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *      h                      - 1跟2交换：2.next=1; 1.next=f(3);
     *                h                  - 3跟4交换：4.next=3; 3.next=f(5);
     *                          h              - return 5->N
     *                h                  - return 4->3->5->N
     *      h                      - return 2->1->4->3->5->N
     * - 👉 技巧：在交换节点时，由于是递，所以无需提供 first 的上一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode first = swap(head);   // 交换第 i、i+1 号节点，并将交换后的 i+1 号节点（即 head.next）赋给 first
        ListNode second = first.next;
        second.next = swapPairs2(second.next);  // 下一层递归跳过第 i+1 号节点，直接 f(i+2) 处理第 i+2、i+3 号节点
        return first;
    }

    private static ListNode swap(ListNode first) {  // 交换 first 和 first.next 并返回交换后的第一个节点
        ListNode second = first.next;               // （∵ 是递归实现 ∴ 无需提供 first 的上一个节点）
        if (second == null) return first;
        ListNode third = second.next;
        second.next = first;
        first.next = third;
        return second;
    }

    /*
     * 解法3：递归（解法2的化简版，🥇最优解）
     * - 思路：与解法2一致，在去程路上交换节点。
     * - 实现：不用想太多，直接写交换逻辑即可 —— 每层递归处理2个节点，例如第一层递归交换节点1和2，将1链到2后面，而1的下一个
     *   节点则是下一层递归（对节点3的递归）结果。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs3(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode second = head.next;          // 本层递归 f(i) 处理第 i、i+1 号节点
        head.next = swapPairs4(second.next);  // 下一层递归跳过第 i+1 号节点，直接 f(i+2) 处理第 i+2、i+3 号节点
        second.next = head;                   // 反向两个节点间的链接
        return second;
    }

    /*
     * 解法4：递归 + 标记（🥈最 intuitive 解）
     * - 思路：在递归回程上 swap 节点。∵ 链表节点数可能为奇可能为偶，需要明确 swap 的时机。通过观察可知，只要将奇数节点与
     *   其后一个节点 swap 即可 ∴ 在去程时可以用 i 标记节点序号，在回程时根据 i 来判断是否 swap。
     * - 例：1 -> 2 -> 3 -> 4 -> 5 -> NULL
     *               ...        h     - return 5->N
     *                     h     - i is even ∴ return 4->5->N
     *                h     - i is odd ∴ swap, return 4->3->5->N
     *           h     - i is even ∴ return 2->4->3->5->N
     *      h     - i is odd ∴ swap, return 2->1->4->3->5->N
     * - 👉 技巧：本解法虽然不是最简洁的，但这种通过传参在去程时打标，回程时根据打标进行相应处理的技巧值得掌握。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode swapPairs4(ListNode head) {
        return helper4(head, 1);  // 也可以用 boolean 变量来标记，在所有奇数节点上置为 true，偶数节点上置为 false
    }

    private static ListNode helper4(ListNode head, int i) {
        if (head == null || head.next == null) return head;
        ListNode returned = helper4(head.next, i + 1);
        if (i % 2 == 1) {                  // 若是奇数节点则与其后节点 swap
            ListNode newHead = head.next;  // head.next 即是交换后的新 head，最后返回到上层递归
            ListNode third = head.next.next;
            head.next.next = head;
            head.next = third;
            return newHead;
        } else {                           // 若是偶数节点则无需 swap
            head.next = returned;
            return head;
        }
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
