package cn.tommyyang.tree;

/**
 * @author TommyYang on 2019-05-08
 */
//翻转一棵二叉树。
public class InvertTree {

    public TreeNode invertTree(TreeNode root) {
        if (root == null){
            return root;
        }

        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;

        invertTree(root.left);
        invertTree(root.right);

        return root;
    }

}
