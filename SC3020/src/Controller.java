import Disks.Address;
import Disks.Disk;
import Index.BPlusTree;
import Records.Record;
import Utils.TsvReader;

import java.util.ArrayList;

/**
 * The Controller class manages the experiments and operations on the disk and
 * B+ tree.
 */
public class Controller {
        private Disk disk;
        private ArrayList<Address> addresses;
        private BPlusTree tree;

        /**
         * Constructs a Controller object with the specified disk and addresses.
         *
         * @param disk      The disk to store records.
         * @param addresses The list of addresses.
         */
        public Controller(Disk disk, ArrayList<Address> addresses) {
                this.disk = disk;
                this.addresses = addresses;
                this.tree = new BPlusTree(25);
        }

        /**
         * Prints the time taken in milliseconds along with a message.
         *
         * @param elapsedTime The elapsed time in nanoseconds.
         * @param msg         The message to be printed.
         */
        public static void timeTaken(long elapsedTime, String msg) {
                double elapsedTimeMS = elapsedTime / (1000000.0);
                System.out.println(msg + elapsedTimeMS + "ms.");
        }

        /**
         * Performs Experiment 1 - Stores data on disk and reports statistics.
         */
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
                System.out.println(
                                "Size of each record: " + records.get(0).getRecordHeader().getRecordSize() + " bytes.");
                System.out.println(
                                "Number of records per block: " + this.disk.getBlocks()[0].getRecords().length
                                                + " records.");
                System.out.println(
                                "Number of blocks to store data: " + this.disk.getTotalUsedBlocks() +
                                                " / " + this.disk.getBlocks().length + " blocks.");
                System.out.println();
        }

        /**
         * Performs Experiment 2 - Builds B+ Tree on 'numVotes' by inserting records
         * sequentially and reports statistics.
         */
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

        /**
         * Performs Experiment 3 - Retrieves movies with 'numVotes' equal to 500 and
         * reports statistics.
         */
        public void experiment3() {
                System.out.println("***Experiment 3 - Retrieve movies with 'numVotes' equal to 500" +
                                " and report statistics.***");
                // B+ Tree scan
                long startTimeBPlusTree = System.nanoTime();
                System.out.println("B+ Tree version: ");
                BPlusTreeExperiments.experiment3(tree, disk, 500);
                long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
                timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
                System.out.println();

                // Brute force linear scan
                long startTimeBruteForce = System.nanoTime();
                System.out.println("Brute Force version: ");
                ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment3(this.disk, 500);
                long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
                timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
                System.out.println();
        }

        /**
         * Performs Experiment 4 - Retrieves movies with 'numVotes' ranging from [30
         * 000, 40 000] and reports statistics.
         */
        public void experiment4() {
                System.out.println("***Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000]," +
                                " and report statistics.***");

                // B+ Tree scan
                long startTimeBPlusTree = System.nanoTime();
                System.out.println("B+ Tree version: ");
                BPlusTreeExperiments.experiment4(tree, disk, 30000, 40000);
                long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
                timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
                System.out.println();

                // Brute force linear scan
                long startTimeBruteForce = System.nanoTime();
                System.out.println("Brute Force version: ");
                ArrayList<Record> bruteforceResults = BruteforceExperiments.experiment4(this.disk, 30000, 40000);
                long elapsedTimeBruteForce = System.nanoTime() - startTimeBruteForce;
                timeTaken(elapsedTimeBruteForce, "Total time taken for brute force: ");
                System.out.println();
        }

        /**
         * Performs Experiment 5 - Deletes movies with 'numVotes' equal to 1000, updates
         * B+ tree, and reports statistics.
         */
        public void experiment5() {
                System.out.println("***Experiment 5 - Delete movies with 'numVotes' equal to 1000, " +
                                "update B+ tree and report statistics.***");

                // Create a duplicate of disk and address table to be used for brute force so
                // that we can perform both B+ Tree and brute force delete together without the
                // first
                // delete affecting the second.
                Disk dupDisk = new Disk();
                ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
                ArrayList<Address> dupAddress = new ArrayList<>();
                records.forEach(
                                record -> {
                                        Address address = dupDisk.addRecord(record);
                                        dupAddress.add(address);
                                });

                // B+ Tree delete
                long startTimeBPlusTree = System.nanoTime();
                System.out.println("B+ Tree Version:");
                BPlusTreeExperiments.experiment5(tree, disk, 1000);

                long elapsedTimeBPlusTree = System.nanoTime() - startTimeBPlusTree;
                timeTaken(elapsedTimeBPlusTree, "Total time taken for B+ Tree: ");
                System.out.println();

                // Brute force delete
                long startTimeBruteForce = System.nanoTime();
                System.out.println("Brute Force Version:");

                BruteforceExperiments.experiment5(dupDisk);
                long elapsedDelRecord = System.nanoTime() - startTimeBruteForce;
                timeTaken(elapsedDelRecord, "Total time taken for brute force: ");
                System.out.println();
        }
}
