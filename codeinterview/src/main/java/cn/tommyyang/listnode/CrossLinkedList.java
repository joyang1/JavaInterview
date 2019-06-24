package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-03-25
 */

//相交链表题目(https://leetcode-cn.com/problems/intersection-of-two-linked-lists/)
public class CrossLinkedList {

    //找到两个单链表相交的起始节点，返回相交的链表
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        ListNode first = headA;
        ListNode second = headB;

        //当第一个链表运行完后，赋值给第二个链表的头节点；
        //第二个链表运行完后，赋值给第一个链表的头结点；
        //运行到相交的时候(相交)，或者都运行到null的时候(不相交)，都是走了同样的距离；可以画图结合理解更简单。
        while (first != second){
            first = (first == null ? headB : first.next);
            second = (second == null ? headA : second.next);
        }

        return first;
    }

}
