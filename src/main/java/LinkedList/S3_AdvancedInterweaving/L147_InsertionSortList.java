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
     * - 思路：插入排序的核心是当发现后一个元素 < 前一个元素时，将后一个元素往前移动，直到前一个元素不再小于它时停止移动并插入进去。
     * - 实现：
     *   1. 在标准的排序算法中，遍历是从后往前逐个比较的，但 ∵ 链表中无法从下一节点回到上一节点 ∴ 可采用从前往后遍历；
     *   2. ∵ 插入位置可能是头结点 ∴ 需要创建虚拟头结点；
     *           4 -> 2 -> 1 -> 3
     *      D -> 4
     *      D -> 2 -> 4
     *      D -> 1 -> 2 -> 4
     *      D -> 1 -> 2 -> 3 -> 4
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static ListNode insertionSortList(ListNode head) {
        ListNode dummyHead = new ListNode();
        ListNode curr = head;

        while (curr != null) {
            ListNode pos = dummyHead;       // 每次从头开始遍历，让 pos 停在最后一个 < curr 的节点上
            while (pos.next != null && pos.next.val < curr.val)
                pos = pos.next;

            ListNode currNext = curr.next;  // 将 curr 节点插入到 pos 后面
            ListNode posNext = pos.next;
            pos.next = curr;
            curr.next = posNext;

            curr = currNext;                // curr 指向下一个节点
        }

        return dummyHead.next;
    }

    /*
     * 解法2：递归
     * - 思路：若采用递归实现则思路会有不同：在下层递归返回后，将本层的节点插入返回的链表的合适位置，再返回上层。
     * - 实现：先递归到最深处，在回程时将当前节点放入合适的位置：
     *        -1 -> 5 -> 3 -> 4 -> 0 -> NULL
     *                           ← 0->N
     *                      ← 0->4->N         - 将4插到0的后面
     *                 ← 0->3->4->N           - 将3插到0的后面
     *            ← 0->3->4->5->N             - 将5插到4的后面
     *        ← -1->0->3->4->5->N             - 已经有序直接返回
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)（递归深度是 logn，但任意时刻都需要存储 n 个元素 ∴ 总体是 O(n)）。
     * */
    public static ListNode insertionSortList2(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode list = insertionSortList2(head.next);
        if (head.val <= list.val) {
            head.next = list;
            return head;
        }

        ListNode pos = list;
        while (pos.next != null && pos.next.val < head.val)  // 让 pos 走到插入位置的前一个节点
            pos = pos.next;

        head.next = pos.next;  // 将 head 插入到 pos 后面
        pos.next = head;

        return list;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{4, 2, 1, 3});
        log(insertionSortList2(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedList(new int[]{-1, 5, 3, 4, 0});
        log(insertionSortList2(l2));  // expects -1->0->3->4->5->NULL
    }
}
