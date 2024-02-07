package Records;

import Utils.DateConverter;

import java.util.Date;

public class RecordHeader {
<<<<<<< Updated upstream
    private int recordPointer; // 4 bytes
    private byte[] timeStamp; // 3 bytes
=======
    private int recordPointer; //4 bytes

    private byte[] schemaPointer; //4 bytes //Can be removed

    private byte[] timeStamp; // 3 bytes //change to datetime
>>>>>>> Stashed changes

    public int getRecordPointer() {
        return recordPointer;
    }

    public byte[] getTimeStamp() {
        return timeStamp;
    }

    public byte[] getSchemaPointer() {
        return schemaPointer;
    }

    public RecordHeader(int recordPointer , byte[] schemaPointer) {
        this.recordPointer = recordPointer;
        this.timeStamp = DateConverter.DateToByte3(new Date());
        this.schemaPointer = schemaPointer;
    }
}
