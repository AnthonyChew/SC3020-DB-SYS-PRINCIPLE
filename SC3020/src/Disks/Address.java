package Disks;

/**
 * The Address class represents a disk address consisting of a block number and
 * an index.
 */
public class Address {
    private int block;
    private int index;

    /**
     * Constructs an Address object with the specified block number and index.
     *
     * @param block the block number
     * @param index the index
     */
    public Address(int block, int index) {
        this.block = block;
        this.index = index;
    }

    /**
     * Returns the block number of this address.
     *
     * @return the block number
     */
    public int getBlock() {
        return block;
    }

    /**
     * Returns the index of this address.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of this address.
     *
     * @param index the new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Returns a string representation of this address.
     *
     * @return a string representation of this address
     */
    public String toString() {
        return "Block(" + block + ") Index(" + index + ")";
    }
}
