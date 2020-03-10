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
