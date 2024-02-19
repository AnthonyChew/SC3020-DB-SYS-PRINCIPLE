import Blocks.Block;
import Disks.Disk;
import Records.Record;

import java.util.ArrayList;

public class BruteforceExperiments {
    /**
     * This class stores the brute force experiments.
     */
    public static ArrayList<Record> experiment3(Disk disk) {
        /*
         * This method is the brute force method for experiment 3 to retrieve records
         * where numVotes = 500.
         *
         * @Param Disk - Pass in the disk to retrieve records via brute force.
         * 
         * @Return ArrayList - Returns the retrieved records in an ArrayList.
         */
        int noOfAccessedBlocks = 0;
        float sum = 0;
        Block[] blocks = disk.getBlocks();
        ArrayList<Record> recordResults = new ArrayList<>();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null && !blocks[i].isEmpty()) {
                noOfAccessedBlocks++;
                Record[] records = blocks[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    if (records[j] != null && records[j].getRecordData().getNumVotes() == 500) {
                        recordResults.add(records[j]);
                        sum += records[j].getRecordData().getAverageRating();
                    }
                }
            }
        }
        float average = sum / recordResults.size();

        System.out.println("Number of records with numVotes = 500: " + recordResults.size() + " records.");
        System.out.println("Number of blocks accessed: " + noOfAccessedBlocks + " blocks.");
        System.out.println("Average of average ratings: " + average);
        return recordResults;
    }

    public static ArrayList<Record> experiment4(Disk disk) {
        /*
         * This method is the brute force method for experiment 4 to retrieve records
         * where 30 000 <= numVotes <= 40 000.
         *
         * @Param Disk - Pass in the disk to retrieve records via brute force.
         * 
         * @Return ArrayList - Returns the retrieved records in an ArrayList.
         */
        int noOfAccessedBlocks = 0;
        Block[] blocks = disk.getBlocks();
        ArrayList<Record> recordResults = new ArrayList<>();
        float sum = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null && !blocks[i].isEmpty()) {
                noOfAccessedBlocks++;
                Record[] records = blocks[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    if (records[j] != null &&
                            (records[j].getRecordData().getNumVotes() >= 30000 &&
                                    records[j].getRecordData().getNumVotes() <= 40000)) {
                        recordResults.add(records[j]);
                        sum += records[j].getRecordData().getAverageRating();
                    }
                }
            }
        }
        float average = sum / recordResults.size();

        System.out
                .println("Number of records with 30 000 <= numVotes <= 40 000: " + recordResults.size() + " records.");
        System.out.println("Number of blocks accessed: " + noOfAccessedBlocks + " blocks.");
        System.out.println("Sum of average ratings: " + sum);
        System.out.println("Average of average ratings: " + average);
        return recordResults;
    }

    public static void experiment5(Disk disk) {
        /*
         * This method is the brute force method for experiment 5 to delete records
         * where numVotes = 1000.
         *
         * @Param Disk - Pass in the disk to delete records via brute force.
         */
        int noOfAccessedBlocks = 0;
        int noOfDeletedRecords = 0;
        Block[] blocks = disk.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null && !blocks[i].isEmpty()) {
                noOfAccessedBlocks++;
                Record[] records = blocks[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    if (records[j] != null && records[j].getRecordData().getNumVotes() == 1000) {
                        // DELETE RECORD BASED ON I and J FROM LOOPING THROUGH
                        // INSTEAD OF THROUGH ADDRESS
                        if (disk.deleteRecord(i, j)) {
                            noOfDeletedRecords++;
                            System.out.println("Successfully deleted record from block(" + i + "), record(" + j + ").");
                        } else {
                            System.out.println("Failed to delete record from block(" + i + "), record(" + j + ").");
                        }
                    }
                }
            }
        }
        System.out.println("Number of deleted records with numVotes = 1000: " + noOfDeletedRecords + " records.");
        System.out.println("Number of blocks accessed: " + noOfAccessedBlocks + " blocks.");
        // for(Address address: addresses)
        // {
        // Record record = address.getRecord(disk, address.getBlock(),
        // address.getIndex());
        // if(record.getRecordData().getNumVotes() == 1000)
        // {
        // disk.deleteRecord(address);
        // }
        // }
    }
}
