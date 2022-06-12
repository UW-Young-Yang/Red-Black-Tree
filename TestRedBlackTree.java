// --== CS400 File Header Information ==--
// Name: Young Yang
// Email: xyang532@wisc.edu
// Team: IC
// TA: Mu Cai
// Lecturer: Gary Dahl
// Notes to Grader:

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

/**
 * This class test different cases for inserting into a red black tree.
 * 
 * @author Young Yang
 */
class TestRedBlackTree {

    /**
     * Tests case 1 - red node's parent is black.
     */
    @Test
    void testParentOfRedNodeIsBlack() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(21);
        tree.insert(16);
        tree.insert(32);
        assertEquals("[21, 16, 32]", tree.toString());
        if (tree.root.isBlack != true && tree.root.leftChild.isBlack != false && tree.root.rightChild.isBlack != false) {
            fail("Red and black attributes conflict");
        }
    }

    /**
     * Tests case 2a - red node's parent is red and parent's sibling is null.
     */
    @Test
    void testSiblingOfParentIsNull() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(16);
        tree.insert(21);
        tree.insert(32);
        assertEquals("[21, 16, 32]", tree.toString());
        if (tree.root.isBlack != true && tree.root.leftChild.isBlack != false && tree.root.rightChild.isBlack != false) {
            fail("Red and black attributes conflict");
        }
    }

    /**
     * Tests case 2b - red node's parent is red and parent's sibling is red.
     */
    @Test
    void testSiblingOfParentIsRed() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(21);
        tree.insert(16);
        tree.insert(32);
        tree.insert(45);
        assertEquals("[21, 16, 32, 45]", tree.toString());
        if (tree.root.isBlack != true && tree.root.leftChild.isBlack != true && tree.root.rightChild.isBlack != true 
            && tree.root.rightChild.isBlack != false) {
            fail("Red and black attributes conflict");
        }
    }

    /**
     * Tests when red node and parent's sibling are on the same side.
     */
    @Test
    void testOnTheSameSide() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(16);
        tree.insert(21);
        tree.insert(20);
        assertEquals("[20, 16, 21]", tree.toString());
        if (tree.root.isBlack != true && tree.root.leftChild.isBlack != false && tree.root.rightChild.isBlack != false) {
            fail("Red and black attributes conflict");
        }
    }

    /**
     * Tests whether the tree can handle the case - after the tree rotate, red and black attribute conflicts occur again
     */
    @Test
    void testRotateTwice() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(21);
        tree.insert(16);
        tree.insert(32);
        tree.insert(45);
        tree.insert(57);
        tree.insert(62);
        tree.insert(82);
        tree.insert(90);
        assertEquals("[45, 21, 62, 16, 32, 57, 82, 90]", tree.toString());
        if (tree.root.isBlack != true && tree.root.leftChild.isBlack != false && tree.root.rightChild.isBlack != false &&
            tree.root.leftChild.leftChild.isBlack != true && tree.root.leftChild.rightChild.isBlack != true &&
            tree.root.rightChild.leftChild.isBlack != true && tree.root.rightChild.rightChild.isBlack != true &&
            tree.root.rightChild.rightChild.rightChild.isBlack != false) {
            fail("Red and black attributes conflict");
        }
    }

}
