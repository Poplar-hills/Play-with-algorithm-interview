package LinkedList.S6_NodeDetection;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.HashSet;
import java.util.Set;

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
     * 解法1：Hashtable
     * - 思路：
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static boolean hasCycle(ListNode head) {
        Set<ListNode> set = new HashSet<>();
        ListNode curr = head;
        while (curr != null) {
            if (set.contains(curr)) return true;
            set.add(curr);
            curr = curr.next;
        }
        return false;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        l1.get(3).next = l1.get(2);
        log(hasCycle(l1));  // expects true
        /*
         *   1 → 2 → 3 → 4
         *       ↑       |
         *       +-------+
         * */

        ListNode l2 = createLinkedList(new int[]{1, 2});
        l2.get(1).next = l2.get(0);
        log(hasCycle(l2));  // expects true
        /*
         *   1 → 2
         *   ↑   |
         *   +---+
         * */

        ListNode l3 = createLinkedList(new int[]{1, 2});
        log(hasCycle(l3));  // expects false
        /*
         *   1 → 2
         * */

        ListNode l4 = createLinkedList(new int[]{1});
        log(hasCycle(l4));  // expects false
        /*
         *   1
         * */
    }
}
