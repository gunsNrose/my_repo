package avltree;

/**
 * @program: test
 * @description: avl tree
 * @author: wangtao15
 * @create: 2019-01-09 09:46
 **/
public class AVLTree {

    private boolean hasIncrease = false;

    private AVLTreeNode root = null;

    class AVLTreeNode{

        int val;

        int balanceFactor;

        AVLTreeNode parent;

        AVLTreeNode right;

        AVLTreeNode left;

        public AVLTreeNode(int val, AVLTreeNode parent, AVLTreeNode right, AVLTreeNode left) {
            this.val = val;
            this.parent = parent;
            this.right = right;
            this.left = left;
            this.balanceFactor = initBalanceFactor(right,left);
        }

        private int initBalanceFactor(AVLTreeNode right, AVLTreeNode left){
            int rightBalanceFactor = -1,leftBalanceFactor = -1;
            if (right != null) {
                rightBalanceFactor = right.getBalanceFactor();
            }
            if (left != null) {
                leftBalanceFactor = left.getBalanceFactor();
            }
            return leftBalanceFactor - rightBalanceFactor;
        }

        public int getVal() {
            return val;
        }

        public int getBalanceFactor() {
            return balanceFactor;
        }

        public void setBalanceFactor(int balanceFactor) {
            this.balanceFactor = balanceFactor;
        }

        public AVLTreeNode getParent() {
            return parent;
        }

        public void setParent(AVLTreeNode parent) {
            this.parent = parent;
        }

        public AVLTreeNode getRight() {
            return right;
        }

        public void setRight(AVLTreeNode right) {
            this.right = right;
        }

        public AVLTreeNode getLeft() {
            return left;
        }

        public void setLeft(AVLTreeNode left) {
            this.left = left;
        }

        @Override
        public String toString() {
            return "AVLTreeNode{" +
                    "val=" + val +
                    ", balanceFactor=" + balanceFactor +
                    '}';
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public boolean insert(int value){
        boolean ret = false;
        if(!isEmpty()){
            AVLTreeNode curNode = root,insertNode = null;
            int curVal;
            do {
                curVal = curNode.getVal();
                if(value > curVal){
                    AVLTreeNode right = curNode.getRight();
                    if (right != null) {
                        curNode = right;
                    }else{
                        insertNode = new AVLTreeNode(value,curNode,null,null);
                        ret = true;
                        break;
                    }
                }else if(value < curVal){
                    AVLTreeNode left = curNode.getLeft();
                    if (left != null) {
                        curNode = left;
                    }else{
                        insertNode = new AVLTreeNode(value,curNode,null,null);
                        ret = true;
                        break;
                    }
                }else {
                    curNode = null;
                }
            }while (curNode != null);
            if(ret){
                adjustment(insertNode);
            }
        }else{
            root = new AVLTreeNode(value,null,null,null);
            ret = true;
        }
        return ret;
    }

    private void adjustment(AVLTreeNode insertNode){
        if (insertNode != null) {
            AVLTreeNode parent = insertNode.getParent();
            int balanceFactor = parent.getBalanceFactor();
            if(1 == balanceFactor || -1 == balanceFactor){
                parent.setBalanceFactor(0);
            }else{
                AVLTreeNode grandFather = parent.getParent();
                int balanceFactor4gf = grandFather.getBalanceFactor();
                int parentVal = parent.getVal();
                int val = insertNode.getVal();
                //在左边
                if(parentVal > val){
                    if(1 == balanceFactor4gf){

                    }else if(-1 == balanceFactor4gf){

                    }else{

                    }
                }else{

                }

            }
        }
    }
    
    private void transformParent(AVLTreeNode node,AVLTreeNode sonNode){
        if (node != null && sonNode != null) {
            AVLTreeNode parent = node.getParent();
            if (parent != null) {
                if(node == parent.getLeft()){
                    parent.setLeft(sonNode);
                }else{
                    parent.setRight(sonNode);
                }
                sonNode.setParent(parent);
            }else {
                root = sonNode;
            }
        }
    }

    private void rightRotate(AVLTreeNode node){
        if (node != null) {
            AVLTreeNode leftSon = node.getRight();
            if (leftSon != null) {
                AVLTreeNode rightGrandSon = leftSon.getRight();
                this.transformParent(node,leftSon);
                leftSon.setRight(node);
                node.setParent(leftSon);
                if (rightGrandSon != null) {
                    rightGrandSon.setParent(node);
                    node.setLeft(rightGrandSon);
                }
            }
        }
    }

    private void leftRotate(AVLTreeNode node){
        if (node != null) {
            AVLTreeNode rightSon = node.getRight();
            if (rightSon != null) {
                AVLTreeNode leftGrandson = rightSon.getLeft();
                this.transformParent(node,rightSon);
                rightSon.setLeft(node);
                node.setParent(rightSon);
                if (leftGrandson != null) {
                    node.setRight(leftGrandson);
                    leftGrandson.setParent(node);
                }
            }
        }
    }

    private void leftRightRotate(AVLTreeNode node){
        if (node != null) {
            this.leftRotate(node.getLeft());
            this.rightRotate(node);
        }
    }

    private void rightLeftRotate(AVLTreeNode node){
        if (node != null) {
            this.rightRotate(node.getRight());
            this.leftRotate(node);
        }
    }
}
