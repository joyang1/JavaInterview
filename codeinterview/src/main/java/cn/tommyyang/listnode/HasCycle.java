package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-04-08
 */
public class HasCycle {

    public boolean hasCycle(ListNode head){
        ListNode dummy = new ListNode(0);

        ListNode first = dummy, second = dummy;

        while (second != null && second.next != null){
            first = first.next;
            second = second.next.next;

            if (first == second){
                return true;
            }
        }

        return false;

    }

}
