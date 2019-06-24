package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-04-08
 */
public class RemoveNthFromEnd {

    public ListNode removeNthFromEnd(ListNode head, int n){
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode first = dummy, second = dummy;

        for(int i = 0; i <= n; i++){
            first = first.next;
        }

        while (first != null){
            first = first.next;
            second = second.next;
        }

        second.next = second.next.next;

        return dummy.next;
    }

}
