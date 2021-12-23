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

        int l = getLength(head);
        int numToRotate = k % l;             // 去掉套圈后需要移动的节点个数
        if (numToRotate == 0) return head;   // 若去掉套圈后没有需要移动的节点则直接返回

        int numToStay = l - numToRotate;     // 不需移动的节点个数
        ListNode curr = head;                // ∵ 要截断链表 ∴ 要找到截断处的上一个节点
        for (int i = 1; i < numToStay; i++)  // 跳过不需移动的节点后，curr 指向截断处的上一个节点（上例中的3节点）
            curr = curr.next;

        ListNode newHead = curr.next, tail = newHead;
        curr.next = null;                    // 截断

        while (tail.next != null)            // 获取待截断链表的最后一个节点
            tail = tail.next;
        tail.next = head;                    // 链接成新链表

        return newHead;
    }

    private static int getLength(ListNode head) {
        int len = 0;       // 注意 ∵ head 可能为 null ∴ len 不能从1开始 ∴ 下面循环的退出条件也得是 head != null
        for (; head != null; head = head.next) len++;
        return len;
    }

    /*
     * 解法2：截断移动（解法1的另一种写法）
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode rotateRight2(ListNode head, int k) {
        if (head == null || k == 0) return head;
        int len = getLength(head);
        int numToRotate = k % len;
        if (numToRotate == 0) return head;
        int numToStay = len - numToRotate;

        ListNode dummyHead = new ListNode();
        ListNode curr = head, prev = null;

        while (curr != null) {
            if (numToStay-- == 0) {  // numToStay 为0时 curr 即是 newHead，prev 即是 newTail
                prev.next = null;
                ListNode tail = curr;
                while (tail.next != null)  // 继续走到链表尾部，找到 tail
                    tail = tail.next;
                dummyHead.next = curr;
                tail.next = head;
            } else {
                prev = curr;
                curr = curr.next;
            }
        }

        return dummyHead.next;
    }

    /*
     * 解法3：截断移动（双指针版本）
     * - 思路：与解法1、2一致。
     * - 实现：采用 L19_RemoveNthNodeFromEndOfList 解法2的思路，使用双指针技巧实现“找到待截断链表的上一节点”。例如已知待截取
     *   的节点个数为2：
     *       1 -> 2 -> 3 -> 4 -> 5
     *       l         r              - 设置两个指针 l,r，并先让 r 向右移动2步，使得 r - l = 2
     *       1 -> 2 -> 3 -> 4 -> 5
     *                 l         r    - 让 l,r 同步移动，当 r 到达尾节点时，l 一定停在截断点的上一节点。
     *       1 -> 2 -> 3    4 -> 5
     *       ↑___________________|    - 让 r.next = head；n = l.next；l.next = null，从而得到新链表 n。
     *                 l    n    r
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

        r.next = head;            // 此时 l 一定停在截断点的上一节点，此时可进行截断
        head = l.next;
        l.next = null;
        return head;
    }

    /*
     * 解法4：递归
     * - 思路：与解法1、2、3一致。
     * - 实现：∵ 使用递归 ∴ 要换一种思考方式，考虑每次递归应返回什么。例如，已知待截取的节点个数为2：
     *     1 -> 2 -> 3 -> 4 -> 5 -> null
     *       →
     *            →
     *                 →
     *                      →
     *                           →
     *                           ← 1->2->3->4->5  - 递归到底，返回原链表
     *                      ← 5->1->2->3->4       - numToRotate=1，断开4->5，把5移动到链表头部
     *                 ← 4->5->1->2->3            - numToRotate=0，断开3->4，把4移动到链表头部
     *            ← 4->5->1->2->3                 - 直接返回
     *       ← 4->5->1->2->3                      - 直接返回
     *   可见：
     *   1. 在递归回程上递减 numToRotate，只要 numToRotate >= 0 就持续将尾节点移到链表头部；
     *   2. 在移动时，需要获取：
     *      - 待移动节点的上一个节点：可通过递归返回到上层时获得；
     *      - 当前链表的头结点：可通过递归返回值获得。
     * */

    private static int numToRotate;     // 声明在外部

    public static ListNode rotateRight4(ListNode head, int k) {
        if (head == null || k == 0) return head;
        numToRotate = k % getLength(head);
        return helper3(head, head, null);
    }

    private static ListNode helper3(ListNode head, ListNode curr, ListNode prev) {
        if (curr == null) return head;  // 递归到底时返回原链表

        ListNode currHead = helper3(head, curr.next, curr);  // 原链表的头节点通过递归一直传递下去（这样才能在需要的时候返回）
        if (--numToRotate < 0) return currHead;  // 只要 numToRotate 还 >= 0，就将尾节点移到链表头部

        prev.next = null;               // 断开上一个节点与当前节点的链接
        curr.next = currHead;           // 将当前节点移动到链表头部
        return curr;                    // 返回新链表
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight3(l1, 2));   // expects 4->5->1->2->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight3(l2, 7));   // expects 4->5->1->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(rotateRight3(l3, 0));   // expects 1->2->3->4->5->NULL

        ListNode l4 = createLinkedList(new int[]{1});
        log(rotateRight3(l4, 1));   // expects 1->NULL

        ListNode l5 = createLinkedList(new int[]{1});
        log(rotateRight3(l5, 99));  // expects 1->NULL
    }
}
