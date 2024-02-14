package Index;

import java.util.LinkedList;
import java.util.Queue;

import Disks.Address;

public class BPlusTree {
    private Node root;
    private int n;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
        this.n = order - 1;
    }

    public int getN() {
        return this.n;
    }

    public void insert(int key, Address value) {
        try {
            this.root.insert(key, value);
            if (this.root.getParent() != null) {
                this.root = this.root.getParent();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            System.exit(1);
        }
    }

    public void printRootKeys() {
        if (root == null) {
            return;
        }

        for (int i = 0; i < root.getNumKeys(); i++) {
            System.out.print(root.getKey(i) + " ");
        }
        System.out.println();
        System.out.println();
    }

    public int calculateNumberOfNodes() {
        if (root == null) {
            return 0;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int count = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();
                count++;

                if (node instanceof InternalNode) {
                    InternalNode internal = (InternalNode) node;
                    for (int j = 0; j < internal.getNumChildren(); j++) {
                        queue.add(internal.getChild(j));
                    }
                }
            }
        }

        return count;
    }

    public int calculateDepth() {
        if (root == null) {
            return 0;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();

                if (node instanceof LeafNode)
                    continue;

                InternalNode internal = (InternalNode) node;
                for (int j = 0; j < internal.getNumChildren(); j++) {
                    queue.add(internal.getChild(j));
                }
            }
            depth++;
        }

        return depth;
    }

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
            System.out.println();
        }
    }

    public void printTree() {
        this.traverseTree(this.root);
    }
}