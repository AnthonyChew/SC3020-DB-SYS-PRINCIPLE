package Index;

// minimum -> (order - 1) // 2 keys
public class InternalNode extends Node {
    private Node[] children;
    private int numChildren;

    public InternalNode(int order) {
        super(order);
        this.children = new Node[order];
        this.numChildren = 0;
    }

    // Constructor for when the internal node is created from a split in the child
    public InternalNode(int order, int key, Node leftChild, Node rightChild) {
        super(order);
        this.children = new Node[order];
        this.numChildren = 0;

        // add the key
        this.setKey(0, key);
        this.numKeys++;

        // add the left and right children
        this.setChild(0, leftChild);
        this.setChild(1, rightChild);
        this.numChildren += 2;
    }

    public int getNumChildren() {
        return this.numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    public Node getChild(int index) {
        return this.children[index];
    }

    public void setChild(int index, Node child) {
        this.children[index] = child;
    }

    // When we insert at InternalNode, it means the leaf node has
    // split and we need to add a new key to the parent node
    // Each addition of a key comes with a new right child
    // We need to add the key and the right child to the parent node
    public void addKey(int key, Node rightChild) {
        if (this.isFull()) {
            splitInternalNode(rightChild.getSubtreeLB(), rightChild);
        } else {
            int index = 0;
            while (index < this.getNumKeys() && key > this.getKey(index)) {
                index++;
            }

            // Shift the keys and values to the right
            for (int i = this.numKeys - 1; i >= index; i--) {
                this.keys[i + 1] = this.keys[i];
                this.children[i + 2] = this.children[i + 1];
            }

            this.keys[index] = key;
            this.children[index + 1] = rightChild;
            this.numKeys++;
            this.numChildren++;
            rightChild.setParent(this);
        }

        // update keys
        this.updateKeys();
    }

    public void updateKeys() {
        for (int i = 0; i < this.numKeys; i++) {
            this.keys[i] = this.children[i + 1].getSubtreeLB();
        }
    }

    public void addKeyToNewInternalNode(int insertPos, int key, Node rightChild) {
        return;
    }

    public void splitInternalNode(int key, Node rightChild) {
        int mid = ((this.getOrder() - 1) / 2) - 1;

        // find insert pos
        int index = 0;
        while (index < this.getNumKeys() && key > this.getKey(index)) {
            index++;
        }
        InternalNode newInternalNode = new InternalNode(this.getOrder());

        // first half including mid -> start copying from mid over to new node
        if (index <= mid) {
            for (int i = mid, j = 0; i < this.getNumKeys(); i++, j++) {
                newInternalNode.setKey(j, this.keys[i]);
                newInternalNode.setChild(j, this.children[i + 1]);
                newInternalNode.getChild(j).setParent(newInternalNode);
            }
            newInternalNode.setNumKeys(this.numKeys - mid);
            newInternalNode.setNumChildren(this.numKeys - mid);
            this.numKeys = mid;
            this.numChildren = mid + 1;
            this.addKey(key, rightChild);
        } else { // [SPECIAL] second half -> copy everything after mid over to new node to insert
            for (int i = mid + 1, j = 0; i < this.numKeys; i++, j++) {
                newInternalNode.setKey(j, this.keys[i]);
                // insert at start -> leave a ptr space at index 0 for new child node
                if (index == mid + 1) {
                    newInternalNode.setChild(j + 1, this.children[i + 1]);
                    newInternalNode.getChild(j + 1).setParent(newInternalNode);
                } else { // insert in middle -> copy all and find slot to insert
                    newInternalNode.setChild(j, this.children[i + 1]);
                    newInternalNode.getChild(j).setParent(newInternalNode);
                }
            }

            // first spot
            if (index == mid + 1) {
                newInternalNode.setChild(0, rightChild);
            } else if (index == this.numKeys) { // last spot
                newInternalNode.setKey(this.numKeys - (mid + 1) - 1, key);
                newInternalNode.setChild(this.numKeys - (mid + 1), rightChild);
                newInternalNode.setNumKeys(this.numKeys - (mid + 1));
            } else { // middle -> shift
                for (int i = index - (mid + 1); i < this.numKeys - (mid + 1); i++) {
                    newInternalNode.setKey(i + 1, newInternalNode.getKey(i));
                    newInternalNode.setChild(i + 2, newInternalNode.getChild(i + 1));
                }
                newInternalNode.setKey(index - (mid + 1), key);
                newInternalNode.setChild(index - (mid + 1) + 1, rightChild);
                newInternalNode.setNumKeys(this.numKeys - (mid + 1) + 1);
            }
            rightChild.setParent(newInternalNode);

            this.numKeys = mid + 1;
            this.numChildren = mid + 2;
            newInternalNode.setNumChildren(this.numKeys - (mid + 1) + 2);
        }

        if (this.getParent() == null) {
            InternalNode parentNode = new InternalNode(this.getOrder());
            this.setParent(parentNode);
            newInternalNode.setParent(parentNode);

            parentNode.setChild(0, this);
            parentNode.setChild(1, newInternalNode);
            parentNode.setKey(0, newInternalNode.getSubtreeLB());
            parentNode.setNumChildren(2);
            parentNode.setNumKeys(1);
        } else {
            this.getParent().addKey(newInternalNode.getSubtreeLB(), newInternalNode);
        }
    }

    public int getSubtreeLB() {
        return this.children[0].getSubtreeLB();
    }

}