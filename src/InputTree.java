/**
 * Created by Josh on 1/30/2017.
 */

import java.util.ArrayList;

public class InputTree {
    public static class InputTreeNode {
        private boolean operator;   //True if the node is an operator, false if it is a relation
        private String name;    //the name of the relation, or the name of the operator
        private String conditional; //if node is an operator and needs a condition, you put this here
        private Rel rel;

        private InputTreeNode root;
        private InputTreeNode leftChild;
        private InputTreeNode rightChild;


        public InputTreeNode (String name, Rel rel) {
            this.operator = false;
            this.name = name;
            this.rel = rel;
            this.conditional = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
        }

        public InputTreeNode (String name) {
            this.operator = true;
            this.name = name;
            this.rel = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
        }

        public InputTreeNode (String name, String conditional) {
            this.operator = true;
            this.name = name;
            this.conditional = conditional;
            this.rel = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
        }

        public InputTreeNode () {
            this.operator = false;
            this.name = null;
            this.conditional = null;
            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
            this.rel = null;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Rel getRel() {
            return this.rel;
        }

        public void setRel(Rel rel) {
            this.rel = rel;
        }

        public String getCon() {
            return this.conditional;
        }

        public void setCon(String conditional) {
            this.conditional = conditional;
        }

        public boolean getOp() {
            return this.operator;
        }

        public void setOp(boolean operator) {
            this.operator = operator;
        }

        public InputTreeNode getRoot() {
            return this.root;
        }

        public void setRoot(InputTreeNode node) {
            this.root = node;
        }

        public InputTreeNode getLeft() {
            return this.leftChild;
        }

        public void setleft(InputTreeNode node) {
            this.leftChild = node;
        }

        public InputTreeNode getRight() {
            return this.rightChild;
        }

        public void setRight(InputTreeNode node) {
            this.rightChild = node;
        }
    }

    private InputTreeNode root;

    public InputTree() {
        this.root = null;
    }

    public void addNode(InputTreeNode node) {
        if (this.root == null) {
            this.root = node;
        } else {
            InputTreeNode temp = this.root;

            this.root = node;
            this.root.setRight(temp);
            temp.setRoot(this.root);
        }
    }

    public InputTreeNode getRoot() {
        return root;
    }

    public String toString() {
        return this.toStringHelper(this.root);
    }

    private String toStringHelper(InputTreeNode node) {
        String toReturn = node.getName() + "\n";
        if (node.getLeft() != null) {
            toReturn += "L: " + toStringHelper(node.getLeft());
        }

        if (node.getRight() != null) {
            toReturn += "R: " + toStringHelper(node.getRight());
        }

        return toReturn;
    }


    public void read(String str) {
        String[] symbols = {"Π","σ","X"};
        String[] line = str.split(" ");


    }


    public static void main(String[] args) {
        InputTree tree = new InputTree();

//        InputTreeNode nodeA = new InputTreeNode("Project");
//        InputTreeNode nodeB = new InputTreeNode("Select");
//        InputTreeNode nodeC = new InputTreeNode("Join");
//        InputTreeNode nodeD = new InputTreeNode("RelOne");
//        InputTreeNode nodeE = new InputTreeNode("RelTwo");
//
//        nodeA.setRight(nodeB);
//        nodeB.setRight(nodeC);
//        nodeC.setRight(nodeE);
//        nodeC.setleft(nodeD);

//        tree.addNode(nodeA);









        String test = "Π penis σ (A<18) X R1 R2";
        tree.read(test);

        System.out.println(tree.toString());
    }

}
