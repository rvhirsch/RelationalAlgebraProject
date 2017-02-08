/**
 * Created by Josh on 1/30/2017.
 */

import java.util.Arrays;

public class InputTree {

    public class paraTuple {
        public InputTreeNode node;
        public int index;
        public paraTuple(InputTreeNode node, int index) {
            this.node = node;
            this.index = index;
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
            InputTreeNode ptr = this.root;
            while (ptr.getRight() != null) {
                ptr = ptr.getRight();
            }
            ptr.setRight(node);
            node.setRoot(ptr);
        }
    }

    public void test() {
        InputTreeNode ptr = this.root;
        while (ptr.getRight() != null) {
            ptr = ptr.getRight();
        }

        System.out.println("Bottom: " + ptr.toString());
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
            toReturn += "LChild | " + toStringHelper(node.getLeft());
        }

        if (node.getRight() != null) {
            toReturn += "RChild | " + toStringHelper(node.getRight());
        }

        return toReturn;
    }


    public void read(String str) {
        String[] line = str.split(" ");

        System.out.println(Arrays.toString(line));
        System.out.println();

        for (int cur = 0; cur < line.length; cur++) {
            InputTreeNode newNode = null;
            paraTuple pt = null;

            switch (line[cur]) {
                case "Π":
                case "σ":   //select has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Select");
                    newNode = new InputTreeNode(line[cur],line[cur+1]);

                    System.out.println("Main loop: " + newNode.toString());

                    this.addNode(newNode);
                    cur += 1;
                    break;

                case "U":
                case "I":
                case "X":   //cp has no conditions and two parameters, both parameters are its two children
//                 System.out.println("Input: Join");
                    newNode = new InputTreeNode("cartesian product", true);
                    System.out.println("Main loop: " + newNode.toString());
                    pt = this.getParams(newNode, line, cur+1);

                    this.addNode(pt.node);
                    cur = pt.index;
                    break;
                default:
            }
        }
    }

    private paraTuple getParams(InputTreeNode parent, String[] line, int firstStartIndex) {
        InputTreeNode topNode = parent;
        int secondStartIndex = 0;
        int finalIndex = 0;

        //get the left parameter
        loop1:  for (int cur = firstStartIndex; cur < line.length; cur++) {
            InputTreeNode newNode = null;
            InputTreeNode ptr = null;
            paraTuple pt = null;
//            System.out.println(line[cur]);

            switch (line[cur]) {
                case "σ":
                case "Π":   //project has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Project");
                    newNode = new InputTreeNode(line[cur],line[cur+1]);

                    System.out.println("Left Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getLeft() != null) {
                        ptr = ptr.getLeft();
                    }
                    ptr.setLeft(newNode);
                    newNode.setRoot(ptr);

//                 System.out.println("Right Param: " + newNode.toString());

                    cur += 1;
                    break;

                case "I":
                case "U":
                case "X":
                    newNode = new InputTreeNode(line[cur], true);
                    pt = this.getParams(newNode, line, cur+1);

                    System.out.println("Left Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getLeft() != null) {
                        ptr = ptr.getLeft();
                    }
                    ptr.setLeft(pt.node);
                    pt.node.setRoot(ptr);
                    cur = pt.index;
                    break;

                case ",": //time to move onto the next parameter
                    secondStartIndex = cur + 1;
                    break loop1;

                default: //the input is a rel
                    newNode = new InputTreeNode(line[cur]);

                    System.out.println("Left Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getLeft() != null) {
                        ptr = ptr.getLeft();
                    }
                    ptr.setLeft(newNode);
                    newNode.setRoot(ptr);

//         System.out.println("Right Param: " + newNode.toString());

                    break;
            }
        }

loop2: for (int y = secondStartIndex; y < line.length; y++) {
            InputTreeNode newNode = null;
            InputTreeNode ptr = null;
            paraTuple pt = null;

            switch (line[y]) {
                case "Π":
                case "σ":   //select has on condition and one parameter, the parameter is 1 entry ahead
//             System.out.println("Input: Select");
                    newNode = new InputTreeNode(line[y],line[y+1]);

                    System.out.println("Right Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getRight() != null) {
                        ptr = ptr.getRight();
                    }
                    ptr.setRight(newNode);
                    newNode.setRoot(ptr);

//             System.out.println("Left Param: " + newNode.toString());

                    y += 1;
                    break;

                case "I":
                case "U":
                case "X":
                    newNode = new InputTreeNode(line[y], true);
                    pt = this.getParams(newNode, line, y+1);

                    System.out.println("Right Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getRight() != null) {
                        ptr = ptr.getRight();
                    }
                    ptr.setRight(pt.node);
                    pt.node.setRoot(ptr);
                    y = pt.index;
                    break;

                case "*": //time to move onto the next parameter
                    finalIndex = y;
                    break loop2;

                default: //the input is a rel
                    newNode = new InputTreeNode(line[y]);

                    System.out.println("Right Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getRight() != null) {
                        ptr = ptr.getRight();
                    }
                    ptr.setRight(newNode);
                    newNode.setRoot(ptr);

//             System.out.println("Left Param: " + newNode.toString());

                    break;
            }
        }
        paraTuple toReturn = new paraTuple(topNode, finalIndex);
        return toReturn;
    }


    public static void main(String[] args) {
        InputTree tree = new InputTree();

//        InputTreeNode nodeA = new InputTreeNode("Project");
//        InputTreeNode nodeB = new InputTreeNode("Select");
//        InputTreeNode nodeC = new InputTreeNode("Cartesian Product");
//        InputTreeNode nodeD = new InputTreeNode("RelOne");
//        InputTreeNode nodeE = new InputTreeNode("RelTwo");
//
//        nodeA.setRight(nodeB);
//        nodeB.setRight(nodeC);
//        nodeC.setRight(nodeE);
//        nodeC.setLeft(nodeD);
//
//        tree.addNode(nodeA);
//
//        tree.test();
//        System.out.println();


        String test = "Π p σ A<18 X R1 , R2 *";
        String test2 = "Π p X σ name='amy' rel1 , σ price<10 rel2 *";
        String test3 = "Π name σ gender='female' X person , eats *";
        String test4 = "I Π name σ gender='female'&&pizza='mushroom' X Person , Eats * , Π name σ gender='female'&&pizza='pepperoni' X Person , Eats **";
        tree.read(test);

        String paramTest = "X σ name='amy' rel1 , σ price<10 rel2";
//        System.out.println(tree.getParams(nodeC, paramTest.split(" "), 1).toString());

        System.out.println();
        System.out.println(tree.toString());
    }
}