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
     * - 思路：采用类似 L206_ReverseLinkedList 解法2的思路 —— 不交换节点，而是反向节点之间的链接。具体来说：
     *     1. 先将 [m, n] 范围内的节点链接反向；
     *     2. 再 fix 反向后的第 m、m-1、n、n+1 号节点间的链接。
     * - 实现：要进行上面第2步 fix 的话需先获得这4个节点的引用 ∴ 在遍历和反向的过程中要能记录到这4个节点。
     *         m-1   m         n   n+1
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr                     - 遍历到 m-1、m 号节点时用 prevBody、prevTail 记录索引
     *     1 -> 2 <- 3 <- 4 <- 5    6 -> 7
     *        pBody pTail     prev  curr      - 反向 [m,n] 之间的链接（实际上反向的是 [m-1,n] 之间的链接，但没关系，
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7     m-1 与 m 之间的链接最后还会被重置）
     *        pBody pTail     prev  curr      - 将5链到2上、将6链到3上。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        ListNode prevBody = head, prevTail = head;  // prevBody 指向第 m-1 个节点，prevTail 指向第 m 个节点（即 reverse 之后的尾节点）

        for (int i = 1; i <= n; i++) {  // ∵ m、n 从1开始 ∴ 这里从1开始遍历
            if (i == m) {               // 遍历到第 m-1、m 号节点时用 prevBody、prevTail 记录索引
                prevBody = prev;
                prevTail = curr;
            }
            if (i < m) {                // 此处也可以是 i <= m，即只反向 [m,n] 内的链接反向
                prev = curr;
                curr = curr.next;
            } else {                    // 反向 [m-1,n] 内的链接
                ListNode next = curr.next;
                curr.next = prev;       // 反向节点间的链接
                prev = curr;
                curr = next;
            }
        }                               // 遍历结束时 prev 停在第 n 号节点上，curr 停在 n+1 号节点上
        if (prevBody != null) prevBody.next = prev;  // 步骤2：将现在第 n 号节点链回原来的第 m-1 号节点上
        else head = prev;               // test case 2、3 中 m=1 ∴ prevBody 是 null，需要特殊处理，此时第 n 号节点就是链表的新 head
        prevTail.next = curr;           // 将 n-1 处的节点链到 m 处节点上
        return head;
    }

    /*
     * 解法2：反向节点间的链接
     * - 思路：与解法1一致。
     * - 实现：与解法1不同之处在于采用两个 while 实现。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null) return null;

        ListNode prev = null, curr = head;
        while (m > 1) {        // 先让 prev、curr 分别移动到 m-1、m 位置
            prev = curr;
            curr = curr.next;
            m--; n--;
        }

        ListNode prevBody = prev, prevTail = curr;
        while (n > 0) {        // 开发反向 [m-1,n] 之间的链接
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
            n--;
        }

        if (prevBody != null) prevBody.next = prev;  // 与解法1一样
        else head = prev;
        prevTail.next = curr;
        return head;
    }

    /*
     * 解法3：反向节点间的链接（解法1、2的递归版，🥇最优解之一）
     * - 思路：
     *   1. 与解法1、2一致，先让两个指针 prev、curr 分别走到第 m-1、m 号节点上；
     *   2. 然后递归地对 [m,n] 内的节点进行反向，并返回反向后新头结点 newBody 及不需要反向的第一个节点 tail；
     *   3. 最终链接相应节点（prev -> newBody、curr -> tail）即可。
     * - 实现：
     *   1. 采用递归反向节点间的链接；
     *   2. 反向的是 [m,n] 之间的链接（这里与解法1、2不同）；
     *   3. 反向后 m-1 与 m 之间的链接会断开（与解法1、2不同）。
     *         m-1   m         n   n+1
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr                     - 先让 prev、curr 分别移动到 m-1、m 上
     *     1 -> 2    3 <- 4 <- 5    6 -> 7
     *        prev  curr     nBody tail      - 递归地将 [m,n] 之间的链接反向
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7
     *        prev  curr     nBody tail      - 再将5链到2上、将6链到3上
     *
     * - 👉 对比：相比解法1、2，该解法更加声明式（declarative）∴ 可读性更强。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseBetween3(ListNode head, int m, int n) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        // 1. 先走到 m 号节点上
        ListNode prev = dummyHead, curr = head;
        for (int i = 1; i < m; i++) {    // 先让 prev、curr 分别移动到 m-1、m 号节点上
            prev = curr;
            curr = curr.next;
        }

        // 2. 反向 [m,n] 间的链接
        ListNode[] reversed = reverseBeforeN(curr, m, n);  // 递归反向 [m,n] 内的链接

        // 3. Fix 与原链表的链接点
        ListNode newBody = reversed[0];  // reverse 后的新头节点
        ListNode tail = reversed[1];     // 不需要反向的第一个节点（即第 n+1 个节点）
        prev.next = newBody;             // 第 m-1 个节点 -> reverse 后的新头节点
        curr.next = tail;                // reverse 后 curr 指向反转后的最后一个节点 ∴ 将 tail 链到其上

        return dummyHead.next;
    }

    private static ListNode[] reverseBeforeN(ListNode head, int i, int n) {
        if (i == n) return new ListNode[]{head, head.next};  // 走到第 n 个节点时递归到底，返回第 n、n+1 个节点
        ListNode[] reversed = reverseBeforeN(head.next, i + 1, n);  // 先递归到底
        head.next.next = head;  // 将 A -> B -> C 改为 A <-> B   C
        head.next = null;       // 将 A <-> B   C 改为 A <- B   C
        return reversed;        // 返回的总是递归到底后返回的节点
    }

    /**
     * 解法4：反向节点间的链接（解法1、2的递归版，🥇最优解之一）
     * - 思路：与解法3一致。
     * - 实现：不用于解法3，该解法预先标记所有需要用到的节点，并在反向之前将需要反向的部分从主链表中分离出来，使得 reverseList
     *   的实现更简单。
     * - 例：
     *              pb   b         pt   t
     *    d -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7,  m=3, n=5
     *    p    c         - m=3,n=5
     *         p    c         - m=2,n=4
     *              p    c         - m=1,n=3, let prevBody=p, body=c
     *                   p    c         - m=0,n=2
     *                        p    c         - m=-1,n=1
     *                             p    c         - m=-2,n=0, let prevTail=p, tail=c
     *
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    public static ListNode reverseBetween4(ListNode head, int m, int n) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode prev = dummyHead, curr = head;
        ListNode prevBody = dummyHead, body = head, prevTail, tail;

        while (n != 0) {
            if (m == 1) {  // 当 curr 走到 m 号节点时，标记 prevBody, body
                prevBody = prev;
                body = curr;
            }
            prev = curr;
            curr = curr.next;
            m--; n--;
        }
        prevTail = prev;  // 当 curr 走到 n+1 号节点时，标记 prevTail, tail
        tail = curr;

        prevBody.next = prevTail.next = null;  // 先将要反向的部分从主链表上分离开，这样 reverseList 的实现会简单一些
        prevBody.next = reverseList(body);
        body.next = tail;   // 反向后 body 即是最后一个节点，把 tail 连上去即可

        return dummyHead.next;
    }

    private static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /*
     * 不成立解：指针对撞 + 交换节点值
     * - 思路：采用指针对撞的方式，在对撞过程中不断交换节点值（而非移动节点）。具体步骤：
     *     1. 先让两个指针 l, r 分别走到 m、n 节点上；
     *     2. 开始对撞 & 交换节点值，直到 l 与 r 撞上时结束。
     * - 演示：
     *     Example 1:                           Example 2:
     *               m         n                     m              n
     *     1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7      1 -> 2 -> 3 -> 4 -> 5 -> 6
     *               l         r                     l              r
     *     1 -> 2 -> 5 -> 4 -> 3 -> 6 -> 7      1 -> 5 -> 3 -> 4 -> 2 -> 6
     *                    lr                              l    r
     *                                          1 -> 5 -> 4 -> 3 -> 2 -> 6
     *                                                    l    r
     * - 问题：大体思路对，但是因为单向链表没有从后一个节点指向前一个节点的指针，所以该解法最终不成立。
     * - 虽然错误，但有助于理解解法1。
     * */
    public ListNode reverseBetween_1(ListNode head, int m, int n) {
        ListNode l = head, r = head;

        for (int i = 1; i < n; i++) {
            if (i < m) l = l.next;
            r = r.next;
        }
        while (l != r && l != r.next) {
            int temp = l.val;
            l.val = r.val;
            r.val = temp;

            l = l.next;
            // r = r.prev;  // r 无法从下一个节点回到上一个节点 ∴ 该解法不成立（若是双向链表，则该解法成立）
        }
        return head;
    }

    /*
     * 解法5：指针对撞 + 交换节点值
     * - 思路：👆解法不成立的原因是单向链表没有从后一个节点指向前一个节点的指针，但若借助递归则可以实现 —— ∵ 每层递归结束时会
     *   回到上一层调用栈，此时即可获得上一个节点（过程可视化 SEE: https://leetcode.com/problems/reverse-linked-list-ii/solution/）
     * - 实现：递归函数的设计要点：
     *     1. ∵ 该递归是交换节点值，并不改变节点之间的链接 ∴ 递归函数无需返回节点（返回 void 即可）；
     *     2. ∵ 交换节点值的范围为 [m,n] ∴ 递归范围应为 [1,n]，当递归到 n+1 时递归到底，开始回程；
     *     3. ∵ 在递归回程中能够从后一个节点回到前一个节点 ∴ 交换节点值的操作应该在这时候（递归回程中）发生；
     *     4. 在递归去程中还需要捕获 l 指针，即当到达到 m 节点时标记它为 l，并且得声明为类成员变量，否则在递归回程过程中访问不到；
     *     5. 当两个指针相撞或错过一步时标志着交换节点值的过程结束，此时需要一个信号量 flag 标记从此之后不再交换节点值。
     *             m              n
     *        1 -> 2 -> 3 -> 4 -> 5 -> 6
     *             l              r         - 在去程中让 l 指向 m 节点；当递归到 n+1 时递归到底，开始回程
     *        1 -> 5 -> 3 -> 4 -> 2 -> 6
     *                  l    r              - 交换节点值2和5，l 右移，r 左移（由递归返回上层实现）
     *        1 -> 5 -> 4 -> 3 -> 2 -> 6
     *                  r    l              - 交换节点值3和4，l 右移，r 左移，之后发现 l == r.next ∴ 交换终止
     * - 💎 技巧：这种通过递归获得上一个节点，从而逆序遍历链表的技巧很经典。
     * - 时间复杂度 O(n)，因为只遍历到 n 处的节点；
     * - 空间复杂度 O(n)，同样因为只遍历到 n 处的节点，因此递归深度为 n。
     * */
    private static ListNode l;   // ∵ 递归过程中不好拿到 l 指针 ∴ 将其作为外部变量
    private static boolean stop;

    public static ListNode reverseBetween5(ListNode head, int m, int n) {
        l = head;
        stop = false;          // 要先 init 静态成员变量，否则 test case 之间会互相影响
        recurseAndReverse(head, m, n);
        return head;
    }

    private static void recurseAndReverse(ListNode head, int m, int n) {  // head 即是右指针 r
        if (n == 0) return;    // 此时右指针（head）抵达 n+1 号节点，递归到底，开始返回上层
        if (m == 1) l = head;  // 去程中路过 m 节点时将 l 指向 m 节点

        recurseAndReverse(head.next, m - 1, n - 1);

        if (l == head || l == head.next)  // 判断两个指针是否已撞上（若 [m,n] 内有奇数个节点则撞上时 l == head，若有偶数
            stop = true;                  // 个则撞上时 l == head.next）。撞上时设置 stop 信号量

        if (!stop) {           // 若没有撞上且处于要交换节点值的范围内，则交换节点值，并让两个指针互相接近一步
            int temp = l.val;
            l.val = head.val;  // 交换节点值
            head.val = temp;
            l = l.next;        // 只需手动管理左指针，而不用管右指针，右指针向左移动到上一节点是由递归返回上层实现的
        }
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7});
        log(reverseBetween(l1, 3, 5));  // expects 1->2->5->4->3->6->7->NULL

        ListNode l2 = createLinkedList(new int[]{3, 5});
        log(reverseBetween(l2, 1, 2));  // expects 5->3->NULL

        ListNode l3 = createLinkedList(new int[]{5});
        log(reverseBetween(l3, 1, 1));  // expects 5->NULL
    }
}
