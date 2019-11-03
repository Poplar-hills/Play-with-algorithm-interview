package LinkedList;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.HashSet;
import java.util.Set;

/*
 * Intersection of Two Linked Lists
 *
 * - Write a program to find the node at which the intersection of two singly linked lists begins.
 *
 * - Notes:
 *   1. If the two linked lists have no intersection at all, return null.
 *   2. The linked lists must retain their original structure after the function returns.
 *   3. You may assume there are no cycles anywhere in the entire linked structure.
 *   4. Your code should preferably run in O(n) time and use only O(1) memory.
 * */

public class L160_IntersectionOfTwoLinkedLists {
    /*
    * 解法1：Set
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        Set<ListNode> set = new HashSet<>();
        for (ListNode a = headA; a != null; a = a.next)
            set.add(a);

        ListNode b = headB;
        while (!set.contains(b) && b != null)
            b = b.next;

        return b;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{4, 1});
        ListNode l2 = createLinkedListFromArray(new int[]{5, 0, 1});
        ListNode l3 = createLinkedListFromArray(new int[]{8, 4, 5});
        ListNode l1Tail = l1.tail(), l2Tail = l2.tail();
        l1Tail.next = l3;
        l2Tail.next = l3;
        ListNode res1 = getIntersectionNode(l1, l2);
        log(res1.val + "; Equals to l3: " + (res1 == l3));  // expects 8, Equals to l3: true.
        /*
         *       4 → 1
         *             ↘
         *   5 → 0 → 1 → 8 → 4 → 5
         * */

        ListNode l4 = createLinkedListFromArray(new int[]{0, 9, 1});
        ListNode l5 = createLinkedListFromArray(new int[]{3});
        ListNode l6 = createLinkedListFromArray(new int[]{2, 4});
        ListNode l4Tail = l4.tail(), l5Tail = l5.tail();
        l4Tail.next = l6;
        l5Tail.next = l6;
        ListNode res2 = getIntersectionNode(l4, l5);
        log(res2.val + "; Equals to l6: " + (res2 == l6));  // expects 2, Equals to l6: true.
        /*
         *   0 → 9 → 1
         *             ↘
         *          3 → 2 → 4
         * */

        ListNode l7 = createLinkedListFromArray(new int[]{2, 6, 4});
        ListNode l8 = createLinkedListFromArray(new int[]{1, 5});
        log(getIntersectionNode(l7, l8));  // expects null.
        /*
         *   0 → 6 → 4
         *   1 → 5
         * */

        ListNode l9 = createLinkedListFromArray(new int[]{1});
        ListNode l10 = l9;
        ListNode res4 = getIntersectionNode(l9, l10);
        log(res4.val + "; Equals to l9: " + (res4 == l9));  // expects 1, Equals to l6: true.
        /*
         *   1
         * */
    }
}
