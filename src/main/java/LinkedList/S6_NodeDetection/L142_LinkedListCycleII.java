package LinkedList.S6_NodeDetection;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Linked List Cycle II
 *
 * - Given a linked list, return the node where the cycle begins. If there is no cycle, return null.
 *
 * - Note: To represent a cycle in the given linked list, we use an integer pos which represents the
 *   position (0-indexed) in the linked list where tail connects to. If pos is -1, then there is no cycle
 *   in the linked list.
 *
 * - 💎 Leetcode 关于环形链表相关题目的总结：
 *   1. 如何判断两个单链表是否有交点，如何找到第一个相交的节点（L160_IntersectionOfTwoLinkedLists）
 *   2. 判断链表是否有环（L141_LinkedListCycle）
 *   3. 找到入环点（L142_LinkedListCycleII）
 *   4. 环长度是多少：
 *      - 方法1：在第一次相遇后，slow 和 fast 的距离等于环长 ∴ 只要继续让这两个指针以各自的速度去走，下一次相遇时走过的节点数即为环长；
 *      - 方法2：在方法1的基础上改进一下，第一次相遇后，让fast停着不走了，slow继续走，记录到下次相遇时走过了几个节点，即为环的长度；
 *   5. 如何将有环的链表变成单链表（解除环）：
 *      - 找到入环点后，将入环点与环上最后一个节点之间的链接断开即可。
 * */

public class L142_LinkedListCycleII {
    /*
     * 解法1：Faster and slower runner
     * - 思路：先使用 fast/slow 方法判断链表是否有环，若有环则（fast == slow）。若此时让第三个指针 slow2 再从头结点出发，
     *   slow 与 slow2 都一步一步移动，则他们相遇的地方一定就是入环点。数学证明 SEE:
     *   https://leetcode.com/problems/linked-list-cycle-ii/discuss/258948/%2B-python
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                ListNode slow2 = head;
                while (slow != slow2) {
                    slow = slow.next;
                    slow2 = slow2.next;
                }
                return slow;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        l1.get(3).next = l1.get(1);
        log(detectCycle(l1).val);  // expects 2
        /*
         *   1 → 2 → 3 → 4
         *       ↑       |
         *       +-------+
         * */

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        l2.get(4).next = l2.get(3);
        log(detectCycle(l2).val);  // expects 4
        /*
         *   1 → 2 → 3 → 4 → 5
         *               ↑   |
         *               +---+
         * */

        ListNode l3 = createLinkedList(new int[]{1, 2});
        l3.get(1).next = l3.get(0);
        log(detectCycle(l3).val);  // expects 1
        /*
         *   1 → 2
         *   ↑   |
         *   +---+
         * */

        ListNode l4 = createLinkedList(new int[]{1, 2, 3});
        log(detectCycle(l4));  // expects null
        /*
         *   1 → 2 → 3
         * */

        ListNode l5 = createLinkedList(new int[]{1});
        log(detectCycle(l5));  // expects null
        /*
         *   1
         * */
    }
}
