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
     * 解法1：反向节点间的链接
     * - 思路：采用类似 L206_ReverseLinkedList 解法1的思路 —— 不交换节点，而是反向节点之间的链接。具体来说：
     *     1. 先将 [m, n] 范围内的节点链接反向；
     *     2. 再 fix 反向后的第 m、m-1、n、n+1 号节点间的链接。
     * - 实现：要进行上面第2步 fix 的话需先获得这4个节点的引用 ∴ 在遍历和反向的过程中要能记录到这4个节点。
     * - 演示：
     *         m-1   m         n   n+1
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr                     - 遍历到 m-1、m 号节点时用 conn、rTail 记录索引
     *     1 -> 2 -> 3 <- 4 <- 5    6 -> 7
     *        conn rTail     prev  curr      - 将 [m,n] 之间的链接反向
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7
     *        conn rTail     prev  curr      - 将5链到2上、将6链到3上。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        ListNode conn = head, rTail = head;  // conn 用于记录 m-1 号节点的引用，rTail 记录 m 号节点的引用（也就是 reverse 之后的尾节点）

        for (int i = 1; i <= n; i++) {       // ∵ m、n 从1开始 ∴ 这里从1开始遍历
            if (i == m) {                    // 遍历到第 m-1、m 号节点时用 conn、rTail 记录索引
                conn = prev;
                rTail = curr;
            }
            if (i <= m || i > n) {           // 注意 i == m 时也要移动 prev、curr
                prev = curr;
                curr = curr.next;
            } else {                         // 将 (m,n] 内的链接反向（注意左边是开区间，要让 prev 进入区间后再开始反向）
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
     * 解法2：反向节点间的链接
     * - 思路：与解法1一致。
     * - 实现：与解法1不同之处在于：
     *     1. 采用两个 while 实现；
     *     2. 反向的是 [m-1,n] 之间的链接。
     * - 演示：
     *         m-1   m         n   n+1
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr                     - 先让 prev、curr 分别移动到 m-1、m 上
     *     1 -> 2 <- 3 <- 4 <- 5    6 -> 7
     *        conn rTail     prev  curr      - 将 [m-1,n] 之间的链接反向
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7
     *        conn rTail     prev  curr      - 再将5链到2上、将6链到3上
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null) return null;

        ListNode prev = null, curr = head;
        while (m > 1) {        // 先让 prev、curr 分别移动到 m-1、m 位置
            prev = curr;
            curr = curr.next;
            m--;
            n--;
        }

        ListNode conn = prev, rTail = curr;
        while (n > 0) {        //
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
            n--;
        }

        if (conn != null) conn.next = prev;  // 与解法2一样
        else head = prev;
        rTail.next = curr;
        return head;
    }

    /*
     * 解法3：反向节点间的链接（解法3的递归版）
     * - 思路：与解法1、2一致。
     * - 实现：与解法3不同之处在于：
     *     1. 采用递归反向节点间的链接；
     *     2. 反向的是 [m,n] 之间的链接（这里与解法1相同，与解法2不同）；
     *     3. 反向后 m-1 与 m 之间的链接会断开（与解法1、2不同）。
     * - 演示：
     *         m-1   m         n   n+1
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr                     - 先让 prev、curr 分别移动到 m-1、m 上
     *     1 -> 2    3 <- 4 <- 5    6 -> 7
     *        prev  curr     rHead rest      - 递归地将 [m,n] 之间的链接反向
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr     rHead rest      - 再将5链到2上、将6链到3上
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween3(ListNode head, int m, int n) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode prev = dummyHead, curr = head;
        for (int i = 1; i < m; i++) {  // 先让 prev、curr 分别移动到 m-1、m 号节点上
            prev = curr;
            curr = curr.next;
        }

        ListNode[] reversed = reverseBeforeN(curr, m, n);  // 将 [m,n] 内的链接反向
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

    /*
     * 不成立解：指针对撞 + 交换节点值
     * - 思路：采用指针对撞的方式，在对撞过程中不断交换节点值。具体步骤：
     *     1. 先让两个指针 left, right 分别走到 m、n 节点上；
     *     2. 开始对撞 & 交换节点值。
     * - 演示：
     *               m         n                     m              n
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7      1 -> 2 -> 3 -> 4 -> 5 -> 6
     *             left      right                 left           right
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7      1 -> 2 -> 3 -> 4 -> 5 -> 6
     *                  left                            right left
     *                  right
     * - 问题：大体思路对，但是因为单向链表没有从后一个节点指向前一个节点的指针，所以该解法最终不成立。
     * - 虽然错误，但有助于理解解法1。
     * */
    public ListNode reverseBetween0(ListNode head, int m, int n) {
        ListNode left = head, right = head;

        for (int i = 1; i < n; i++) {
            if (i < m) left = left.next;
            right = right.next;
        }
        while (left != right && left != right.next) {
            int temp = left.val;
            left.val = right.val;
            right.val = temp;

            left = left.next;
            // right = right.prev;  // right 无法从下一个节点回到上一个节点 ∴ 该解法不成立（若是双向链表，则该解法成立）
        }
        return head;
    }

    /*
     * 解法4：指针对撞 + 交换节点值（👆不成立解法的递归版）
     * - 思路：👆解法不成立的原因是单向链表没有从后一个节点指向前一个节点的指针，但若借助递归则可以实现 —— ∵ 每层递归结束时会
     *   回到上一层调用栈，此时即可获得上一个节点（过程可视化 SEE: https://leetcode.com/problems/reverse-linked-list-ii/solution/）
     * - 实现：递归函数的设计要点：
     *     1. ∵ 该递归是交换节点值，并不改变节点之间的链接 ∴ 递归函数无需返回节点（返回 void 即可）；
     *     2. ∵ 交换节点值的范围为 [m,n] ∴ 递归范围应为 [1,n]，当递归到 n+1 时递归到底，开始回程；
     *     3. ∵ 在递归回程中能够从后一个节点回到前一个节点 ∴ 交换节点值的操作应该在这时候（递归回程中）发生；
     *     4. 在递归去程中还需要捕获 left 指针，即当到达到 m 节点时标记它为 left，并且得声明为类成员变量，否则在递归回程过程中访问不到；
     *     5. 当两个指针相撞或错过一步时标志着交换节点值的过程结束，此时需要一个信号量 flag 标记从此之后不再交换节点值。
     * - 💎技巧：这种通过递归获得上一个节点，从而逆序遍历链表的技巧很经典。
     * - 时间复杂度 O(n)，因为只遍历到 n 处的节点；
     * - 空间复杂度 O(n)，同样因为只遍历到 n 处的节点，因此递归深度为 n。
     * */
    private static ListNode left;
    private static boolean stop;

    public static ListNode reverseBetween4(ListNode head, int m, int n) {
        recurseAndReverse(head, m, n);
        return head;
    }

    private static void recurseAndReverse(ListNode head, int m, int n) {
        if (n == 0) return;         // 此时右指针抵达 n+1，递归到底，开始返回上层
        if (m == 1) left = head;    // 去程中将 m 节点标记为 left

        recurseAndReverse(head.next, m - 1, n - 1);

        if (left == head || left == head.next)  // 判断两个指针是否已撞上
            stop = true;                        // 撞上则设置信号量

        if (!stop) {                // 若没有撞上且处于要交换节点值的范围内，则交换节点值，并让两个指针互相接近一步
            int temp = left.val;
            left.val = head.val;    // head 即是 right 指针
            head.val = temp;
            left = left.next;       // 不需要手动管理右指针，其向左移动到上一节点是由递归返回上层实现的
        }
    }

	public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7});
        printLinkedList(reverseBetween4(l1, 3, 5));  // expects 1->2->5->4->3->6->7->NULL

        ListNode l2 = createLinkedList(new int[]{3, 5});
        printLinkedList(reverseBetween4(l2, 1, 2));  // expects 5->3->NULL

        ListNode l3 = createLinkedList(new int[]{5});
        printLinkedList(reverseBetween4(l3, 1, 1));  // expects 5->NULL
    }
}
