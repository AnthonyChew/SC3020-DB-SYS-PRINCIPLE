package Index;

import java.util.LinkedList;
import java.util.Queue;

public class BPlusTree {
    private Node root;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
    }

    public void insert(int key, int value) {
        this.root.insert(key, value);
        if (this.root.getParent() != null) {
            this.root = this.root.getParent();
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

    // public void traverseTree(Node root) {
    // if (root instanceof LeafNode) {
    // LeafNode leaf = (LeafNode) root;
    // for (int i = 0; i < leaf.getNumKeys(); i++) {
    // System.out.print(leaf.getKey(i) + " ");
    // }
    // } else {
    // InternalNode internal = (InternalNode) root;
    // for (int i = 0; i < internal.getNumChildren(); i++) {
    // traverseTree(internal.getChild(i));
    // }
    // }
    // }

    public void traverseTree(Node root) {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();

                if (node instanceof LeafNode) {
                    LeafNode leaf = (LeafNode) node;
                    for (int j = 0; j < leaf.getNumKeys(); j++) {
                        System.out.print(leaf.getKey(j) + " ");
                    }
                } else {
                    InternalNode internal = (InternalNode) node;
                    for (int j = 0; j < internal.getNumChildren(); j++) {
                        if (j != internal.getNumChildren() - 1)
                            System.out.print(internal.getKey(j) + " ");
                        queue.add(internal.getChild(j));
                    }
                }
            }
            System.out.println(); // Print a new line after each level
        }
    }

    public void printTree() {
        this.traverseTree(this.root);
    }
}