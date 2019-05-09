package LinkedList;

import static Utils.Helpers.*;

/*
* Reverse Linked List II
*
* - Reverse a linked list from position m to n. Do it in one-pass. Note 1 ≤ m ≤ n ≤ length of list.
*   注意 m 和 n 是从1开始的。
* */

public class L92_ReverseLinkedListII {
    /*
    * 错误解：大体思路对，但是最后因为无法从下一个节点回到上一个节点而最终失败。
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
    * 解法1：指针对撞 + 交换节点值
    * - 思路：类似将一个数组倒序的思路 —— 将两个指针移动到数组 [m, n] 区间的头尾，在让他们互相逼近的过程中不断 swap。但是
    *   因为单向链表没有从后一个节点回到前一个节点的指针，因此要让右侧指针往数组头部移动就需要借助递归来实现 —— 在回到上一层调
    *   用栈时即可获得上一个节点。（过程可视化 SEE: https://leetcode.com/problems/reverse-linked-list-ii/solution/）
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
            if (n == 1) return;
            if (m > 1) this.left = this.left.next;
            right = right.next;

            recurseAndReverse(right, m - 1, n - 1);

            if (this.left == right || right.next == this.left)
                this.stop = true;
            if (!this.stop) {
                swapNodeValue(this.left, right);
                this.left = this.left.next;
            }
        }

        private void swapNodeValue(ListNode left, ListNode right) {
            int temp = left.val;
            this.left.val = right.val;
            right.val = temp;
        }
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

        ListNode reversed = new solution1().reverseBetween(head, 2, 4);
        printLinkedList(reversed);  // 1->4->3->2->5->NULL

    }
}
