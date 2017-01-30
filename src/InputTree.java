/**
 * Created by Josh on 1/30/2017.
 */

public class InputTree {
    public class InputTreeNode {
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
}
