import Records.Record;

public class Block {
    public static final int BLOCK_SIZE = 200; // 200 bytes block size
    public static final int RECORD_SIZE = 20; // 20 bytes record size
    public static final int MAX_NUM_RECORDS = BLOCK_SIZE / RECORD_SIZE;
    private Record[] records;
    private byte emptyIndex = 0;
    private byte prevIndex = 0;

    // Getter
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
     * Constructor point current empty slot to first
     * Generate all empty records
     */
    public Block() {
        emptyIndex = 0;
        records = new Record[MAX_NUM_RECORDS];
    }

    public Address addRecord(Record record) {
        int offset = emptyIndex > MAX_NUM_RECORDS ? emptyIndex = MAX_NUM_RECORDS : emptyIndex;

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
        if (index < MAX_NUM_RECORDS) {
            emptyIndex = (byte) index;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return emptyIndex >= MAX_NUM_RECORDS;
    }

    public boolean isEmpty() {
        return emptyIndex == 0;
    }
}
