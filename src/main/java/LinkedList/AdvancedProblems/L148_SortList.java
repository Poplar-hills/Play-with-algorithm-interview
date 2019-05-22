package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Sort List
*
* - Sort a linked list in O(nlogn) time using constant space complexity.
* */

public class L148_SortList {
    /*
    * 解法1：Merge Sort
    * - 思路：因为要求用 O(nlogn) 的算法排序，而 Quick Sort 非常依赖于数组的随机访问能力，不适用于链表，而 Merge Sort 适用于链表。
    *        要使用 Merge Sort 为链表排序，则需解决：1. 如何对链表进行二分（分成前半部分和后半部分）；2. 如何对各自有序的链表进行合并。
    *        1. 采用类似 Floyd Cycle detection 的方法，指针 slow 每次走1步，指针 fast 每次走2步，当 fast 到头时，slow 一定指向
    *           链表中点。这里有个小坑 —— 当 fast 和 slow 都到达各自位置后，需要断开 fast 最后一个节点和 slow 第一个节点之间的链接，
    *           否则往下递归二分时 fast 仍然是整个链表，而非前半部分。
    *        2. 没啥好说的。
    * - 时间复杂度 O(nlogn)，空间复杂度 O(n)（递归深度是 logn，但任意时刻都需要存储 n 个元素，因此总体是 O(n)）。
    * */
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode slow = head, fast = head, prevOfSlow = null;
        while (fast != null && fast.next != null) {
            prevOfSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prevOfSlow.next = null;

        return merge(sortList(head), sortList(slow));
    }

    private static ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), prev = dummyHead;
        ListNode n1 = l1, n2 = l2;

        while (n1 != null && n2 != null) {
            if (n1.val < n2.val) {
                prev.next = n1;
                n1 = n1.next;
            } else {
                prev.next = n2;
                n2 = n2.next;
            }
            prev = prev.next;
        }

        if (n1 != null) prev.next = n1;
        if (n2 != null) prev.next = n2;
        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l0 = createLinkedListFromArray(new int[]{4, 1, 6, 5, 2, 8, 3, 7});
        printLinkedList(sortList(l0));  // expects 1->2->3->4->5->6->7->8->NULL

        ListNode l1 = createLinkedListFromArray(new int[]{4, 2, 1, 3});
        printLinkedList(sortList(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(sortList(l2));  // expects -1->0->3->4->5->NULL
    }
}
