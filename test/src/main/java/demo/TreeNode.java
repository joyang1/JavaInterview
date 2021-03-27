package demo;

/**
 * @Author : TommyYang
 * @Time : 2019-08-11 14:33
 * @Software: IntelliJ IDEA
 * @File : TreeNode.java
 */
public class TreeNode {

    private TreeNode leftNode;
    private TreeNode rithtNode;
    private int data;
    private boolean isLeft;

    public TreeNode(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public TreeNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(TreeNode leftNode) {
        this.leftNode = leftNode;
    }

    public TreeNode getRithtNode() {
        return rithtNode;
    }

    public void setRithtNode(TreeNode rithtNode) {
        this.rithtNode = rithtNode;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public void display(TreeNode rootNode, boolean isLeft) {
        while (rootNode != null) {
            System.out.println(rootNode.getData());
        }

    }
}
