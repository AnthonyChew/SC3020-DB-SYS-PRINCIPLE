package Disks;

import Blocks.Block;
import Records.Record;

//Disk class
public class Disk {
    private static final int DiskSize = 100; // Disk size 100 MB
    private final int TotalDiskSize; // Total disk size (in Bytes)
    private final int MaxBlock; // Amount of block size for disk

    private Block[] blocks; // Block array

    private int currIndex = 0; // Current index

    // Constructor to calculate total disk size, max block amount and set all empty
    // block in disk
    public Disk() {
        TotalDiskSize = DiskSize * 1024 * 1024;
        MaxBlock = this.TotalDiskSize / Block.BlockSize;

        blocks = new Block[MaxBlock];
    }

    // Getter
    public Block[] getBlocks() {
        return blocks;
    }

    /*
     * Add record to disk
     * if current block is full create a new block
     * add a new record
     */
    public Address addRecord(Record record) {
        if (currIndex > MaxBlock) {
            return null;
        } else {
            if (blocks[currIndex] == null)
                blocks[currIndex] = new Block(); // to creat first block in disk

            if (blocks[currIndex].isFull()) {
                currIndex++;
                blocks[currIndex] = new Block();
            }

            return blocks[currIndex].addRecord(currIndex, record);
        }
    }

    /*
     * Get specific record inside block
     */
    public Record getRecord(Address address) {
        return this.blocks[address.getBlock()].getRecords()[address.getIndex()];
    }

    // Overload deleteRecord with Address for B+ tree
    public boolean deleteRecord(Address address) {
        // Get block from disk
        if (blocks[address.getBlock()] != null && !blocks[address.getBlock()].isEmpty()) {
            Block block = blocks[address.getBlock()];
            return block.deleteRecord(address.getIndex());
        } else
            return false;
    }

    // Overload deleteRecord with block & record index for brute force
    public boolean deleteRecord(int blockIdx, int recordIdx) {
        if (blocks[blockIdx] != null && !blocks[blockIdx].isEmpty()) {
            Block block = blocks[blockIdx];
            return block.deleteRecord(recordIdx);
        } else
            return false;
    }

    // Check through all blocks in disk to calculate
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
