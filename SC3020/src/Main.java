import Index.BPlusTree;
import Records.Record;
import Records.RecordData;
import Records.RecordHeader;
import Utils.DateConverter;
import Utils.IntConverter;
import Utils.TsvReader;

public class Main {
    public static void main(String[] args) {
        // testRecord();
        // testBlockFull();
        // testBlockDelnInsert();
        testBPlusTree();

        // TsvReader.TsvToStringArray("data.tsv").get(0).printRecord();
    }

    static Record record;
    static Block block = new Block();;

    public static void testRecord() {
        record = new Record(new RecordHeader(0), new RecordData("tt0000001", 5.6f, 1645));
        record.printRecord();
    }

    public static void testBlockFull() {
        while (!block.isFull()) {
            block.addRecord(record);
            System.out.println("Inserting to block" + block.getEmptyIndex());
        }
        System.out.println("Block is full");
    }

    public static void testBlockDelnInsert() {
        if (block.deleteRecord(11)) {
            System.out.println("Block not found!");
        }

        if (block.deleteRecord(5)) {
            System.out.println("Block 5 deleted!\nCurrent pointer at : " + block.getEmptyIndex() + "\nPrev pointer at :"
                    + block.getPrevIndex());

            block.addRecord(record);

            System.out.println("Inserted to empty block!\nCurrent pointer at : " + block.getEmptyIndex()
                    + "\nPrev pointer at :" + block.getPrevIndex());
        }
    }

    public static void testBPlusTree() {
        int keys[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int values[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        BPlusTree tree = new BPlusTree(3);
        tree.insert(4, 4);
        tree.insert(3, 3);
        tree.insert(2, 2);
        tree.insert(1, 1);

        tree.printTree();
    }

}