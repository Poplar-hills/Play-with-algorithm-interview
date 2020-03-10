package LinkedList.S6_NodeDetection;

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
     * 解法1：HashTable (Set)
     * - 思路：利用 Set 比较引用地址的特性来查找链表 a 中是否有节点存在于链表 b 中。
     * - 时间复杂度 O(m+n)，空间复杂度 O(m) or O(n)。
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

    /*
     * 解法2：双指针循环遍历
     * - 思路：若两个链表 a、b 交叉，则当指针 p1 遍历完 a->b、指针 p2 遍历完 b->a 时，p1、p2 一定会同时抵达交叉点。
     *   例如在 test case 2 中，a->b = [0 → 9 → 1 → 2 → 4] → [3 → 2 → 4]；
     *                      而 b->a = [3 → 2 → 4] → [0 → 9 → 1 → 2 → 4]；
     *   用指针 p1、p2 同时遍历这两个链表：
     *     1. 若两个链表有交叉，则第一个使 p1 == p2 的节点就是交叉点2；
     *     2. 若两个链表不交叉，则第一个使 p1 == p2 的节点就是链表两个链表尾部的 null；
     * - 时间复杂度 O(m+n)，空间复杂度 O(1)，相比解法1，该解法最小化了空间开销。
     * */
    public static ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode p1 = headA, p2 = headB;

        while (p1 != p2) {
            p1 = (p1 == null ? headB : p1.next);  // p1 先遍历 headA 再继续遍历 headB
            p2 = (p2 == null ? headA : p2.next);  // p2 先遍历 headB 再继续遍历 headA
        }

        return p1;  // 循环结束时 p1 == p2，返回其中任意即可
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{4, 1});
        ListNode l2 = createLinkedList(new int[]{5, 0, 1});
        ListNode l3 = createLinkedList(new int[]{8, 4, 5});
        l1.tail().next = l2.tail().next = l3;
        ListNode node1 = getIntersectionNode(l1, l2);
        log("Intersection node val: " + node1.val);  // expects 8
        /*
         *       4 → 1
         *             ↘
         *   5 → 0 → 1 → 8 → 4 → 5
         * */

        ListNode l4 = createLinkedList(new int[]{0, 9, 1});
        ListNode l5 = createLinkedList(new int[]{3});
        ListNode l6 = createLinkedList(new int[]{2, 4});
        l4.tail().next = l5.tail().next = l6;
        ListNode node2 = getIntersectionNode(l4, l5);
        log("Intersection node val: " + node2.val);  // expects 2
        /*
         *   0 → 9 → 1
         *             ↘
         *          3 → 2 → 4
         * */

        ListNode l7 = createLinkedList(new int[]{2, 6, 4});
        ListNode l8 = createLinkedList(new int[]{1, 5});
        log(getIntersectionNode(l7, l8));  // expects null.
        /*
         *   0 → 6 → 4
         *   1 → 5
         * */

        ListNode l9 = createLinkedList(new int[]{1});
        ListNode l10 = l9;
        ListNode node4 = getIntersectionNode(l9, l10);
        log("Intersection node val: " + node4.val);  // expects 1
        /*
         *   1
         * */
    }
}
