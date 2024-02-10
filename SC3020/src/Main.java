import Records.Record;
import Records.RecordData;
import Records.RecordHeader;
import Utils.TsvReader;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //Record test
        testRecord();

        //Block test
        //testBlockFull();
        //testBlockDelnInsert();
        //testBlockFullDel();

        //Disk test
        testDisk();

        TsvReader.TsvToStringArray("data.tsv").get(0).printRecord();

    }

    static Record record;
    static Block block = new Block();
    public static void testRecord()
    {
        record = new Record(new RecordHeader(0,null) , new RecordData("tt0000001" , 5.6f, 1645));
        record.printRecord();
    }

    public static void testBlockFull()
    {
        System.out.println("\n\nBlock insert test");
        while(!block.isFull())
        {
            Address adr =  block.addRecord(record);
        }
        System.out.println("Block is full");
    }

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
        }
    }

    public static void testBlockFullDel()
    {
        System.out.println("\n\n##Block full del##");

        Block cBlock = new Block();
        Random rnd = new Random();

        ArrayList<Integer> allIndex = new ArrayList<>();
        int index = 0;

        while(!cBlock.isFull())
        {
            Address adr =  cBlock.addRecord(record);
            allIndex.add(index) ;
            index ++;
        }

        while (!cBlock.isEmpty())
        {
            int indexDel;
            if(allIndex.size() != 0) indexDel = rnd.nextInt(allIndex.size());
            else indexDel = 0;
            System.out.println("Deleting block " + indexDel);

            cBlock.deleteRecord(indexDel);
            allIndex.remove(indexDel);

            for (Record r : cBlock.getRecords())
            {
                if(r != null)
                {
                    //r.printRecord();
                }
                else
                {
                    System.out.println("Block is empty");
                }
            }

            System.out.println("\n");
        }
    }

    public static void testDisk()
    {
        System.out.println("\n\n##Disk insert test##");
        Disk disk = new Disk();
        ArrayList<Address> addresses = new ArrayList<>();
        Random rnd = new Random();

        int i;
        //fill disk with records
        for(i = 0; i < 18; i++)
        {
            addresses.add(disk.addRecord(record));
        }

        System.out.println("Current block amount : " + disk.getBlockCount());

        for(int j = 0; j < 18; j++)
        {
            Address addToDel = addresses.get( rnd.nextInt(i - j));
            addresses.remove(addToDel);

            disk.deleteRecord(addToDel);
        }

        System.out.println("Current block amount : " + disk.getBlockCount());

    }
}