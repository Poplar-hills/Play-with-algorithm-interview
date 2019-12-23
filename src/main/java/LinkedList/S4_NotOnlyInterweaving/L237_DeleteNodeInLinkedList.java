package LinkedList.S4_NotOnlyInterweaving;

import Utils.Helpers.ListNode;

/*
* Delete Node in a Linked List
*
* - Write a function to delete a node
* - 思路：Since we do not have access to the node before the one we want to delete, we cannot modify the next
*   pointer of that node in any way. Instead, we have to replace the value of the node we want to delete with
*   the value in the node after it, and then delete the node after it.
*   例子：要删除 1 -> 2 -> 3 -> 4 -> 5 中的节点3：
*              1 -> 2 -> 4    4 -> 5
*                        |_________↑
* - 意义：本题不是通过操作节点 next 指针解决的，而是改变节点值，这种题比较少见，但也是有的。
* */

public class L237_DeleteNodeInLinkedList {
    public static void deleteNode(ListNode node) {
        if (node == null) return;
        if (node.next == null) node = null;  // 待删除节点是链表最后一个节点
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
