package cn.tommyyang.listnode;

/**
 * 链表反转
 * 1、迭代算法
 * 2、递归算法
 *
 * @Author : TommyYang
 * @Time : 2019-06-19 18:29
 * @Software: IntelliJ IDEA
 * @File : ReverseLinkedList.java
 */
public class ReverseLinkedList {

    /**
     * 迭代算法反转
     *
     * @param head
     * @return
     */
    public ListNode reverseWithIterate(ListNode head) {
        ListNode cur = head;
        ListNode next;
        ListNode pre = null;

        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * 递归算法反转
     *
     * @param head
     * @return
     */
    public ListNode reverseWithRecursion(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode newNode = reverseWithRecursion(head.next);

        head.next.next = head;
        head.next = null;

        return newNode;
    }

    public static void main(String[] args) {
        ListNode node5 = new ListNode(5, null);
        ListNode node4 = new ListNode(4, node5);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);

        print(node1);

        ListNode newNode = new ReverseLinkedList().reverseWithRecursion(node1);

        print(newNode);
    }

    /**
     * 打印链表
     *
     * @param head 链表头
     */
    private static void print(ListNode head) {
        while (head != null) {
            System.out.print(head.val);
            if (head.next != null) {
                System.out.print("->");
            }
            head = head.next;
        }
        System.out.println();
    }


}
