package UnitTest;

import Blocks.Block;
import Disks.Address;
import Disks.Disk;
import Index.BPlusTree;
import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Test1 {
    // Global data for reuse
    static Record record;
    static Block block = new Block();

    public static void testRecord() {
        RecordData recordData = new RecordData("tt0000001", 5.6f, 1645);
        record = new Record(
                new RecordHeader(0),
                recordData);
        record.printRecord();
    }

    public static void testBlockFull() {
        System.out.println("\n\n################Blocks.Block insert test################");
        while (!block.isFull()) {
            block.addRecord(0, record);
        }
        System.out.println("Blocks.Block is full");
    }

    public static void testBlockDelnInsert() {
        System.out.println("\n\n################Blocks.Block del & insert test################");
        if (block.deleteRecord(11)) {
            System.out.println("Blocks.Block not found!");
        }

        if (block.deleteRecord(5)) {
            System.out.println("Inserted to empty block! Blocks.Block full: " + block.isFull() + "\nCurrent index : "
                    + block.getCurrIndex());

            block.addRecord(0, record);

            System.out.println("Inserted to empty block! Blocks.Block full: " + block.isFull());
        }
    }

    public static void testBlockFullDel() {
        System.out.println("\n\n################Blocks.Block full del################");

        Block cBlock = new Block();
        Random rnd = new Random();

        ArrayList<Integer> allIndex = new ArrayList<>();
        int index = 0;

        while (!cBlock.isFull()) {
            cBlock.addRecord(0, record);
            allIndex.add(index);
            index++;
        }

        while (!cBlock.isEmpty()) {
            int indexDel;
            if (allIndex.size() != 0)
                indexDel = rnd.nextInt(allIndex.size());
            else
                indexDel = 0;
            System.out.println("Deleting block " + indexDel);

            cBlock.deleteRecord(indexDel);
            allIndex.remove(indexDel);

            for (Record r : cBlock.getRecords()) {
                if (r != null) {
                    // r.printRecord();
                } else {
                    System.out.println("Blocks.Block is empty");
                }
            }

            System.out.println("\n");
        }
    }

    public static void testDisk() {
        System.out.println("\n\n################Disks.Disk insert test################");
        Disk disk = new Disk();
        ArrayList<Address> Addresss = new ArrayList<>();
        Random rnd = new Random();

        int i;
        // fill disk with records
        for (i = 0; i < 18; i++) {
            Addresss.add(disk.addRecord(record));
        }

        // System.out.println("Current block amount : " + disk.getBlockCount());

        for (int j = 0; j < 18; j++) {

            int indexToDel = rnd.nextInt(i - j);
            Address addToDel = Addresss.get(indexToDel);

            Addresss.remove(addToDel);

            disk.deleteRecord(addToDel);
        }

        System.out.println("Current block amount : " + disk.getBlocks().length);

    }

    public void testBPlusTreeInsert() {
        int keys[] = new int[] { 25, 16, 17, 60, 9, 21, 18, 12, 17, 16, 62, 14, 159 };

        BPlusTree tree = new BPlusTree(25);
        Address addr = new Address(3, 6);

        for (int key : keys) {
            System.out.println("Inserting key: " + key);
            tree.insert(key, addr);
        }

        tree.printTree();
    }

    public void debugBTree() {
        BPlusTree test_tree = new BPlusTree(25);

        int[] keys = null;

        // Generate random numbers to append to keys up to 1000
        // Random random = new Random();
        // int[] keys = new int[2000];
        // for (int i = 0; i < 2000; i++) {
        // keys[i] = i + random.nextInt(1000);
        // }

        // String fileName = "keys.txt";
        // try {
        // FileWriter writer = new FileWriter(fileName);
        // for (int key : keys) {
        // test_tree.insert(key, new Address(0, 0));
        // writer.write(key + " ");
        // }
        // writer.close();
        // System.out.println("Keys written to file: " + fileName);
        // } catch (IOException e) {
        // System.out.println("An error occurred while writing keys to file: " +
        // e.getMessage());
        // }

        // Read keys from text file
        String fileName = "keys.txt";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            String[] keyStrings = line.split(" ");
            keys = new int[keyStrings.length];
            for (int i = 0; i < keyStrings.length; i++) {
                keys[i] = Integer.parseInt(keyStrings[i]);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading keys from file: " + e.getMessage());
        }

        for (int key : keys) {
            test_tree.insert(key, new Address(0, 0));
        }

        // test_tree.printTree();
        // System.out.println();
        // test_tree.printLeafs();
        // System.out.println();

        // for (int key : keys) {
        // test_tree.insert(key, new Address(0, 0));
        // }

        // test_tree.printTree();
        // System.out.println();
        // test_tree.printLeafs();
        // System.out.println();
        // test_tree.printKPLusOneKeys();
    }

    public static void testBPlusTree() {
        BPlusTree bPlusTree = new BPlusTree(4); // Assuming BPlusTree class exists
        Scanner scanner = new Scanner(System.in);
        String userInput;
        Disk disk = new Disk();

        while (true) {
            System.out.println("Enter your choice (i/d/q/e):");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("i")) {
                System.out.println("Enter the key to insert:");
                int key = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Insert the key into the B+ tree
                Address addr = disk.addRecord(new Record(new RecordHeader(key), new RecordData("tt00001", key, key)));
                bPlusTree.insert(key, addr);
                System.out.println("Key inserted successfully.");
                bPlusTree.printTree();
            } else if (userInput.equalsIgnoreCase("d")) {
                System.out.println("Enter the key to delete:");
                int key = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Delete the key from the B+ tree
                bPlusTree.delete(key, disk);
                System.out.println("Key deleted successfully.");
                bPlusTree.printTree();
            } else if (userInput.equalsIgnoreCase("q")) {
                System.out.println("Enter the key to query:");
                int key = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Query the B+ tree for the key
                bPlusTree.query(key, disk);
            } else if (userInput.equalsIgnoreCase("e")) {
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        }

        scanner.close();
    }

}
