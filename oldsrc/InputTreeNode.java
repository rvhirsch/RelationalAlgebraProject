public class InputTreeNode {
    private int nodeType;   //0 = Unknown
                            //1 = Operator that takes 1 relation and 1 condition
                            //2 = Operator that takes 2 relations
                            //3 = Relation
    private String name;    //the name of the relation, or the name of the operator
    private String conditional; //if node is an operator and needs a condition, you put this here

    private InputTreeNode root;
    private InputTreeNode leftChild;
    private InputTreeNode rightChild;


    public InputTreeNode(String name) {
        this.nodeType = 3;
        this.name = name;
        this.conditional = null;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode(String name, String conditional) {
        this.nodeType = 1;
        this.name = name;
        this.conditional = conditional;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode(String name, int type) {
        this.nodeType = 2;
        this.name = name;
        this.conditional = null;

        this.root = null;
        this.leftChild = null;
        this.rightChild = null;
    }

    public InputTreeNode() {
        this.nodeType = 0;
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

    public int getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(int type) {
        this.nodeType = type;
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

        if (this.nodeType == 3) {
            toReturn += "{Rel: " + this.name + "}";
        } else if (this.nodeType == 1) {
            toReturn += "{Op: " + this.name + ", Con: " + this.conditional + "}";
        } else if (this.nodeType == 2) {
            toReturn += "{Op: " + this.name + ", Param1: " + this.leftChild.getName() + ", Param2: " + this.rightChild.getName() + "}";
        }
        return toReturn;
    }
}


