package cn.tommyyang.tree;

/**
 * @author TommyYang on 2019-03-28
 */
public class ValidBST {


    public static void main(String[] args) {
        TreeNode t1 = new TreeNode(10);
        TreeNode t2 = new TreeNode(5);

        TreeNode t3 = new TreeNode(15);
        TreeNode t4 = new TreeNode(6);
        TreeNode t5 = new TreeNode(20);

        t1.left = t2;
        t1.right = t3;
        t3.left = t4;
        t3.right = t5;

        new ValidBST().isValidBST(t1);
    }


    double last = -Double.MAX_VALUE;
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (isValidBST(root.left)) {
            if (last < root.val) {
                last = root.val;
                return isValidBST(root.right);
            }
        }
        return false;
    }

}
