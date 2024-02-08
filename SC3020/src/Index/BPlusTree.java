package Index;

public class BPlusTree {
    private Node root;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
    }

    public void insert(int key, int value) {
        // If root is a LeafNode, insert the key and value
        if (this.root instanceof LeafNode) {
            LeafNode leaf = (LeafNode) this.root;
            if (!leaf.addKey(key, value)) {
                // If the leaf is full, split it
                LeafNode newLeaf = leaf.splitLeafNode(key, value);
                if (leaf.getNextLeafNode() != null) {
                    newLeaf.setNextLeafNode(leaf.getNextLeafNode());
                }
                leaf.setNextLeafNode(newLeaf);
                InternalNode newRoot = new InternalNode(leaf.getOrder(), newLeaf.getSubtreeLB(), leaf, newLeaf);
                leaf.setParent(newRoot);
                newLeaf.setParent(newRoot);
                this.root = newRoot;
            }
        } else { // If root is an InternalNode, find the leaf node to insert
            LeafNode leaf = this.findInsertLeafNode(key);
            // leaf.insert();
            // if (root.hasParent()) {
            // this.root = root.getParent()
            // }

            if (!leaf.addKey(key, value)) {
                // If the leaf is full, split it
                LeafNode newLeaf = leaf.splitLeafNode(key, value);
                if (leaf.getNextLeafNode() != null) {
                    newLeaf.setNextLeafNode(leaf.getNextLeafNode());
                }
                leaf.setNextLeafNode(newLeaf);
                // TODO: figure out how to recursively split the parent nodes
                InternalNode parent = leaf.getParent();
                if (!parent.addKey(newLeaf.getSubtreeLB(), newLeaf)) {
                    InternalNode newInternalNode = parent.splitInternalNode(newLeaf.getSubtreeLB(), newLeaf);
                }
            }
        }
    }

    public LeafNode findInsertLeafNode(int key) {
        Node node = this.root;
        while (node instanceof InternalNode) {
            InternalNode internal = (InternalNode) node;
            int index = 0;
            while (index < internal.getNumKeys() && internal.getKey(index) <= key) {
                index++;
            }
            node = internal.getChild(index);
        }
        return (LeafNode) node;
    }
}