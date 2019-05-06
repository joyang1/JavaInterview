package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-05-06
 */
//环形链表2
public class CycleNode2 {

    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null){
            return null;
        }

        ListNode fast = head;
        ListNode slow = head;
        boolean meet = false;

        while (fast != null && fast.next != null){
            fast = meet ? fast.next : fast.next.next;
            slow = slow.next;

            if (fast == slow){
                if (!meet){
                    //说明环相交的地方在头结点，则直接相交的时候，快的结点走了两圈，慢的结点走了一圈
                    if (fast == head){
                        return fast;
                    }
                    // 如果相交的地方不在头节点，通过a+b+b+c=2(a+b)；
                    // 这题一定要利用fast指针的速度是slow指针的2倍，得到fast指针重设为head指针
                    // 运行后第二次相遇的地方一定是相交的地方
                    fast = head;
                    meet = true;
                } else {
                    return fast;
                }
            }
        }

        return null;
    }

}
