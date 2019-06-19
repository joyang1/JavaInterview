package cn.tommyyang.listnode;

/**
 * @Author : TommyYang
 * @Time : 2019-06-19 18:29
 * @Software: IntelliJ IDEA
 * @File : ReverseLinkedList.java
 */
public class ReverseLinkedList {

    public ListNode reverse (ListNode head) {

        ListNode cur = head;
        ListNode next;
        ListNode left = null;

        while (cur != null) {
            next = cur.next;
            cur.next = left;
            left = cur;
            cur = next;
        }

        return left;

    }

}
