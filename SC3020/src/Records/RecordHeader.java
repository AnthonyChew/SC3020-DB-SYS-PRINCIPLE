package Records;

import Utils.CalculateSizeUtil;

public class RecordHeader {
    private int recordPointer; //4 bytes
    private int recordSize; // Record Size, including recordData and recordHeader

    // Constructor
    public RecordHeader(int recordPointer, byte[] schemaPointer, RecordData recordData) {
        this.recordPointer = recordPointer;
        this.recordSize = calculateRecordSize(recordData);
    }

    // Getters
    public int getRecordPointer() {
        return recordPointer;
    }

    public int getRecordSize() {
        return recordSize;
    }

    // Setters
    public void setRecordSize(RecordData data) {
        this.recordSize = calculateRecordSize(data);
    }

    public int calculateRecordSize(RecordData data) {
        int size = 0;
        size = size + (CalculateSizeUtil.getSize(data.gettConst()[0]) * data.gettConst().length);
        size = size + CalculateSizeUtil.getSize(data.getAverageRating());
        size = size + CalculateSizeUtil.getSize(data.getNumVotes());
        size = size + CalculateSizeUtil.getSize(getRecordPointer());
        size = size + CalculateSizeUtil.getSize(getRecordSize());
        return size;
    }
}
