package Utils;

import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/*
    Read line by line in from file and return an array list of records
*/
/**
 * The TsvReader class provides methods to read data from a TSV (Tab-Separated
 * Values) file.
 */
public class TsvReader {

    /**
     * Reads the contents of a TSV file and converts it into an ArrayList of Record
     * objects.
     *
     * @param fileName the name of the TSV file to read
     * @return an ArrayList of Record objects representing the data in the TSV file
     */
    public static ArrayList<Record> TsvToStringArray(String fileName) {
        // Make sure always get from project path
        File file = new File(System.getProperty("user.dir") + "/SC3020/src/" + fileName);

        // Only for deployment
        // File file = new File(System.getProperty("user.dir")+ "/" + fileName);

        ArrayList<Record> Data = new ArrayList<>();
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = TSVReader.readLine()) != null) {
                if (index != 0) {
                    String[] lineItems = line.split("\t");
                    RecordData recordData = new RecordData(
                            lineItems[0],
                            Float.parseFloat(lineItems[1]),
                            Integer.parseInt(lineItems[2]));
                    Record record = new Record(
                            new RecordHeader(index),
                            recordData);

                    Data.add(record); // add to Data
                }

                index++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Data;
    }
}
