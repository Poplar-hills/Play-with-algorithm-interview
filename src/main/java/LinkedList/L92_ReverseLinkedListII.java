package LinkedList;

import static Utils.Helpers.*;

/*
* Reverse Linked List II
*
* - Reverse a linked list from position m to n. Note:
*   - 1 ≤ m ≤ n ≤ length of list（m 和 n 是从1开始的）
*   - Do it in one-pass（要求在一次遍历内完成）
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
            right = right.next;
            if (i < m) left = left.next;
        }

        while (left != right && right.next != left) {  // 开始指针对撞
            int temp = left.val;
            left.val = right.val;
            right.val = temp;

            left = left.next;
//            right = right.prev;  // 若是双向链表，节点上有 prev 属性，能回到上一个节点，则此解法就可以工作了
        }

        return head;
    }

    /*
    * 解法1：递归指针对撞 + 交换节点值
    * - 思路：类似将一个数组倒序的思路 —— 先将两个指针移动到数组 m, n 的位置上，再在他们互相逼近的过程中不断 swap 节点里的值。但是因
    *   为单向链表没有从后一个节点指向前一个节点的指针，因此若要让右指针左移到上一个节点就需要借助递归来实现，因为在每层递归结束回到上
    *   一层调用栈时可以获得上一个节点。（过程可视化 SEE: https://leetcode.com/problems/reverse-linked-list-ii/solution/）
    * - 时间复杂度 O(n)：遍历节点两遍
    * - 空间复杂度 O(n)：递归深度最大是链表元素个数
    * */
    public static class solution1 {
        private ListNode left;
        private boolean stop;

        public ListNode reverseBetween(ListNode head, int m, int n) {
            this.left = head;
            this.stop = false;
            recurseAndReverse(head, m, n);
            return head;
        }

        private void recurseAndReverse(ListNode right, int m, int n) {
            // 进入下一层递归之前：让两个指针向右移动，直到 left 抵达 m，right 抵达 n
            if (n == 1) return;  // 此时右指针已抵达 n，停止右移，开始左移（即递归到底，开始返回上层）
            if (m > 1) this.left = this.left.next;
            right = right.next;  // 只要没递归到底，每层都让右指针右移，这样后面返回上层递归时就能取到右移之前的节点（即上一个节点）

            recurseAndReverse(right, m - 1, n - 1);

            // 归回上一层递归之后：判断两个指针是否已撞上，若没有则交换节点值，并让两个指针互相接近一步
            if (this.left == right || right.next == this.left)
                this.stop = true;
            if (!this.stop) {
                swapNodeValue(this.left, right);
                this.left = this.left.next;  // 不需要手动管理右指针，其向左移动到上一节点是由递归返回上层时实现的
            }
        }

        private void swapNodeValue(ListNode left, ListNode right) {
            int temp = left.val;
            this.left.val = right.val;
            right.val = temp;
        }
    }

    /*
    * 解法2：
    * -
    * */
    public static ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null || head.next == null || m >= n) return head;
        ListNode prev = null, curr = head;
        ListNode conn = head, tail = head;
        for (int i = 1; i <= n; i++) {
            if (i == m) {
                conn = prev;
                tail = curr;
            }
            if (i >= m + 1 && i <= n) {
                ListNode third = curr.next;
                curr.next = prev;
                prev = curr;
                curr = third;
            } else {
                prev = curr;
                curr = curr.next;
            }
        }
        conn.next = prev;
        tail.next = curr;
        return head;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        head.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        printLinkedList(head);  // 1->2->3->4->5->NULL

//        ListNode reversed = new solution1().reverseBetween(head, 2, 4);
//        printLinkedList(reversed);  // 1->4->3->2->5->NULL

        printLinkedList(reverseBetween2(head, 2, 4));

        ListNode head2 = new ListNode(5);

    }
}
