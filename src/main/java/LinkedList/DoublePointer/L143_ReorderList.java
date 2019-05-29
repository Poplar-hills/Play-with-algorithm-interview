package LinkedList.DoublePointer;

import Utils.Helpers.ListNode;

import java.util.Stack;

import static Utils.Helpers.*;

/*
* Reorder List
*
* - Given a singly linked list L0 -> L1 -> ... -> Ln-1 -> Ln, reorder it to: L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...
* - You may not modify the values in the list's nodes, only nodes itself may be changed.
* */

public class L143_ReorderList {
    /*
    * 解法1：借助 Stack 实现从右往左遍历
    * - 思路：按照题目要求 reorder 链表则一定需要一个能从后往前移动的指针，而链表本身无法做到这一点，因此将链表装入 Stack 中，
    *   再逐个 pop 出来，从而实现从右往左遍历。
    * - 过程：对于 1 -> 2 -> 3 -> 4 -> 5 来说，先装入 Stack 中得到：[ 1 | 2 | 3 | 4 | 5 ->（右边是出口）
    *   1. 初始化 curr 指针，用来指向从左到右遍历链表过程中的当前节点。而整个 reorder 的过程就是穿插的从 curr 和 Stack 里获得
    *      下一个节点，并链接到一起。因此还需要一个指针 prev 指向 reorder 的链表的最后一个节点。
    *   2. 当 i=0 时，取 Stack 中的节点（节点5），链到 prev 后面，得到：
    *      1    2 -> 3 -> 4 -> 5   - prev 从1移到5，curr 从1移到2
    *      |___________________↑
    *   3. 当 i=1 时，取 curr 上的节点（节点2），链到 prev 后面，得到：
    *           ↓--------------|
    *      1    2 -> 3 -> 4 -> 5   - prev 从5移到2，curr 从2移到3
    *      |___________________↑
    *   4. 当 i=2 时，取 Stack 里的节点（节点4），链到 prev 后面，得到：
    *           ↓--------------|
    *      1    2    3 -> 4 -> 5   - prev 从2移到4，curr 不动仍是3（因为本次没有从 curr 上取节点，下次从这里继续）
    *      |    |_________↑    ↑
    *      |___________________|
    *   5. 当 i=3 时，取 curr 上的节点（节点3），链到 prev 后面，得到：
    *           ↓--------------|
    *      1    2   3 <-> 4    5   - prev 从4移到3，curr 不用再管。至此新链表的长度 == 原链表长度，停止遍历
    *      |    |_________↑    ↑
    *      |___________________|
    *   6. 将新链表的最后一个节点与其下一个节点断开：
    *           ↓--------------|
    *      1    2    3 -> 4    5   - 整个 reorder 过程完成
    *      |    |_________↑    ↑
    *      |___________________|
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * * */
    public static void reorderList(ListNode head) {
        if (head == null || head.next == null) return;

        Stack<ListNode> stack = new Stack<>();
        int len = 0;
        for (ListNode curr = head; curr != null; curr = curr.next) {
            stack.push(curr);
            len++;  // 在将链表节点放入堆中的同时获得链表长度
        }

        int i = 0;
        ListNode prev = head, curr = head;  // prev 指向 reorder 的每个节点，curr 指向原链表的当前节点，
        while (i < len) {  // reorder 的停止条件是链表长度与原来相等
            if (i % 2 == 0) {
                ListNode temp = prev.next;
                prev.next = stack.pop();
                curr = temp;
                prev = prev.next;
            } else {
                prev.next = curr;
                curr = curr.next;
                prev = prev.next;
            }
            i++;
        }
        prev.next = null;  // put an end
    }

    /*
    * 解法2：借助 Stack 实现从右往左遍历（化简版）
    * - 思路：不同于解法1中穿插的从 curr 和 Stack 中取节点的方式，该解法将 Stack 中的节点插入到链表的适合位置上。因此需要知道：
    *   1. 每个节点的插入位置 —— 在链表上每隔一个节点插入一个 Stack 中的节点。
    *   2. 停止条件 —— ∵ 只需要 Stack 中的前一半元素 ∴ 停止条件是 Stack.size / 2。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static void reorderList2(ListNode head) {
        if (head == null || head.next == null) return;

        Stack<ListNode> stack = new Stack<>();
        for (ListNode curr = head; curr != null; curr = curr.next)
            stack.push(curr);

        int i = 0, len = stack.size();  // Stack 的 size 就是链表长度，不需解法1中的手动计算
        ListNode curr = head;
        while (i < len / 2) {
            ListNode temp = curr.next;
            curr.next = stack.pop();
            curr.next.next = temp;
            curr = temp;
            i++;
        }
        curr.next = null;  // 对于 test case 2 来说，最后这里 curr 不是指向 null，而是指向3节点（自己画一下）
    }

    /*
    * 解法3：生成反向链表
    * - 思路：实际上如果我们需要的是一个能从后往前移动的指针，那么最直接的方式就是先生成一个反向链表。因此可知有这几步：
    *   1. 生成反向链表 —— 不需包含全部节点，只需要原链表的后一半节点即可，因此需要一个方法找到链表的中间节点，从中间节点开始反向。
    *   2. 将反向的半截链表中的每个节点插入原链表中：
    *      head:  1 -> 2 -> 3 -> 4 -> 5，中间节点为3，可得反向链表：
    *      head2: 5 -> 4 -> 3
    *      将 head2 的每个节点插入 head 得到：1 -> 5 -> 2 -> 4 -> 3
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static void reorderList3(ListNode head) {
        ListNode midNode = mid(head);
        ListNode head2 = reverse(midNode);
        ListNode prev1 = head, prev2 = head2;

        while (prev2 != null && prev2.next != null) {  // 当反向列表中的元素耗尽时即插入完成
            ListNode temp = prev1.next;
            prev1.next = prev2;
            prev2 = prev2.next;
            prev1.next.next = temp;
            prev1 = temp;
        }
    }

    private static ListNode reverse(ListNode head) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }

    private static ListNode mid(ListNode head) {  // 找链表的中间点的方法有很多（如先求得长度），但这种 slow/fast 是最快的 O(n/2)
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        reorderList3(l1);
        printLinkedList(l1);  // expects 1->5->2->4->3->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{1, 2, 3, 4});
        reorderList3(l2);
        printLinkedList(l2);  // expects 1->4->2->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{1, 1, 1, 2, 1, 3, 1, 1, 3});
        reorderList3(l3);
        printLinkedList(l3);  // expects 1->3->1->1->1->1->2->3->1->NULL
    }
}
