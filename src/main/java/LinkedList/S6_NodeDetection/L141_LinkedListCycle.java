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
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
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

    /*
     * 解法2：Faster and slower runner
     * - 💎 总结：若链表有环，从头节点到入环点的距离是 D，环长是 S，则 fast、slow 的相遇点跟 D、S 都相关，不总在固定的地方相遇。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static boolean hasCycle2(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }

        return false;
    }

    /*
     * 解法3：Reverse
     * - 思路：若一个链表有环，则将其反向之后的头结点一定就是原链表的头结点：
     *        1 → 2 → 3 → 4              1   2 ← 3 ← 4
     *            ↑       |      --->        |       ↑   - 反向过程结束后 curr == null, prev 指向1节点 ∴ 返回1节点
     *            +-------+                  +-------+
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static boolean hasCycle3(ListNode head) {
        return head != null && head.next != null && head == reverse(head);
    }

    private static ListNode reverse(ListNode head) {  // 注意不能使用递归，否则会 stack overflow
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        l1.get(7).next = l1.get(2);
        log(hasCycle3(l1));  // expects true.
        /*
         *   1 → 2 → 3 → 4 → 5
         *           ↑       ↓
         *           8 ← 7 ← 6
         * */

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        l2.get(3).next = l2.get(1);
        log(hasCycle3(l2));  // expects true
        /*
         *   1 → 2 → 3 → 4
         *       ↑       |
         *       +-------+
         * */

        ListNode l3 = createLinkedList(new int[]{1, 2});
        l3.get(1).next = l3.get(0);
        log(hasCycle3(l3));  // expects true
        /*
         *   1 → 2
         *   ↑   |
         *   +---+
         * */

        ListNode l4 = createLinkedList(new int[]{1, 2});
        log(hasCycle3(l4));  // expects false
        /*
         *   1 → 2
         * */

        ListNode l5 = createLinkedList(new int[]{1});
        log(hasCycle3(l5));  // expects false
        /*
         *   1
         * */
    }
}
