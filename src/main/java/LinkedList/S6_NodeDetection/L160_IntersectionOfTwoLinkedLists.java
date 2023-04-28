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
     * 解法1：Hash table (Set)
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
     * 解法2：双指针 + 循环遍历
     * - 💎 思路：若两个链表 l1、l2 交叉，则当指针 a 遍历完 l1->l2、指针 b 遍历完 l2->l1 时，a、b 一定会同时抵达交叉点。
     *   例如在 test case 2 中： l1: 0 → 9 → 1
     *                                       ↘
     *                                 l2: 3 → 2 → 4
     *   l1->l2 = [0 → 9 → 1 → 2 → 4] → [3 → 2 → 4]；而 l2->l1 = [3 → 2 → 4] → [0 → 9 → 1 → 2 → 4]；
     *   用指针 a, b 同时遍历这两个链表：
     *     1. 若两个链表有交叉，则第一个使 a == b 的节点就是交叉点2；
     *     2. 若两个链表不交叉，则第一个使 a == b 的节点就是链表两个链表尾部的 null；
     * - 时间复杂度 O(m+n)，空间复杂度 O(1)，相比解法1，该解法最小化了空间开销。
     * */
    public static ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode a = headA, b = headB;

        while (a != b) {
            a = (a == null ? headB : a.next);  // a 先遍历 headA 再继续遍历 headB
            b = (b == null ? headA : b.next);  // b 先遍历 headB 再继续遍历 headA
        }

        return a;  // 循环结束时 a == b，返回其中任意即可
    }

    /*
     * 解法3：双指针 + 找齐前进
     * - 思路：另一种思路是，若两个链表相交，则交点之后的长度是两个链表共有的，只有交点之前的长度可能不同 ∴ 若在节点之前将链表的
     *   长度只差消除，则通过双指针前进的方式一定能同时走到交点上。
     * - 时间复杂度 O(max(m+n))，空间复杂度 O(1)。
     * */
    public static ListNode getIntersectionNode3(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        int lenA = getLength(headA), lenB = getLength(headB);

        while (lenA > lenB) {     // 当 lenA != lenB 时先让较长的那个链表前进
            headA = headA.next;
            lenA--;
        }

        while (lenB > lenA) {
            headB = headB.next;
            lenB--;
        }

        while (headA != headB) {  // 当 lenA == lenB 时开始遍历寻找相交节点，遍历的停止条件就是找到交点
            headA = headA.next;
            headB = headB.next;
        }

        return headA;
    }

    private static int getLength(ListNode head) {
        int len = 0;
        for ( ; head != null; head = head.next) len++;
        return len;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{4, 1});
        ListNode l2 = createLinkedList(new int[]{5, 0, 1});
        ListNode l3 = createLinkedList(new int[]{8, 4, 5});
        l1.tail().next = l2.tail().next = l3;
        ListNode node1 = getIntersectionNode3(l1, l2);
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
        ListNode node2 = getIntersectionNode3(l4, l5);
        log("Intersection node val: " + node2.val);  // expects 2
        /*
         *   0 → 9 → 1
         *             ↘
         *          3 → 2 → 4
         * */

        ListNode l7 = createLinkedList(new int[]{1});
        ListNode l8 = l7;
        ListNode node4 = getIntersectionNode3(l7, l8);
        log("Intersection node val: " + node4.val);  // expects 1
        /*
         *   1
         * */

        ListNode l9 = createLinkedList(new int[]{2, 6, 4});
        ListNode l10 = createLinkedList(new int[]{1, 5});
        log(getIntersectionNode(l9, l10));  // expects null.
        /*
         *   0 → 6 → 4
         *   1 → 5
         * */
    }
}
