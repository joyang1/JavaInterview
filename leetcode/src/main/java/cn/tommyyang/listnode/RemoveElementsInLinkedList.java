package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-05-07
 */
//删除链表中等于给定值 val 的所有节点。
public class RemoveElementsInLinkedList {

    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode current = dummy;
        while (current.next != null){
            if (current.next.val == val) {
                // 删除链表后面一个结点后 即相当于链表后移了一个位置
                // 需要继续判断current.next.val 需不需要移除
                current.next = current.next.next;
            }else {
                current = current.next;
            }

        }
        return dummy.next;
    }

}
