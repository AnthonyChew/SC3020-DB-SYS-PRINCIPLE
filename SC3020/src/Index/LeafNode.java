package Index;

public class LeafNode extends Node {
    private int[] values; // Will be replaced with the Address array instead
    private LeafNode nextLeafNode;

    public LeafNode(int order) {
        super(order);
        this.values = new int[order - 1];
        this.nextLeafNode = null;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public int getKey(int index) {
        return this.keys[index];
    }

    public LeafNode getNextLeafNode() {
        return this.nextLeafNode;
    }

    public void setNextLeafNode(LeafNode nextLeafNode) {
        this.nextLeafNode = nextLeafNode;
    }

    // value will be replaced with Address obj
    public boolean addKey(int key, int value) {
        // If the node is full, return false
        if (this.isFull()) {
            return false;
        }

        // Find the index where the key should be inserted
        int index = 0;
        while (index < this.numKeys && this.keys[index] < key) {
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

        return true;
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

    // Split the leaf node and return the new leaf node
    /*
     * if numKeys is odd, mid + 1
     * 
     * 
     * 
     */
    public LeafNode splitLeafNode(int key, int value) {
        int mid = this.numKeys / 2;
        if (this.numKeys % 2 == 1) {
            mid++;
        }
        LeafNode newLeaf = new LeafNode(super.getOrder());
        this.nextLeafNode = newLeaf;

        // Copy the second half of the keys and values to the new leaf
        for (int i = mid; i < this.numKeys; i++) {
            newLeaf.keys[i - mid] = this.keys[i];
            newLeaf.values[i - mid] = this.values[i];
        }
        newLeaf.numKeys = this.numKeys - mid;
        this.numKeys = mid;

        // If the key belongs to the new leaf, insert it there
        if (key >= newLeaf.keys[0]) {
            newLeaf.addKey(key, value);
        } else {
            this.addKey(key, value);
        }

        return newLeaf;
    }

    public int getSubtreeLB() {
        return this.keys[0];
    }
}
