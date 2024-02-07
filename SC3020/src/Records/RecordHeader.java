package Records;

import Utils.DateConverter;

import java.util.Date;

public class RecordHeader {
    private int recordPointer; //4 bytes

    private Date timeStamp; // 3 bytes //change to datetime

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
}
