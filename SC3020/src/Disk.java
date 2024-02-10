import Records.Record;


public class Disk {
    private static final int DiskSize = 100;
    private final int TotalDiskSize;
    private final int MaxBlock;

    private Block[] blocks;

    private int currIndex = 0;

    //Getter
    public Block[] getBlocks() {
        return blocks;
    }

    public Disk()
    {
        TotalDiskSize = DiskSize * 1024 * 1024;
        MaxBlock = this.TotalDiskSize / Block.BlockSize;

        blocks = new Block[MaxBlock];
    }

    public Address addRecord(Record record)
    {
        if(currIndex > MaxBlock)
        {
            return null;
        }
        else
        {
            if(blocks[currIndex] == null) blocks[currIndex] = new Block(); //to creat first block in disk

            if(blocks[currIndex].isFull())
            {
                currIndex++;
                blocks[currIndex] = new Block();
            }

            return blocks[currIndex].addRecord(currIndex ,record);
        }
    }

    public boolean deleteRecord(Address address)
    {
        //Get block from disk
        Block block = blocks[address.getBlock()];

        if(block.deleteRecord(address.getIndex()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

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
