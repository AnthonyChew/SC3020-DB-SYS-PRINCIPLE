package Blocks;

import Disks.Address;
import Records.Record;
import Utils.ByteUtils;

//Block class
public class Block {
    public static final int BlockSize = 200; // 200 bytes block size
    public static final int RecordSize = 34; // 34 bytes max record size
    public static final int MaxNumRecords = BlockSize / RecordSize; // Max number of records per block

    //Record array to store records
    private Record[] records;
    //Bit flag to determine if slot is filled / empty
    private byte emptyFlag = 0;

    /*
     * Constructor
     * point current empty slot to first
     * Generate all empty records
     */
    public Block() {
        records = new Record[MaxNumRecords];
    }

    // Getter
    public Record[] getRecords() {
        return records;
    }

    public byte getCurrIndex() {
        return emptyFlag;
    }

    //Add record into block
    public Address addRecord(int blockIndex, Record record) {

        //Bitwise flag to find which slot in block is empty
        int offset = ByteUtils.findFirstZeroBitPosition(emptyFlag);

        //If offset is better than 0 then slot found
        //else record is full
        if (offset >= 0) {
            records[offset] = record;
            emptyFlag = ByteUtils.toggleBit(emptyFlag, offset);
        } else {
            return null;
        }

        return new Address(blockIndex, offset);
    }

    //Delete record by marking record is empty
    public boolean deleteRecord(int index) {
        if (index < MaxNumRecords) {
            emptyFlag = ByteUtils.toggleBit(emptyFlag, index);

            return true;
        } else {
            return false;
        }
    }

    //Func to return is block full
    public boolean isFull() {
        return ByteUtils.isBitAllOne(emptyFlag, MaxNumRecords);
    }

    //Func to return is block empty
    public boolean isEmpty() {
        return emptyFlag == 0;
    }
}
