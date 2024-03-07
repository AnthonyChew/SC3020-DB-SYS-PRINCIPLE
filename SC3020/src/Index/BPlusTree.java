package Index;

import Disks.Address;
import Disks.Disk;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a B+ tree data structure.
 */
public class BPlusTree {
    private Node root;
    private int n;

    /**
     * Constructs a B+ tree with the specified order.
     *
     * @param order the order of the B+ tree
     */
    public BPlusTree(int order) {
        this.root = new LeafNode(order);
        this.n = order - 1;
    }

    /**
     * Returns the root node of the B+ tree.
     *
     * @return the root node
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * Returns the order of the B+ tree.
     *
     * @return the order
     */
    public int getN() {
        return this.n;
    }

    /**
     * Inserts a key-value pair into the B+ tree.
     *
     * @param key   the key to insert
     * @param value the value to insert
     */
    public void insert(int key, Address value) {
        this.root.insert(key, value);
        if (this.root.getParent() != null) {
            this.root = this.root.getParent();
        }
    }

    /**
     * Performs a query for the given key in the B+ tree.
     *
     * @param key  the key to query
     * @param disk the disk to store the query result
     */
    public void query(int key, Disk disk) {
        this.root.query(key, disk);
    }

    /**
     * Performs a range query for the given start and end keys in the B+ tree.
     *
     * @param startKey the start key of the range query
     * @param endKey   the end key of the range query
     * @param disk     the disk to store the query result
     */
    public void rangeQuery(int startKey, int endKey, Disk disk) {
        this.root.rangeQuery(startKey, endKey, disk);
    }

    /**
     * Deletes a key-value pair from the B+ tree.
     *
     * @param key  the key to delete
     * @param disk the disk to store the deleted value
     */
    public void delete(int key, Disk disk) {
        this.root.delete(key, disk);
        if (this.root.getNumKeys() == 0) {
            this.root = ((InternalNode) this.root).getChild(0);
            this.root.setParent(null);
        }
    }

    /**
     * Prints the keys of the root node.
     */
    public void printRootKeys() {
        if (root == null) {
            return;
        }

        for (int i = 0; i < root.getNumKeys(); i++) {
            System.out.print(" | " + root.getKey(i));
        }
        System.out.println();
    }

    /**
     * Calculates the number of nodes in the B+ tree.
     *
     * @return the number of nodes
     */
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

    /**
     * Calculates the depth of the B+ tree.
     *
     * @return the depth of the tree
     */
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

    /**
     * Traverses the B+ tree and prints the keys of each node.
     *
     * @param root the root node of the tree
     */
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

    /**
     * Prints the B+ tree.
     */
    public void printTree() {
        this.traverseTree(root);
    }

    /**
     * Prints the keys of the leaf nodes in the B+ tree.
     *
     * @param root the root node of the tree
     */
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

    /**
     * Prints the keys of the (k+1)th level nodes in the B+ tree.
     */
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