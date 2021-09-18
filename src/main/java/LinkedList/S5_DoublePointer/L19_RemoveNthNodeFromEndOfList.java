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
     *   1. 待删除的节点4 ∴ 只要获得节点3即可完成删除；
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
     * - 思路：先递归到底，在递归回程上的第 n 个节点就是链表的倒数第 n 个节点。
     * - 实现：为了在回程时数出第 n 个节点，每层递归函数一个 Pair<新链表的头结点, 该头结点是倒数第几个节点>。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode removeNthFromEnd3(ListNode head, int n) {
        Pair<ListNode, Integer> p = helper3(head, n);
        return p.getKey();
    }

    private static Pair<ListNode, Integer> helper3(ListNode head, int n) {
        if (head == null || head.next == null)
            return new Pair<>(n == 1 ? null : head, 1);  // 若 n=1，则跳过最后一个节点返回 null

        Pair<ListNode, Integer> p = helper3(head.next, n);
        ListNode tail = p.getKey();
        int count = p.getValue();

        if (count + 1 == n)  // 若当前节点就是倒数第 n 个节点，则跳过当前节点返回 tail
            return new Pair<>(tail, count + 1);
        head.next = tail;    // 若不是，则需手动链接上层递归返回的节点（∵ 有可能上层递归中已找到并跳过了头结点）
        return new Pair<>(head, count + 1);
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(removeNthFromEnd(l1, 2));  // expects 1->2->3->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3});
        printLinkedList(removeNthFromEnd(l2, 3));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1});
        printLinkedList(removeNthFromEnd(l3, 2));  // expects 1->NULL (n 越界的 case)

        ListNode l4 = createLinkedList(new int[]{});
        printLinkedList(removeNthFromEnd(l4, 2));  // expects NULL
    }
}
