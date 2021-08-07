package LinkedList.S1_BasicInterweaving;

import java.util.Stack;

import static Utils.Helpers.*;

/*
 * Reverse Linked List
 * */

public class L206_ReverseLinkedList {
    /*
     * 解法1：遍历
     * - 思路：在遍历过程中不断将当前节点插入到 dummyHead 之后。
     * - 时间复杂度 O(n)，空间复杂度 O(1)
     * */
    private static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = head, curr = head.next;

        while (curr != null) {
            ListNode next = curr.next;
            ListNode tail = dummyHead.next;
            dummyHead.next = curr;
            curr.next = tail;
            prev.next = next;
            curr = next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：遍历
     * - 思路：在遍历过程中不断反向两个节点间的链接。
     * - 时间复杂度 O(n)，空间复杂度 O(1)
     * */
    public static ListNode reverseList2(ListNode head) {
        if (head == null) return null;
        ListNode prev = null, curr = head;

        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;  // 注意最后返回的是 prev 而非 curr（∵ 此时 curr 抵达 null 而 prev 抵达尾节点）
    }

    /*
     * 解法3：递归
     * - 思路：当使用递归反向链表时，我们期望的过程是：
     *           0 -> 1 -> 2 -> 3 -> 4
     *                             ← 4
     *                        ← 4->3
     *                   ← 4->3->2
     *              ← 4->3->2->1
     *         ← 4->3->2->1->0
     *   ∴ 总体逻辑应该是先递归到底，在返回的路上反向节点。
     * - 实现：该思路在实现是的难点在于，下层递归返回的节点和要链接当前节点的节点不是一个，比如👆的例子中下层递归返回了 4->3->2，
     *   当前节点 head=1，而要链接当前节点的节点是2（即要将1链接到2后面）∴ 如何能在不遍历的情况下快速获得节点2是个问题。对于
     *   该问题画图理解：当下层递归返回 4->3->2 时，链表的完整形态是：0->1  4->3->2 。此时若要把1链接到2后面就需要：
     *                                                          |________↑
     *     1. 添加 2->1 的链接：这就需要先获取的节点2。∵ 有两个方向都可到达节点2，若从4开始则需遍历，而从1开始则只需 1.next；
     *        即 head.next。∴ 添加 2->1 的链接就是 head.next.next = head。
     *     2. 断开 1->2 的链接：head.next = null;
     *   ∴ 最后得到的链表形态为：0  4->3->2->1，至此本轮递归结束，可以将节点4再返回给再上一层的递归。
     *                       |___________↑
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseList3(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /*
     * 解法4：利用数据结构 Stack
     * - 💎 思路：说起"反向"就应该能联想到 Stack 这种数据结构。
     * - 注：BST 的前序、中序遍历就是同样的思路。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseList4(ListNode head) {
        Stack<ListNode> stack = new Stack<>();
        while (head != null) {
            stack.push(head);
            head = head.next;
        }

        ListNode dummyHead = new ListNode(), prev = dummyHead;
        while (!stack.isEmpty()) {
            prev.next = stack.pop();
            prev = prev.next;
        }
        prev.next = null;  // 注意要把最后一个节点的 next 置空（否则会与前一个节点形成双向链接）

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l = createLinkedList(new int[]{0, 1, 2, 3, 4, 5});
        printLinkedList(reverseList(l));   // expects 5->4->3->2->1->0->NULL
    }
}
