package Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Disks.Address;

// minimum -> (order) // 2 keys
public class LeafNode extends Node {
    // private int[] values; // Will be replaced with the Address array instead
    private List<Address>[] values;
    private LeafNode nextLeafNode;

    public LeafNode(int order) {
        super(order);
        this.values = new List[order - 1];
        Arrays.fill(this.values, new LinkedList<Address>());
        this.nextLeafNode = null;
    }

    public List<Address> getValue(int index) {
        return this.values[index];
    }

    public void setValue(int index, List<Address> value) {
        this.values[index] = value;
    }

    public List<Address>[] getValues() {
        return this.values;
    }

    public LeafNode getNextLeafNode() {
        return this.nextLeafNode;
    }

    public void setNextLeafNode(LeafNode nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    public boolean containsKey(int key) {
        for (int i = 0; i < this.numKeys; i++) {
            if (this.keys[i] == key) {
                return true;
            }
        }
        return false;
    }

    public void addKey(int key, Address value) {
        if (this.isFull() && !this.containsKey(key)) {
            this.splitLeafNode(key, value);
            return;
        }

        // find the index where the key should be inserted
        int index = 0;
        while (index < this.numKeys && key > this.keys[index]) {
            if (key == this.keys[index])
                break;
            index++;
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

    public void splitLeafNode(int key, Address value) {
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
            newLeafNode.setNextLeafNode(this.nextLeafNode);
        }
        this.nextLeafNode = newLeafNode;

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

        // create parent if no parent
        if (this.getParent() == null) {
            InternalNode parent = new InternalNode(this.getOrder(), newLeafNode.getSubtreeLB(), this, newLeafNode);
            this.parent = parent;
            newLeafNode.setParent(parent);
            return;
        }
        this.getParent().addKey(newLeafNode.getSubtreeLB(), newLeafNode);
    }

    public boolean deleteKey(int key) {
        int index = this.binarySearch(key);
    }

    public int getSubtreeLB() {
        return this.getKey(0);
    }
}
