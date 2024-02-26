import Disks.Disk;
import Index.BPlusTree;

public class BPlusTreeExperiments {
    public static void experiment2(BPlusTree tree) {
        System.out.println("Parameter n of B+ tree: " + tree.getN() + ".");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }

    public static void experiment3(BPlusTree tree, Disk disk, int key) {
        tree.query(key, disk);
    }

    public static void experiment4(BPlusTree tree, Disk disk, int start, int end) {
        tree.rangeQuery(start, end, disk);
    }

    public static void experiment5(BPlusTree tree, Disk disk, int key) {
        tree.delete(key, disk);
        System.out.println("*** New B+ Tree ***");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }
}
