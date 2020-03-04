package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.Stack;

/*
 * Reverse Nodes in k-Group
 *
 * - 给定一个链表，每 k 个节点为一组，反向每一组节点，返回修改后的链表。
 *
 * - Notes:
 *   1. If the number of nodes is not a multiple of k then left-out nodes in the end should remain as it is.
 *   2. Only constant extra memory is allowed.
 * */

public class L25_ReverseNodesInKGroup {
    /*
     * 解法1：递归
     * - 思路：非常 straightforward —— 先检查一组中的节点是否充足，若充足则进行反向，否则直接返回该组头节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode prev = null, curr = head, temp = head;

        for (int i = 0; i < k; i++, temp = temp.next)  // 检查本组中是否有足够的节点
            if (temp == null)
                return head;

        for (int i = 0; i < k; i++) {  // 反向本组中的节点
            temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }

        head.next = reverseKGroup(curr, k);  // 反向完成后 head 变成了本组的尾节点、prev 变成了本组的头节点
        return prev;
    }

    /*
     * 解法2：递归
     * - 思路：与解法1一致，也是先检测每组几点数是否够 k 个，若够则反向，否则直接返回。
     * - 实现：先递归到底，然后在递归回程路对每组内的节点进行反向，即从整个链表上来看是从右到左按组反向的。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup2(ListNode head, int k) {
        ListNode curr = head;
        int count = 0;

        while (curr != null && count < k) {  // 先让 curr 走过本组的节点，停在下一组的第一个节点上
            curr = curr.next;
            count++;
        }
        if (count != k) return head;     // 如果走不到说明本组节点不足，直接返回本组头节点

        curr = reverseKGroup2(curr, k);  // 进入下一层递归

        while (count-- > 0) {            // 递归返回之后再反向本组的节点
            ListNode temp = head.next;
            head.next = curr;
            curr = head;
            head = temp;
        }

        return curr;                     // 反向完成后 curr 会指向本组头结点
    }

    /*
     * 解法3：迭代 + Stack
     * - 思路：解法1、2都是先检查本组节点数是否够 k 个，再觉得是否反向本组。而该解法通过检查 stack.size() 来确定一组节点是否
     *   够数、是否需要反向。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode reverseKGroup3(ListNode head, int k) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;
        Stack<ListNode> stack = new Stack<>();

        while (curr != null) {
            if (stack.size() < k) {         // 不够 k 个之前持续入栈
                stack.push(curr);
                curr = curr.next;
            }
            if (stack.size() == k) {        // 注意这里不能用 else 承接上面的 if 否则 test case 1 的第2组不会被反向
                while (!stack.isEmpty()) {  // 够了 k 个之后开始反向
                    prev.next = stack.pop();
                    prev = prev.next;
                }
                prev.next = curr;           // 反向结束时 prev 指向本组反向后的最后一个节点 ∴ 要让下一组头结点链到它上
            }
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup3(l1, 2));  // expects 2->1->4->3->NULL

        ListNode l2 = createLinkedList(new int[]{1, 2, 3, 4});
        printLinkedList(reverseKGroup3(l2, 3));  // expects 3->2->1->4->NULL

        ListNode l3 = createLinkedList(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        printLinkedList(reverseKGroup3(l3, 3));  // expects 3->2->1->6->5->4->7->8->NULL. (最后一组不够3个 ∴ 应该保持不变)

        ListNode l4 = createLinkedList(new int[]{1, 2, 3});
        printLinkedList(reverseKGroup3(l4, 1));  // expects 1->2->3->NULL
    }
}
