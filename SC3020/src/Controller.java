import Disks.Address;
import Disks.Disk;
import Index.BPlusTree;
import Index.InternalNode;
import Index.LeafNode;
import Index.Node;
import Records.Record;
import Utils.TsvReader;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Disk disk;
    private ArrayList<Address> addresses;
    private BPlusTree tree;

    public Controller(Disk disk, ArrayList<Address> addresses) {
        this.disk = disk;
        this.addresses = addresses;
        this.tree = new BPlusTree(25);
    }

    public static void timeTaken(long elapsedTime, String msg) {
        double elapsedTimeMS = elapsedTime / (1000000.0);
        System.out.println(msg + elapsedTimeMS + "ms.");
    }

    public void experiment1() {
        System.out.println("***Experiment 1 - Store data on disk and report statistics.***");
        ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
        records.forEach(
                record -> {
                    Address address = disk.addRecord(record);
                    addresses.add(address);
                });
        // Report statistics:
        System.out.println("Total number of records: " + records.size() + " records.");
        System.out.println("Size of each record: " + records.get(0).getRecordHeader().getRecordSize() + " bytes.");
        System.out
                .println("Number of records per block: " + this.disk.getBlocks()[0].getRecords().length + " records.");
        System.out.println("Number of blocks to store data: " +
                this.disk.getTotalUsedBlocks() + "/" +
                this.disk.getBlocks().length + " blocks.");
        System.out.println();
    }

    public void experiment2() {
        System.out.println("***Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially, " +
                "and report statistics.***");
        addresses.forEach(address -> {
            Record record = disk.getRecord(address);
            // for
            tree.insert(record.getRecordData().getNumVotes(), address);
        });
        BPlusTreeExperiments.experiment2(tree);
        System.out.println();
    }

    public void experiment3() {
        System.out.println("***Experiment 3 - Retrieve movies with 'numVotes' equal to 500" +
                " and report statistics.***");

        long startTimeBPlusTree = System.nanoTime();
        BPlusTreeExperiments.experiment3(tree, disk);
        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
        System.out.println();

        // Brute force linear scan
        long startTimeBruteForce = System.nanoTime();
        ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment3(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
        System.out.println();
    }

    public void experiment4() {
        System.out.println("***Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000]," +
                " and report statistics.***");

        long startTimeBPlusTree = System.nanoTime();
        BPlusTreeExperiments.experiment4(tree, disk);
        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
        System.out.println();

        long startTimeBruteForce = System.nanoTime();
        // Brute force linear scan
        ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment4(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
        System.out.println();
    }

    public void experiment5() {
        System.out.println("***Experiment 5 - Delete movies with 'numVotes' equal to 1000, " +
                "update B+ tree and report statistics.***");

        // Create a duplicated disk and address table for brute force so that all
        // records are still there
        Disk dupDisk = new Disk();
        ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
        ArrayList<Address> dupAddress = new ArrayList<>();
        records.forEach(
                record -> {
                    Address address = dupDisk.addRecord(record);
                    dupAddress.add(address);
                });

        long startTimeBPlusTree = System.nanoTime();
        BPlusTreeExperiments.experiment5(tree, disk);

        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");

        // TODO: Before we start brute force, we need to "add" back the records that
        // were deleted when we used B+ tree.
        // How to do that?
        long startTimeBruteForce = System.nanoTime();
        // Brute force search through record
        BruteforceExperiments.experiment5(dupDisk);
        long elapsedDelRecord = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedDelRecord, "Total time taken for brute force: ");
        System.out.println();
    }

    public void debugBTree() {
        BPlusTree test_tree = new BPlusTree(4);
        int keys[] = {
                1, 4, 7, 10, 17, 21, 31, 25, 19, 20, 28, 42
        };

        for (int key : keys) {
            test_tree.insert(key, new Address(0, 0));
        }

        test_tree.printTree();
        System.out.println();
        test_tree.printLeafs();
        System.out.println();

        test_tree.printKPLusOneKeys();
    }
}
