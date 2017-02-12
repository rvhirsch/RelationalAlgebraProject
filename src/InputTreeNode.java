public class InputTreeNode {
    private boolean operator;   //True if the node is an operator, false if it is a relation
    private String name;    //the name of the relation, or the name of the operator
    private String conditional; //if node is an operator and needs a condition, you put this here

    private InputTreeNode root;
    private InputTreeNode leftChild;
    private InputTreeNode rightChild;


    public InputTreeNode(String name) {
        this.operator = false;
        this.name = name;
        this.conditional = null;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode(String name, String conditional) {
        this.operator = true;
        this.name = name;
        this.conditional = conditional;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode(String name, boolean op) {
        this.operator = op;
        this.name = name;
        this.conditional = null;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode() {
        this.operator = false;
        this.name = null;
        this.conditional = null;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setLeft(InputTreeNode node) {
        this.leftChild = node;
    }

    public InputTreeNode getRight() {
        return this.rightChild;
    }

    public void setRight(InputTreeNode node) {
        this.rightChild = node;
    }

    public String toString() {
        String toReturn = "Parent: ";
        if (this.root != null) {
            toReturn += this.root.getName() + " ";
            if (this.root.conditional != null) {
                toReturn += "con: " + this.root.conditional + " | ";
            }
        }

        if (this.operator == false) {
            toReturn += "{Rel: " + this.name + "}";
        } else if (this.operator == true && (this.leftChild == null || this.rightChild == null)) {
            toReturn += "{Op: " + this.name + ", Con: " + this.conditional + "}";
        } else if (this.operator == true && this.leftChild != null && this.rightChild != null) {
            toReturn += "{Op: " + this.name + ", Param1: " + this.leftChild.getName() + ", Param2: " + this.rightChild.getName() + "}";
        }
        return toReturn;
    }
}


