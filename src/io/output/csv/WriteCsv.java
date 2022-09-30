package io.output.csv;

import com.opencsv.CSVWriter;
import io.output.csv.OutputData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WriteCsv {
    public static void  write(List<String[]> strArrayList, String path) {
        String OutputFileName = path + "outputData.csv";
        CSVWriter csvWriter = null;
        try{
            File file = new File(OutputFileName);
            FileWriter fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter
                    , ",".charAt(0)
                    , "\"".charAt(0)
                    , "\"".charAt(0)
                    , "\r\n");
            csvWriter.writeAll(strArrayList);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (csvWriter != null) {
                    csvWriter.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
