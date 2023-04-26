package LinkedList.S3_AdvancedInterweaving;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

/*
 * Insertion Sort List
 *
 * - Sort a linked list using insertion sort.
 * */

public class L147_InsertionSortList {
    /*
     * 解法1：迭代
     * - 思路：插入排序的核心是当发现后一个元素 < 前一个元素时，将后一个元素往前移动，直到前一个元素不再小于它时停止移动并插入进去。
     * - 实现：
     *   1. 在标准的排序算法中，遍历是从后往前逐个比较的，但 ∵ 链表中无法从下一节点回到上一节点 ∴ 可采用从前往后遍历；
     *   2. ∵ 插入位置可能是头结点 ∴ 需要创建虚拟头结点；
     * - 例：D    4 -> 2 -> 1 -> 3      - 初始化时 D 不与链表连接
     *      p    c                      - p 从 D 开始遍历，停在 D 上 ∴ D 就是插入点，D.next = 4
     *      D -> 4 -> N    2 -> 1 -> 3
     *      p              c            - p 从 D 开始遍历，停在 D 上 ∴ D 就是插入点，D.next = 2
     *      D -> 2 -> 4 -> N    1 -> 3
     *      p                   c       - p 从 D 开始遍历，停在 D 上 ∴ D 就是插入点，D.next = 1
     *      D -> 1 -> 2 -> 4 -> N    3
     *                p              c  - p 从 D 开始遍历，停在 2 上 ∴ 2 就是插入点，2.next = 3
     *      D -> 1 -> 2 -> 3 -> 4
     * - 时间复杂度 O(n^2)，空间复杂度 O(1)。
     * */
    public static ListNode insertionSortList(ListNode head) {
        ListNode dummyHead = new ListNode();
        ListNode curr = head;

        while (curr != null) {
            ListNode pos = dummyHead;  // pos 为插入点的前节点，每次从头开始遍历，让 pos 停在最后一个 < curr 的节点上
            while (pos.next != null && pos.next.val < curr.val)
                pos = pos.next;

            ListNode next = curr.next;  // 将 curr 节点插入到 pos 后面
            ListNode third = pos.next;
            pos.next = curr;
            curr.next = third;

            curr = next;  // curr 指向下一个节点
        }

        return dummyHead.next;
    }

    /*
     * 解法2：递归
     * - 思路：若采用递归实现则思路会有不同：在下层递归返回后，将本层的节点插入返回的链表的合适位置，再返回上层。
     * - 实现：先递归到最深处，在回程时将当前节点放入合适的位置：
     * - 例：-1 -> 5 -> 3 -> 4 -> 0 -> NULL
     *                           h   - return 0->N
     *                      h   - 4 > 0 ∴ insert 4 after 0, return 0->4->N
     *                 h   - 3 > 0 ∴ insert 3 after 0, return 0->3->4->N
     *            h   - 5 > 0 ∴ insert 5 after 4, return 0->3->4->5->N
     *       h   - -1 < 0 ∴ return -1->0->3->4->5->N
     * - 时间复杂度 O(n^2)，空间复杂度 O(n)（递归深度是 logn，但任意时刻都需要存储 n 个元素 ∴ 总体是 O(n)）。
     * */
    public static ListNode insertionSortList2(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode returned = insertionSortList2(head.next);

        if (head.val <= returned.val) {
            head.next = returned;
            return head;
        }

        ListNode pos = returned;  // 插入点的前节点
        while (pos.next != null && head.val > pos.next.val)  // 让 pos 停在插入点的上一个节点
            pos = pos.next;

        head.next = pos.next;  // 将 head 插入到 pos 后面
        pos.next = head;

        return returned;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{4, 2, 1, 3});
        log(insertionSortList(l1));  // expects 1->2->3->4->NULL

        ListNode l2 = createLinkedList(new int[]{-1, 5, 3, 4, 0});
        log(insertionSortList(l2));  // expects -1->0->3->4->5->NULL
    }
}
