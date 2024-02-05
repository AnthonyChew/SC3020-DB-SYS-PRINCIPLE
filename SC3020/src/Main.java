import  Records.Record;
import Records.RecordData;
import Records.RecordHeader;
import Utils.DateConverter;
import Utils.IntConverter;

public class Main {
    public static void main(String[] args) {
        testRecord();
        testBlockFull();
        testBlockDelnInsert();
    }

    static Record record;
    static Block block = new Block();;
    public static void testRecord()
    {
        record = new Record(new RecordHeader(0) , new RecordData("tt0000001" , 5.6f,	1645));

        System.out.println("Record pointer : " + record.getRecordHeader().getRecordPointer() +
                        "\nRecord timestamp : " + DateConverter.DateFromByte3(record.getRecordHeader().getTimeStamp())  +
                        "\nRecord Data : " + new String(record.getRecordData().gettConst()) + "\n" + record.getRecordData().getAverageRating() + "\n" + IntConverter.ByteToInt(record.getRecordData().getNumVotes())  );
    }

    public static void testBlockFull()
    {
        while(!block.isFull())
        {
            block.addRecord(record);
            System.out.println("Inserting to block" + block.getEmptyIndex());
        }
        System.out.println("Block is full");
    }

    public static void testBlockDelnInsert()
    {
        if(block.deleteRecord(11))
        {
            System.out.println("Block not found!");
        }

        if(block.deleteRecord(5))
        {
            System.out.println("Block 5 deleted!\nCurrent pointer at : " + block.getEmptyIndex() + "\nPrev pointer at :"+ block.getPrevIndex());

            block.addRecord(record);

            System.out.println("Inserted to empty block!\nCurrent pointer at : " + block.getEmptyIndex() + "\nPrev pointer at :"+ block.getPrevIndex());
        }
    }

}