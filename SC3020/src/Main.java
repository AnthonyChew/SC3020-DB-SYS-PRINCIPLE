import Index.BPlusTree;
import Records.Record;
import Records.Record;
import Records.RecordData;
import Records.RecordHeader;
import Utils.TsvReader;

import javax.management.relation.RelationNotification;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Record test
        // testRecord();

        // Block test
        // //testBlockFull();
        // //testBlockDelnInsert();
        testBPlusTree();
        // testBlockFullDel();

        // Disk test
        testDisk();

        // TsvReader.TsvToStringArray("data.tsv").get(0).printRecord();
        TsvReader.TsvToStringArray("data.tsv").get(0).printRecord();

    }

    static Record record;<<<<<<<
    Updated upstream
    static Block block = new Block();;

    public static void testRecord() {
        record = new Record(new RecordHeader(0), new RecordData("tt0000001", 5.6f, 1645));

    public static void testRecord() {
        record = new Record(new RecordHeader(0), new RecordData("tt0000001", 5.6f, 1645));
        record.printRecord();
    }

    public static void testBlockFull() {
        while (!block.isFull()) {
            block.addRecord(record);
            System.out.println("Inserting to block" + block.getEmptyIndex());
=======

    static Block block = new Block();

    public static void testRecord() {
        record = new Record(new RecordHeader(0, null), new RecordData("tt0000001", 5.6f, 1645));
        record.printRecord();
    }

    public static void testBlockFull()
    {
        System.out.println("\n\nBlock insert test");
        while(!block.isFull())
        {
            Address adr =  block.addRecord(record);
>>>>>>> Stashed changes
        }
        System.out.println("Block is full");
    }

    <<<<<<<

    Updated upstream

    public static void testBlockDelnInsert() {
        if (block.deleteRecord(11)) {
            System.out.println("Block not found!");
        }

        if (block.deleteRecord(5)) {
            System.out.println("Block 5 deleted!\nCurrent pointer at : " + block.getEmptyIndex() + "\nPrev pointer at :"
                    + block.getPrevIndex());

            block.addRecord(record);

            System.out.println("Inserted to empty block!\nCurrent pointer at : " + block.getEmptyIndex()
                    + "\nPrev pointer at :" + block.getPrevIndex());
=======

    public static void testBlockDelnInsert()
    {
        System.out.println("\n\n##Block del & insert test##");
        if(block.deleteRecord(11))
        {
            System.out.println("Block not found!");
        }

        if(block.deleteRecord(5))
        {
            //System.out.println("Block 5 deleted! Block size: " + block.getRecords().size() );

            //block.addRecord(record);

            System.out.println("Inserted to empty block! Block full: " + block.isFull() + "\nCurrent index : " + block.getCurrIndex());

            block.addRecord(record);

            System.out.println("Inserted to empty block! Block full: " + block.isFull());
>>>>>>> Stashed changes
        }
    }

    public static void testBlockFullDel() {
        System.out.println("\n\n##Block full del##");

        Block cBlock = new Block();
        Random rnd = new Random();

        ArrayList<Integer> allIndex = new ArrayList<>();
        int index = 0;

        while (!cBlock.isFull()) {
            Address adr = cBlock.addRecord(record);
            allIndex.add(index);
            index++;
        }

        while (!cBlock.isEmpty()) {
            int indexDel;
            if (allIndex.size() != 0)
                indexDel = rnd.nextInt(allIndex.size());
            else
                indexDel = 0;
            System.out.println("Deleting block " + indexDel);

            cBlock.deleteRecord(indexDel);
            allIndex.remove(indexDel);

            for (Record r : cBlock.getRecords()) {
                if (r != null) {
                    // r.printRecord();
                } else {
                    System.out.println("Block is empty");
                }
            }

            System.out.println("\n");
        }
    }

    public static void testDisk() {
        System.out.println("\n\n##Disk insert test##");
        Disk disk = new Disk();
        ArrayList<Address> addresses = new ArrayList<>();
        Random rnd = new Random();

        int i;
        // fill disk with records
        for (i = 0; i < 18; i++) {
            addresses.add(disk.addRecord(record));
        }

        System.out.println("Current block amount : " + disk.getBlockCount());

        for (int j = 0; j < 18; j++) {
            Address addToDel = addresses.get(rnd.nextInt(i - j));
            addresses.remove(addToDel);

            disk.deleteRecord(addToDel);
        }

        System.out.println("Current block amount : " + disk.getBlockCount());

    }

    public static void testBPlusTree() {
        int keys[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int values[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        BPlusTree tree = new BPlusTree(3);
        tree.insert(5, 4);
        tree.insert(3, 3);
        tree.insert(1, 2);
        tree.insert(2, 1);
        tree.insert(4, 1);
        tree.insert(10, 1);

        tree.printTree();
    }

}