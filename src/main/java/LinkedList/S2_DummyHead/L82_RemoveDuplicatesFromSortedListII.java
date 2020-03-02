package LinkedList.S2_DummyHead;

import Utils.Helpers.ListNode;

import static Utils.Helpers.*;

import java.util.HashMap;
import java.util.Map;

/*
 * Remove Duplicates from Sorted List II
 *
 * - 删除一个有序链表中所有的重复节点。
 * - 说明：不同于 L83_RemoveDuplicatesFromSortedList 中删除原节点后面的所有重复节点，该题要求删除所有具有相同值的节点。
 *   例如对于 1->2->2->2->3 来说，三个值为2的节点都要删除。
 * */

public class L82_RemoveDuplicatesFromSortedListII {
    /*
     * 解法1：Map 计数
     * - 思路：使用 HashMap 记录链表中节点值的频次，然后再次遍历，将频次 >1 的节点移除。
     * - 实现：需要遍历链表两遍，效率较低。
     * - 时间复杂度 O(2n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates(ListNode head) {
        Map<Integer, Integer> freq = new HashMap<>();

        ListNode curr = head;
        while (curr != null) {
            freq.merge(curr.val, 1, Integer::sum);
            curr = curr.next;
        }

        ListNode dummyHead = new ListNode();  // ∵ 链表的头节点可能就是需要移除的重复节点 ∴ 要使用虚拟头结点
        dummyHead.next = head;
        ListNode prev = dummyHead;
        curr = head;
        while (curr != null) {
            if (freq.get(curr.val) > 1)
                prev.next = curr.next;        // 将重复节点移除后 prev 不动，只更新 curr 的指向即可
            else
                prev = curr;
            curr = prev.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法2：双指针 + 标志位
     * - 思路：∵ 原节点也要删除 ∴ 像 L83 中那样只使用一个指针、在发现原节点后跳过后面的重复节点的方法是不行的。需要一个指针指向
     *   原节点的上一个节点，另一个指针向后移动直到最后一个重复节点的下一个节点，最后连接这两个节点完成对中间节点的删除。
     * - 演示：例如对于 2->3->3->4->5 来说，若想将去除 3->3，则需要链接2和4 ∴ 需要先获取这两个节点：
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *      p    c                      - p 初始指向虚拟头结点，c 指向 head。此时 c 与下个节点不重复 ∴ 让 p, c 后移
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *           p    c                 - 此时 c 与下个节点重复 ∴ 只让 c 后移
     *      D -> 2 -> 3 -> 3 -> 4 -> 5
     *           p         c            - 此时 c 与下个节点不重复，但 ∵ c 也是需要删除的节点 ∴ 需要让 p.next = c.next，再让 c 后移
     *      D -> 2 -> 4 -> 5
     *           p    c                 - 此时 c 与下个节点不重复 ∴ 同时让 p、c 后移
     *      D -> 2 -> 4 -> 5
     *                p    c            - 此时 c.next == null，遍历结束
     * - 实现：1. ∵ 链表中可能存在多个重复节点 ∴ 可在第2步中使用 while 一直走到最后一个重复节点；
     *        2. 在第3步中，p.next = c.next 这一操作只能对最后一个重复节点进行（在第4步中就不能有）∴ 需要一个标志位标识在
     *           某一时刻 c 是否指向重复节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates2(ListNode head) {
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;
        ListNode prev = dummyHead, curr = head;
        boolean foundDuplicates = false;

        while (curr != null && curr.next != null) {
            while (curr.next != null && curr.val == curr.next.val) {
                foundDuplicates = true;
                curr = curr.next;
            }
            if (foundDuplicates) {
                prev.next = curr.next;
                foundDuplicates = false;
            } else {
                prev = curr;
            }
            curr = curr.next;
        }

        return dummyHead.next;
    }

    /*
     * 解法3：解法2的递归版
     * - 思路：关键在于返回上一层时需要让上一层知道是否已经出现了重复节点，从而删除其中一个，这就需要在 return 时加入标志位。
     *        D -> 2 -> 3 -> 3 -> 4                      D -> 1 -> 1 -> 1 -> 2
     *                         ← 返回 (4, false)                           ← 返回 (2, false)
     *                    ← 返回 (3->4, false)                        ← 返回 (1->2, false)
     *               ← 返回 (3->4，true)                         ← 返回 (1->2, true)
     *          ← 返回 (2->4，false)                        ← 返回 (1->2, true)
     *                                                   到达最上层时判断若标志位仍然为 true 则取第二个节点之后的链表，即只有2
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates3(ListNode head) {
        if (head == null) return null;
        Pair<ListNode, Boolean> pair = deleteHeadDuplicates(head);
        ListNode r = pair.getKey();
        return pair.getValue() ? r.next : r;
    }

    private static Pair<ListNode, Boolean> deleteHeadDuplicates(ListNode head) {
        if (head.next == null)
            return new Pair<>(head, false);

        Pair<ListNode, Boolean> pair = deleteHeadDuplicates(head.next);
        ListNode next = pair.getKey();
        boolean foundDuplicate = pair.getValue();

        if (head.val == next.val)            // 前后节点相同
            return new Pair<>(next, true);
        if (foundDuplicate) {                // 前后节点不同，但标志位为 true
            head.next = next.next;
            return new Pair<>(head, false);
        }
        head.next = next;                    // 前后节点不同，标志位为 false
        return new Pair<>(head, false);
    }

    /*
     * 解法4：双指针 + 内部 while 循环直到找到不重复节点
     * - 思路：与解法1的相同，都需要两个指针（conn, curr），分别指向重复节点之前和之后的节点；与解法1的不同的是不需要标志位，
     *   而是当发现有重复节点后通过 while 循环一直往下找，直到找到不重复节点为止，再链接 conn 与不重复节点。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates4(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode conn = dummyHead, curr = head;
        int duplicateVal;  // 记录发现的重复节点的节点值

        while (curr != null) {
            if (curr.next == null || curr.val != curr.next.val) {  // 当前后节点不重复时
                conn = curr;
                curr = curr.next;
            } else {                                               // 当前后节点重复时
                duplicateVal = curr.val;
                while (curr.next != null && curr.next.val == duplicateVal)  // 内部 while 循环
                    curr = curr.next;
                conn.next = curr = curr.next;                      // 将 curr.next 链接到 conn 上
            }
        }

        return dummyHead.next;
    }

    /*
     * 解法5：解法4的递归版
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static ListNode deleteDuplicates5(ListNode head) {
        if (head == null || head.next == null) return head;
        if (head.val == head.next.val) {
            int duplicateVal = head.val;
            while (head.next != null && head.next.val == duplicateVal)  // 通过 while 循环跳过 val 相同的节点
                head = head.next;
            return deleteDuplicates4(head.next);
        } else {
            head.next = deleteDuplicates4(head.next);
            return head;
        }
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedList(new int[]{1, 2, 3, 3, 4, 4, 5});
        printLinkedList(deleteDuplicates0(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 1, 1, 2, 3});
        printLinkedList(deleteDuplicates0(l2));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1});
        printLinkedList(deleteDuplicates0(l3));  // expects NULL

        ListNode l4 = createLinkedList(new int[]{});
        printLinkedList(deleteDuplicates0(l4));  // expects NULL
    }
}
