package Index;

import Disks.Address;
import Disks.Disk;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// minimum -> (order) // 2 keys
/**
 * Represents a leaf node in a B+ tree.
 */
public class LeafNode extends Node {
    // private int[] values; // Will be replaced with the Address array instead
    private final int MIN_KEYS = this.getOrder() / 2;
    private List<Address>[] values;
    private LeafNode nextLeafNode;

    /**
     * Constructor for LeafNode.
     * 
     * @param order The order of the B+ tree.
     */
    public LeafNode(int order) {
        super(order);
        this.values = new List[order - 1];
        Arrays.fill(this.values, new LinkedList<Address>());
        this.nextLeafNode = null;
    }

    /**
     * Returns the value at the specified index.
     * 
     * @param index The index of the value to return.
     * @return The value at the specified index.
     */
    public List<Address> getValue(int index) {
        return this.values[index];
    }

    /**
     * Sets the value at the specified index.
     * 
     * @param index The index at which the value should be set.
     * @param value The value to set.
     */
    public void setValue(int index, List<Address> value) {
        this.values[index] = value;
    }

    /**
     * Returns all values in the node.
     * 
     * @return An array of all values in the node.
     */
    public List<Address>[] getValues() {
        return this.values;
    }

    /**
     * Returns the next leaf node.
     * 
     * @return The next leaf node.
     */
    public LeafNode getNextLeafNode() {
        return this.nextLeafNode;
    }

    /**
     * Sets the next leaf node.
     * 
     * @param nextLeafNode The next leaf node.
     */
    public void setNextLeafNode(LeafNode nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    /**
     * Checks if the node contains the specified key.
     * 
     * @param key The key to check.
     * @return True if the node contains the key, false otherwise.
     */
    public boolean containsKey(int key) {
        for (int i = 0; i < this.numKeys; i++) {
            if (this.keys[i] == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a key-value pair to the node.
     * 
     * @param key   The key to add.
     * @param value The value to add.
     */
    public void addKey(int key, Address value) {
        if (this.isFull() && !this.containsKey(key)) {
            this.splitLeafNode(key, value);
            return;
        }

        // find the index where the key should be inserted
        int index = binarySearchInsertPos(key);
        if (index == this.getOrder() - 1) {
            System.out.println("Error: Index out of bounds");
        }

        // duplicate -> add to linked list
        if (this.keys[index] == key) {
            this.values[index].add(value);
            return;
        }

        // shift the keys and values to the right
        for (int i = this.numKeys - 1; i >= index; i--) {
            this.keys[i + 1] = this.keys[i];
            this.values[i + 1] = this.values[i];
        }

        // insert the key and value
        this.keys[index] = key;
        this.values[index] = new LinkedList<Address>();
        this.values[index].add(value);
        this.numKeys++;
    }

    /**
     * Performs a binary search for the specified key.
     * 
     * @param key The key to search for.
     * @return The index of the first key greater than or equal to the specified
     *         key.
     */
    public int binarySearch(int key) {
        // returns index of first key greater than or equal to key
        int left = 0;
        int right = this.numKeys - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (this.keys[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /**
     * Splits the leaf node.
     * 
     * @param key   The key to add.
     * @param value The value to add.
     */
    public void splitLeafNode(int key, Address value) {
        // index of min number of nodes in a leaf
        int mid = this.MIN_KEYS - 1;

        int index = binarySearchInsertPos(key);

        // set new leaf node
        LeafNode newLeafNode = new LeafNode(this.getOrder());
        newLeafNode.setNextLeafNode(this.nextLeafNode);
        this.nextLeafNode = newLeafNode;

        // first half including mid -> start copying from mid over to new leaf
        if (index <= mid) {
            for (int i = mid, j = 0; i < this.numKeys; i++, j++) {
                newLeafNode.setKey(j, this.keys[i]);
                newLeafNode.setValue(j, this.values[i]);

                this.keys[i] = Integer.MAX_VALUE;
                this.values[i] = null;
            }
            newLeafNode.setNumKeys(this.numKeys - mid);
            this.numKeys = mid;
            this.addKey(key, value);
        } else { // second half -> copy everything after mid over to new leaf
            for (int i = mid + 1, j = 0; i < this.numKeys; i++, j++) {
                newLeafNode.setKey(j, this.keys[i]);
                newLeafNode.setValue(j, this.values[i]);

                this.keys[i] = Integer.MAX_VALUE;
                this.values[i] = null;
            }
            newLeafNode.setNumKeys(this.numKeys - (mid + 1));
            this.numKeys = mid + 1;
            newLeafNode.addKey(key, value);
        }

        // create parent if no parent
        if (this.getParent() == null) {
            InternalNode parent = new InternalNode(this.getOrder(), newLeafNode.getSubtreeLB(), this, newLeafNode);
            this.parent = parent;
            newLeafNode.setParent(parent);
            return;
        }
        this.getParent().addKey(newLeafNode.getSubtreeLB(), newLeafNode);
    }

    /**
     * Deletes a key from the node.
     * 
     * @param key          The key to delete.
     * @param disk         The disk from which to delete the key.
     * @param leftSibling  The left sibling of the node.
     * @param rightSibling The right sibling of the node.
     * @return True if the key was deleted, false otherwise.
     */
    public boolean deleteKey(int key, Disk disk, Node leftSibling, Node rightSibling) {
        LeafNode _leftSibling = (LeafNode) leftSibling;
        LeafNode _rightSibling = (LeafNode) rightSibling;

        int index = binarySearch(key);
        if (index == this.numKeys)
            return false;

        int numDataBlocks = 0;
        List<Address> addresses = this.values[index];
        for (Address address : addresses) {
            // System.out.println("Deleting record with address: " + address);
            disk.deleteRecord(address);
            numDataBlocks++;
        }
        System.out.println("Number of data blocks accessed: " + numDataBlocks + " blocks.");
        System.out.println("Number of records deleted: " + numDataBlocks + " records.");

        // case 1: simple delete
        if (this.numKeys - 1 >= this.MIN_KEYS || this.getParent() == null) {
            deleteAndShiftLeft(index);
            this.numKeys--;
            return true;
        }

        // case 2: borrow from left sibling
        if (leftSibling != null && leftSibling.getNumKeys() - 1 >= this.MIN_KEYS) {
            this.deleteAndBorrowFromLeft(index, _leftSibling);
        } else if (rightSibling != null && rightSibling.getNumKeys() - 1 >= this.MIN_KEYS) {
            this.deleteAndBorrowFromRight(index, _rightSibling);
        } else if (leftSibling != null) { // case 3: merge with left sibling
            this.mergeWithLeft(index, _leftSibling);
        } else if (rightSibling != null) { // case 4: merge with right sibling
            this.mergeWithRight(index, _rightSibling);
        }

        return true;
    }

    /**
     * Deletes a key and shifts all keys to the left.
     * 
     * @param deletePos The position of the key to delete.
     */
    public void deleteAndShiftLeft(int deletePos) {
        for (int i = deletePos; i < this.numKeys - 1; i++) {
            this.keys[i] = this.keys[i + 1];
            this.values[i] = this.values[i + 1];
        }
        this.keys[numKeys - 1] = Integer.MAX_VALUE;
        this.values[numKeys - 1] = null;
    }

    /**
     * Deletes a key and shifts all keys to the right.
     * 
     * @param deletePos The position of the key to delete.
     */
    public void deleteAndShiftRight(int deletePos) {
        for (int i = deletePos; i > 0; i--) {
            this.keys[i] = this.keys[i - 1];
            this.values[i] = this.values[i - 1];
        }
    }

    /**
     * Deletes a key and borrows a key from the left sibling.
     * 
     * @param deletePos   The position of the key to delete.
     * @param leftSibling The left sibling of the node.
     */
    public void deleteAndBorrowFromLeft(int deletePos, LeafNode leftSibling) {
        this.deleteAndShiftRight(deletePos);
        this.keys[0] = leftSibling.getKey(leftSibling.getNumKeys() - 1);
        this.values[0] = leftSibling.getValue(leftSibling.getNumKeys() - 1);
        leftSibling.setKey(leftSibling.getNumKeys() - 1, Integer.MAX_VALUE);
        leftSibling.setValue(leftSibling.getNumKeys() - 1, null);
        leftSibling.setNumKeys(leftSibling.getNumKeys() - 1);
    }

    /**
     * Deletes a key and borrows a key from the right sibling.
     * 
     * @param deletePos    The position of the key to delete.
     * @param rightSibling The right sibling of the node.
     */
    public void deleteAndBorrowFromRight(int deletePos, LeafNode rightSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.keys[this.numKeys - 1] = rightSibling.getKey(0);
        this.values[this.numKeys - 1] = rightSibling.getValue(0);
        rightSibling.deleteAndShiftLeft(0);
        rightSibling.setNumKeys(rightSibling.getNumKeys() - 1);
    }

    /**
     * Merges the node with its left sibling.
     * 
     * @param deletePos   The position of the key to delete.
     * @param leftSibling The left sibling of the node.
     */
    public void mergeWithLeft(int deletePos, LeafNode leftSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        for (int i = 0; i < this.numKeys; i++) {
            leftSibling.setKey(leftSibling.getNumKeys() + i, this.keys[i]);
            leftSibling.setValue(leftSibling.getNumKeys() + i, this.values[i]);
        }
        leftSibling.setNextLeafNode(this.nextLeafNode);
        leftSibling.setNumKeys(leftSibling.getNumKeys() + this.numKeys);
        this.numKeys = 0;
        this.setParent(null);
    }

    /**
     * Merges the node with its right sibling.
     * 
     * @param deletePos    The position of the key to delete.
     * @param rightSibling The right sibling of the node.
     */
    public void mergeWithRight(int deletePos, LeafNode rightSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        for (int i = 0; i < rightSibling.getNumKeys(); i++) {
            this.keys[this.numKeys + i] = rightSibling.getKey(i);
            this.values[this.numKeys + i] = rightSibling.getValue(i);
        }
        this.nextLeafNode = rightSibling.getNextLeafNode();
        this.numKeys += rightSibling.getNumKeys();
        rightSibling.setNumKeys(0);
        rightSibling.setParent(null);
    }

    /**
     * Returns the left sibling of this leaf node.
     * 
     * @return The left sibling of this leaf node, or null if this node is the
     *         leftmost node.
     */
    public LeafNode getLeftSibling() {
        InternalNode parent = this.getParent();

        while (parent.getParent() != null && parent.getParent().getChild(0) == parent) {
            parent = parent.getParent();
        }

        // This means this leafnode is the leftmost leaf node and wont have any left
        // siblings
        if (parent.getParent() == null) {
            return null;
        }

        int index = parent.getParent().getChildIndex(parent);
        return ((InternalNode) parent.getParent().getChild(index - 1)).getRightMostLeafNode();
    }

    /**
     * Performs a query for a specific key in the B+ tree.
     * 
     * @param key                The key to search for.
     * @param disk               The disk to search on.
     * @param indexNodesAccessed The number of index nodes accessed during the
     *                           search.
     */
    public void query(int key, Disk disk, int indexNodesAccessed) {
        int index = binarySearch(key);
        if (index == this.numKeys) {
            System.out.println("Query with key: " + key + " not found.");
            return;
        }

        float sum = 0;
        int dataBlocks = 0;
        List<Address> addresses = this.values[index];
        for (Address address : addresses) {
            sum += disk.getRecord(address).getRecordData().getAverageRating();
            dataBlocks++;
        }
        float average = sum / dataBlocks;

        System.out.println("Number of records with numVotes = 500: " + dataBlocks + " records.");
        System.out.println("Number of index nodes accessed: " + indexNodesAccessed + " nodes.");
        System.out.println("Number of data blocks accessed: " + dataBlocks + " blocks.");
        System.out.println("Average of average ratings: " + average);
    }

    /**
     * Performs a range query for a specific range of keys in the B+ tree.
     * 
     * @param startKey           The start key of the range.
     * @param endKey             The end key of the range.
     * @param disk               The disk to search on.
     * @param indexNodesAccessed The number of index nodes accessed during the
     *                           search.
     */
    public void rangeQuery(int startKey, int endKey, Disk disk, int indexNodesAccessed) {
        int index = binarySearch(startKey);
        if (index == this.numKeys) {
            System.out.println("Query with start key: " + startKey + " not found.");
            return;
        }

        float sum = 0;
        int dataBlocks = 0;
        LeafNode cur = this;
        while (cur != null && cur.getKey(index) <= endKey) {
            List<Address> addresses = cur.getValue(index);
            for (Address address : addresses) {
                sum += disk.getRecord(address).getRecordData().getAverageRating();
                dataBlocks++;
            }
            index++;

            if (index == cur.getNumKeys()) {
                cur = cur.getNextLeafNode();
                index = 0;
                indexNodesAccessed++;
            }
        }
        float average = sum / dataBlocks;

        System.out.println(
                "Number of records with " + startKey + " <= numVotes <= " + endKey + ": " + dataBlocks + " records.");
        System.out.println("Number of index nodes accessed: " + indexNodesAccessed + " blocks.");
        System.out.println("Number of data blocks accessed: " + dataBlocks + " blocks.");
        // System.out.println("Sum of average ratings: " + sum);
        System.out.println("Average of average ratings: " + average);
    }

    /**
     * Returns the lower bound of the subtree rooted at this node.
     * 
     * @return The key of the first key-value pair in the subtree rooted at this
     *         node.
     */
    public int getSubtreeLB() {
        return this.getKey(0);
    }
}
