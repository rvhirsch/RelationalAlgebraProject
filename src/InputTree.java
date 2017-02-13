/**
 * Created by Josh on 1/30/2017.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.*;

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
    private ArrayList<Rel> relList;

    public InputTree(ArrayList<Rel> relList) {
        this.root = null;
        this.relList = relList;
    }

    private Rel getRel(String name) {
        for (int x = 0; x < this.relList.size(); x++) {
            if (this.relList.get(x).getName().equalsIgnoreCase(name)) {
                return this.relList.get(x);
            }
        }
        throw new IllegalArgumentException("Execute: Relation not found | String: " + name);
    }

    private void addNode(InputTreeNode node) {
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

    private InputTreeNode getRoot() {
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

//        System.out.println(Arrays.toString(line));
//        System.out.println();

        for (int cur = 0; cur < line.length; cur++) {
            InputTreeNode newNode = null;
            paraTuple pt = null;

            switch (line[cur]) {

                case "up":
                case "rnm":
                case "rnmt":
                case "rmv":
                case "G":
                case "in":
                case "=":
                case "pt":
                case "min":
                case "max":
                case "sum":
                case "cnt":
                case "avg":
                case "Π":
                case "σ":   //select has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Select");
                    newNode = new InputTreeNode(line[cur],line[cur+1]);

//                    System.out.println("Main loop: " + newNode.toString());

                    this.addNode(newNode);
                    cur += 1;
                    break;

                case "lnj":
                case "fnj":
                case "U":
                case "I":
                case "X":   //cp has no conditions and two parameters, both parameters are its two children
//                 System.out.println("Input: Join");
                    newNode = new InputTreeNode(line[cur], 2);
//                    System.out.println("Main loop: " + newNode.toString());
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
                case "up":
                case "rnm":
                case "rnmt":
                case "rmv":
                case "G":
                case "in":
                case "=":
                case "pt":
                case "min":
                case "max":
                case "sum":
                case "cnt":
                case "avg":
                case "Π":
                case "σ":   //project has on condition and one parameter, the parameter is 1 entry ahead
//                    System.out.println("Input: Project");
                    newNode = new InputTreeNode(line[cur],line[cur+1]);

//                    System.out.println("Left Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getLeft() != null) {
                        ptr = ptr.getLeft();
                    }
                    ptr.setLeft(newNode);
                    newNode.setRoot(ptr);
                    cur += 1;
                    break;

                case "lnj":
                case "fnj":
                case "U":
                case "I":
                case "X":
                    newNode = new InputTreeNode(line[cur], 2);
                    pt = this.getParams(newNode, line, cur+1);

//                    System.out.println("Left Param loop: " + newNode.toString());

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

//                    System.out.println("Left Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getLeft() != null) {
                        ptr = ptr.getLeft();
                    }
                    ptr.setLeft(newNode);
                    newNode.setRoot(ptr);
                    break;
            }
        }

        loop2: for (int y = secondStartIndex; y < line.length; y++) {
            InputTreeNode newNode = null;
            InputTreeNode ptr = null;
            paraTuple pt = null;

            switch (line[y]) {
                case "up":
                case "rnm":
                case "rnmt":
                case "rmv":
                case "G":
                case "in":
                case "=":
                case "pt":
                case "min":
                case "max":
                case "sum":
                case "cnt":
                case "avg":
                case "Π":
                case "σ":   //select has on condition and one parameter, the parameter is 1 entry ahead
//             System.out.println("Input: Select");
                    newNode = new InputTreeNode(line[y],line[y+1]);

//                    System.out.println("Right Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getRight() != null) {
                        ptr = ptr.getRight();
                    }
                    ptr.setRight(newNode);
                    newNode.setRoot(ptr);

//             System.out.println("Left Param: " + newNode.toString());

                    y += 1;
                    break;

                case "lnj":
                case "fnj":
                case "U":
                case "I":
                case "X":
                    newNode = new InputTreeNode(line[y], 2);
                    pt = this.getParams(newNode, line, y+1);

//                    System.out.println("Right Param loop: " + newNode.toString());

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

//                    System.out.println("Right Param loop: " + newNode.toString());

                    ptr = topNode;
                    while (ptr.getRight() != null) {
                        ptr = ptr.getRight();
                    }
                    ptr.setRight(newNode);
                    newNode.setRoot(ptr);

                    break;
            }
        }
        paraTuple toReturn = new paraTuple(topNode, finalIndex);
        return toReturn;
    }

    private void expandConditions() {
        if (this.root.getLeft() != null) {
            expandConditionsHelper(this.root, "left");
        }
        if (this.root.getRight() != null) {
            expandConditionsHelper(this.root, "right");
        }
    }

    private void expandConditionsHelper (InputTreeNode node, String side) {
        String[] conditions = null;
        InputTreeNode parent = node.getRoot();
        InputTreeNode lChild = node.getLeft();
        InputTreeNode rChild = node.getRight();
        InputTreeNode tmp = null;

        if (node.getCon() != null) {
            conditions = node.getCon().split("&&");
        }

        if (conditions != null && conditions.length > 1) {

            tmp = expandConditionsHelperHelper(node, conditions);

            if (parent != null) {
                if (side.equalsIgnoreCase("left")) {
                    parent.setLeft(tmp);
                } else {
                    parent.setRight(tmp);
                }
            } else {
                this.root = tmp;
            }
        }

//    	System.out.println("lChild: " + lChild + " | rChild: " + rChild);

        if (lChild != null) {
            expandConditionsHelper(lChild, "left");
        }

        if (rChild != null) {
            expandConditionsHelper(rChild, "right");
        }
    }

    private InputTreeNode expandConditionsHelperHelper (InputTreeNode node, String[] conditions) {
        ArrayList<InputTreeNode> nodesToAdd = new ArrayList<InputTreeNode>();
        InputTreeNode parent, lChild, rChild, toReturn, tmp, tmp2;
        parent = lChild = rChild = toReturn = tmp = tmp2 = null;

        if (node.getRoot() != null) {
            parent = node.getRoot();
        }

        if (node.getLeft() != null) {
            lChild = node.getLeft();
        }
        if (node.getRight() != null) {
            rChild = node.getRight();
        }

        for (int x = 0; x < conditions.length; x++) {
            InputTreeNode newNode = new InputTreeNode(node.getName(), conditions[x]);
            nodesToAdd.add(newNode);

        }

        toReturn = nodesToAdd.get(0);
        toReturn.setRoot(parent);
        tmp = toReturn;

        for (int y = 1; y < nodesToAdd.size(); y++) {
            tmp.setRight(nodesToAdd.get(y));
            tmp2 = tmp;
            tmp = tmp.getRight();
            tmp.setRoot(tmp2);
        }

        if (lChild != null) {
            tmp.setLeft(lChild);
            tmp.getLeft().setRoot(tmp);

        }
        if (rChild != null) {
            tmp.setRight(rChild);
            tmp.getRight().setRoot(tmp);
        }
        System.out.println();
        return toReturn;
    }

    public  Rel execute() throws Exception {
        this.expandConditions();	//first thing so we can have a realistic trees
        Rel toReturn = null;		//final rel to return to user

        //we need to get to the very bottom of the tree, right side preferred
        InputTreeNode ptr = this.root;
        while (ptr.getRight() != null || ptr.getLeft() != null)	 {
            //travel down the right child, if the right child doesn't exist, travel down the left child
            if (ptr.getRight() != null) {
                ptr = ptr.getRight();
            } else {
                ptr = ptr.getLeft();
            }
        }

        Rel transRel = null;

        //main loop, collapsing the tree up, continue until we have reached the root node (when the node has no parent)
        while (ptr != null) {
            InputTreeNode parent = null;
            InputTreeNode cousin = null;
            String methodName = null;
            String conditional = null;
            Method method = null;

            //get the name of the method represented by the node
            switch(ptr.getName()) {
                case "Π":
                    methodName = "proj";
                    break;
                case "σ":
                    methodName = "select";
                    break;
                case "X":
                    methodName = "crossProd";
                    break;
                default:
                    methodName = ptr.getName();
                    break;
            }

            //rules for relations

            if (ptr.getNodeType() == 3) {
                parent = ptr.getRoot();
                if (parent.getRight() == ptr) {
                    cousin = parent.getLeft();
                } else if (parent.getLeft() == ptr) {
                    cousin = parent.getRight();
                }
            }

            // Rules for Joins
            else if (ptr.getNodeType() == 2) {
                Rel rel1 = this.getRel(ptr.getLeft().getName());
                Rel rel2 = this.getRel(ptr.getRight().getName());
                method = Rel.class.getMethod(methodName, Rel.class);

               transRel = (Rel) method.invoke(rel1, rel2);
            }

            else if (ptr.getNodeType() == 1) {
                Rel rel1 = null;
                if (transRel == null) {
                    if (ptr.getRight() != null) {
                        rel1 = this.getRel(ptr.getRight().getName());
                    } else if (ptr.getLeft() != null) {
                        rel1 = this.getRel(ptr.getLeft().getName());
                    }
                } else {
                    rel1 = transRel;
                }

                method = Rel.class.getMethod(methodName, String.class);

                transRel = (Rel) method.invoke(rel1, booleanExpander(ptr.getCon()));
            }











//    		System.out.println("Current: " + ptr);
//    		System.out.println("Parent: " + parent);
//    		System.out.println("Cousin: " + cousin);
//    		System.out.println();


            ptr = ptr.getRoot();
        }















        return transRel;
    }

    private String booleanExpander (String toExpand) {
        String toReturn = "";
        //
        String[] equation = toExpand.split("((?<=\\W+)|(?=\\W+))");
        System.out.println(Arrays.toString(equation));

        toReturn += equation[0];

        if (equation.length > 1) {
            for (int x = 1; x < equation.length; x++) {
                toReturn += " ";
                toReturn += equation[x];
            }
        }

        return toReturn;
    }

    public static void main(String[] args) throws Exception {
        InputTree tree = new InputTree(null);

        /****************************************************************************************
         * ****************************For Testing Node Interactions*****************************
         * **************************************************************************************
         */

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


        /****************************************************************************************
         * ****************************For Testing Tree Input Methods****************************
         * **************************************************************************************
         */


//        String paramTest = "X σ name='amy' rel1 , σ price<10 rel2";
//        System.out.println(tree.getParams(nodeC, paramTest.split(" "), 1).toString());
//        String test = "Π name σ age<18 X person , eats *";
//        String test2 = "Π p X σ name='amy' rel1 , σ price<10 rel2 *";
//        String test3 = "Π name σ gender='female' X person , eats *";
//        String test4 = "I Π name σ gender='female'&&pizza='mushroom' X Person , Eats * , Π name σ gender='female'&&pizza='pepperoni' X Person , Eats * *";
//        String test5 = "I X Rel1 , Rel2 * , X Rel3 , Rel4 * *";
//        String test6 = "I Π name X Rel1 , Rel2 * , X Rel3 , Rel4 * *";
//        String test7 = "I Π name X Rel1 , Rel2 * , Π gender X Rel3 , Rel4 * *";

//        tree.read(test);



        /****************************************************************************************
         * ****************************For Testing Condition Parsers*****************************
         * **************************************************************************************
         */


//        String conTest = "gender='female'&&pizza='mushroom";
//        String conTest2 = "dicksize>18&&height>7";
//        String conTest3 = "dicksize>18&&height>7&&allthedicks&&intheworld";
//        InputTreeNode node1 = new InputTreeNode("Π", conTest2);
//        InputTreeNode node2 = new InputTreeNode("σ", conTest);
//        InputTreeNode node3 = new InputTreeNode("σ", conTest3);
//        InputTreeNode node4 = new InputTreeNode("Rel1");


//        node1.setRight(node2);
//        node2.setRoot(node1);
//        node2.setRight(node3);
//        node3.setRoot(node2);
//        node3.setRight(node4);
//        node4.setRoot(node3);


//        tree.addNode(node1);

//        System.out.println();
//        System.out.println(tree.toString());

//        tree.expandConditions();


        /****************************************************************************************
         * ****************************For Testing The Execute Method****************************
         * **************************************************************************************
         */

//        tree.execute();


//        System.out.println();
//        System.out.println(tree.toString());


        System.out.println(tree.booleanExpander("tits"));
    }
}


