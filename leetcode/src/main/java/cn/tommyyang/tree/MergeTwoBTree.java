package cn.tommyyang.tree;

/**
 * @author TommyYang on 2019-03-22
 */
public class MergeTwoBTree {

    public static void main(String[] args) {
        TreeNode root1 = new TreeNode(1);
        TreeNode t1 = new TreeNode(2);
        TreeNode t2 = new TreeNode(3);
        TreeNode t3 = new TreeNode(4);

        TreeNode root2 = new TreeNode(5);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(3);
        TreeNode t6 = new TreeNode(2);

        root1.left = t1;
        t1.left = t2;
        t1.right = t3;

        root2.right = t4;
        t4.left = t5;
        t5.right = t6;

        TreeNode resNode = mergeTrees2(t1, t4);
        displayTree(resNode);
    }

    //以t1为存储结果的TreeNode
    public static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if(t1 == null){
            return t2;
        }
        if (t2 == null){
            return t1;
        }
        t1.val += t2.val;
        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);
        return t1;
    }

    //新建一个TreeNode来存储结果
    public static TreeNode mergeTrees2(TreeNode t1, TreeNode t2){
        if(t1 == null && t2 == null){
            return null;
        }

        TreeNode root = new TreeNode((t1 == null ? 0 : t1.val) + (t2 == null ? 0 : t2.val));
        root.left = mergeTrees2((t1 == null ? null : t1.left), (t2 == null ? null : t2.left));
        root.right = mergeTrees2((t1 == null ? null : t1.right), (t2 == null ? null : t2.right));
        return  root;
    }

    public static void displayTree(TreeNode node){
        if (node == null){
            return;
        }
        System.out.println(node.val);
        displayTree(node.left);
        displayTree(node.right);
    }

}
