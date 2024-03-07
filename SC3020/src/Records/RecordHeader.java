package Records;

//Record header class conatins [4 bytes | Record Pointer for identification][4 bytes | To store size of whole record]
/**
 * The RecordHeader class represents the header of a record in a database.
 * It contains information about the record's pointer and size.
 */
public class RecordHeader {
    private int recordPointer; // 4 bytes
    private int recordSize; // Record Size = (recordData + recordHeader)

    /**
     * Constructs a RecordHeader object with the specified record pointer.
     *
     * @param recordPointer the pointer to the record
     */
    public RecordHeader(int recordPointer) {
        this.recordPointer = recordPointer;
    }

    /**
     * Returns the pointer to the record.
     *
     * @return the record pointer
     */
    public int getRecordPointer() {
        return recordPointer;
    }

    /**
     * Returns the size of the record.
     *
     * @return the record size
     */
    public int getRecordSize() {
        return recordSize;
    }

    /**
     * Sets the size of the record.
     *
     * @param recordSize the size of the record
     */
    protected void setRecordSize(int recordSize) {
        this.recordSize = recordSize;
    }
}
