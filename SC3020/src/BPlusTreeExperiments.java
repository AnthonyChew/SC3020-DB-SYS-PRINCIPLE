import java.util.ArrayList;

import Disks.Address;
import Index.BPlusTree;
import Utils.TsvReader;

public class BPlusTreeExperiments {
    public static void experiment2(BPlusTree tree) {
        System.out.println("Parameter n of B+ tree: " + tree.getN() + ".");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }

    public static void experiment3() {
        // TODO: Add experiment 3.
    }

    public static void experiment4() {
        // TODO: Add experiment 4.
    }

    public static void experiment5(BPlusTree tree) {
        tree.delete(1000);
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }
}
