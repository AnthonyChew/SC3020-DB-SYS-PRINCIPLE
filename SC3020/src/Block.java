import Records.Record;

public class Block {
    public static final int BlockSize = 200; // 200 bytes block size
    public static final int RecordSize = 20; // 20 bytes record size
    public static final int MaxNumRecords = BlockSize / RecordSize;
    private Record[] records;
    private byte emptyIndex = 0;

    private byte prevIndex = 0;

    //Getter
    public Record[] getRecords() {
        return records;
    }

    public byte getEmptyIndex() {
        return emptyIndex;
    }

    public byte getPrevIndex() {
        return prevIndex;
    }

    /*
    Constructor point current empty slot to first
    Generate all empty records
    */
    public Block() {
        emptyIndex = 0;
        records = new Record[MaxNumRecords];
    }

    public Address addRecord(Record record) {
        int offset = emptyIndex > MaxNumRecords ? emptyIndex = MaxNumRecords : emptyIndex;

        if (emptyIndex == prevIndex) {
            records[offset] = record;
            emptyIndex++;
        } else {
            records[emptyIndex] = record;
            emptyIndex = prevIndex;
        }

        prevIndex = emptyIndex;

        return new Address(this, offset);
    }

    public boolean deleteRecord(int index) {
        if (index < MaxNumRecords) {
            emptyIndex = (byte) index;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return emptyIndex >= MaxNumRecords;
    }

    public boolean isEmpty() {
        return emptyIndex == 0;
    }
}
