package Index;

import Disks.Address;
import Disks.Disk;

import java.util.Arrays;

// An abstract class necessary so that InternalNode and LeafNode can be
// passed in as Node objects to the InternalNode constructor
import java.util.Arrays;

/**
 * The abstract class representing a node in the index tree.
 */
public abstract class Node {
    protected int[] keys;
    protected int numKeys;
    protected InternalNode parent;
    protected int nodeIndex;
    private int order; // max keys in a node + 1

    /**
     * Constructs a new Node object with the specified order.
     *
     * @param order the maximum number of keys in a node + 1
     */
    public Node(int order) {
        this.order = order;
        this.parent = null;
        this.numKeys = 0;
        this.keys = new int[order - 1];
        Arrays.fill(this.keys, Integer.MAX_VALUE);
    }

    /**
     * Returns the keys in the node.
     *
     * @return the keys in the node
     */
    public int[] getKeys() {
        return keys;
    }

    /**
     * Returns the number of keys in the node.
     *
     * @return the number of keys in the node
     */
    public int getNumKeys() {
        return this.numKeys;
    }

    /**
     * Sets the number of keys in the node.
     *
     * @param numKeys the number of keys to set
     */
    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
    }

    /**
     * Returns the index of the node.
     *
     * @return the index of the node
     */
    public int getNodeIndex() {
        return nodeIndex;
    }

    /**
     * Sets the index of the node.
     *
     * @param nodeIndex the index to set
     */
    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    /**
     * Returns the key at the specified index.
     *
     * @param index the index of the key
     * @return the key at the specified index
     */
    public int getKey(int index) {
        return this.keys[index];
    }

    /**
     * Sets the key at the specified index.
     *
     * @param index the index of the key
     * @param key   the key to set
     */
    public void setKey(int index, int key) {
        this.keys[index] = key;
    }

    /**
     * Returns the order of the node.
     *
     * @return the order of the node
     */
    public int getOrder() {
        return order;
    }

    /**
     * Returns the parent node.
     *
     * @return the parent node
     */
    public InternalNode getParent() {
        return parent;
    }

    /**
     * Sets the parent node.
     *
     * @param parent the parent node to set
     */
    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    /**
     * Checks if the node is full.
     *
     * @return true if the node is full, false otherwise
     */
    public boolean isFull() {
        return this.numKeys == this.order - 1;
    }

    /**
     * Inserts a key-value pair into the node.
     *
     * @param key   the key to insert
     * @param value the value to insert
     */
    public void insert(int key, Address value) {
        if (this instanceof InternalNode) {
            Node child = this.findChild(key);
            child.insert(key, value);
            return;
        }

        LeafNode _this = (LeafNode) this;
        _this.addKey(key, value);
    }

    /**
     * Queries the node for a key and writes the result to the disk.
     *
     * @param key  the key to query
     * @param disk the disk to write the result to
     */
    public void query(int key, Disk disk) {
        int indexNodes = 0;
        boolean isRootLeaf = this instanceof LeafNode;

        Node cur = this;
        while (cur instanceof InternalNode) {
            InternalNode _cur = (InternalNode) cur;
            cur = _cur.findChild(key);
            indexNodes++;
        }

        LeafNode leaf = (LeafNode) cur;
        leaf.query(key, disk, (isRootLeaf ? 1 : indexNodes + 1));
    }

    /**
     * Performs a range query on the node and writes the result to the disk.
     *
     * @param startKey the starting key of the range
     * @param endKey   the ending key of the range
     * @param disk     the disk to write the result to
     */
    public void rangeQuery(int startKey, int endKey, Disk disk) {
        int indexNodes = 0;
        boolean isRootLeaf = this instanceof LeafNode;

        Node cur = this;
        while (cur instanceof InternalNode) {
            InternalNode _cur = (InternalNode) cur;
            cur = _cur.findChild(startKey);
            indexNodes++;
        }

        LeafNode leaf = (LeafNode) cur;
        leaf.rangeQuery(startKey, endKey, disk, (isRootLeaf ? 1 : indexNodes + 1));
    }

    /**
     * Deletes a key from the node and updates the disk.
     *
     * @param key  the key to delete
     * @param disk the disk to update
     * @return true if the key was deleted, false otherwise
     */
    public boolean delete(int key, Disk disk) {
        boolean deleted = false;
        if (this instanceof InternalNode) {
            InternalNode _this = (InternalNode) this;

            // At k - 1 level
            if (_this.getChild(0) instanceof LeafNode) {
                // The leaf node that contains the key
                LeafNode child = (LeafNode) _this.findChild(key);
                LeafNode leftSibling;

                // Find the left sibling
                // If not leftmost, then left sibling within the same parent at k - 1 level
                int childIndex = child.getNodeIndex();
                if (childIndex > 0) {
                    leftSibling = (LeafNode) _this.getChild(childIndex - 1);
                } else { // Otherwise, call getLeftSibling() to retrieve the left sibling from another
                    // parent
                    leftSibling = child.getLeftSibling();
                }
                deleted = child.deleteKey(key, disk, leftSibling, child.getNextLeafNode());
                _this.rebalance();
                return deleted;
            }

            // At other levels other than k - 1 and leaf node level
            InternalNode child = (InternalNode) _this.findChild(key);
            deleted = child.delete(key, disk);
            _this.rebalance();
            return deleted;
        }

        LeafNode _this = (LeafNode) this;
        return _this.deleteKey(key, disk, null, null);
    }

    /**
     * Finds the child node that contains the specified key.
     *
     * @param key the key to find
     * @return the child node that contains the key
     */
    public Node findChild(int key) {
        InternalNode _this = (InternalNode) this;
        for (int i = 0; i < this.getNumKeys(); i++) {
            if (key < this.keys[i]) {
                return _this.getChild(i);
            }
        }
        return _this.getChild(this.getNumKeys());
    }

    /**
     * Performs a binary search to find the insertion position for a key.
     *
     * @param key the key to insert
     * @return the insertion position for the key
     */
    public int binarySearchInsertPos(int key) {
        int left = 0;
        int right = this.numKeys;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (this.keys[mid] < key) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    /**
     * Returns the lower bound of the subtree rooted at this node.
     *
     * @return the lower bound of the subtree
     */
    public abstract int getSubtreeLB();
}
