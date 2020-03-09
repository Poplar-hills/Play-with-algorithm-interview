package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Sort List
 *
 * - Sort a linked list in O(nlogn) time using constant space complexity.
 * */

public class L148_SortList {
    /*
     * 解法1：Merge Sort (top-down)
     * - 注意：该解法的空间复杂度未达到题目要求。
     * - 思路：∵ 要求 O(nlogn)，而 Quick Sort 非常依赖于对元素的随机访问能力 ∴ 不适用于链表，而 Merge Sort 则不存在这个
     *   问题，对链表更加适用。
     * - 实现：要使用 Merge Sort 为链表排序，需要能够：
     *     1. 对链表进行二分（分成前半部分和后半部分）；
     *     2. 对两个有序链表进行合并。
     *   对于这两个需求，解法如下：
     *     1. 采用 Floyd Cycle detection 的思路，指针 slow 每次走1步，指针 fast 每次走2步，当 fast 到头时，slow 一定
     *        指向链表中点。这里有个小坑 —— 当 slow 到达中点后，需要断开 slow 的前一节点和 slow 之间的链接，否则往下递归
     *        二分时处理的仍然是整个链表，而非前半部分。
     *     2. 见 L21_MergeTwoSortedLists。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)（递归深度是 logn，但任意时刻都需要存储 n 个元素，因此总体是 O(n)）。
     * */
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode prevSlow = null, slow = head, fast = head;
        while (fast != null && fast.next != null) {  // 直到 fast 走到最后一个节点才停止循环
            prevSlow = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prevSlow.next = null;  // 将链表的前半截和后半截断开，以便于后面分别递归前、后半段链表

        return merge(sortList(head), sortList(slow));  // 不断二分，直到每个节点为一组时再开始 merge
    }

    private static ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), prev = dummyHead;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        if (l1 != null) prev.next = l1;
        if (l2 != null) prev.next = l2;

        return dummyHead.next;
    }

    /*
     * 解法2：Merge Sort (bottom-up)
     * - 思路：要做到空间复杂度为 O(1) 就不能使用递归，而得采用迭代 ∴ 使用 bottom-up 的 Merge Sort。Bottom-up 的
     *   Merge Sort 的本质是通过多轮遍历来模拟二分操作：
     *     5 -> 1 -> 6 -> 4 -> 2 -> 7 -> 3 -> NULL
     *     +    +    +    +    +    +    +          - 第1次遍历，每组1个节点，两组两组归并
     *     1 -> 5 -> 4 -> 6 -> 2 -> 7 -> 3 -> NULL
     *     +----+    +----+    +----+    +          - 第2次遍历，每组2个节点，两组两组归并
     *     1 -> 4 -> 5 -> 6 -> 2 -> 3 -> 7 -> NULL
     *     +--------------+    +---------+          - 第3次遍历，每组4个节点，两组两组归并
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> NULL
     *                                              - 第4次遍历，每组8个节点，已经超过链表长度 ∴ 不再继续，排序结束
     * - 实现：
     *   1. ∵ 采用多轮遍历来模拟二分 ∴ 首先需要生成一个 1, 2, 4... 的单组节点个数序列，之后按照这个序列两组两组遍历链表。
     *   2. ∵ 停止遍历的条件是一组中节点个数超过量表长度 ∴ 要先获取链表长度。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static ListNode sortList2(ListNode head) {
        if (head == null || head.next == null) return head;

        int len = 0;
        for (ListNode curr = head; curr != null; curr = curr.next)  // 先获取链表长度
            len++;

        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        for (int step = 1; step <= len; step *= 2) {  // 生成 1, 2, 4...（单组节点个数序列）序列
            ListNode prev = dummyHead;
            for (int i = 0; i + step < len; i += step * 2) {  // 两组两组遍历链表，i + step < len 保证了第二组中至少有元素存在
                ListNode l1 = prev.next;              // l1、l2 分别指向第1、2组头结点
                ListNode l2 = split(l1, step);
                ListNode tail = split(l2, step);
                ListNode last = merge(l1, l2, prev);
                last.next = tail;                     // 切剩下的部分链回到 merge 之后的链表上
                prev = last;                          // 让 prev 指向 merge 之后的链表的最后一个节点以对下一组进行排序
            }
        }

        return dummyHead.next;
    }

    private static ListNode split(ListNode left, int count) {  // 将一个链表按照给定的节点个数（count）切成两段（第二段可能不够 count 个），返回第二段链表的头节点
        for (int i = 1; left != null && i < count; i++)        // 注：∵ Java is passed by value ∴ 可以直接用参数 left 作为指针来移动，不需要再定义 curr
            left = left.next;

        if (left == null) return null;
        ListNode right = left.next;
        left.next = null;
        return right;
    }

    private static ListNode merge(ListNode l1, ListNode l2, ListNode prev) {  // 将两个链表 merge 到 prev 节点之后，返回最后一个节点
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }
        if (l1 != null) prev.next = l1;
        if (l2 != null) prev.next = l2;

        while (prev.next != null) prev = prev.next;  // 走到最后一个节点上
        return prev;                           // 返回最后一个节点
    }

    /*
     * 解法3：Dual-Pivot Quick Sort (3-way Quick Sort)
     * - 思路：直接将传入的 head 作为标定节点，建立三个子链表，分别存储 > head.val、== head.val、< head.val 的节点，
     *   之后对除了 == head.val 以外的子链表进行递归排序，最后返回两两 merge 的结果。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static ListNode sortList3(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode ltDummy = new ListNode(), lt = ltDummy;
        ListNode eqDummy = new ListNode(), eq = eqDummy;
        ListNode gtDummy = new ListNode(), gt = gtDummy;
        ListNode curr = head;

        while (curr != null) {
            if (curr.val < head.val) {
                lt.next = curr;
                lt = lt.next;
            } else if (curr.val == head.val) {
                eq.next = curr;
                eq = eq.next;
            } else {
                gt.next = curr;
                gt = gt.next;
            }
            curr = curr.next;
        }
        lt.next = eq.next = gt.next = null;        // put an end

        ListNode sortedLt = sortList3(ltDummy.next);  // 递归排序 > head、< head 的子链表（== head 的子链表不需要再排）
        ListNode sortedGt = sortList3(gtDummy.next);
        return merge(merge(sortedLt, sortedGt), eqDummy.next);  // 最终两两 merge 在一起（merge 顺序无所谓）
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{5, 1, 6, 4, 2, 7, 3});
        printLinkedList(sortList2(l1));  // expects 1->2->3->4->5->6->7->NULL

        ListNode l2 = createLinkedList(new int[]{4, 2, 1, 3});
        printLinkedList(sortList2(l2));  // expects 1->2->3->4->NULL

        ListNode l3 = createLinkedList(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(sortList2(l3));  // expects -1->0->3->4->5->NULL
    }
}
