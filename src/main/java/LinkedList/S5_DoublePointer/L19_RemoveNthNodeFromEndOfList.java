package LinkedList.S5_DoublePointer;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Nth Node From End of List
 *
 * - 删除给定链表的倒数第 n 个节点（n 从1开始），返回删除节点后的链表。
 * */

public class L19_RemoveNthNodeFromEndOfList {
    /*
     * 解法1：获取长度（Two-pass）
     * - 思路：先求链表长度，再找到并删除目标节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        int l = getLength(head);
        if (l < n) return head;
        int m = l - n;   // 从头开始第 m 个节点即是待删除节点（m 从 0 开始）

        ListNode dummyHead = new ListNode();  // ∵ 待删除的可能是第一个节点 ∴ 需要 dummyHead
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;

        while (m-- != 0) {
            prev = prev.next;
            curr = curr.next;
        }
        prev.next = curr.next;  // 当找到第 m 个节点时，跳过它

        return dummyHead.next;
    }

    private static int getLength(ListNode head) {
        int count = 0;
        for ( ; head != null; head = head.next) count++;
        return count;
    }

    /*
     * 解法2：双指针（One-pass）
     * - 思路：解法1中的瓶颈在于需要先获取链表长度。而另一种思路是通过双指针同时移动找到待删除节点的上一节点。例如：
     *   对于 1->2->3->4->5->null，n=2 来说：
     *   1. 待删除的是节点4 ∴ 只要获得节点3即可完成删除；
     *   2. 虽然不知道链表长度，但能知道节点3与 null 的差距是 n+1 ∴ 可以利用这个差距，设置两个初始差距为 n+1 的指针，然后
     *      让他们同时移动，当右指针到达 null 时，左指针即指向节点3；
     * - 💎 技巧：👆利用节点与 null 之间的距离差移动指针来找到待删除节点的前一节点，这个技巧很妙。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode removeNthFromEnd2(ListNode head, int n) {
        if (head == null || n < 0) return head;

        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode l = dummyHead, r = dummyHead;

        for (int i = 0; i < n + 1; i++) {  // 先将 r 移动到距 l n+1 的位置
            if (r == null) return head;    // 处理 n 越界的情况
            r = r.next;
        }

        while (r != null) {     // 同时移动两个指针，当 r 到达 null 时，l 就到了待删除节点的前一节点上了
            l = l.next;
            r = r.next;
        }
        l.next = l.next.next;   // 删除节点

        return dummyHead.next;
    }

    /*
     * 解法3：递归
     * - 思路：先递归到底，在递归回程上检查当前节点是否是倒数第 n 个节点，若是则删除。根据该思路，关键点转化为如何
     *   找到链表上的倒数第 n 个节点。
     * - 例：1 -> 2 -> 3 -> 4 -> 5 -> NULL, n=2
     *                ...            h     - return N
     *                          h     - return 5->N
     *                     h     - is the 2nd to last node ∴ delete, return 5->N
     *                h     - return 3->5->N
     *           h     - return 2->3->5->N
     *      h     - return 1->2->3->5->N
     * - 实现：要实现以上思路，最简单的方式是在递归去程时记录节点编号 i，并用一个外部变量 count 记录链表节点总数。
     *   然后在回程时检查每个节点编号 i 是否 == count - n + 1，即可确定该节点是否需要删除。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    private static int count = 0;

    public static ListNode removeNthFromEnd3(ListNode head, int n) {
        return helper3(head, n, 1);
    }

    private static ListNode helper3(ListNode head, int n, int i) {
        if (head == null) return null;  // 注意退出条件不能是 head.next == null ∵ 要删除的可能是最后一个节点
        count = i;
        ListNode returned = helper3(head.next, n, i + 1);
        if (count - n + 1 == i) return returned;  // 判断是否是倒数第 n 个节点
        head.next = returned;
        return head;
    }

    /*
     * 解法4：递归
     * - 思路：与解法3一致。
     * - 实现：不再使用解法3中的外部变量，而是每层递归返回一个 Pair<新链表的头结点, 该头结点是倒数第几个节点>。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeNthFromEnd4(ListNode head, int n) {
        Pair<ListNode, Integer> p = helper4(head, n);
        return p.getKey();
    }

    private static Pair<ListNode, Integer> helper4(ListNode head, int n) {
        if (head == null) return new Pair<>(null, null);  // 即使是空链表也要返回 Pair 否则👆🏻p.getKey() 会 NPE
        if (head.next == null) return new Pair<>(head, 1);

        Pair<ListNode, Integer> p = helper4(head.next, n);
        ListNode returned = p.getKey();
        int currNthToLast = p.getValue() + 1;  // 获得当前节点的倒数编号

        if (currNthToLast == n) return new Pair<>(returned, currNthToLast);
        head.next = returned;
        return new Pair<>(head, currNthToLast);
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        log(removeNthFromEnd4(l1, 2));  // expects 1->2->3->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3});
        log(removeNthFromEnd4(l2, 3));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        log(removeNthFromEnd4(l3, 2));  // expects 1->NULL (n 越界的 case)

        ListNode l4 = createLinkedList(new int[]{});
        log(removeNthFromEnd4(l4, 2));  // expects NULL
    }
}
