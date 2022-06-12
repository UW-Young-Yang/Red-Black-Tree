// --== CS400 File Header Information ==--
// Name: Young Yang
// Email: xyang532@wisc.edu
// Team: IC
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader:

import java.util.LinkedList;

/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes within a binary search tree.  You can use this class' insert
 * method to build a binary search tree, and its toString method to display
 * the level order (breadth first) traversal of values in that tree.
 * 
 * @author Young Yang
 */
public class RedBlackTree<T extends Comparable<T>> {

    /**
     * This class represents a node holding a single value within a binary tree
     * the parent, left, and right child references are always be maintained.
     */
    protected static class Node<T> {
        public T data;
        public Node<T> parent; // null for root node
        public Node<T> leftChild; 
        public Node<T> rightChild; 
        public boolean isBlack = false;
        public Node(T data) { this.data = data; }
        /**
         * @return true when this node has a parent and is the left child of
         * that parent, otherwise return false
         */
        public boolean isLeftChild() {
            return parent != null && parent.leftChild == this;
        }
        /**
         * This method performs a level order traversal of the tree rooted
         * at the current node.  The string representations of each data value
         * within this tree are assembled into a comma separated string within
         * brackets (similar to many implementations of java.util.Collection).
         * @return string containing the values of this tree in level order
         */
        @Override
        public String toString() { // display subtree in order traversal
            String output = "[";
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.leftChild != null) q.add(next.leftChild);
                if(next.rightChild != null) q.add(next.rightChild);
                output += next.data.toString();
                if(!q.isEmpty()) output += ", ";
            }
            return output + "]";
        }
    }

    protected Node<T> root; // reference to root node of tree, null when empty

    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree.  After  
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when the tree already contains data
     */
    public void insert(T data) throws NullPointerException,
				      IllegalArgumentException {
        // null references cannot be stored within this tree
        if(data == null) throw new NullPointerException(
            "This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if(root == null) { root = newNode; } // add first node to an empty tree
        else insertHelper(newNode,root); // recursively insert into subtree
        root.isBlack = true; // always set the root node to be black
    }

    /** 
     * Recursive helper method to find the subtree with a null reference in the
     * position that the newNode should be inserted, and then extend this tree
     * by the newNode in that position.
     * @param newNode is the new node that is being added to this tree
     * @param subtree is the reference to a node within this tree which the 
     *      newNode should be inserted as a descenedent beneath
     * @throws IllegalArgumentException when the newNode and subtree contain
     *      equal data references (as defined by Comparable.compareTo())
     */
    private void insertHelper(Node<T> newNode, Node<T> subtree) {
        int compare = newNode.data.compareTo(subtree.data);
        // do not allow duplicate values to be stored within this tree
        if(compare == 0) throw new IllegalArgumentException(
            "This RedBlackTree already contains that value.");

        // store newNode within left subtree of subtree
        else if(compare < 0) {
            if(subtree.leftChild == null) { // left subtree empty, add here
                subtree.leftChild = newNode;
                newNode.parent = subtree;
                enforceRBTreePropertiesAfterInsert(newNode);
            // otherwise continue recursive search for location to insert
            } else insertHelper(newNode, subtree.leftChild);
        }

        // store newNode within the right subtree of subtree
        else { 
            if(subtree.rightChild == null) { // right subtree empty, add here
                subtree.rightChild = newNode;
                newNode.parent = subtree;
                enforceRBTreePropertiesAfterInsert(newNode);
            // otherwise continue recursive search for location to insert
            } else insertHelper(newNode, subtree.rightChild);
        }
    }

    /**
     * This method performs a level order traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the values of this tree in level order
     */
    @Override
    public String toString() { return root.toString(); }

    /**
     * Performs the rotation operation on the provided nodes within this BST.
     * When the provided child is a leftChild of the provided parent, this
     * method will perform a right rotation (sometimes called a left-right 
     * rotation).  When the provided child is a rightChild of the provided 
     * parent, this method will perform a left rotation (sometimes called a 
     * right-left rotation).  When the provided nodes are not related in one 
     * of these ways, this method will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    private void rotate(Node<T> child, Node<T> parent)
	throws IllegalArgumentException {   
        if (parent.leftChild != child && parent.rightChild != child) throw new IllegalArgumentException(
            "The provided child and parent node references are not initially (pre-rotation) related.");
        
        // connect parent's parent node to child node
        if (parent.parent != null) {
            if (parent.parent.leftChild == parent) {
                parent.parent.leftChild = child;
            } else {
                parent.parent.rightChild = child;
            }
            child.parent = parent.parent;
        } else {
            root = child;
        }
        
        if (parent.leftChild == child) {  // right rotation
            // connect parent's left to child's rightChild node
            parent.leftChild = child.rightChild;
            if (child.rightChild != null) child.rightChild.parent = parent;
            
            // connect child's right to parent node
            child.rightChild = parent;
            parent.parent = child;
        } else {  // left rotation
            // connect parent's right to child's leftChild node
            parent.rightChild = child.leftChild;
            if (child.leftChild != null) child.leftChild.parent = parent;
            
            // connect child's left to parent node
            child.leftChild = parent;
            parent.parent = child;
        }
    }

    /**
     * Resolve red child under red parent red black tree property violations 
     * that are introduced by inserting new nodes into a red black tree.
     * @param redNode is newly red node being added to the tree
     */
    private void enforceRBTreePropertiesAfterInsert(Node<T> redNode) {

        Node<T> parent = redNode.parent;

        if (parent != null) {
            if (parent.isBlack == true) {  // parent is black
                return;
            } else {  // parent is red

                if (parent.parent.leftChild != null && parent.parent.rightChild != null &&
                    parent.parent.leftChild.isBlack == false && parent.parent.rightChild.isBlack == false) {  // parent and parent's sibling is red
        
                    parent.parent.isBlack = false;
                    parent.parent.leftChild.isBlack = true;
                    parent.parent.rightChild.isBlack = true;

                    redNode = parent.parent;  // parent's parent is a redNode
                    enforceRBTreePropertiesAfterInsert(redNode);

                } else { // parent's sibling is black or null

                    if (parent.parent.leftChild == parent) {  // redNode's parent is left child
                        
                        if (parent.rightChild == redNode) {  // if redNode is right child
                            rotate(redNode, parent);  // left rotation
                            parent = redNode;
                            redNode = parent.leftChild;
                        }

                    } else {  // redNode's parent is right child

                        if (parent.leftChild == redNode) {  // if redNode is left child
                            rotate(redNode, parent);  // right rotation
                            parent = redNode;
                            redNode = parent.rightChild;
                        }

                    }

                    parent.isBlack = true;
                    parent.parent.isBlack = false;
                    rotate(parent, parent.parent);  // rotation

                }
                
            }
        } else {  // redNode is root
            return;
        }

    }

}
