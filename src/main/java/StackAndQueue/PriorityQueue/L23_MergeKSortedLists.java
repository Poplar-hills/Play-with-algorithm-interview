package StackAndQueue.PriorityQueue;

import static Utils.Helpers.*;

/*
* Merge k Sorted Lists
*
* - Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
* */

public class L23_MergeKSortedLists {
    public static ListNode mergeKLists(ListNode[] lists) {
        return lists[0];
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 5});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        ListNode l3 = createLinkedListFromArray(new int[]{2, 6});
        mergeKLists((ListNode[]) new Object[]{l1, l2, l3});
    }
}
