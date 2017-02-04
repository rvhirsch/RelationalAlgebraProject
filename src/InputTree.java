/**
 * Created by Josh on 1/30/2017.
 */

import jdk.internal.util.xml.impl.Input;

import java.util.ArrayList;
import java.util.Arrays;

public class InputTree {
    public static class InputTreeNode {
        private boolean operator;   //True if the node is an operator, false if it is a relation
        private String name;    //the name of the relation, or the name of the operator
        private String conditional; //if node is an operator and needs a condition, you put this here
        private String rel1;
        private String rel2;

        private InputTreeNode root;
        private InputTreeNode leftChild;
        private InputTreeNode rightChild;


        public InputTreeNode (String name) {
            this.operator = false;
            this.name = name;
            this.rel1 = name;
            this.rel2 = null;
            this.conditional = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
        }

        public InputTreeNode (String name, String conditional) {
            this.operator = true;
            this.name = name;
            this.conditional = conditional;
            this.rel1 = null;
            this.rel2 = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
        }

        public InputTreeNode (String name, String rel1, String rel2) {
            this.operator = true;
            this.name = name;
            this.conditional = null;
            this.rel1 = rel1;
            this.rel2 = rel2;

            this.root = null;
            this.leftChild = new InputTreeNode(rel1);
            this.leftChild.setRoot(this);
            this.rightChild = new InputTreeNode(rel2);
            this.rightChild.setRoot(this);
        }

        public InputTreeNode () {
            this.operator = false;
            this.name = null;
            this.conditional = null;

            this.root = null;
            this.leftChild = null;
            this.rightChild = null;
            this.rel1 = null;
            this.rel2 = null;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRel1() {
            return this.rel1;
        }

        public void setRel1(String rel) {
            this.rel1 = rel;
        }

        public String getRel2() {
            return this.rel2;
        }

        public void setRel2(String rel) {
            this.rel2 = rel;
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

        public String toString() {
            String toReturn = "P: ";
            if (this.root != null) {
                toReturn += this.root.getName() + "  |  ";
            }

            if (this.operator == false) {
                toReturn += "{Rel: " + this.name + "}";
            } else if (this.operator == true && this.rel2 == null) {
                toReturn += "{Op: " + this.name + ", Con: " + this.conditional + "}";
            } else if (this.operator == true && this.rel1 != null && this.rel2 != null) {
                toReturn += "{Op: " + this.name + ", Rel1: " + this.rel1 + ", Rel2: " + this.rel2 + "}";
            }
            return toReturn;
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
        if (this.root == null) {
            return "Tree is empty";
        } else {
            return this.toStringHelper(this.root);
        }
    }

    private String toStringHelper(InputTreeNode node) {
        String toReturn = node.toString() + "\n";
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

        System.out.println(Arrays.toString(line));

        for (int cur = line.length-1; cur >= 0; cur--) {
            InputTreeNode newNode = null;

            switch (line[cur]) {
                case "Π":   //project has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Project");
                    newNode = new InputTreeNode("project",line[cur+1]);
                    break;
                case "σ":   //select has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Select");
                    newNode = new InputTreeNode("select",line[cur+1]);
                    break;
                case "X":   //Join has no conditions and two parameters
//                    System.out.println("Input: Join");
                    newNode = new InputTreeNode("cartesian product", line[cur+1], line[cur+2]);
                    break;
                case "NJ":  //Natural Join
                    newNode = new InputTreeNode("natural join;", line[cur+1], line[cur+2]);
                    break;
                case "TJ":  //Theta Join
                    newNode = new InputTreeNode("theta join", line[cur+1], line[cur+2]);
                    break;
                case "LOJ": //Left Outer Join
                    newNode = new InputTreeNode("left outer join", line[cur+1], line[cur+2]);
                    break;
                case "ROJ": //Right Outer Join
                    newNode = new InputTreeNode("Right outer join", line[cur+1], line[cur+2]);
                    break;
                case "FOJ": //Full Outer Join
                    newNode = new InputTreeNode("full outer join", line[cur+1], line[cur+2]);
                    break;
                case "U":   //Union
                    newNode = new InputTreeNode("union", line[cur+1], line[cur+2]);
                    break;
                case "I":   //Intersection
                    newNode = new InputTreeNode("intersection", line[cur+1], line[cur+2]);
                    break;
                case "→":   //Rename
                    newNode = new InputTreeNode("rename", line[cur+1], line[cur+2]);
                    break;
                case "min":   //Rename
                    newNode = new InputTreeNode("minimum",line[cur+1]);
                    break;
                case "max":   //Rename
                    newNode = new InputTreeNode("maximum",line[cur+1]);
                    break;
                case "avg":   //Rename
                    newNode = new InputTreeNode("average",line[cur+1]);
                    break;
                case "cnt":   //Rename
                    newNode = new InputTreeNode("count",line[cur+1]);
                    break;
                case "sum":   //Rename
                    newNode = new InputTreeNode("sum",line[cur+1]);
                    break;
                default:

            }

            if (newNode != null)
                this.addNode(newNode);
                //System.out.println(newNode.toString());
        }

    }


    public static void main(String[] args) {
        InputTree tree = new InputTree();

//        InputTreeNode nodeA = new InputTreeNode("Project");
//        InputTreeNode nodeB = new InputTreeNode("Select");
//        InputTreeNode nodeC = new InputTreeNode("Join");
//        InputTreeNode nodeD = new InputTreeNode("RelOne");
//        InputTreeNode nodeE = new InputTreeNode("RelTwo");

//        nodeA.setRight(nodeB);
//        nodeB.setRight(nodeC);
//        nodeC.setRight(nodeE);
//        nodeC.setleft(nodeD);

//        tree.addNode(nodeA);


        String test = "Π p σ A<18 X R1 R2";
        tree.read(test);

        System.out.println();
        System.out.println(tree.toString());
    }

}
