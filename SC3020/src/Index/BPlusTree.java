package Index;

public class BPlusTree {
    private Node root;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
    }

    public void insert(int key, int value) {
        Node result = this.root.insert(key, value);
        if (result != null) {
            InternalNode newRoot = new InternalNode(this.root.getOrder(), result.getKey(0), this.root, result);
            this.root = newRoot;
        } 
    }

    // Not used in the current implementation
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

    public void traverseTree(Node root) {
        if (root instanceof LeafNode) {
            LeafNode leaf = (LeafNode) root;
            while (leaf != null) {
                for (int i = 0; i < leaf.getNumKeys(); i++) {
                    System.out.print(leaf.getKey(i) + " ");
                }
                leaf = leaf.getNextLeafNode();
            }
        } else {
            InternalNode internal = (InternalNode) root;
            for (int i = 0; i < internal.getNumChildren(); i++) {
                traverseTree(internal.getChild(i));
            }
        }
    }

    public void printTree() {
        this.traverseTree(this.root);
    }
}