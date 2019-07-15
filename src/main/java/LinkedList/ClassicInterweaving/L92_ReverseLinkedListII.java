package LinkedList.ClassicInterweaving;

import static Utils.Helpers.*;

/*
* Reverse Linked List II
*
* - Reverse a linked list from position m to n. Note:
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
    * - 时间复杂度 O(n)，遍历节点两遍；空间复杂度 O(n)，递归深度最大是链表元素个数。
    * */
    public static class Solution1 {
        private ListNode left;
        private boolean stop;

        public ListNode reverseBetween(ListNode head, int m, int n) {
            left = null;
            stop = false;
            recurseAndReverse(head, m, n);
            return head;
        }

        private void recurseAndReverse(ListNode head, int m, int n) {
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
    }

    /*
    * 解法2：不交换节点中的值，而是改变节点间的链接
    * - 思路：改变节点间的链接并不意味着要交换两个节点，而是可以：
    *   1. 先将 [m, n] 范围内的节点之间的链接反向；
    *   2. 再 fix 反向后的节点与 m 之前、n 之后的节点的链接。
    *   具体来说，例如 7 -> 9 -> 2 -> 10 -> 1 -> 8 -> 6, m=2, n=5，则：
    *   1. 先将9和8之间的链接反向：7 -> 9 <-> 2 <- 10 <- 1 <- 8   6，注意：
    *     - 因为 8.next 指向了1，因此8于6之间的链接断开了。
    *     - 对两个节点 a -> b 间的链接进行反向会断开 b 与后面节点间的链接，但因为9是范围内第一个节点，因此9指向2的链接没有断开。
    *   2. 再 fix 反向后的节点与前后节点的链接：把8链接到7后面、把6链接到9后面。
    *   - 因为反向两个节点之间的链接这个过程只需这两个节点参与，因此程序的大体结构是在 for 中不断获取前后两个节点，对他们进行反向或不反向。
    *   - 在进行步骤2时，需要能够在遍历结束后还能获取到 m-1 节点、m 节点、n 节点、n+1 节点，因此需要定义变量维护他们的引用。
    *   - 注意特殊情况的处理：test case 2、3。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null) return null;
        ListNode prev = null, curr = head;
        ListNode conn = head, tail = head;  // 用于保存 m-1 节点、m 节点的引用

        for (int i = 1; i <= n; i++) {
            if (i == m) {
                conn = prev;
                tail = curr;
            }
            if (i >= m + 1 && i <= n) {      // 在有效范围内对前后两个节点间的链接进行反向
                ListNode third = curr.next;  // 先保存再后面一个节点的引用
                curr.next = prev;
                prev = curr;
                curr = third;
            } else {                         // 在有效范围外只移动指针
                prev = curr;
                curr = curr.next;
            }
        }
        if (conn != null) conn.next = prev;  // 进行上面说的步骤2（在 test case2中 conn 会是 null，需要特殊处理）
        else head = prev;
        tail.next = curr;
        return head;
    }

    /*
    * 解法3：与解法2思路完全一致，实现上稍有不同
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
        while (n > 0) {  // 再开始反向节点间的链接
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
            n--;
        }
        if (conn != null) conn.next = prev;
        else head = prev;
        tail.next = curr;
        return head;
    }

    /*
    * 解法4：
    * - 思路：先找到并标记需要反向的部分，进行反向后将结果链回原链表中。
    * - 时间复杂度 O(n)，空间复杂度 O(1)。
    * */
    public static ListNode reverseBetween4(ListNode head, int m, int n) {
        if (head == null || m == n) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode curr = head;
        ListNode prev = null, start = null, tail = null;
        if (m == 1) {          // 从第1个元素开始就要反向属于特例，单独处理
            prev = dummyHead;
            start = head;
        }
        for (int i = 1; i <= n; i++) {  // 只要遍历到第 n 个元素即可，后面的无需处理
            if (i == m - 1) {  // 若到达要反向的首节点的前一个节点，标记 prev, start
                prev = curr;
                start = curr.next;
            }
            if (i == n) {      // 若到达要反向的末节点，标记 tail，并断开与后面节点的链接（∵ 反向过程需要知道结尾在哪）
                tail = curr.next;
                curr.next = null;
            }
            curr = curr.next;
        }
        prev.next = reverseList(start);  // 进行反向，并将反向后的结果链回原链表中
        start.next = tail;
        return dummyHead.next;
    }

    private static ListNode reverseList(ListNode list) {
        ListNode prev = null, curr = list;
        while (curr != null) {
            ListNode third = curr.next;
            curr.next = prev;
            prev = curr;
            curr = third;
        }
        return prev;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        printLinkedList(reverseBetween2(l1, 2, 4));  // expects 1->4->3->2->5->NULL

        ListNode l2 = createLinkedListFromArray(new int[]{3, 5});
        printLinkedList(reverseBetween2(l2, 1, 2));  // expects 5->3->NULL

        ListNode l3 = createLinkedListFromArray(new int[]{5});
        printLinkedList(reverseBetween2(l3, 1, 1));  // expects 5->NULL

        // 解法1是一个类，因此测试方式与其他解法不同
        ListNode l4 = createLinkedListFromArray(new int[]{1, 2, 3, 4, 5});
        ListNode l5 = createLinkedListFromArray(new int[]{3, 5});
        ListNode l6 = createLinkedListFromArray(new int[]{5});
        Solution1 s1 = new Solution1();
//        printLinkedList(s1.reverseBetween(l4, 2, 4));
        printLinkedList(s1.reverseBetween(l5, 1, 2));
        printLinkedList(s1.reverseBetween(l6, 1, 1));
    }
}
