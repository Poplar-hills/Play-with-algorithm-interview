package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Reverse Nodes in k-Group
 *
 * - 给定一个链表，每 k 个节点为一组，反转每一组节点，返回修改后的链表。
 * */

public class L25_ReverseNodesInKGroup {
    /*
     * 解法1：
     * - 思路：从左到右按组进行反转，反转前先检查节点是否充足。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode prev = null, curr = head, temp = head;

        for (int i = 0; i < k; i++, temp = temp.next)  // 检查当前组中是否有足够的节点
            if (temp == null)
                return head;

        for (int i = 0; i < k; i++) {  // 反向当前组中的节点
            temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }

        head.next = reverseKGroup(curr, k);  // 反转完成后 head 变成了当前组的尾节点，将下一组反转后连接到这个尾节点上
        return prev;                         // 反转完成后 prev 变成了当前组的头节点，因此返回到上一层连接到上一层的尾节点上
    }

    /*
     * 解法2：
     * - 思路：先走到最右端，然后从右到左按组反转。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup2(ListNode head, int k) {
        ListNode curr = head;
        int count = 0;

        while (curr != null && count < k) {  // 先让 curr 走到下一组的第一个节点
            curr = curr.next;
            count++;
        }
        if (count != k) return head;         // 如果走不到说明本组节点不足

        curr = reverseKGroup2(curr, k);      // 反转下一组节点
        while (count-- > 0) {                // 反转本组节点
            ListNode temp = head.next;
            head.next = curr;
            curr = head;
            head = temp;
        }
        return curr;  // 一组反转完成后 curr 会指向本组头结点，因此返回 curr 即返回了反转后的头结点
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup2(l1, 2));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup2(l2, 3));  // expects 3->2->1->4->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5});
        printLinkedList(reverseKGroup2(l3, 3));  // expects 3->2->1->4->5->NULL
    }
}
