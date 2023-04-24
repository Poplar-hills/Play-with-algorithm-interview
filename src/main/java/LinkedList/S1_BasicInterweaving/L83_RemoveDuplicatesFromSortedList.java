package LinkedList.S1_BasicInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Remove Duplicates from Sorted List
 *
 * - 为有序链表去重。
 * */

public class L83_RemoveDuplicatesFromSortedList {
    /*
     * 解法1：迭代
     * - 思路：当发现下一个节点与当前节点重复时，跳过下一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val)
                curr.next = curr.next.next;  // 跳过 curr.next 节点后无需再移动 curr（test case 2）
            else
                curr = curr.next;            // 若没有跳过节点则需要移动 curr
        }
        return head;
    }

    /**
     * 错误解：递归
     * - 思路：与解法1一致，在递归去程路上，当发现下一个节点与当前节点重复时，跳过下一个节点。
     * - 问题：∵ 是"跳过下一个节点" ∴ 第一个节点总会被保留下来。而当最后一个节点与第一个节点重复时，解就会有问题了。
     * - 例：1 -> 1 -> 1 -> NULL
     *      h             - h.val == h.next.val ∴ skip next, 1->f(1->NULL)
     *                h   - h.next == null ∴ return h, 1->1->NULL
     */
    private static ListNode deleteDuplicates0(ListNode head) {
        if (head == null || head.next == null) return head;
        head.next = (head.val == head.next.val)
                ? deleteDuplicates0(head.next.next)
                : deleteDuplicates0(head.next);
        return head;
    }

    /**
     * - 解法2：递归
     * - 思路：在👆🏻错误解的基础上调整思路：在递归去程路上，当发现下一个节点与当前节点重复时，跳过当前节点（而非下一个节点）。
     * - 例：1 -> 1 -> 1 -> NULL
     *      h             - h.val == h.next.val ∴ skip curr, return f(1->1->NULL)
     *           h        - h.val == h.next.val ∴ skip curr, return f(1->NULL)
     *                h   - head.next == null ∴ return 1->NULL
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     */
    private static ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) return head;
        if (head.val == head.next.val) {
            head = head.next;
            return deleteDuplicates2(head);
        } else {
            head.next = deleteDuplicates2(head.next);
            return head;
        }
    }

    /*
     * 解法3：递归（解法2的另一版本）
     * - 思路：与解法1一致，在递归去程路上，若发现下一个节点与当前节点重复时，跳过下一个节点。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates3(ListNode head) {
        helper3(head);
        return head;  // ∵ 在递归去程路上跳过下一个节点就无法返回链表头结点了 ∴ 递归返回类型为 void，并需要在外部单独返回头结点
    }
    
    public static void helper3(ListNode head) {
        if (head == null || head.next == null) return;
        if (head.val == head.next.val) {
            head.next = head.next.next;
            helper3(head);
        } else {
            helper3(head.next);
        }
    }

    /*
     * 解法4：递归
     * - 思路：不同于以上解法，该解法是在递归回程路上，若发现下一个节点与当前节点重复时，跳过当前节点。
     * - 例：0 -> 1 -> 1 -> 1 -> 2 -> 3 -> 3 -> NULL
     *                     ...              - 先一直递归到底
     *                                    h   - return 3->NULL
     *                               h    - h.val == h.next.val ∴ skip curr, return 3->NULL
     *                          h    - h.val != h.next.val ∴ return 2->3->NULL
     *                     h    - h.val != h.next.val ∴ return 1->2->3->NULL
     *                h    - h.val == h.next.val ∴ skip curr, return 1->2->3->NULL
     *           h    - h.val == h.next.val ∴ skip curr, return 1->2->3->NULL
     *      h    - h.val != h.next.val ∴ return 0->1->2->3->NULL
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates4(ListNode head) {
        if (head == null || head.next == null) return head;
        head.next = deleteDuplicates4(head.next);             // 先递归到底
        return head.val == head.next.val ? head.next : head;  // 回程路上跳过/不跳过当前节点
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 1, 2});
        log(deleteDuplicates2(l1));  // expects 1->2->NULL

        ListNode l2 = createLinkedList(new int[]{0, 1, 1, 1, 2, 3, 3});
        log(deleteDuplicates2(l2));  // expects 0->1->2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1, 1});
        log(deleteDuplicates2(l3));  // expects 1->NULL
    }
}
