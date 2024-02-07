package Index;

public class InternalNode extends Node {
    private int[] keys;
    private Node[] children;
    private int numKeys;
    private int numChildren;

    public InternalNode(int order, int key, Node leftChild, Node rightChild) {
        super(order);
        this.keys = new int[order - 1];
        this.children = new Node[order];
        this.numKeys = 0;
        this.numChildren = 0;

        // Add the key
        this.keys[0] = key;
        this.numKeys++;
    
        // Add the left and right children
        this.children[0] = leftChild;
        this.children[1] = rightChild;
        this.numChildren += 2;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public int getNumChildren() {
        return this.numChildren;
    }

    public boolean isFull() {
        return this.numKeys == super.getOrder() - 1;
    }

    // When we insert at InternalNode, it means the leaf node has 
    // split and we need to add a new key to the parent node
    // Each addition of a key comes with a new right child
    // We need to add the key and the right child to the parent node
    public boolean addKey(int key, Node rightChild) {
        // If the node is full, return false
        if (this.isFull()) {
            return false;   // Will call split method instead
        }

        // Find the index where the key should be inserted
        int index = 0;
        while (index < this.numKeys && this.keys[index] < key) {
            index ++;
        }

        // Shift the keys and children to the right
        for (int i = this.numKeys - 1; i > index; i--) {
            this.keys[i] = this.keys[i - 1];
        }
        for (int i = this.numChildren - 1; i > index + 1; i--) {
            this.children[i] = this.children[i - 1];
        }

        // Add the key and the right child
        this.keys[index] = key;
        this.children[index + 1] = rightChild;
        this.numKeys ++;
        this.numChildren ++;

        return true;
    }

    // Split the internal node
    // Only called when the internal node is full
    public InternalNode splitInternalNode() {
        
    }
}