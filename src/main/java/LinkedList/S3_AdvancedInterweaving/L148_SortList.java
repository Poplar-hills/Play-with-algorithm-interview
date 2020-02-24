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

    /*
     * 解法2：Merge Sort (bottom-up)
     * - 思路：要做到空间复杂度为 O(1) 就不能使用递归，而得采用迭代，因此使用 bottom-up 的 Merge Sort。
     *        bottom-up 的 Merge Sort 的原理是通过多轮迭代模拟二分操作，例：
     *        5 -> 1 -> 6 -> 4 -> 2 -> 7 -> 3 -> NULL
     *        +    +    +    +    +    +    +          - 第1次遍历，每组1个节点，两组两组归并
     *        1 -> 5 -> 4 -> 6 -> 2 -> 7 -> 3 -> NULL
     *        +----+    +----+    +----+    +          - 第2次遍历，每组2个节点，两组两组归并
     *        1 -> 4 -> 5 -> 6 -> 2 -> 3 -> 7 -> NULL
     *        +--------------+    +---------+          - 第3次遍历，每组4个节点，两组两组归并
     *        1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> NULL
     *        因此，首先需要生成一个 1, 2, 4... 的单组节点个数序列，之后在每个单组节点个数下两组两组遍历链表。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static ListNode sortList2(ListNode head) {
        if (head == null || head.next == null)
            return head;

        int len = 0;
        for (ListNode curr = head; curr != null; curr = curr.next)  // 先获取链表长度
            len++;

        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        for (int step = 1; step <= len; step *= 2) {  // 生成单组节点个数序列
            ListNode prev = dummyHead;
            for (int i = 0; i + step < len; i += step * 2) {  // 在每个单组节点个数下两组两组遍历链表
                ListNode l1 = prev.next;
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
        ListNode p = prev, n1 = l1, n2 = l2;  // p 初始化为 prev，之后每次都将节点直接添加到 prev 之后

        while (n1 != null && n2 != null) {
            if (n1.val < n2.val) {
                p.next = n1;
                n1 = n1.next;
            } else {
                p.next = n2;
                n2 = n2.next;
            }
            p = p.next;
        }
        if (n1 != null) p.next = n1;
        if (n2 != null) p.next = n2;

        while (p.next != null) p = p.next;  // 走到最后一个节点上
        return p;                           // 返回最后一个节点
    }

    /*
     * 解法3：Dual-Pivot Quick Sort (3-way Quick Sort)
     * - 思路：直接将传入的 head 作为标定节点，建立三个子链表，分别存储 > head.val、== head.val、< head.val 的节点，
     *        之后对除了 == head.val 以外的子链表进行递归排序，最后返回两两 merge 的结果。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(1)。
     * */
    public static ListNode sortList3(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode smallDummy = new ListNode(), small = smallDummy;
        ListNode equalDummy = new ListNode(), equal = equalDummy;
        ListNode largeDummy = new ListNode(), large = largeDummy;

        ListNode curr = head;
        while (curr != null) {
            if (curr.val < head.val) {
                small.next = curr;
                small = small.next;
            } else if (curr.val == head.val) {
                equal.next = curr;
                equal = equal.next;
            } else {
                large.next = curr;
                large = large.next;
            }
            curr = curr.next;
        }
        small.next = equal.next = large.next = null;        // put an end

        ListNode sortedSmall = sortList3(smallDummy.next);  // 对于 > head、< head 的子链表递归排序（== head 的子链表不需要再排）
        ListNode sortedLarge = sortList3(largeDummy.next);
        return merge(merge(sortedSmall, sortedLarge), equalDummy.next);  // 最终两两 merge 在一起（merge 顺序无所谓）
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{5, 1, 6, 4, 2, 7, 3});
        printLinkedList(sortList3(l1));  // expects 1->2->3->4->5->6->7->NULL

        ListNode l2 = createLinkedList(new int[]{4, 2, 1, 3});
        printLinkedList(sortList3(l2));  // expects 1->2->3->4->NULL

        ListNode l3 = createLinkedList(new int[]{-1, 5, 3, 4, 0});
        printLinkedList(sortList3(l3));  // expects -1->0->3->4->5->NULL
    }
}
