import Records.Record;

public class BlockHeader {
    private Block block;
    private int index;

    public BlockHeader(Block block, int index) {
        this.block = block;
        this.index = index;
    }

    public Block getBlock() {
        return block;
    }

    public int getIndex() {
        return index;
    }

    public Record getRecord(int index) {
        /**
         * Get specific record inside block
         */
        return block.getRecords()[index];
    }
}
