package LinkedList.S3_AdvancedInterweaving;

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

    /*
     * 解法2：递归
     * - 思路：先递归到最深处，在回程时将当前节点放入合适的位置
     *   -1 -> 5 -> 3 -> 4 -> 0 -> NULL
     *                      ← 0->N
     *                 ← 0->4->N    将4插到0的后面
     *            ← 0->3->4->N      将3插到0的后面
     *       ← 0->3->4->5->N        将5插到4的后面
     *   ← -1->0->3->4->5->N        已经有序直接返回
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)（递归深度是 logn，但任意时刻都需要存储 n 个元素，因此总体是 O(n)）。
     * */
    public static ListNode insertionSortList2(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode list = insertionSortList(head.next);
        if (head.val <= list.val) {
            head.next = list;
            return head;
        }

        ListNode pos = list;
        while (pos.next != null && head.val > pos.next.val)  // 找到插入位置的前一个节点（循环结束后 pos 指向插入位置的前一个节点）
            pos = pos.next;

        head.next = pos.next;  // 插入节点
        pos.next = head;

        return list;
    }

    /*
     * 解法3：递归（dummyNode 版）
     * - 思路：与解法2一致，只是在插入节点时使用虚拟头结点。
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)。
     * */
    public static ListNode insertionSortList3(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode list = insertionSortList2(head.next);
        ListNode dummyNode = new ListNode(), curr = list, pos = dummyNode;

        while (curr != null && head.val > curr.val) {  // 找到插入位置
            pos.next = curr;
            curr = curr.next;
            pos = pos.next;
        }

        pos.next = head;
        pos = pos.next;
        pos.next = curr;

        return dummyNode.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{4, 2, 1, 3});
        printLinkedList(insertionSortList2(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedList(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(insertionSortList2(l2));  // expects -1->0->3->4->5->NULL
    }
}
