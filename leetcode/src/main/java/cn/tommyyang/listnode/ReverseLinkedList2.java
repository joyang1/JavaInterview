package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-05-04
 */
//反转从位置 m 到 n 的链表。请使用一趟扫描完成反转。
public class ReverseLinkedList2 {

    public ListNode reverseBetween(ListNode head, int m, int n) {

        ListNode dummy = new ListNode(0);
        dummy.next  = head;

        ListNode index = dummy;
        ListNode left, right, tmp;

        //count 为倒转链表的长度
        int count = n - m + 1;
        //找到倒转链表左结点的前面一个结点
        while (m > 1){
            m--;
            index = index.next;
        }
        left = index;
        index = index.next;
        right = index;
        left.next = null;

        while (count > 0){
            count--;
            tmp = index;
            index = index.next;
            tmp.next = left.next;
            left.next = tmp;
        }

        //连接右半部分
        right.next = index;

        return left == dummy ? left.next : head;
    }

}

