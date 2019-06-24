package cn.tommyyang.listnode;

/**
 * @author TommyYang on 2019-03-20
 */
public class MergeTwoSortLinkedList {

    public static void main(String[] args) throws InterruptedException {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(4);

        ListNode node4 = new ListNode(1);
        ListNode node5 = new ListNode(2);
        ListNode node6 = new ListNode(4);

        node1.setNext(node2);
        node2.setNext(node3);

        node4.setNext(node5);
        node5.setNext(node6);

        ListNode head = mergeTwoLists(node1, node4);
        while (head != null){
            System.out.println(head.val);
            head = head.next;
        }
    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
//        if(l1 == null){
//            return l2;
//        }
//
//        if(l2 == null){
//            return l1;
//        }
//
//        ListNode tmp;
//        if(l1.val > l2.val){
//            tmp = l1;
//            l2 = tmp;
//            l1 = l2;
//        }
//        ListNode head = l1;
//        ListNode tmp2;
//        while(l1.next != null && l2 != null){
//            if(l1.val <= l2.val && l1.next.val <= l2.val){
//                l1 = l1.next;
//            }else if (l1.val <= l2.val && l1.next.val > l2.val){
//                tmp = l1.next;
//                l1.next = l2;
//                tmp2 = l2.next;
//                l2.next = tmp;
//                l2 = tmp2;
//                l1 = l1.next;
//            }
//        }
//        if(l1 != null){
//            l1.next = l2;
//        }
//
//        return head;

        if(l1 == null){
            return l2;
        }

        if(l2 == null){
            return l1;
        }

        if(l1.val <= l2.val){
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else{
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }

    }

}
