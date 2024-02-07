package Index;

public class BPlusTree  {
    private Node root;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
    }

    public void insert(int key, int value) {
        // If root is a LeafNode, insert the key and value
        if (this.root instanceof LeafNode) {
            LeafNode leaf = (LeafNode) this.root;
            if (! leaf.addKey(key, value)) {
                // If the leaf is full, split it
                LeafNode newLeaf = leaf.splitLeafNode(key, value);
                InternalNode newRoot = new InternalNode(leaf.getOrder(), newLeaf.getKey(0), leaf, newLeaf);
                this.root = newRoot;
            }
        } 
    }
}