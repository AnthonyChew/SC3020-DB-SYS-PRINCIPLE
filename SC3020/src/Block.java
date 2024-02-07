import Records.Record;


public class Block {
    public static final int BlockSize = 200; // 200 bytes block size
    public static final int RecordSize = 30; // 30 bytes record size
    public static final int MaxNumRecords = BlockSize / RecordSize;
    private Record[] records;
    private byte currIndex = 0;

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
        currIndex = 0;
        records = new Record[MaxNumRecords];
    }

    public BlockHeader addRecord(Record record) {

        if (currIndex < MaxNumRecords) {
            records[currIndex] = record;
            currIndex++;
        } else {
            records[currIndex] = record;
        }

        return new BlockHeader(this, currIndex);
    }

    public boolean deleteRecord(int index) {
        if (index < MaxNumRecords) {
            //Reduce curr index - 1 since will be compressed
            currIndex--;

            //Compress func
            compress(index);
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return currIndex >= MaxNumRecords;
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
