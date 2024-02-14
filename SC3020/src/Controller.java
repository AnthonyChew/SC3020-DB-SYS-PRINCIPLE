import Disks.Address;
import Disks.Disk;
import Records.Record;
import Utils.TsvReader;

import java.util.ArrayList;

public class Controller {
    private Disk disk;
    private ArrayList<Address> addresses;

    public Controller(Disk disk, ArrayList<Address> addresses) {
        this.disk = disk;
        this.addresses = addresses;
    }

    public void experiment1() {
        System.out.println("***Experiment 1 - Store data on disk and report statistics.***");
        ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
        records.forEach(
                record -> {
                    Address address = disk.addRecord(record);
                    addresses.add(address);
                }
        );
        // Report statistics:
        System.out.println("Total number of records: " + records.size() + " records.");
        System.out.println("Size of each record: " + records.get(0).getRecordHeader().getRecordSize() + " bytes.");
        System.out.println("Number of records per block: " + this.disk.getBlocks()[0].getRecords().length + " records.");
        System.out.println("Number of blocks to store data: " +
                this.disk.getTotalUsedBlocks() + "/" +
                this.disk.getBlocks().length + " blocks."
        );
    }

    public void experiment2() {
        System.out.println("***Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially, " +
                "and report statistics.***");
        // TODO: ...
        BPlusTreeExperiments.experiment2();

    }

    public void experiment3() {
        System.out.println("***Experiment 3 - Retrieve movies with 'numVotes' equal to 500" +
                " and report statistics.***");
        long startTimeBPlusTree = System.nanoTime();
        // TODO: Retrieve movies through B+ Tree
        BPlusTreeExperiments.experiment3();

        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
//        System.out.println("Total time taken for B+ Tree: " + elapsedTimeBPlusTree + "ns.");

        long startTimeBruteForce = System.nanoTime();
        // Brute force linear scan
        ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment3(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
//        System.out.println("Total time taken for brute force: " + elapsedTimeBruteForce + "ns.");
    }

    public void experiment4() {
        System.out.println("***Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000]," +
                " and report statistics.***");
        long startTimeBPlusTree = System.nanoTime();
        // TODO: Retrieve movies through B+ Tree
        BPlusTreeExperiments.experiment4();

        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");

        long startTimeBruteForce = System.nanoTime();
        // Brute force linear scan
        ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment4(this.disk);
        long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
    }

    public void experiment5() {
        System.out.println("***Experiment 5 - Delete movies with 'numVotes' equal to 1000, " +
                "update B+ tree and report statistics.***");
        long startTimeBPlusTree = System.nanoTime();
        // TODO: Retrieve movies through B+ Tree
        BPlusTreeExperiments.experiment5();

        long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
        timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
        // TODO: Before we start brute force, we need to "add" back the records that were deleted when we used B+ tree.
        //  How to do that?
        long startTimeBruteForce = System.nanoTime();
        //Brute force search through record
        BruteforceExperiments.experiment5(disk);
        long elapsedDelRecord = System.nanoTime() - startTimeBruteForce;
        timeTaken(elapsedDelRecord, "Total time taken for brute force: ");
    }

    public static void timeTaken(long elapsedTime, String msg) {
        double elapsedTimeMS = elapsedTime/(1000000.0);
        System.out.println(msg + elapsedTimeMS + "ms.");
    }
}
