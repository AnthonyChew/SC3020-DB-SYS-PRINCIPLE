import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    // For testing
    static Record record;
    static Block block = new Block();


    public static void main(String[] args) {
        Disk disk = new Disk();
        ArrayList<Address> addresses = new ArrayList<>();
        Controller controller = new Controller(disk, addresses);
        int choice = -1;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Select an option:");
            System.out.println("1) Experiment 1 - Store data on disk and report statistics.");
            System.out.println("2) Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially and report statistics.");
            System.out.println("3) Experiment 3 - Retrieve movies with 'numVotes' equal to 500 and report statistics.");
            System.out.println("4) Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000] and report statistics.");
            System.out.println("5) Experiment 5 - Delete movies with 'numVotes' equal to 1000, update B+ tree and report statistics.");
            System.out.println("\n0) Exit.");

            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    controller.experiment1();
                    break;
                case 2:
                    controller.experiment2();
                    break;
                case 3:
                    controller.experiment3();
                    break;
                case 4:
                    controller.experiment4();
                    break;
                case 5:
                    controller.experiment5();
                    break;
                case 0:
                    break;
            }
        } while (choice != 0);

        //Record test
//        testRecord();

        //Block test
        //testBlockFull();
        //testBlockDelnInsert();
        //testBlockFullDel();

        //Disk test
//        testDisk();

        //TsvReader.TsvToStringArray("data.tsv").get(0).printRecord();

    }

    public static void testRecord() {
        RecordData recordData = new RecordData("tt0000001", 5.6f, 1645);
        record = new Record(
                new RecordHeader(0, null, recordData),
                recordData
        );
        record.printRecord();
    }

    public static void testBlockFull() {
        System.out.println("\n\n################Block insert test################");
        while (!block.isFull()) {
            Address adr = block.addRecord(0, record);
        }
        System.out.println("Block is full");
    }

    public static void testBlockDelnInsert() {
        System.out.println("\n\n################Block del & insert test################");
        if (block.deleteRecord(11)) {
            System.out.println("Block not found!");
        }

        if (block.deleteRecord(5)) {
            //System.out.println("Block 5 deleted! Block size: " + block.getRecords().size() );

            //block.addRecord(record);

            System.out.println("Inserted to empty block! Block full: " + block.isFull() + "\nCurrent index : " + block.getCurrIndex());

            block.addRecord(0, record);

            System.out.println("Inserted to empty block! Block full: " + block.isFull());
        }
    }

    public static void testBlockFullDel() {
        System.out.println("\n\n################Block full del################");

        Block cBlock = new Block();
        Random rnd = new Random();

        ArrayList<Integer> allIndex = new ArrayList<>();
        int index = 0;

        while (!cBlock.isFull()) {
            Address adr = cBlock.addRecord(0, record);
            allIndex.add(index);
            index++;
        }

        while (!cBlock.isEmpty()) {
            int indexDel;
            if (allIndex.size() != 0) indexDel = rnd.nextInt(allIndex.size());
            else indexDel = 0;
            System.out.println("Deleting block " + indexDel);

            cBlock.deleteRecord(indexDel);
            allIndex.remove(indexDel);

            for (Record r : cBlock.getRecords()) {
                if (r != null) {
                    //r.printRecord();
                } else {
                    System.out.println("Block is empty");
                }
            }

            System.out.println("\n");
        }
    }

    public static void testDisk() {
        System.out.println("\n\n################Disk insert test################");
        Disk disk = new Disk();
        ArrayList<Address> Addresss = new ArrayList<>();
        Random rnd = new Random();

        int i;
        //fill disk with records
        for (i = 0; i < 18; i++) {
            Addresss.add(disk.addRecord(record));
        }

        //System.out.println("Current block amount : " + disk.getBlockCount());

        for (int j = 0; j < 18; j++) {

            int indexToDel = rnd.nextInt(i - j);
            Address addToDel = Addresss.get(indexToDel);

            Addresss.remove(addToDel);

            disk.deleteRecord(addToDel);
        }

        System.out.println("Current block amount : " + disk.getBlocks().length);

    }
}