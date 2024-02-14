package Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// minimum -> (order) // 2 keys
public class LeafNode extends Node {
    private int[] values; // Will be replaced with the Address array instead
    private LeafNode nextLeafNode;

    public LeafNode(int order) {
        super(order);
        this.values = new int[order - 1];
        this.nextLeafNode = null;
    }

    public int getValue(int index) {
        return this.values[index];
    }

    public void setValue(int index, int value) {
        this.values[index] = value;
    }

    public int[] getValues() {
        return this.values;
    }

    public LeafNode getNextLeafNode() {
        return this.nextLeafNode;
    }

    public void setNextLeafNode(LeafNode nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    // value will be replaced with Address obj
    public void addKey(int key, int value) {
        if (this.isFull()) {
            this.splitLeafNode(key, value);
            return;
        }

        // Find the index where the key should be inserted
        int index = 0;
        while (index < this.numKeys && key > this.keys[index]) {
            index++;
        }

        // Shift the keys and values to the right
        for (int i = this.numKeys - 1; i >= index; i--) {
            this.keys[i + 1] = this.keys[i];
            this.values[i + 1] = this.values[i];
        }

        // Insert the key and value
        this.keys[index] = key;
        this.values[index] = value;
        this.numKeys++;
    }

    // Search for the Address mapped to the given key
    public int binarySearch(int key) { // Will be changed to return Address obj instead
        int left = 0;
        int right = this.numKeys - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (this.keys[mid] == key) {
                return mid;
            } else if (this.keys[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public void splitLeafNode(int key, int value) {
        // index of min number of nodes in a leaf
        int mid = (this.getOrder() / 2) - 1;

        // find insert pos
        int index = 0;
        while (index < this.numKeys && key > this.keys[index]) {
            index++;
        }

        // set new leaf node
        LeafNode newLeafNode = new LeafNode(this.getOrder());
        if (this.nextLeafNode != null) {
            this.nextLeafNode.setNextLeafNode(newLeafNode);
        }
        this.nextLeafNode = newLeafNode;

        // NOTE: numKeys == order - 1
        // first half including mid -> start copying from mid over to new leaf
        if (index <= mid) {
            for (int i = mid, j = 0; i < this.numKeys; i++, j++) {
                newLeafNode.setKey(j, this.keys[i]);
                newLeafNode.setValue(j, this.values[i]);
            }
            newLeafNode.setNumKeys(this.numKeys - mid);
            this.numKeys = mid;
            this.addKey(key, value);
        } else { // second half -> copy everything after mid over to new leaf
            for (int i = mid + 1, j = 0; i < this.numKeys; i++, j++) {
                newLeafNode.setKey(j, this.keys[i]);
                newLeafNode.setValue(j, this.values[i]);
            }
            newLeafNode.setNumKeys(this.numKeys - (mid + 1));
            this.numKeys = mid + 1;
            newLeafNode.addKey(key, value);
        }

        if (this.getParent() == null) {
            InternalNode parent = new InternalNode(this.getOrder(), newLeafNode.getSubtreeLB(), this, newLeafNode);
            this.parent = parent;
            newLeafNode.setParent(parent);
        } else {
            this.getParent().addKey(newLeafNode.getSubtreeLB(), newLeafNode);
        }
    }

    public int getSubtreeLB() {
        return this.getKey(0);
    }
}
