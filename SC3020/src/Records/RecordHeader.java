package Records;

import Utils.DateConverter;

import java.util.Date;

public class RecordHeader {
    private int recordPointer; //4 bytes

    private Date timeStamp; // 3 bytes //change to datetime

    private int recordSize; // Record Size, including recordData and recordHeader

    public int getRecordPointer() {
        return recordPointer;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public RecordHeader(int recordPointer , byte[] schemaPointer) {
        this.recordPointer = recordPointer;
        this.timeStamp = new Date();
    }

    public void setRecordSize(int size) {
        this.recordSize = size;
    }
}
