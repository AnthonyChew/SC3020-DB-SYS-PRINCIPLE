import Records.Record;

import java.util.ArrayList;

public class Experiments {
    public static ArrayList<Record> experiment3(Disk disk) {
        int noOfAccessedBlocks = 0;
        Block[] blocks = disk.getBlocks();
        ArrayList<Record> recordResults = new ArrayList<>();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null && !blocks[i].isEmpty()) {
                noOfAccessedBlocks++;
                Record[] records = blocks[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    if (records[j] != null && records[j].getRecordData().getNumVotes() == 500) {
                        recordResults.add(records[j]);
                    }
                }
            }
        }
        System.out.println("Number of records with numVotes = 500: " + recordResults.size() + " records.");
        System.out.println("Number of blocks accessed: " + noOfAccessedBlocks + " blocks.");
        return recordResults;
    }

    public static ArrayList<Record> experiment4(Disk disk) {
        int noOfAccessedBlocks = 0;
        Block[] blocks = disk.getBlocks();
        ArrayList<Record> recordResults = new ArrayList<>();
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != null && !blocks[i].isEmpty()) {
                noOfAccessedBlocks++;
                Record[] records = blocks[i].getRecords();
                for (int j = 0; j < records.length; j++) {
                    if (records[j] != null &&
                            (
                                    records[j].getRecordData().getNumVotes() >= 30000 &&
                                            records[j].getRecordData().getNumVotes() <= 40000
                            )
                    ) {
                        recordResults.add(records[j]);
                    }
                }
            }
        }
        System.out.println("Number of records with 30 000 <= numVotes <= 40 000: " + recordResults.size() + " records.");
        System.out.println("Number of blocks accessed: " + noOfAccessedBlocks + " blocks.");
        return recordResults;
    }
}
