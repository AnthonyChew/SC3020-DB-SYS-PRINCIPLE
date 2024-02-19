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
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
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
        BPlusTree test_tree = new BPlusTree(25);

        int[] keys = null;

        // Generate random numbers to append to keys up to 1000
        // Random random = new Random();
        // int[] keys = new int[2000];
        // for (int i = 0; i < 2000; i++) {
        // keys[i] = i + random.nextInt(1000);
        // }

        // String fileName = "keys.txt";
        // try {
        // FileWriter writer = new FileWriter(fileName);
        // for (int key : keys) {
        // test_tree.insert(key, new Address(0, 0));
        // writer.write(key + " ");
        // }
        // writer.close();
        // System.out.println("Keys written to file: " + fileName);
        // } catch (IOException e) {
        // System.out.println("An error occurred while writing keys to file: " +
        // e.getMessage());
        // }

        // Read keys from text file
        String fileName = "keys.txt";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] keyStrings = line.split(" ");
            keys = new int[keyStrings.length];
            for (int i = 0; i < keyStrings.length; i++) {
                keys[i] = Integer.parseInt(keyStrings[i]);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading keys from file: " + e.getMessage());
        }

        for (int key : keys) {
            test_tree.insert(key, new Address(0, 0));
        }

        // test_tree.printTree();
        // System.out.println();
        // test_tree.printLeafs();
        // System.out.println();

        // for (int key : keys) {
        // test_tree.insert(key, new Address(0, 0));
        // }

        // test_tree.printTree();
        // System.out.println();
        // test_tree.printLeafs();
        // System.out.println();
        // test_tree.printKPLusOneKeys();
    }

    public static void timeTaken(long elapsedTime, String msg) {
        double elapsedTimeMS = elapsedTime / (1000000.0);
        System.out.println(msg + elapsedTimeMS + "ms.");
    }
}
