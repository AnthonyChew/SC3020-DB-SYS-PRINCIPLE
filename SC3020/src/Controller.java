import Disks.Address;
import Disks.Disk;
import Index.BPlusTree;
import Index.InternalNode;
import Index.LeafNode;
import Index.Node;
import Records.Record;
import Utils.TsvReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    private Disk disk;
    private ArrayList<Address> addresses;
    private BPlusTree tree;

    public Controller(Disk disk, ArrayList<Address> addresses) {
        this.disk = disk;
        this.addresses = addresses;
        this.tree = new BPlusTree(25);
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
        BPlusTree test_tree = new BPlusTree(5);
        int keys[] = { 1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6, 8, 23, 9,
                27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52,
                53, 54, 55, 70, 90, 100, 120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240, 250, 260, 270,
                280, 290, 300, 310, 320, 330, 340, 350, 360, 370, 380, 390, 400, 410, 420, 430, 440, 450, 460, 470, 480,
                490, 500, 510, 520, 530, 540, 550, 560, 570, 580, 590, 600, 610, 620, 630, 640, 650, 660, 670, 680, 690,
                700, 710, 720, 730, 740, 750, 760, 770, 780, 790, 800, 810, 820, 830, 840, 850, 860, 870, 880, 890, 900,
                910, 920, 930, 940, 950, 960, 970, 980, 990, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000,
                10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000, 200000, 300000, 400000, 500000,
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

    public static void timeTaken(long elapsedTime, String msg) {
        double elapsedTimeMS = elapsedTime / (1000000.0);
        System.out.println(msg + elapsedTimeMS + "ms.");
    }
}
