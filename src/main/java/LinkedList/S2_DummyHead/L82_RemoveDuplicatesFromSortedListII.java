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
     * - 思路：∵ 原节点也要删除 ∴ 像 L83 中那样只使用一个指针、在发现原节点后存在重复节点就跳过的方法是不行的。需要一个指针指向原节点
     *   的上一个节点，另一个指针向后移动直到找到最后一个重复节点的下一个节点，这样才能将需要删除的节点跳过。
     * - 实现：对于 2->3->3->4 来说，若想将去除 3->3，则需要链接2节点和4节点，因此需要先获取这两个节点：
     *        D -> 2 -> 3 -> 3 -> 4
     *        a    b                 让 a 初始指向虚拟头结点，b 指向 head。此时 b.val != b.next.val，则 a, b 向后移动
     *        D -> 2 -> 3 -> 3 -> 4
     *             a    b            此时 b.val == b.next.val，则 a 不动，b 向后移动，标志位置为 true
     *        D -> 2 -> 3 -> 3 -> 4
     *             a         b       此时 b.val != b.next.val，但因为标志位置为 true，所以 a 不动，b 向后移动，标志位置为 false，再让 a.next 指向 b
     *        D -> 2 -> 4
     *             a    b            此时 b.next == null，结束遍历
     * - 注意：特殊情况的处理：D -> 2 -> 1 -> 1，此时重复节点后面没有更多节点，因此 a 停在 2 上，b 停在最后一个1上，无法按上面的过
     *   程完成删除（即循环已结束，但标志位还是 true），因此可直接将 a.next 指向 null 即可。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static ListNode deleteDuplicates2(ListNode head) {
        if (head == null) return null;  // 注意这个 case 得特殊处理
        ListNode dummyHead = new ListNode();
        dummyHead.next = head;

        ListNode conn = dummyHead, curr = head;
        boolean foundDuplicates = false;

        while (curr.next != null) {
            if (!foundDuplicates && curr.val != curr.next.val) {
                conn = conn.next;
                curr = curr.next;
            }
            else if (curr.val == curr.next.val) {
                foundDuplicates = true;
                curr = curr.next;
            }
            else {  // 若 foundDuplicates && curr.val != curr.next.val（即"实现"中第3步的情况）
                foundDuplicates = false;
                curr = curr.next;
                conn.next = curr;
            }
        }

        if (foundDuplicates)  // "注意"中的特殊情况
            conn.next = null;

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
        printLinkedList(deleteDuplicates(l1));  // expects 1->2->5->NULL

        ListNode l2 = createLinkedList(new int[]{1, 1, 1, 2, 3});
        printLinkedList(deleteDuplicates(l2));  // expects 2->3->NULL

        ListNode l3 = createLinkedList(new int[]{1, 1});
        printLinkedList(deleteDuplicates(l3));  // expects NULL

        ListNode l4 = createLinkedList(new int[]{});
        printLinkedList(deleteDuplicates(l4));  // expects NULL
    }
}
