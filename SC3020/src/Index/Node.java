package Index;

// An abstract class necessary so that InternalNode and LeafNode can be
// passed in as Node objects to the InternalNode constructor
public abstract class Node {
    private int order; // max keys in a node + 1
    protected int[] keys;
    protected int numKeys;
    protected InternalNode parent;

    public Node(int order) {
        this.order = order;
        this.parent = null;
        this.numKeys = 0;
        this.keys = new int[order - 1];
    }

    public int getKey(int index) {
        return keys[index];
    }

    public int[] getKeys() {
        return keys;
    }

    public int getOrder() {
        return order;
    }

    public int getNumKeys() {
        return numKeys;
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

    public abstract int getSubtreeLB();
}
