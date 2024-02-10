import Records.Record;
import Utils.ByteUtils;


public class Block {
    public static final int BlockSize = 200; // 200 bytes block size
    // TODO: Change recordSize from being hardcoded to retrieve the recordSize from recordHeader
    public static final int RecordSize = 30; // 30 bytes record size
    public static final int MaxNumRecords = BlockSize / RecordSize;
    private Record[] records;
    private byte emptyFlag = 0;

    // Getter
    public Record[] getRecords() {
        return records;
    }

    public byte getCurrIndex() {
        return emptyFlag;
    }

    /*
     * Constructor point current empty slot to first
     * Generate all empty records
     */
    public Block() {
        records = new Record[MaxNumRecords];
    }

    public Address addRecord(int blockIndex, Record record) {
        int offset = ByteUtils.findFirstZeroBitPosition(emptyFlag);

        if (offset >= 0) {
            records[offset] = record;
            emptyFlag = ByteUtils.toggleBit(emptyFlag, offset);
        } else {
            return null;
        }

        return new Address(blockIndex, offset);
    }

    public boolean deleteRecord(int index) {
        if (index < MaxNumRecords) {
            emptyFlag = ByteUtils.toggleBit(emptyFlag, index);

            //Compress func
            //compress(index);
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return ByteUtils.isBitAllOne(emptyFlag, MaxNumRecords);
    }

    public boolean isEmpty() {
        return emptyFlag == 0;
    }
}
