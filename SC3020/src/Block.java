import Records.Record;

import java.lang.reflect.Array;

public class Block {
<<<<<<< Updated upstream
    public static final int BLOCK_SIZE = 200; // 200 bytes block size
    public static final int RECORD_SIZE = 20; // 20 bytes record size
    public static final int MAX_NUM_RECORDS = BLOCK_SIZE / RECORD_SIZE;
    private Record[] records;
    private byte emptyIndex = 0;
    private byte prevIndex = 0;
=======
    public static final int BlockSize = 200; // 200 bytes block size
    public static final int RecordSize = 30; // 30 bytes record size
    public static final int MaxNumRecords = BlockSize / RecordSize;
    private Record[] records;
    private byte currIndex = 0;
>>>>>>> Stashed changes

    // Getter
    public Record[] getRecords() {
        return records;
    }

    public byte getCurrIndex() {
        return currIndex;
    }

    /*
     * Constructor point current empty slot to first
     * Generate all empty records
     */
    public Block() {
<<<<<<< Updated upstream
        emptyIndex = 0;
        records = new Record[MAX_NUM_RECORDS];
    }

    public Address addRecord(Record record) {
        int offset = emptyIndex > MAX_NUM_RECORDS ? emptyIndex = MAX_NUM_RECORDS : emptyIndex;
=======
        records = new Record[MaxNumRecords];
    }

    public Address addRecord(Record record) {
>>>>>>> Stashed changes

        if (currIndex < MaxNumRecords) {
            records[currIndex] = record;
            currIndex++;
        } else {
            records[currIndex] = record;
        }

        return new Address(this, currIndex);
    }

    public boolean deleteRecord(int index) {
<<<<<<< Updated upstream
        if (index < MAX_NUM_RECORDS) {
            emptyIndex = (byte) index;
=======
        if (index < MaxNumRecords) {
            //Reduce curr index - 1 since will be compressed
            currIndex--;

            //Compress func
            compress(index);
>>>>>>> Stashed changes
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
<<<<<<< Updated upstream
        return emptyIndex >= MAX_NUM_RECORDS;
=======
        return currIndex >= MaxNumRecords;
>>>>>>> Stashed changes
    }

    public boolean isEmpty() {
        return currIndex == 0;
    }

    public void compress(int index) {
        //Create new array
        Record[] recordArray = new Record[MaxNumRecords];

        //Copy unmoved slot
        if (index > 0) System.arraycopy(records, 0, recordArray, 0, index);

        //Copy moved slot
        if (index + 1 < MaxNumRecords) System.arraycopy(records, index + 1, recordArray, index, records.length - index - 1);

        //Update records to new array
        records = recordArray;
    }
}
