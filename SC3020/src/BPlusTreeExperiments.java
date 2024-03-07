import Disks.Disk;
import Index.BPlusTree;

/**
 * This class contains methods to perform experiments on a B+ tree.
 */
public class BPlusTreeExperiments {

    /**
     * Performs experiment 2 on the B+ tree.
     * Prints the parameter n of the B+ tree, the number of nodes, the number of
     * levels, and the contents of the root node.
     *
     * @param tree The B+ tree to perform the experiment on.
     */
    public static void experiment2(BPlusTree tree) {
        System.out.println("Parameter n of B+ tree: " + tree.getN() + ".");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out.println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }

    /**
     * Performs experiment 3 on the B+ tree.
     * Queries the B+ tree for a given key using the specified disk.
     *
     * @param tree The B+ tree to perform the experiment on.
     * @param disk The disk to use for the query operation.
     * @param key  The key to query in the B+ tree.
     */
    public static void experiment3(BPlusTree tree, Disk disk, int key) {
        tree.query(key, disk);
    }

    /**
     * Performs experiment 4 on the B+ tree.
     * Performs a range query on the B+ tree for keys within the specified range
     * using the specified disk.
     *
     * @param tree  The B+ tree to perform the experiment on.
     * @param disk  The disk to use for the range query operation.
     * @param start The starting key of the range.
     * @param end   The ending key of the range.
     */
    public static void experiment4(BPlusTree tree, Disk disk, int start, int end) {
        tree.rangeQuery(start, end, disk);
    }

    /**
     * Performs experiment 5 on the B+ tree.
     * Deletes a key from the B+ tree using the specified disk and prints the
     * updated B+ tree.
     *
     * @param tree The B+ tree to perform the experiment on.
     * @param disk The disk to use for the delete operation.
     * @param key  The key to delete from the B+ tree.
     */
    public static void experiment5(BPlusTree tree, Disk disk, int key) {
        tree.delete(key, disk);
        System.out.println("*** New B+ Tree ***");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out.println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }
}
