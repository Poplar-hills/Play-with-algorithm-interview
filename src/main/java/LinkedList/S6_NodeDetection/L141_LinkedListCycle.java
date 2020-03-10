package LinkedList.S6_NodeDetection;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Linked List Cycle
 *
 * - Given a linked list, determine if it has a cycle in it.
 *
 * - Note: To represent a cycle in the given linked list, we use an integer pos which represents the
 *   position (0-indexed) in the linked list where tail connects to. If pos is -1, then there is no cycle
 *   in the linked list.
 * */

public class L141_LinkedListCycle {
    /*
     * 解法1：
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean hasCycle(ListNode head) {
        return false;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{3, 2, 0, -4});
        l1.get(3).next = l1.get(1);
        log(hasCycle(l1));  // expects true
        /*
         *   3 → 2 → 0 → -4
         *       ↑        |
         *       +--------+
         * */

        ListNode l2 = createLinkedList(new int[]{1, 2});
        l2.get(1).next = l2.get(0);
        log(hasCycle(l2));  // expects true
        /*
         *   1 → 2
         *   ↑   |
         *   +---+
         * */

        ListNode l3 = createLinkedList(new int[]{1});
        log(hasCycle(l3));  // expects false
        /*
         *   1
         * */
    }
}
