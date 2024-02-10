import Records.Record;


public class Disk {
    private static final int DiskSize = 100;
    private final int TotalDiskSize;
    private final int MaxBlock;

    private Block[] blocks;

    private int currIndex = 0;

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

            return blocks[currIndex].addRecord(record);
        }
    }

    public boolean deleteRecord(Address address)
    {
        Block block = address.getBlock();

        if(block.deleteRecord(address.getIndex() - 1))
        {
            if(block.getCurrIndex() < getBlockCount() )
            {
                diskCompress(block);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int getBlockCount()
    {
        int count;
        for(count = 0 ; count < MaxBlock; count++)
        {
            if(this.getBlocks()[count] == null) return count;
        }
        return MaxBlock;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    private void diskCompress(Block block)
    {
        //Find located of the block
        int index = 0;
        for (Block b : blocks )
        {
            if(b == block)
            {
                 break;
            }
            index++;
        }

        //Make sure next block is not empty
        if(index + 1 <= currIndex)
        {
            //Since each delete always compress block just need to push in data from the next block if exist until the latest block
            for(int i = index; i < currIndex; i++)
            {
                if(i + 1 <= currIndex) {
                    if (blocks[i + 1] != null) {
                        //Add first record from next block
                        blocks[i].addRecord(blocks[i + 1].getRecords()[0]);

                        //Delete first record from next block and compressed
                        blocks[i + 1].deleteRecord(0);

                        //If the currentIndex block is empty delete the block
                        if (blocks[i + 1].isEmpty()) {
                            blocks[currIndex] = null;
                        }
                    }
                }
            }
        }
        else
        {
            if(blocks[currIndex] != null) {
                if (blocks[currIndex].isEmpty()) {
                    blocks[currIndex] = null;
                }
            }
        }
    }
}
