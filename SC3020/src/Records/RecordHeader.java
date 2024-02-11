package Records;

//Record header class conatins [4 bytes | Record Pointer for identification][4 bytes | To store size of whole record]
public class RecordHeader {
    private int recordPointer; //4 bytes
    private int recordSize; // Record Size = (recordData + recordHeader)

    // Constructor
    public RecordHeader(int recordPointer) {
        this.recordPointer = recordPointer;
    }

    // Getters
    public int getRecordPointer() {
        return recordPointer;
    }

    public int getRecordSize() {
        return recordSize;
    }

    // Setters
    protected void setRecordSize(int recordSize) {
        this.recordSize = recordSize;
    }
}
