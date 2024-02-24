package UnitTest;

import Index.LeafNode;
import Disks.Address;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;

public class LeafNodeTest {
    @Test
    public void testDeleteAndBorrowFromLeft() {
        LeafNode node = new LeafNode(5);
        LeafNode leftSibling = new LeafNode(5);

        // Add some keys and values to the nodes
        for (int i = 0; i < 4; i++) {
            leftSibling.setKey(i, i);
            leftSibling.setValue(i, new LinkedList<Address>());
        }
        leftSibling.setNumKeys(4);

        for (int i = 0; i < 2; i++) {
            node.setKey(i, i + 5);
            node.setValue(i, new LinkedList<Address>());
        }
        node.setNumKeys(2);

        int deletePos = 1;
        node.deleteAndBorrowFromLeft(deletePos, leftSibling);

        assertEquals(3, node.getKey(0));

        // Check that the last key in leftSibling has been set to Integer.MAX_VALUE
        assertEquals(Integer.MAX_VALUE, leftSibling.getKey(leftSibling.getNumKeys()));

        // Check that the last value in leftSibling has been set to null
        assertNull(leftSibling.getValue(leftSibling.getNumKeys()));

        // Check that the number of keys in leftSibling has been decremented
        assertEquals(3, leftSibling.getNumKeys());
    }

    @Test
    public void testDeleteAndBorrowFromRight() {
        LeafNode node = new LeafNode(5);
        LeafNode rightSibling = new LeafNode(5);

        // Add some keys and values to the nodes
        for (int i = 0; i < 3; i++) {
            rightSibling.setKey(i, i + 5);
            rightSibling.setValue(i, new LinkedList<Address>());
        }
        rightSibling.setNumKeys(3);

        for (int i = 0; i < 2; i++) {
            node.setKey(i, i);
            node.setValue(i, new LinkedList<Address>());
        }
        node.setNumKeys(2);

        int deletePos = 1;
        node.deleteAndBorrowFromRight(deletePos, rightSibling);

        // Check that the last key in node has been set to 5
        assertEquals(5, node.getKey(node.getNumKeys() - 1));

        // Check that the first key in rightSibling has been shifted
        assertEquals(6, rightSibling.getKey(0));

        // Check that the last value in rightSibling has been set to null
        assertNull(rightSibling.getValue(node.getNumKeys()));

        // Check that the number of keys in node has been incremented
        assertEquals(Integer.MAX_VALUE, rightSibling.getKey(rightSibling.getNumKeys()));

        // Check that the number of keys in rightSibling has been decremented
        assertEquals(2, rightSibling.getNumKeys());
    }

    @Test
    public void testContainsKey() {
        LeafNode node = new LeafNode(5);

        // Add some keys and values to the node
        for (int i = 0; i < 4; i++) {
            node.addKey(i, new Address(i, i));
        }

        assertTrue(node.containsKey(2));
        assertFalse(node.containsKey(5));
    }

    @Test
    public void testBinarySearchInsertPos() {
        LeafNode node = new LeafNode(6);

        int keys[] = { 7, 13, 21, 34, 47 };

        // Add some keys and values to the node
        for (int i = 0; i < 5; i++) {
            node.addKey(keys[i], new Address(i, i));
        }

        assertEquals(3, node.binarySearchInsertPos(25));
        assertEquals(4, node.binarySearchInsertPos(52));
        assertEquals(0, node.binarySearchInsertPos(5));
    }

    @Test
    public void testBinarySearch() {
        LeafNode node = new LeafNode(6);

        int keys[] = { 7, 13, 21, 34, 47 };

        // Add some keys and values to the node
        for (int i = 0; i < 5; i++) {
            node.addKey(keys[i], new Address(i, i));
        }

        assertEquals(2, node.binarySearch(20));
        assertEquals(2, node.binarySearch(21));
        assertEquals(4, node.binarySearch(40));
        assertEquals(5, node.binarySearch(52));
        assertEquals(0, node.binarySearch(5));
    }

}
