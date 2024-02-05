package Records;
import Utils.DateConverter;

import java.util.Date;

public class RecordHeader {
    private int recordPointer; //4 bytes
    private byte[] timeStamp; // 3 bytes

    public int getRecordPointer() {
        return recordPointer;
    }

    public byte[] getTimeStamp() {
        return timeStamp;
    }

    public RecordHeader(int recordPointer) {
        this.recordPointer = recordPointer;
        this.timeStamp = DateConverter.DateToByte3(new Date());
    }
}
