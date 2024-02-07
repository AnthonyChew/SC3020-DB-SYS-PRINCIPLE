public class Disk {
    private final int DISKSIZE = (int) (500 * Math.pow(2, 20));
    private final int BLOCKSIZE = 200;
    Block[] blocks;

    public Disk() {
        blocks = new Block[DISKSIZE / BLOCKSIZE];
    }

    // Getter
    public Block[] getBlock() {
        return blocks;
    }
}
