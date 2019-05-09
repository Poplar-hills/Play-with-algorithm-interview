package LinkedList;

import static Utils.Helpers.*;

/*
* Reverse Linked List II
*
* - Reverse a linked list from position m to n. Do it in one-pass. Note 1 ≤ m ≤ n ≤ length of list.
* */

public class L92_ReverseLinkedListII {
    /*
    * 解法1：双指针节点交换
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
                swapNode(this.left, right);
                this.left = this.left.next;
            }
        }

        private void swapNode(ListNode left, ListNode right) {
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
