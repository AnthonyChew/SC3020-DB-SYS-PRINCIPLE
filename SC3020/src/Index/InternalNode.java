package Index;

// minimum -> (order - 1) // 2 keys
/**
 * Represents an internal node in a B+ tree.
 * Internal nodes are used to store keys and references to child nodes.
 */
public class InternalNode extends Node {
    private final int MIN_KEYS = (this.getOrder() - 1) / 2;
    private Node[] children;
    private int numChildren;

    /**
     * Constructs a new internal node with the specified order.
     *
     * @param order the order of the B+ tree
     */
    public InternalNode(int order) {
        super(order);
        this.children = new Node[order];
        this.numChildren = 0;
    }

    /**
     * Constructs a new internal node with the specified order, key, left child, and
     * right child.
     * This constructor is used when the internal node is created from a split in
     * the child.
     *
     * @param order      the order of the B+ tree
     * @param key        the key to be added to the parent node
     * @param leftChild  the left child node
     * @param rightChild the right child node
     */
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

    /**
     * Returns the number of children of this internal node.
     *
     * @return the number of children
     */
    public int getNumChildren() {
        return this.numChildren;
    }

    /**
     * Sets the number of children of this internal node.
     *
     * @param numChildren the number of children
     */
    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    /**
     * Returns the child node at the specified index.
     *
     * @param index the index of the child node
     * @return the child node at the specified index
     */
    public Node getChild(int index) {
        return this.children[index];
    }

    /**
     * Sets the child node at the specified index.
     *
     * @param index the index of the child node
     * @param child the child node to be set
     */
    public void setChild(int index, Node child) {
        this.children[index] = child;
        child.setNodeIndex(index);
    }

    /**
     * Adds a new key and right child to this internal node.
     * This method is called when a leaf node splits and a new key needs to be added
     * to the parent node.
     *
     * @param key        the key to be added
     * @param rightChild the right child node to be added
     */
    public void addKey(int key, Node rightChild) {
        if (this.isFull()) {
            splitInternalNode(rightChild.getSubtreeLB(), rightChild);
        } else {
            int index = binarySearchInsertPos(key);

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

    /**
     * Updates the keys of this internal node based on the subtree lower bounds of
     * its children.
     */
    public void updateKeys() {
        for (int i = 0; i < this.numKeys; i++) {
            this.keys[i] = this.children[i + 1].getSubtreeLB();
        }
    }

    /**
     * Splits this internal node by moving keys and children to a new internal node.
     *
     * @param key        the key to be inserted
     * @param rightChild the right child node to be inserted
     */
    public void splitInternalNode(int key, Node rightChild) {
        int mid = this.MIN_KEYS - 1;

        // find insert pos
        int index = binarySearchInsertPos(key);
        InternalNode newInternalNode = new InternalNode(this.getOrder());

        // first half including mid -> start copying from mid over to new node
        if (index <= mid) {
            for (int i = mid, j = 0; i < this.numKeys; i++, j++) {
                if (i + 1 < this.numKeys) { // mid will be promoted
                    newInternalNode.setKey(j, this.keys[i + 1]);
                    this.keys[i + 1] = Integer.MAX_VALUE;
                }
                newInternalNode.setChild(j, this.children[i + 1]);
                newInternalNode.getChild(j).setParent(newInternalNode);
                this.children[i + 1] = null;
            }
            newInternalNode.setNumKeys(this.numKeys - mid - 1);
            newInternalNode.setNumChildren(this.numKeys - mid);
            this.numKeys = mid;
            this.numChildren = mid + 1;
            this.keys[mid] = Integer.MAX_VALUE;
            this.addKey(key, rightChild);
        } else { // second half -> copy everything after mid over to new node to insert
            int startShiftIndex = mid + 1;
            for (int i = startShiftIndex, j = 0; i < this.numKeys; i++, j++) {
                // insert at start -> leave a ptr space at index 0 for new child node
                if (index == startShiftIndex) {
                    newInternalNode.setKey(j, this.keys[i]);
                    newInternalNode.setChild(j + 1, this.children[i + 1]);
                    newInternalNode.getChild(j + 1).setParent(newInternalNode);

                    this.keys[i] = Integer.MAX_VALUE;
                    this.children[i + 1] = null;
                } else { // insert in middle -> copy all except first key and find slot to insert
                    if (i + 1 < this.numKeys) { // (mid + 1) will be promoted
                        newInternalNode.setKey(j, this.keys[i + 1]);
                        this.keys[i + 1] = Integer.MAX_VALUE;
                    }
                    newInternalNode.setChild(j, this.children[i + 1]);
                    newInternalNode.getChild(j).setParent(newInternalNode);
                    this.children[i + 1] = null;
                }
            }

            // first spot
            if (index == startShiftIndex) {
                newInternalNode.setChild(0, rightChild);
                newInternalNode.setNumKeys(this.numKeys - (startShiftIndex));
            } else if (index == this.numKeys) { // last spot
                newInternalNode.setKey(this.numKeys - startShiftIndex - 1, key);
                newInternalNode.setChild(this.numKeys - startShiftIndex, rightChild);
                newInternalNode.setNumKeys(this.numKeys - startShiftIndex);
            } else { // middle -> shift right
                for (int i = this.numKeys - startShiftIndex - 1; i > index - startShiftIndex - 1; i--) {
                    newInternalNode.setKey(i, newInternalNode.getKey(i - 1));
                    newInternalNode.setChild(i + 1, newInternalNode.getChild(i));
                }
                newInternalNode.setKey(index - startShiftIndex - 1, key);
                newInternalNode.setChild(index - startShiftIndex, rightChild);
                newInternalNode.setNumKeys(this.numKeys - startShiftIndex);
            }
            rightChild.setParent(newInternalNode);

            newInternalNode.setNumChildren(newInternalNode.getNumKeys() + 1);
            this.keys[mid + 1] = Integer.MAX_VALUE;
            this.numKeys = mid + 1;
            this.numChildren = mid + 2;
        }

        if (this.getParent() == null) {
            InternalNode parentNode = new InternalNode(this.getOrder(), newInternalNode.getSubtreeLB(), this,
                    newInternalNode);
            this.setParent(parentNode);
            newInternalNode.setParent(parentNode);
            return;
        }
        this.getParent().addKey(newInternalNode.getSubtreeLB(), newInternalNode);
    }

    /**
     * Returns the rightmost leaf node in the subtree rooted at this internal node.
     *
     * @return the rightmost leaf node
     */
    public LeafNode getRightMostLeafNode() {
        Node target = this.getChild(this.numChildren - 1);

        // keep going right
        while (target instanceof InternalNode) {
            InternalNode _target = (InternalNode) target;
            target = _target.getChild(_target.getNumChildren() - 1);
        }
        return (LeafNode) target;
    }

    /**
     * Returns the index of the specified child node in this internal node.
     *
     * @param child the child node
     * @return the index of the child node, or -1 if not found
     */
    public int getChildIndex(Node child) {
        for (int i = 0; i < this.numChildren; i++) {
            if (this.children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Rebalances this internal node by updating its children and keys.
     */
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

    /**
     * Returns the left sibling of this internal node.
     *
     * @return the left sibling, or null if not found
     */
    public InternalNode getLeftSibling() {
        if (this.getParent() == null)
            return null;

        int nodeIndex = this.getNodeIndex();
        if (nodeIndex == 0) {
            int levels = 1;
            int prevNodeIndex = this.nodeIndex;
            InternalNode ancestor = this.getParent();
            InternalNode cur = this;
            while (ancestor != null && ancestor.getChild(0) == cur) {
                prevNodeIndex = ancestor.getNodeIndex();
                cur = ancestor;
                ancestor = ancestor.getParent();
                levels++;
            }

            // it is rightmost node already
            if (ancestor == null) {
                return null;
            }

            InternalNode target = (InternalNode) ancestor.getChild(prevNodeIndex - 1);
            levels--;
            while (levels > 0) {
                target = (InternalNode) target.getChild(target.getNumChildren() - 1);
                levels--;
            }
            return target;
        }
        return ((InternalNode) this.getParent().getChild(nodeIndex - 1));
    }

    /**
     * Returns the right sibling of this internal node.
     *
     * @return the right sibling, or null if not found
     */
    public InternalNode getRightSibling() {
        if (this.getParent() == null)
            return null;

        int nodeIndex = this.getNodeIndex();
        if (nodeIndex == this.getParent().getNumChildren() - 1) {
            int levels = 1;
            int prevNodeIndex = this.nodeIndex;
            InternalNode ancestor = this.getParent();
            InternalNode cur = this;
            while (ancestor != null && ancestor.getChild(ancestor.getNumChildren() - 1) == cur) {
                prevNodeIndex = ancestor.getNodeIndex();
                cur = ancestor;
                ancestor = ancestor.getParent();
                levels++;
            }

            // it is rightmost node already
            if (ancestor == null) {
                return null;
            }

            InternalNode target = (InternalNode) ancestor.getChild(prevNodeIndex + 1);
            levels--;
            while (levels > 0) {
                target = (InternalNode) target.getChild(0);
                levels--;
            }
            return target;
        }
        return ((InternalNode) this.getParent().getChild(nodeIndex + 1));
    }

    /**
     * Updates the children of the internal node by deleting any child that does not
     * have the current node as its parent.
     * If a child is deleted, it is replaced by the left sibling or the right
     * sibling.
     *
     * @param leftSibling  the left sibling of the current node
     * @param rightSibling the right sibling of the current node
     */
    public void updateChildren(InternalNode leftSibling, InternalNode rightSibling) {
        for (int i = 0; i < this.numChildren; i++) {
            if (this.children[i].getParent() != this) {
                this.deleteChild(i, leftSibling, rightSibling);
                return;
            }
        }
    }

    /**
     * Deletes a child node at the specified position within the internal node.
     * If possible, the method will perform a simple delete, borrow from a left or
     * right sibling,
     * or merge with a left or right sibling to maintain the minimum number of keys
     * in the node.
     *
     * @param deletePos    the position of the child node to delete
     * @param leftSibling  the left sibling of the internal node
     * @param rightSibling the right sibling of the internal node
     */
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

    /**
     * Deletes a key at the given position and shifts all keys and children to the
     * left.
     * 
     * @param deletePos The position of the key to delete.
     */
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

    /**
     * Deletes a key at the given position and shifts all keys and children to the
     * right.
     * 
     * @param deletePos The position of the key to delete.
     */
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

    /**
     * Deletes a key at the given position, shifts all keys and children to the
     * right, and borrows a node from the left sibling.
     * 
     * @param deletePos   The position of the key to delete.
     * @param leftSibling The left sibling to borrow a node from.
     */
    public void deleteAndBorrowFromLeft(int deletePos, InternalNode leftSibling) {
        this.deleteAndShiftRight(deletePos);
        Node borrow = leftSibling.getChild(leftSibling.getNumChildren() - 1);
        this.children[0] = borrow;
        borrow.setParent(this);
        borrow.setNodeIndex(0);
        leftSibling.setNumKeys(leftSibling.getNumKeys() - 1);
        leftSibling.setNumChildren(leftSibling.getNumChildren() - 1);
    }

    /**
     * Deletes a key at the given position, shifts all keys and children to the
     * left, and borrows a node from the right sibling.
     * 
     * @param deletePos    The position of the key to delete.
     * @param rightSibling The right sibling to borrow a node from.
     */
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

    /**
     * Merges the current node with its left sibling.
     * 
     * @param deletePos   The position of the key to delete before merging.
     * @param leftSibling The left sibling to merge with.
     */
    public void mergeWithLeft(int deletePos, InternalNode leftSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        this.numChildren--;
        for (int i = 0; i <= this.numKeys; i++) {
            leftSibling.setKey(leftSibling.getNumKeys() + i, this.keys[i]);
            leftSibling.setChild(leftSibling.getNumChildren() + i, this.children[i]); // node index set by setChild()
            this.children[i].setParent(leftSibling);
        }
        leftSibling.setNumKeys(leftSibling.getNumKeys() + this.numChildren); // we copy the leftmost ptr over -> extra 1
                                                                             // key
        leftSibling.setNumChildren(leftSibling.getNumChildren() + this.numChildren);
        this.numKeys = 0;
        this.numChildren = 0;
        this.setParent(null);
    }

    /**
     * Merges the current node with its right sibling.
     * 
     * @param deletePos    The position of the key to delete before merging.
     * @param rightSibling The right sibling to merge with.
     */
    public void mergeWithRight(int deletePos, InternalNode rightSibling) {
        this.deleteAndShiftLeft(deletePos);
        this.numKeys--;
        this.numChildren--;
        for (int i = 0; i <= rightSibling.getNumKeys(); i++) {
            if (i < rightSibling.getNumKeys())
                this.keys[this.numKeys + i] = rightSibling.getKey(i);
            this.children[this.numChildren + i] = rightSibling.getChild(i);
            rightSibling.getChild(i).setNodeIndex(this.numChildren + i);
            rightSibling.getChild(i).setParent(this);
        }
        this.numKeys += rightSibling.getNumChildren(); // we copy the leftmost ptr over -> extra 1 key
        this.numChildren += rightSibling.getNumChildren();
        rightSibling.setNumKeys(0);
        rightSibling.setNumChildren(0);
        rightSibling.setParent(null);
    }

    /**
     * Returns the lower bound of the subtree.
     * 
     * @return The lower bound of the subtree.
     */
    public int getSubtreeLB() {
        return this.children[0].getSubtreeLB();
    }
}