package Records;

import Utils.DateConverter;
import Utils.IntConverter;

/*
Record includes [7 byte record header] [23 byte record data]

record header : [ record pointer (4 bytes) | timestamp (3 bytes) ]

record data : [ tCont (18 bytes) | averageRating (1 bytes) | numVotes (4 bytes) ]
*/
public class Record {
    private RecordHeader recordHeader;
    private RecordData recordData;

    public RecordHeader getRecordHeader() {
        return recordHeader;
    }

    public RecordData getRecordData() {
        return recordData;
    }

    public Record(RecordHeader recordHeader, RecordData recordData) {
        this.recordHeader = recordHeader;
        this.recordData = recordData;
    }

    public void printRecord() {
        System.out.println("Record pointer : " + this.getRecordHeader().getRecordPointer() +
                "\nRecord Data : " + new String(this.getRecordData().gettConst()) + "\n" +
                this.getRecordData().getAverageRating() + "\n" +
                this.getRecordData().getNumVotes()  + "\n");
    }

    public void setRecordSize() {
        this.recordHeader.setRecordSize(this.recordData);
    }
}
