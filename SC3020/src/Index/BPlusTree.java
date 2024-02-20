package Index;

import Disks.Address;
import Disks.Disk;

import java.util.LinkedList;
import java.util.Queue;

public class BPlusTree {
    private Node root;
    private int n;

    public BPlusTree(int order) {
        this.root = new LeafNode(order);
        this.n = order - 1;
    }

    public Node getRoot() {
        return this.root;
    }

    public int getN() {
        return this.n;
    }

    public void insert(int key, Address value) {
        this.root.insert(key, value);
        if (this.root.getParent() != null) {
            this.root = this.root.getParent();
        }
    }

    public void query(int key, Disk disk) {
        this.root.query(key, disk);
    }

    public void rangeQuery(int startKey, int endKey, Disk disk) {
        this.root.rangeQuery(startKey, endKey, disk);
    }

    public void delete(int key, Disk disk) {
        this.root.delete(key, disk);
        if (this.root.getNumKeys() == 0) {
            this.root = ((InternalNode) this.root).getChild(0);
            this.root.setParent(null);
        }
    }

    public void printRootKeys() {
        if (root == null) {
            return;
        }

        for (int i = 0; i < root.getNumKeys(); i++) {
            System.out.print(" | " + root.getKey(i));
        }
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

        int leafs = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();

                if (node instanceof LeafNode) {
                    LeafNode leaf = (LeafNode) node;
                    for (int j = 0; j < leaf.getNumKeys(); j++) {
                        System.out.print(leaf.getKey(j) + " ");
                    }
                    leafs++;
                    System.out.print("   ");
                } else {
                    InternalNode internal = (InternalNode) node;
                    for (int j = 0; j < internal.getNumChildren(); j++) {
                        if (j != internal.getNumChildren() - 1)
                            System.out.print(internal.getKey(j) + " ");
                        queue.add(internal.getChild(j));
                    }
                    System.out.print("   ");
                }
            }
            System.out.println();
        }

        System.out.println("Number of leafs: " + leafs);
    }

    public void printTree() {
        this.traverseTree(root);
    }

    public void printLeafs(Node root) {
        Node cur = root;
        while (cur instanceof InternalNode) {
            cur = ((InternalNode) cur).getChild(0);
        }

        int leafs = 0;
        while (cur != null) {
            LeafNode leaf = (LeafNode) cur;
            for (int i = 0; i < leaf.getNumKeys(); i++) {
                System.out.print(leaf.getKey(i) + " ");
            }
            leafs++;
            System.out.println();
            cur = leaf.getNextLeafNode();
        }

        System.out.println("Number of leafs: " + leafs);
    }

    public void printKPLusOneKeys() {
        if (this.root instanceof LeafNode)
            return;

        InternalNode cur = (InternalNode) this.root;
        while (!(cur.getChild(0) instanceof LeafNode)) {
            cur = (InternalNode) cur.getChild(0);
        }

        while (cur != null) {
            for (int i = 0; i < cur.getNumKeys(); i++) {
                System.out.print(cur.getKey(i) + " ");
            }
            System.out.println();
            cur = cur.getRightSibling();
        }
    }
}