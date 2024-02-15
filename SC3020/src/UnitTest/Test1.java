package UnitTest;

import Blocks.Block;
import Disks.Address;
import Disks.Disk;
import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.util.ArrayList;
import java.util.Random;

public class Test1 {
    // Global data for reuse
    static Record record;
    static Block block = new Block();

    public static void testRecord() {
        RecordData recordData = new RecordData("tt0000001", 5.6f, 1645);
        record = new Record(
                new RecordHeader(0),
                recordData
        );
        record.printRecord();
    }

    public static void testBlockFull() {
        System.out.println("\n\n################Blocks.Block insert test################");
        while (!block.isFull()) {
            block.addRecord(0, record);
        }
        System.out.println("Blocks.Block is full");
    }

    public static void testBlockDelnInsert() {
        System.out.println("\n\n################Blocks.Block del & insert test################");
        if (block.deleteRecord(11)) {
            System.out.println("Blocks.Block not found!");
        }

        if (block.deleteRecord(5)) {
            System.out.println("Inserted to empty block! Blocks.Block full: " + block.isFull() + "\nCurrent index : " + block.getCurrIndex());

            block.addRecord(0, record);

            System.out.println("Inserted to empty block! Blocks.Block full: " + block.isFull());
        }
    }

    public static void testBlockFullDel() {
        System.out.println("\n\n################Blocks.Block full del################");

        Block cBlock = new Block();
        Random rnd = new Random();

        ArrayList<Integer> allIndex = new ArrayList<>();
        int index = 0;

        while (!cBlock.isFull()) {
            cBlock.addRecord(0, record);
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
                    System.out.println("Blocks.Block is empty");
                }
            }

            System.out.println("\n");
        }
    }

    public static void testDisk() {
        System.out.println("\n\n################Disks.Disk insert test################");
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
