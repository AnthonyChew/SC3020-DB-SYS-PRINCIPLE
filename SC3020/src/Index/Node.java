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

    public Node insert(int key, int value) {
        while (this instanceof InternalNode) {
            Node child = this.findChild(key); // func in intenral node
            Node newChild = child.insert(key, value); // new child created by splitting full child

            if (newChild == null) {
                return null;
            }

            InternalNode _this = (InternalNode) this;
            boolean isFull = _this.addKey(newChild.getKey(0), newChild);
            if (isFull) {
                Node newInternalNode = _this.splitInternalNode(newChild.getKey(0), newChild);
                return newInternalNode;
            } else {
                return null;
            }
        }

        LeafNode _this = (LeafNode) this;
        boolean isNotFull = _this.addKey(key, value);
        if (isNotFull) {
            return null;
        } else {
            Node newChild = _this.splitLeafNode(key, value);
            return newChild;
        }
    }

    public Node findChild(int key) {
        for (int i = 0; i < this.numKeys; i++) {
            if (key >= this.keys[i]) {
                InternalNode _this = (InternalNode) this;
                return _this.getChild(i + 1);
            }
        }
        return null;
    }
}
