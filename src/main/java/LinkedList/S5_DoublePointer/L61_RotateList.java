package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Rotate List
 *
 * - Given a linked list, rotate the list to the right by k places, where k is non-negative.
 * */

public class L61_RotateList {
    /*
     * 解法1：截断移动
     * - 思路：要得到 rotate 之后的链表，关键在于要确定从哪里截断并链到头节点上去。例如 1->2->3->4->5，k=7，rotate 之后
     *   得到 4->5->1->2->3，即从原链表3节点之后截断，并把截断的部分放到链表头部。
     * - 实现：要确定从哪里截断则需要知道去掉套圈后的剩余距离（上例中 k % len = 2），即说明原链表中最后2个节点要被截断并放到
     *   链表头部，至此问题转化成了类似 L19_RemoveNthNodeFromEndOfList 中的问题。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || k == 0) return head;  // 若只有1个节点则直接返回

        // 1. Get the length and num of step to rotate
        int len = getLength(head);
        int numToRotate = k % len;          // 去掉套圈后需要移动的节点个数
        if (numToRotate == 0) return head;  // 若去掉套圈后没有需要移动的节点则直接返回

        // 2. Find the disconnection point and truncate the list
        ListNode prev = head;               // ∵ 要截断链表 ∴ 要找到截断处的上一个节点
        for (int i = 1; i < len - numToRotate; i++)  // 跳过不需移动的节点后，prev 指向截断处的上一个节点（上例中的3节点）
            prev = prev.next;

        ListNode newHead = prev.next, tail = newHead;
        prev.next = null;          // 截断

        // 3. Move the detached list to the front
        while (tail.next != null)  // 获取待截断链表的最后一个节点（优化：tail 可在 getLength 的时候获得，无需再次遍历）
            tail = tail.next;
        tail.next = head;          // 链接成新链表

        return newHead;
    }

    private static int getLength(ListNode head) {
        int len = 0;       // 注意 ∵ head 可能为 null ∴ len 不能从1开始 ∴ 下面循环的退出条件也得是 head != null
        for (; head != null; head = head.next) len++;
        return len;
    }

    /*
     * 解法2：截断移动（双指针版本，最优🥇）
     * - 思路：与解法1一致。
     * - 实现：采用 L19_RemoveNthNodeFromEndOfList 解法2的思路，使用双指针技巧实现“找到待截断链表的上一节点”。例如已知待截取
     *   的节点个数为2：
     *       1 -> 2 -> 3 -> 4 -> 5 -> 6
     *       l         r                 - 设置两个指针 l,r，并先让 r 向右移动2步，使得 r - l = 2
     *       1 -> 2 -> 3 -> 4 -> 5 -> 6
     *                      l         r  - 让 l,r 同步移动，当 r 到达尾节点时，l 一定停在截断点的上一节点。
     *       1 -> 2 -> 3 -> 4    5 -> 6
     *       ↑________________________|  - 让 r.next=head；n=l.next；l.next=null，从而得到新链表 n。
     *                      l    n    r
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight3(ListNode head, int k) {
        if (head == null || k == 0) return null;
        int numToRotate = k % getLength(head);

        ListNode l = head, r = head;
        for (int i = 0; i < numToRotate; i++)  // 让 l, r 相距 numToRotate 步
            r = r.next;

        while (r.next != null) {  // 同步移动双指针，直到 r 抵达尾节点（而非像 L19 中抵达 null）
            r = r.next;
            l = l.next;
        }

        ListNode newHead = l.next;
        l.next = null;            // 此时 l 一定停在截断点的上一节点，此时可进行截断
        r.next = head;
        return newHead;
    }

    /*
     * 解法3：递归
     * - 思路：与解法1、2一致。
     * - 实现：∵ 使用递归 ∴ 要换一种思考方式，考虑每次递归应返回什么。
     * - 例：1 -> 2 -> 3 -> 4 -> 5 -> null, k=2
     *                 ...      p    c    - return 1->2->3->4->5->N（返回链表头结点，而非最后一个节点）
     *                     p    c    - numToRotate=2, break 4->5, move 5 to front, return 5->1->2->3->4->N
     *                p    c    - numToRotate=1, break 3->4, move 4 to front, return 4->5->1->2->3->N
     *           p    c    - numToRotate=0, return 4->5->1->2->3->N
     *      p    c    - numToRotate=0, return 4->5->1->2->3->N
     * - 可见：
     *   1. 在递归回程上递减 numToRotate，只要 numToRotate > 0 就持续将尾节点移到链表头部；
     *   2. 在移动时，需要获取：
     *      - 待移动节点的上一个节点 p：可通过递归参数逐层传递，当前层的 c 是下一层的 p；
     *      - 当前链表的头结点 head：可通过递归参数逐层传递。
     * */
    private static int numToRotate;     // 声明在外部

    public static ListNode rotateRight4(ListNode head, int k) {
        if (head == null || k == 0) return head;
        numToRotate = k % getLength(head);
        return helper4(head, head, null);
    }

    private static ListNode helper4(ListNode head, ListNode curr, ListNode prev) {
        if (curr == null) return head;  // 注意：递归到底时返回链表头结点（而非最后一个节点）
        ListNode returned = helper4(head, curr.next, curr);  // 原链表的头节点通过递归一直传递下去（这样才能在需要的时候返回）

        if (numToRotate-- > 0) {
            prev.next = null;      // 断开上一个节点与当前节点的链接
            curr.next = returned;  // 将当前节点移动到链表头部
            return curr;
        }
        return returned;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight4(l1, 2));   // expects 4->5->1->2->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight4(l2, 7));   // expects 4->5->1->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight4(l3, 0));   // expects 1->2->3->4->5->NULL

        ListNode l4 = createLinkedList(new int[]{1});
        log(rotateRight4(l4, 1));   // expects 1->NULL

        ListNode l5 = createLinkedList(new int[]{1});
        log(rotateRight4(l5, 99));  // expects 1->NULL
    }
}
