package Records;

import Utils.CalculateSizeUtil;

/*
Record includes [8 byte record header] [26 byte record data]

record header : [record pointer (4 bytes)][record size (4 bytes)] = 8 bytes

record data : [ tCont (18 bytes) | averageRating (4 bytes) | numVotes (4 bytes) ] = 26 bytes
*/
public class Record {
    private RecordHeader recordHeader;
    private RecordData recordData;

    //Getter
    public RecordHeader getRecordHeader() {
        return recordHeader;
    }

    public RecordData getRecordData() {
        return recordData;
    }

    //Update size when Header is changed
    public void setRecordHeader(RecordHeader recordHeader) {
        this.recordHeader = recordHeader;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    //Update size when record is changed
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    //Constructor
    public Record(RecordHeader recordHeader, RecordData recordData) {
        this.recordHeader = recordHeader;
        this.recordData = recordData;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    //Print data for record
    public void printRecord() {
        System.out.println("Record pointer : " + this.getRecordHeader().getRecordPointer() +
                "\nRecord Data : " + new String(this.getRecordData().gettConst()) + "\n" +
                this.getRecordData().getAverageRating() + "\n" +
                this.getRecordData().getNumVotes()  + "\n");
    }

    //Calculate record size
    private int calculateRecordSize() {
        int size = 0;
        size = size + (CalculateSizeUtil.getSize(getRecordData().gettConst()[0]) * getRecordData().gettConst().length);
        size = size + CalculateSizeUtil.getSize(getRecordData().getAverageRating());
        size = size + CalculateSizeUtil.getSize(getRecordData().getNumVotes());
        size = size + CalculateSizeUtil.getSize(getRecordHeader().getRecordPointer());
        return size;
    }
}
