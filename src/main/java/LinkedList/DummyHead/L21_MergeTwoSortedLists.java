package LinkedList.DummyHead;

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
        ListNode dummyHead = new ListNode(), curr = dummyHead, n1 = l1, n2 = l2;

        while (n1 != null || n2 != null) {
            if (n1 == null) {
                curr.next = n2;
                n2 = n2.next;
            } else if (n2 == null) {
                curr.next = n1;
                n1 = n1.next;
            } else {
                if (n1.val < n2.val) {
                    curr.next = n1;
                    n1 = n1.next;
                } else {
                    curr.next = n2;
                    n2 = n2.next;
                }
            }
            curr = curr.next;
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 4});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        printLinkedList(mergeTwoLists(l1, l2));  // expects 1->1->2->3->4->4->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{5});
        ListNode l4 = createLinkedListFromArray(new int[]{1, 2, 4});
        printLinkedList(mergeTwoLists(l3, l4));  // expects 1->2->4->5->NULL
    }
}
