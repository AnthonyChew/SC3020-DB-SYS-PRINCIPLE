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
        System.out.println("Experiment 1 - Store data on disk and report statistics.");
        ArrayList<Record> records = TsvReader.TsvToStringArray("data.tsv");
        records.forEach(
                record -> {
                    Address address = disk.addRecord(record);
                    addresses.add(address);
                }
        );
        // Report statistics:
        System.out.println("Total number of records: " + records.size() + " records.");
        // TODO: Write function to retrieve data type and calculate record size.
        System.out.println("Size of each record: " + records.get(0).getRecordHeader().getRecordSize() + " bytes.");
        System.out.println("Number of records per block: " + this.disk.getBlocks()[0].getRecords().length + " records.");
        // TODO: Write function to retrieve number of blocks that was used, not total number of blocks.
        System.out.println("Number of blocks to store data: " +
                this.disk.getTotalUsedBlocks() + "/" +
                this.disk.getBlocks().length + " blocks."
        );
    }

    public void experiment2() {
        System.out.println("Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially and report statistics.");
    }

    public void experiment3() {
        System.out.println("Experiment 3 - Retrieve movies with 'numVotes' equal to 500 and report statistics.");
    }

    public void experiment4() {
        System.out.println("Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000] and report statistics.");
    }

    public void experiment5() {
        System.out.println("Experiment 5 - Delete movies with 'numVotes' equal to 1000, update B+ tree and report statistics.");
    }
}
