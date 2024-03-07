package Blocks;

import Disks.Address;
import Records.Record;
import Utils.ByteUtils;

//Block class
/**
 * Represents a block in a database.
 */
public class Block {
    public static final int BlockSize = 200; // 200 bytes block size
    public static final int RecordSize = 34; // 34 bytes max record size
    public static final int MaxNumRecords = BlockSize / RecordSize; // Max number of records per block

    // Record array to store records
    private Record[] records;
    // Bit flag to determine if slot is filled / empty
    private byte emptyFlag = 0;

    /**
     * Constructs a new Block object.
     * Initializes the record array with empty records.
     */
    public Block() {
        records = new Record[MaxNumRecords];
    }

    /**
     * Gets the records stored in the block.
     * 
     * @return The array of records.
     */
    public Record[] getRecords() {
        return records;
    }

    /**
     * Gets the current index of the empty slot in the block.
     * 
     * @return The current index.
     */
    public int getCurrIndex() {
        return ByteUtils.findFirstZeroBitPosition(emptyFlag);
    }

    /**
     * Adds a record to the block.
     * 
     * @param blockIndex The index of the block.
     * @param record     The record to be added.
     * @return The address of the added record.
     */
    public Address addRecord(int blockIndex, Record record) {
        // Bitwise flag to find which slot in block is empty
        int offset = ByteUtils.findFirstZeroBitPosition(emptyFlag);

        // If offset is better than 0 then slot found
        // else record is full
        if (offset >= 0) {
            records[offset] = record;
            emptyFlag = ByteUtils.toggleBit(emptyFlag, offset);
        } else {
            return null;
        }

        return new Address(blockIndex, offset);
    }

    /**
     * Deletes a record from the block.
     * 
     * @param index The index of the record to be deleted.
     * @return True if the record was successfully deleted, false otherwise.
     */
    public boolean deleteRecord(int index) {
        if (index < MaxNumRecords) {
            emptyFlag = ByteUtils.toggleBit(emptyFlag, index);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the block is full.
     * 
     * @return True if the block is full, false otherwise.
     */
    public boolean isFull() {
        return ByteUtils.isBitAllOne(emptyFlag, MaxNumRecords);
    }

    /**
     * Checks if the block is empty.
     * 
     * @return True if the block is empty, false otherwise.
     */
    public boolean isEmpty() {
        return emptyFlag == 0;
    }
}
