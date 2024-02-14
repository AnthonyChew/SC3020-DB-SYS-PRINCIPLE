import Disks.Address;
import Disks.Disk;
import Index.BPlusTree;
import Records.Record;
import Utils.TsvReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Blocks.Block;

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
    }

    public void experiment2() {
        System.out.println("***Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially, " +
                "and report statistics.***");
        ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
        records.forEach(
                record -> {
                    Address address = disk.addRecord(record);
                    tree.insert(record.getRecordData().getNumVotes(), address);
                    addresses.add(address);
                });
        // tree.printTree();
        System.out.println("Parameter n of B+ tree: " + tree.getN() + ".");
        System.out.println("Number of nodes: " + tree.calculateNumberOfNodes() + " nodes.");
        System.out
                .println("Number of levels: " + tree.calculateDepth() + " levels.");
        System.out.println("Contents of root node:");
        tree.printRootKeys();
    }

    public void experiment3() {
        System.out.println("***Experiment 3 - Retrieve movies with 'numVotes' equal to 500" +
                " and report statistics.***");
        long startTimeBPlusTree = System.nanoTime();
        // TODO: Retrieve movies through B+ Tree
        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
        // System.out.println("Total time taken for B+ Tree: " + elapsedTimeBPlusTree +
        // "ns.");

        long startTimeBruteForce = System.nanoTime();
        // TODO: BRUTE FORCE LINEAR SCAN
        ArrayList<Record> bruteforceResults = Experiments.experiment3(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
        // System.out.println("Total time taken for brute force: " +
        // elapsedTimeBruteForce + "ns.");
    }

    public void experiment4() {
        System.out.println("***Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000]," +
                " and report statistics.***");
        long startTimeBPlusTree = System.nanoTime();
        // TODO: Retrieve movies through B+ Tree
        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");

        long startTimeBruteForce = System.nanoTime();
        // TODO: BRUTE FORCE LINEAR SCAN
        ArrayList<Record> bruteforceResults = Experiments.experiment4(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
    }

    public void experiment5() {
        System.out.println("***Experiment 5 - Delete movies with 'numVotes' equal to 1000, " +
                "update B+ tree and report statistics.***");

        long startDelRecord = System.nanoTime();

        // Brute force search through record
        for (Address address : addresses) {
            Record record = address.getRecord(disk, address.getBlock(), address.getIndex());

            if (record.getRecordData().getNumVotes() == 1000) {
                this.disk.deleteRecord(address);
            }
        }

        long elapsedDelRecord = System.nanoTime() - startDelRecord;
        timeTaken(elapsedDelRecord,
                "Total time taken for Delete movies with 'numVotes' equal to 1000 & Update B+ Tree: ");
    }

    public static void timeTaken(long elapsedTime, String msg) {
        double elapsedTimeMS = elapsedTime / (1000000.0);
        System.out.println(msg + elapsedTimeMS + "ms.");
    }
}
