package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-05-07
 */
//删除某个链表中给定的（非末尾）节点
public class DeleteNodeInLinkedList {

    //其实这个题目很简单 删除某个结点 其实你可以想成是删除某个结点的后面一个结点
    //同时把后面一个结点的数据填充到当前结点 从而做到删除当前结点
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

}
