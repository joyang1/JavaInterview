package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-03-20
 */
public class ListNode {

    int val;

    ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }
}
