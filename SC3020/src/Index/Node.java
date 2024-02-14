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

    public int[] getKeys() {
        return keys;
    }

    public int getNumKeys() {
        return this.numKeys;
    }

    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
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

    public void insert(int key, int value) {
        if (this instanceof InternalNode) {
            Node child = this.findChild(key);
            child.insert(key, value);
            return;
        }

        LeafNode _this = (LeafNode) this;
        _this.addKey(key, value);
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

    public abstract int getSubtreeLB();
}
