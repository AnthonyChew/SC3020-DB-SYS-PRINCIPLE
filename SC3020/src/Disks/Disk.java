package Disks;

import Blocks.Block;
import Records.Record;

//Disk class
/**
 * Represents a disk with a fixed size and blocks to store records.
 */
public class Disk {
    private static final int DiskSize = 100; // Disk size 100 MB
    private final int TotalDiskSize; // Total disk size (in Bytes)
    private final int MaxBlock; // Amount of block size for disk

    private Block[] blocks; // Block array

    private int currIndex = 0; // Current index

    /**
     * Constructs a Disk object with the specified disk size and initializes all
     * blocks as empty.
     */
    public Disk() {
        TotalDiskSize = DiskSize * 1024 * 1024;
        MaxBlock = this.TotalDiskSize / Block.BlockSize;

        blocks = new Block[MaxBlock];
    }

    /**
     * Retrieves the array of blocks in the disk.
     *
     * @return the array of blocks
     */
    public Block[] getBlocks() {
        return blocks;
    }

    /**
     * Adds a record to the disk.
     * If the current block is full, a new block is created and the record is added
     * to it.
     *
     * @param record the record to be added
     * @return the address of the added record, or null if the disk is full
     */
    public Address addRecord(Record record) {
        if (currIndex > MaxBlock) {
            return null;
        } else {
            if (blocks[currIndex] == null)
                blocks[currIndex] = new Block(); // to create the first block in the disk

            if (blocks[currIndex].isFull()) {
                currIndex++;
                blocks[currIndex] = new Block();
            }

            return blocks[currIndex].addRecord(currIndex, record);
        }
    }

    /**
     * Retrieves the record at the specified address.
     *
     * @param address the address of the record
     * @return the record at the specified address
     */
    public Record getRecord(Address address) {
        return this.blocks[address.getBlock()].getRecords()[address.getIndex()];
    }

    /**
     * Deletes the record at the specified address.
     *
     * @param address the address of the record to be deleted
     * @return true if the record is successfully deleted, false otherwise
     */
    public boolean deleteRecord(Address address) {
        if (blocks[address.getBlock()] != null && !blocks[address.getBlock()].isEmpty()) {
            Block block = blocks[address.getBlock()];
            return block.deleteRecord(address.getIndex());
        } else
            return false;
    }

    /**
     * Deletes the record at the specified block and record index.
     *
     * @param blockIdx  the index of the block
     * @param recordIdx the index of the record
     * @return true if the record is successfully deleted, false otherwise
     */
    public boolean deleteRecord(int blockIdx, int recordIdx) {
        if (blocks[blockIdx] != null && !blocks[blockIdx].isEmpty()) {
            Block block = blocks[blockIdx];
            return block.deleteRecord(recordIdx);
        } else
            return false;
    }

    /**
     * Calculates the total number of used blocks in the disk.
     *
     * @return the total number of used blocks
     */
    public int getTotalUsedBlocks() {
        int usedBlockCount = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            if (this.blocks[i] != null && !this.blocks[i].isEmpty()) {
                usedBlockCount++;
            }
        }
        return usedBlockCount;
    }
}
