import Disks.Address;
import Disks.Disk;

import java.util.ArrayList;
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
                    "4) Experiment 4 - Retrieve movies with 'numVotes' ranging from [30 000, 40000] and report statistics.");
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
}