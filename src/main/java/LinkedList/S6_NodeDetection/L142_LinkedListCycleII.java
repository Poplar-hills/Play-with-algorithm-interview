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
 * - 关于leetcode环形链表相关题目的总结：
 *   1. 判断链表是否有环（L141_LinkedListCycle）
 *   2. 找到入环点（L142_LinkedListCycleII）
 *   3. 环长度是多少
 *      - 方法1：在第一次相遇后，slow 和 fast 的距离等于环长 ∴ 只要继续让这两个指针以各自的速度去走，下一次相遇时走过的节点数即为环长；
 *      - 方法2：在方法1的基础上改进一下，第一次相遇后，让fast停着不走了，slow继续走，记录到下次相遇时走过了几个节点，即为环的长度；
 *   4. 如何将有环的链表变成单链表（解除环）
 *      - 找到入环点后，将入环点与环上最后一个节点之间的链接断开即可。
 *   5. 如何判断两个单链表是否有交点，如何找到第一个相交的节点（L160_IntersectionOfTwoLinkedLists）
 * */

public class L142_LinkedListCycleII {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static ListNode detectCycle(ListNode head) {
        return null;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{3, 2, 0, -4});
        l1.get(3).next = l1.get(1);
        log(detectCycle(l1).val);  // expects 2
        /*
         *   3 → 2 → 0 → -4
         *       ↑        |
         *       +--------+
         * */

        ListNode l2 = createLinkedList(new int[]{1, 2});
        l2.get(1).next = l2.get(0);
        log(detectCycle(l2).val);  // expects 1
        /*
         *   1 → 2
         *   ↑   |
         *   +---+
         * */

        ListNode l3 = createLinkedList(new int[]{1});
        log(detectCycle(l3));  // expects null
        /*
         *   1
         * */
    }
}
