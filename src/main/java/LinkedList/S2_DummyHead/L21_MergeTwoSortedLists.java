package LinkedList.S2_DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Merge Two Sorted Lists
 *
 * - merge 两个有序链表 l1, l2，返回一个新的有序链表，其中节点应该从 l1, l2 中截取而来，而非创建新的。
 * */

public class L21_MergeTwoSortedLists {
    /*
     * 解法1：双指针模拟归并排序过程
     * - 时间复杂度 O(m+n)，空间复杂度 O(1)。
     * */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), curr = dummyHead;

        while (l1 != null || l2 != null) {
            if (l1 == null) {
                curr.next = l2;
                l2 = l2.next;
            } else if (l2 == null) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                if (l1.val < l2.val) {
                    curr.next = l1;
                    l1 = l1.next;
                } else {
                    curr.next = l2;
                    l2 = l2.next;
                }
            }
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：解法1的递归版
     * - 时间复杂度 O(m+n)，空间复杂度 O(m+n)。
     * */
    public static ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) return null;
        if (l1 == null) {
            l2.next = mergeTwoLists2(null, l2.next);
            return l2;
        }
        if (l2 == null) {
            l1.next = mergeTwoLists2(l1.next, null);
            return l1;
        }
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists2(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists2(l1, l2.next);
            return l2;
        }
    }

    /*
     * 解法3：解法1的改进版
     * - 时间复杂度 O(m+n)，空间复杂度 O(1)。
     * */
    public static ListNode mergeTwoLists3(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), curr = dummyHead;

        while (l1 != null && l2 != null) {  // 此处是 &&，而非解法1中的 ||
            if (l1.val < l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }

        if (l1 != null) curr.next = l1;  // while 结束后 l1, l2 中至少有一个被遍历完了，另一个中的节点直接接到新链表上
        if (l2 != null) curr.next = l2;

        return dummyHead.next;
    }

    /*
     * 解法4：解法3的递归版（即解法2的改进版）
     * - 时间复杂度 O(m+n)，空间复杂度 O(m+n)。
     * */
    public static ListNode mergeTwoLists4(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists4(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists4(l1, l2.next);
            return l2;
        }
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 4});
        ListNode l2 = createLinkedList(new int[]{3, 4, 5});
        printLinkedList(mergeTwoLists4(l1, l2));  // expects 1->2->3->4->4->5->NULL

        ListNode l3 = createLinkedList(new int[]{5});
        ListNode l4 = createLinkedList(new int[]{1, 2, 4});
        printLinkedList(mergeTwoLists4(l3, l4));  // expects 1->2->4->5->NULL
    }
}
