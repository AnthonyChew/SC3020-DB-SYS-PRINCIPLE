package Index;

// minimum -> (order - 1) // 2 keys
public class InternalNode extends Node {
    private final int MIN_KEYS = (this.getOrder() - 1) / 2;
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
        child.setNodeIndex(index);
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
                this.children[i + 1].setNodeIndex(i + 2);
            }

            this.keys[index] = key;
            this.children[index + 1] = rightChild;
            this.numKeys++;
            this.numChildren++;
            rightChild.setParent(this);
            rightChild.setNodeIndex(index + 1);
        }

        // update keys as LB may change after insert
        this.updateKeys();
    }

    public void updateKeys() {
        for (int i = 0; i < this.numKeys; i++) {
            this.keys[i] = this.children[i + 1].getSubtreeLB();
        }
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
                if (i + 1 < this.numKeys) // mid will be promoted
                    newInternalNode.setKey(j, this.keys[i + 1]);
                newInternalNode.setChild(j, this.children[i + 1]);
                newInternalNode.getChild(j).setParent(newInternalNode);
            }

            newInternalNode.setNumKeys(this.numKeys - mid - 1);
            newInternalNode.setNumChildren(this.numKeys - mid);
            this.numKeys = mid;
            this.numChildren = mid + 1;
            this.addKey(key, rightChild);
        } else { // second half -> copy everything after mid over to new node to insert
            for (int i = mid + 1, j = 0; i < this.numKeys; i++, j++) {
                // insert at start -> leave a ptr space at index 0 for new child node
                if (index == mid + 1) {
                    newInternalNode.setKey(j, this.keys[i]);
                    newInternalNode.setChild(j + 1, this.children[i + 1]);
                    newInternalNode.getChild(j + 1).setParent(newInternalNode);
                } else { // insert in middle -> copy all except first key and find slot to insert
                    if (i + 1 < this.numKeys) // (mid + 1) will be promoted
                        newInternalNode.setKey(j, this.keys[i + 1]);
                    newInternalNode.setChild(j, this.children[i + 1]);
                    newInternalNode.getChild(j).setParent(newInternalNode);
                }
            }

            // first spot
            if (index == mid + 1) {
                newInternalNode.setChild(0, rightChild);
                newInternalNode.setNumKeys(this.numKeys - (mid + 1));
            } else if (index == this.numKeys) { // last spot
                newInternalNode.setKey(this.numKeys - (mid + 1) - 1, key);
                newInternalNode.setChild(this.numKeys - (mid + 1), rightChild);
                newInternalNode.setNumKeys(this.numKeys - (mid + 1));
            } else { // middle -> shift
                for (int i = index - (mid + 1) - 1; i < this.numKeys - (mid + 1) - 1; i++) {
                    newInternalNode.setKey(i + 1, newInternalNode.getKey(i));
                    newInternalNode.setChild(i + 2, newInternalNode.getChild(i + 1));
                }
                newInternalNode.setKey(index - (mid + 1) - 1, key);
                newInternalNode.setChild(index - (mid + 1), rightChild);
                newInternalNode.setNumKeys(this.numKeys - (mid + 1));
            }
            rightChild.setParent(newInternalNode);

            newInternalNode.setNumChildren(newInternalNode.getNumKeys() + 1);
            this.numKeys = mid + 1;
            this.numChildren = mid + 2;
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
            return;
        }
        this.getParent().addKey(newInternalNode.getSubtreeLB(), newInternalNode);
    }

    public LeafNode getRightMostLeafNode() {
        Node target = this.getChild(this.numChildren - 1);

        // keep going right
        while (target instanceof InternalNode) {
            InternalNode _target = (InternalNode) target;
            target = _target.getChild(_target.getNumChildren() - 1);
        }
        return (LeafNode) target;
    }

    public int getChildIndex(Node child) {
        for (int i = 0; i < this.numChildren; i++) {
            if (this.children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public void rebalance() {
        InternalNode leftSibling = this.getLeftSibling();
        InternalNode rightSibling = this.getRightSibling();

        // no scenario where left sibling needs to update children
        this.updateChildren(leftSibling, rightSibling);
        if (rightSibling != null)
            rightSibling.updateChildren(this, rightSibling.getRightSibling());

        this.updateKeys();
        if (leftSibling != null)
            leftSibling.updateKeys();
        if (rightSibling != null)
            rightSibling.updateKeys();
    }

    public InternalNode getLeftSibling() {
        InternalNode parent = this.getParent();
        if (parent == null) {
            return null;
        }

        nodeIndex = this.getNodeIndex();
        if (nodeIndex == 0) {
            int levels = 0;
            // traverse to 1 level below ancestor
            while (parent.getParent() != null && parent.getParent().getChild(0) == parent) {
                parent = parent.getParent();
                levels++;
            }
            InternalNode ancestor = parent.getParent();
            levels++;

            // it is leftmost node already
            if (ancestor == null) {
                return null;
            }

            int index = ancestor.getChildIndex(parent);
            System.out.println(index);

            InternalNode target = ancestor;
            while (levels > 0) {
                target = (InternalNode) target.getChild(target.getNumChildren() - 1);
                levels--;
            }
            return target;
        }
        return ((InternalNode) parent.getChild(nodeIndex - 1));
    }

    public InternalNode getRightSibling() {
        InternalNode parent = this.getParent();
        if (parent == null) {
            return null;
        }

        nodeIndex = this.getNodeIndex();
        if (nodeIndex == this.numChildren - 1) {
            int levels = 0;
            // traverse to 1 level below ancestor
            while (parent.getParent() != null && parent.getParent().getChild(this.numChildren - 1) == parent) {
                parent = parent.getParent();
                levels++;
            }
            InternalNode ancestor = parent.getParent();
            levels++;

            // it is leftmost node already
            if (ancestor == null) {
                return null;
            }

            int index = ancestor.getChildIndex(parent);
            System.out.println(index);

            InternalNode target = ancestor;
            while (levels > 0) {
                target = (InternalNode) target.getChild(0);
                levels--;
            }
            return target;
        }
        return ((InternalNode) parent.getChild(nodeIndex + 1));
    }

    public void updateChildren(InternalNode leftSibling, InternalNode rightSibling) {
        for (int i = 0; i < this.numChildren; i++) {
            if (this.children[i].getParent() != this) {
                this.deleteChild(i, leftSibling, rightSibling);
                return;
            }
        }
    }

    public void deleteChild(int deletePos, InternalNode leftSibling, InternalNode rightSibling) {
        // case 1: is root or simple delete
        if (this.getParent() == null || this.numKeys - 1 >= this.MIN_KEYS) {
            deleteAndShiftLeft(deletePos);
            this.numKeys--;
            this.numChildren--;
            return;
        }

        // case 2: borrow from left sibling
        if (leftSibling != null && leftSibling.getNumKeys() - 1 >= this.MIN_KEYS) {
            this.deleteAndBorrowFromLeft(deletePos, leftSibling);
        } else if (rightSibling != null && rightSibling.getNumKeys() - 1 >= this.MIN_KEYS) {
            this.deleteAndBorrowFromRight(deletePos, rightSibling);
        } else if (leftSibling != null) { // case 3: merge with left sibling
            this.mergeWithLeft(deletePos, leftSibling);
        } else if (rightSibling != null) { // case 4: merge with right sibling
            this.mergeWithRight(deletePos, rightSibling);
        }
    }

    public void deleteAndShiftLeft(int deletePos) {
        // 2 cases -> index 0 and not 0
        for (int i = deletePos; i <= this.numKeys; i++) {
            if (deletePos == 0) {
                if (i + 1 < this.numKeys)
                    this.keys[i] = this.keys[i + 1];
            } else {
                this.keys[i - 1] = this.keys[i];
            }
            this.children[i] = this.children[i + 1];
            if (this.children[i + 1] != null)
                this.children[i + 1].setNodeIndex(i);
        }
    }

    public void deleteAndShiftRight(int deletePos) {
        // 2 cases -> index 0 and not 0
        // index 0 -> will exit directly
        // else -> shift all starting from index 1 right
        for (int i = deletePos; i > 0; i--) {
            if (i == 1) {
                this.keys[i - 1] = this.children[i - 1].getSubtreeLB();
            } else {
                this.keys[i - 1] = this.keys[i - 2];
            }
            this.children[i] = this.children[i - 1];
            this.children[i - 1].setNodeIndex(i);
        }
    }

    public void deleteAndBorrowFromLeft(int deletePos, InternalNode leftSibling) {
        this.deleteAndShiftRight(deletePos);
        Node borrow = leftSibling.getChild(leftSibling.getNumChildren() - 1);
        this.children[0] = borrow;
        borrow.setParent(this);
        borrow.setNodeIndex(0);
        leftSibling.setNumKeys(leftSibling.getNumKeys() - 1);
        leftSibling.setNumChildren(leftSibling.getNumChildren() - 1);
    }

    public void deleteAndBorrowFromRight(int deletePos, InternalNode rightSibling) {
        this.deleteAndShiftLeft(deletePos);
        Node borrow = rightSibling.getChild(0);
        this.children[this.numChildren - 1] = borrow;
        this.keys[this.numChildren - 2] = borrow.getSubtreeLB();
        borrow.setParent(this);
        borrow.setNodeIndex(this.numChildren - 1);
        rightSibling.deleteAndShiftLeft(0);
        rightSibling.setNumKeys(rightSibling.getNumKeys() - 1);
        rightSibling.setNumChildren(rightSibling.getNumChildren() - 1);
    }

    public void mergeWithLeft(int deletePos, InternalNode leftSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        this.numChildren--;
        for (int i = 0; i <= this.numKeys; i++) {
            leftSibling.setKey(leftSibling.getNumKeys() + i, this.keys[i]);
            leftSibling.setChild(leftSibling.getNumChildren() + i, this.children[i]); // node index set by setChild()
            this.children[i].setParent(leftSibling);
        }
        leftSibling.setNumKeys(leftSibling.getNumKeys() + this.numKeys);
        leftSibling.setNumChildren(leftSibling.getNumChildren() + this.numChildren);
        this.numKeys = 0;
        this.numChildren = 0;
        this.setParent(null);
    }

    public void mergeWithRight(int deletePos, InternalNode rightSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        this.numChildren--;
        for (int i = 0; i < rightSibling.getNumKeys(); i++) {
            this.keys[this.numKeys + i] = rightSibling.getKey(i);
            this.children[this.numChildren + i] = rightSibling.getChild(i);
            rightSibling.getChild(i).setNodeIndex(this.numChildren + i);
            rightSibling.getChild(i).setParent(this);
        }
        this.numKeys += rightSibling.getNumKeys();
        this.numChildren += rightSibling.getNumChildren();
        rightSibling.setNumKeys(0);
        rightSibling.setNumChildren(0);
        rightSibling.setParent(null);
    }

    public int getSubtreeLB() {
        return this.children[0].getSubtreeLB();
    }
}