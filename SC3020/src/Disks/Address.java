package Disks;

import Records.Record;

public class Address {
    private int block;
    private int index;

    public Address(int block, int index) {
        this.block = block;
        this.index = index;
    }

    public int getBlock() {
        return block;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /*
     * Get specific record inside block
     */
    public Record getRecord(Disk disk, int blockIndex , int recordIndex) {

        return disk.getBlocks()[blockIndex].getRecords()[index];
    }
}
