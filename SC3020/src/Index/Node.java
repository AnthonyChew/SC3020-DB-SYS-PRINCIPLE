package Index;

import java.util.Arrays;

import Disks.Address;
import Disks.Disk;

// An abstract class necessary so that InternalNode and LeafNode can be
// passed in as Node objects to the InternalNode constructor
public abstract class Node {
    private int order; // max keys in a node + 1
    protected int[] keys;
    protected int numKeys;
    protected InternalNode parent;
    protected int nodeIndex;

    public Node(int order) {
        this.order = order;
        this.parent = null;
        this.numKeys = 0;
        this.keys = new int[order - 1];
        Arrays.fill(this.keys, Integer.MAX_VALUE);
    }

    public int[] getKeys() {
        return keys;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public int getKey(int index) {
        return this.keys[index];
    }

    public void setKey(int index, int key) {
        this.keys[index] = key;
    }

    public int getOrder() {
        return order;
    }

    public InternalNode getParent() {
        return parent;
    }

    public void setParent(InternalNode parent) {
        this.parent = parent;
    }

    public boolean isFull() {
        return this.numKeys == this.order - 1;
    }

    public void insert(int key, Address value) {
        if (this instanceof InternalNode) {
            Node child = this.findChild(key);
            child.insert(key, value);
            return;
        }

        LeafNode _this = (LeafNode) this;
        _this.addKey(key, value);
    }

    public void query(int key, Disk disk) {
        int indexBlocks = 0;
        boolean isRootLeaf = this instanceof LeafNode;

        Node cur = this;
        while (cur instanceof InternalNode) {
            InternalNode _cur = (InternalNode) cur;
            cur = _cur.findChild(key);
            indexBlocks++;
        }

        LeafNode leaf = (LeafNode) cur;
        leaf.query(key, disk, (isRootLeaf ? 1 : indexBlocks + 1));
    }

    public void rangeQuery(int startKey, int endKey, Disk disk) {
        int indexBlocks = 0;
        boolean isRootLeaf = this instanceof LeafNode;

        Node cur = this;
        while (cur instanceof InternalNode) {
            InternalNode _cur = (InternalNode) cur;
            cur = _cur.findChild(startKey);
            indexBlocks++;
        }

        LeafNode leaf = (LeafNode) cur;
        leaf.rangeQuery(startKey, endKey, disk, (isRootLeaf ? 1 : indexBlocks + 1));
    }

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

    public Node findChild(int key) {
        InternalNode _this = (InternalNode) this;
        for (int i = 0; i < this.getNumKeys(); i++) {
            if (key < this.keys[i]) {
                return _this.getChild(i);
            }
        }
        return _this.getChild(this.getNumKeys());
    }

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

    public abstract int getSubtreeLB();
}
