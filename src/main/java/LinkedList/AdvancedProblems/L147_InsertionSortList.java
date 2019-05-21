package LinkedList.AdvancedProblems;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
* Insertion Sort List
*
* - Sort a linked list using insertion sort.
* */

public class L147_InsertionSortList {
    /*
    * 解法1：遍历
    * - 思路：插入排序中，当前节点之前的是已经有序的节点，因此只要遍历这些节点找到适合当前节点的插入位置即可。注意：
    *   1. 在 play-with-algrithms 的 InsertionSort 中，遍历是从后往前逐个比较的，但链表中无法回到上一节点，因此这里采用从前往后遍历。
    *   2. 因为插入位置可能是头结点，因此需要创建虚拟头结点。
    *   3. 虚拟头结点不需要（也不能）链接到 head 上。
    * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
    * */
    public static ListNode insertionSortList(ListNode head) {
        ListNode dummyNode = new ListNode(), curr = head, pos;  // pos 是插入位置的上一个节点

        while (curr != null) {
            // locate the insertion position
            pos = dummyNode;  // 每次重置 pos
            while (pos.next != null && curr.val > pos.next.val)
                pos = pos.next;

            // insert curr between pos and pos.next
            ListNode posNext = pos.next, currNext = curr.next;
            pos.next = curr;
            curr.next = posNext;

            curr = currNext;
        }

        return dummyNode.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{4, 2, 1, 3});
        printLinkedList(insertionSortList(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(insertionSortList(l2));  // expects -1->0->3->4->5->NULL
    }
}
