package Index;

public class InternalNode extends Node {
    private Node[] children;
    private int numChildren;

    public InternalNode(int order, int key, Node leftChild, Node rightChild) {
        super(order);
        this.children = new Node[order];
        this.numChildren = 0;

        // Add the key
        this.keys[0] = key;
        this.numKeys++;

        // Add the left and right children
        this.children[0] = leftChild;
        this.children[1] = rightChild;
        this.numChildren += 2;
    }

    public InternalNode(int order) {
        super(order);
        this.keys = new int[order - 1];
        this.children = new Node[order];
        this.numChildren = 0;
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

    public Node getChild(int index) {
        return this.children[index];
    }

    // When we insert at InternalNode, it means the leaf node has
    // split and we need to add a new key to the parent node
    // Each addition of a key comes with a new right child
    // We need to add the key and the right child to the parent node
    public boolean addKey(int key, Node rightChild) {
        // If the node is full, return false
        if (this.isFull()) {
            return false; // Will call split method instead
        }

        // Find the index where the key should be inserted
        int index = 0;
        while (index < this.numKeys && this.keys[index] < key) {
            index++;
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
        this.numKeys++;
        this.numChildren++;

        return true;
    }

    // Split the internal node
    // Only called when the internal node is full
    public InternalNode splitInternalNode(int key, Node rightChild) {
        // find insert index
        int index = 0;
        while (index < this.numKeys && this.keys[index] <= key) {
            // duplicate key
            if (this.keys[index] == key)
                break;
            index++;
        }
        InternalNode rightNode = new InternalNode(super.getOrder());
        int mid = this.numKeys / 2;

        // insert into left node
        if (index <= mid) {
            // copy over to right node starting from mid
            for (int i = mid, j = 0; i < this.numKeys + 1; i++, j++) {
                if (i < this.numKeys)
                    rightNode.keys[j] = this.keys[i];

                rightNode.children[j] = this.children[i];
                rightNode.children[j].setParent(rightNode);
            }

            // Shift the keys and children to the right
            for (int i = this.numKeys - 1; i > index; i--) {
                this.keys[i] = this.keys[i - 1];
            }
            for (int i = this.numChildren - 1; i > index + 1; i--) {
                this.children[i] = this.children[i - 1];
            }

            // inserting of new key and updating data pointer
            this.keys[index] = key;
            this.children[index + 1] = rightChild;
        } else { // insert into right node
            // boolean inserted = false; // check whether the new key has been inserted
            // already
            // for (int i = mid, j = 0; i <= this.numKeys; i++, j++) {
            // // else if inserted child is somewhere in the middle of the new right node,
            // need
            // // copy both pointer and key
            // if (index == i && !inserted) {
            // if (i == this.numKeys) {
            // rightNode.children[j] = rightNode;
            // rightNode.children[j].setParent(rightNode);
            // } else {
            // rightNode.children[j] = rightNode;
            // rightNode.children[j].setParent(rightNode);
            // rightNode.keys[j] = rightNode.keys[j - 1];
            // rightNode.keys[j - 1] = key;
            // i--;
            // inserted = true;
            // }
            // } else {
            // rightNode.children[j] = this.children[i + 1];
            // rightNode.children[j].setParent(rightNode);
            // if (i + 1 == this.numKeys)
            // break;
            // if (i + 1 < this.numKeys)
            // rightNode.keys[j] = this.keys[i + 1];
            // }
            // }
        }
        return rightNode;
    }

    public int getSubtreeLB() {
        return this.children[0].getSubtreeLB();
    }
}