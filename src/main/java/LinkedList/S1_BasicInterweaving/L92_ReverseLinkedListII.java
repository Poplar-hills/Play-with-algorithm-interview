package LinkedList.S1_BasicInterweaving;

import static Utils.Helpers.*;

import Utils.Helpers.ListNode;

/*
 * Reverse Linked List II
 *
 * - Reverse a linked list from position m to n.
 *
 * - Note:
 *   - 1 ≤ m ≤ n ≤ length of list (m 和 n 是从1开始的)
 *   - Do it in one-pass (要求在一次遍历内完成)
 * */

public class L92_ReverseLinkedListII {
    /*
     * 错误解：迭代指针对撞 + 交换节点值
     * - 大体思路对，但是最后因为无法从下一个节点回到上一个节点而最终失败。
     * - 虽然错误，但有助于理解解法1。
     * */
    public ListNode reverseBetween0(ListNode head, int m, int n) {
        ListNode left = head, right = head;

        for (int i = 1; i < n; i++) {  // 先让两个指针各自走到 m, n 上
            if (i < m) left = left.next;
            right = right.next;
        }
        while (left != right && right.next != left) {  // 开始指针对撞
            int value = left.val;
            left.val = right.val;
            right.val = value;

            left = left.next;
//            right = right.prev;  // 若是双向链表，节点上有 prev 属性，能回到上一个节点，则此解法就可以工作了
        }
        return head;
    }

    /*
     * 解法1：递归指针对撞 + 交换节点值
     * - 思路：类似将数组倒序的思路 —— 先将两个指针移动到数组 m, n 的位置上，再在他们互相逼近的过程中不断 swap 节点里的值。但是因
     *   为单向链表没有从后一个节点指向前一个节点的指针，若要让右指针左移到上一个节点需要借助递归来实现，因为在每层递归结束回到上一
     *   层调用栈时可以获得上一个节点。（过程可视化 SEE: https://leetcode.com/problems/reverse-linked-list-ii/solution/）
     * - 时间复杂度 O(n)，因为只遍历到 n 处的节点；
     * - 空间复杂度 O(n)，同样因为只遍历到 n 处的节点，因此递归深度为 n。
     * */
    private static ListNode left;
    private static boolean stop;

    public static ListNode reverseBetween(ListNode head, int m, int n) {
        recurseAndReverse(head, m, n);
        return head;
    }

    private static void recurseAndReverse(ListNode head, int m, int n) {
        // 进入下一层递归之前：让两个指针向右移动，直到 left 抵达 m，head 抵达 n
        if (n == 0) return;  // 此时右指针抵达 n+1（该处的节点不需要交换值，因此递归到底，开始返回上层）
        if (m == 1) left = head;

        recurseAndReverse(head.next, m - 1, n - 1);

        // 回到上一层递归之后：判断两个指针是否已撞上，若没有则交换节点值，并让两个指针互相接近一步
        if (left == head || left == head.next)  // 对于只有两个节点的链表（test case 2）需要添加 left == head.next 判断（∵ 两个指针互相接近一步相当于互相调换位置，此时 left 在 head 右侧）
            stop = true;
        if (!stop) {
            int value = left.val;
            left.val = head.val;
            head.val = value;
            left = left.next;  // 不需要手动管理右指针，其向左移动到上一节点是由递归返回上层时实现的
        }
    }

    /*
     * 解法2：反向节点间的链接
     * - 思路：采用类似 L206_ReverseLinkedList 解法1的思路 —— 不交换节点，而是反向节点之间的链接。具体来说：
     *     1. 先将 [m, n] 范围内的节点链接反向；
     *     2. 再 fix 反向后的第 m、m-1、n、n+1 号节点间的链接。
     *   例如对于 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7, m=3, n=5 来说：
     *     1. 先将 [3, 5] 之间的节点反向：1 -> 2 -> 3 <- 4 <- 5   6 -> 7；
     *     2. 再 fix 第2、3、5、6号节点之间的链接：将5链到2上、将6链到3上。
     * - 实现：要进行上面第2步 fix 的话需先获得这4个节点的引用 ∴ 在遍历和反向的过程中要能记录到这4个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        ListNode conn = head, rTail = head;  // conn 用于记录 m-1 号节点的引用，rTail 记录 m 号节点的引用（也就是 reverse 之后的尾节点）

        for (int i = 1; i <= n; i++) {       // ∵ m、n 从1开始 ∴ 这里从1开始遍历
            if (i == m) {                    // 遍历到第 m 号节点时记录第 m-1、m 号节点
                conn = prev;
                rTail = curr;
            }
            if (i <= m || i > n) {           // 注意 i == m 时也要移动 prev、curr
                prev = curr;
                curr = curr.next;
            } else {                         // 在 (m,n] 范围内开始反向链表（注意左边是开区间 ∵ 也要让 prev 进入区间才能开始反向）
                ListNode next = curr.next;
                curr.next = prev;            // 后一个节点的 next 指向前一个节点
                prev = curr;
                curr = next;
            }
        }                                    // 遍历结束时 prev 停在第 n 号节点上，curr 停在 n+1 号节点上
        if (conn != null) conn.next = prev;  // 步骤2：将现在第 n 号节点链回原来的第 m-1 号节点上
        else head = prev;                    // test case 2、3中 m=1 ∴ conn 是 null，需要特殊处理，此时第 n 号节点就是链表的新 head
        rTail.next = curr;                   // 将 n-1 处的节点链到 m 处节点上
        return head;
    }

    /*
     * 解法3：反向节点间的链接
     * - 思路：与解法2一致。
     * - 实现：上稍有不同
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween3(ListNode head, int m, int n) {
        if (head == null) return null;

        ListNode prev = null, curr = head;
        while (m > 1) {  // 先让 prev、curr 分别移动到 m-1、m 位置
            prev = curr;
            curr = curr.next;
            m--;
            n--;
        }

        ListNode conn = prev, tail = curr;
        while (n > 0) {  // 开始将有效范围内的节点反向
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
            n--;
        }

        if (conn != null) conn.next = prev;  // 与解法2一样
        else head = prev;
        tail.next = curr;
        return head;
    }

    /*
     * 解法4：反向节点间的链接（递归）
     * - 思路：与解法2、3一致。
     * - 实现：先走到第 m 个节点，然后对 m ~ n 内的节点进行反向，反向过程采用类似 L206_ReverseLinkedList 解法2的递归实现，
     *   不同点在于返回的是两个节点：1. reverse 之后的新头节点； 2. 第 n+1 个节点（即后面不需要反向的第一个节点）。有了这两个
     *   节点就能拼接出最终要返回的链表：
     *     1. 第 m-1 个节点 -> reverse 后的新头节点；
     *     2. reverse 后的第 n 个节点（即原第 m 个节点）-> 第 n+1 个节点。
     *   最终返回的链表：
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7, m=3, n=5
     *         m-1   m         n   n+1
     *        prev  curr     rHead rest
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween4(ListNode head, int m, int n) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode prev = dummyHead, curr = head;
        for (int i = 1; i < m; i++) {  // 先让 prev 走到第 m-1 个节点上，curr 走到第 m 个节点上
            prev = curr;
            curr = curr.next;
        }

        ListNode[] reversed = reverseBeforeN(curr, m, n);
        ListNode rHead = reversed[0];  // reverse 后的新头节点
        ListNode rest = reversed[1];   // 第 n+1 个节点（即后面不需要反向的第一个节点）
        prev.next = rHead;             // 第 m-1 个节点 -> reverse 后的新头节点
        curr.next = rest;              // reverse 后的第 n 个节点 -> 第 n+1 个节点

        return dummyHead.next;
    }

    private static ListNode[] reverseBeforeN(ListNode head, int i, int n) {
        if (i == n) return new ListNode[]{head, head.next};  // 走到第 n 个节点时递归到底，返回第 n 个、第 n+1 个节点
        ListNode[] reversed = reverseBeforeN(head.next, ++i, n);
        head.next.next = head;
        head.next = null;
        return reversed;
    }

	public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7});
        printLinkedList(reverseBetween2(l1, 3, 5));  // expects 1->2->5->4->3->6->7->NULL

        ListNode l2 = createLinkedList(new int[]{3, 5});
        printLinkedList(reverseBetween2(l2, 1, 2));  // expects 5->3->NULL

        ListNode l3 = createLinkedList(new int[]{5});
        printLinkedList(reverseBetween2(l3, 1, 1));  // expects 5->NULL
    }
}
