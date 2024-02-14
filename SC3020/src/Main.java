import Blocks.Block;
import Disks.Address;
import Disks.Disk;
import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Disk disk = new Disk();
        ArrayList<Address> addresses = new ArrayList<>();
        Controller controller = new Controller(disk, addresses);
        int choice = -1;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Select an option:");
            System.out.println("1) Experiment 1 - Store data on disk and report statistics.");
            System.out.println(
                    "2) Experiment 2 - Build B+ Tree on 'numVotes' by inserting records sequentially and report statistics.");
            System.out.println("3) Experiment 3 - Retrieve movies with 'numVotes' equal to 500 and report statistics.");
            System.out.println(
                    "4) Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40 000] and report statistics.");
            System.out.println(
                    "5) Experiment 5 - Delete movies with 'numVotes' equal to 1000, update B+ tree and report statistics.");
            System.out.println("\n0) Exit.");

            System.out.print("Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    controller.experiment1();
                    break;
                case 2:
                    controller.experiment2();
                    break;
                case 3:
                    controller.experiment3();
                    break;
                case 4:
                    controller.experiment4();
                    break;
                case 5:
                    controller.experiment5();
                    break;
                case 0:
                    break;
            }
        } while (choice != 0);
    }

    public static void testBPlusTree() {
        int keys[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        int values[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        BPlusTree tree = new BPlusTree(3);
        tree.insert(5, 4);
        tree.insert(3, 3);
        tree.insert(1, 2);
        tree.insert(2, 1);
        tree.insert(4, 1);
        tree.insert(10, 1);
        tree.insert(15, 1);
        tree.insert(20, 1);
        tree.insert(17, 1);

        tree.printTree();
    }

}