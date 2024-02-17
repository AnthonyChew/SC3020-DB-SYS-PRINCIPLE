import java.util.ArrayList;

import Disks.Address;
import Disks.Disk;
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

    public static void experiment3(BPlusTree tree, Disk disk) {
        tree.query(500, disk);
    }

    public static void experiment4(BPlusTree tree, Disk disk) {
        tree.printLeafs();
//         tree.printKPLusOneKeys();
//         tree.rangeQuery(30000, 40000, disk);
    }

    public static void experiment5(BPlusTree tree, Disk disk) {
        tree.delete(1000, disk);
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }
}
