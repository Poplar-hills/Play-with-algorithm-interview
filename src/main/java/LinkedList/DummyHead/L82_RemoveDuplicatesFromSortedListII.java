package LinkedList.DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Remove Duplicates from Sorted List II
*
* - 删除一个有序链表中所有的重复节点。
* */

public class L82_RemoveDuplicatesFromSortedListII {
    /*
    * 解法1：Intuitive solution
    * - 思路：对于 1->2->3->3->4->4->5 来说，若想将去除 3->3，则需要链接3之前的2节点和第一个4节点，因此这两个节点需要有指针指向才行。
    *        因此设计两个指针（分别指向重复节点之前和之后的节点）+ 一个标志位（标志当前是否发现了重复节点）：
    *        D -> 1 -> 2 -> 3 -> 3 -> 4 -> 4 -> 5
    *        a    b                                 此时 b.val != b.next.val，此时 a, b 向后移动
    *        D -> 1 -> 2 -> 3 -> 3 -> 4 -> 4 -> 5
    *             a    b                            此时 b.val != b.next.val，此时 a, b 向后移动
    *        D -> 1 -> 2 -> 3 -> 3 -> 4 -> 4 -> 5
    *                  a    b                       此时 b.val != b.next.val，此时 a, b 向后移动
    *        D -> 1 -> 2 -> 3 -> 3 -> 4 -> 4 -> 5
    *                  a         b                  此时 b.val == b.next.val，此时 a 不动，b 向后移动，标志位置为 true
    *        D -> 1 -> 2 -> 3 -> 3 -> 4 -> 4 -> 5
    *                  a              b             此时 b.val != b.next.val，此时 a 不动，b 向后移动，标志位置为 false，将 a.next 指向 b
    *        D -> 1 -> 2 -> 4 -> 4 -> 5
    *                  a    b                       继续遍历直到 b.next == null 结束
    * - 特殊情况：D -> 1 -> 1，因为此时重复节点后面没有更多节点，无法完成
    * */
    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode conn = dummyHead, curr = head;
        boolean foundDuplicates = false;

        while (curr.next != null) {
            if (!foundDuplicates && curr.val != curr.next.val) {
                conn = conn.next;
                curr = curr.next;
            }
            else if (curr.val == curr.next.val) {
                foundDuplicates = true;
                curr = curr.next;
            }
            else {
                foundDuplicates = false;
                curr = curr.next;
                conn.next = curr;
            }
        }

        if (foundDuplicates)  // curr.next == null 的情况（SEE test case 3）
            conn.next = null;

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 3, 4, 4, 5});
        printLinkedList(deleteDuplicates(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 1, 1, 2, 3});
        printLinkedList(deleteDuplicates(l2));  // expects 2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1, 1});
        printLinkedList(deleteDuplicates(l3));  // expects NULL

        ListNode l4 = createLinkedListFromArray(new int[]{});
        printLinkedList(deleteDuplicates(l4));  // expects NULL
    }
}
