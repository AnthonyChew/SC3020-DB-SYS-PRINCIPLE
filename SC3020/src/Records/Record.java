package Records;

import Utils.CalculateSizeUtil;

/*
Record includes [8 byte record header] [26 byte record data]

record header : [record pointer (4 bytes)][record size (4 bytes)] = 8 bytes

record data : [ tCont (18 bytes) | averageRating (4 bytes) | numVotes (4 bytes) ] = 26 bytes
*/
/**
 * The Record class represents a record in a database.
 * It contains a record header and record data.
 */
public class Record {
    private RecordHeader recordHeader;
    private RecordData recordData;

    /**
     * Constructs a Record object with the specified record header and record data.
     * The record size is calculated and set in the record header.
     *
     * @param recordHeader the record header
     * @param recordData   the record data
     */
    public Record(RecordHeader recordHeader, RecordData recordData) {
        this.recordHeader = recordHeader;
        this.recordData = recordData;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    /**
     * Returns the record header.
     *
     * @return the record header
     */
    public RecordHeader getRecordHeader() {
        return recordHeader;
    }

    /**
     * Sets the record header and updates the record size in the header.
     *
     * @param recordHeader the record header
     */
    public void setRecordHeader(RecordHeader recordHeader) {
        this.recordHeader = recordHeader;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    /**
     * Returns the record data.
     *
     * @return the record data
     */
    public RecordData getRecordData() {
        return recordData;
    }

    /**
     * Sets the record data and updates the record size in the header.
     *
     * @param recordData the record data
     */
    public void setRecordData(RecordData recordData) {
        this.recordData = recordData;
        this.recordHeader.setRecordSize(calculateRecordSize() + CalculateSizeUtil.getSize(calculateRecordSize()));
    }

    /**
     * Prints the record data.
     */
    public void printRecord() {
        System.out.println("Record pointer : " + this.getRecordHeader().getRecordPointer() +
                "\nRecord Data : " + new String(this.getRecordData().gettConst()) + "\n" +
                this.getRecordData().getAverageRating() + "\n" +
                this.getRecordData().getNumVotes() + "\n");
    }

    /**
     * Calculates the size of the record.
     *
     * @return the size of the record
     */
    private int calculateRecordSize() {
        int size = 0;
        size = size + (CalculateSizeUtil.getSize(getRecordData().gettConst()[0]) * getRecordData().gettConst().length);
        size = size + CalculateSizeUtil.getSize(getRecordData().getAverageRating());
        size = size + CalculateSizeUtil.getSize(getRecordData().getNumVotes());
        size = size + CalculateSizeUtil.getSize(getRecordHeader().getRecordPointer());
        return size;
    }
}
