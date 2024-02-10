package Utils;

import Records.Record;
import Records.RecordData;
import Records.RecordHeader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TsvReader {
    public static ArrayList<Record> TsvToStringArray(String fileName) {
        //Make sure always get from project path 
        File file = new File(System.getProperty("user.dir") + "/SC3020/src/" + fileName);

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
                            Integer.parseInt(lineItems[2])
                    );
                    Record record = new Record(
                            new RecordHeader(index,null, recordData),
                            recordData
                    );

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
