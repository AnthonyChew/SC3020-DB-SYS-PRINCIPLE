import Records.Record;

public class Address {
    private Block block;
    private int blockIndex;
    private int recordIndex;

    public Address(Block block, int recordIndex) {
        this.block = block;
        this.recordIndex = recordIndex;
    }

    public Block getBlock() {
        return block;
    }

    public int getBlockIndex() {
        return this.blockIndex;
    }

    public int getIndex() {
        return recordIndex;
    }

    public Record getRecord(int recordIndex) {
        /**
         * Get specific record inside block
         */
        return block.getRecords()[recordIndex];
    }
}
