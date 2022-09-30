package io.output.txt;

import java.io.FileWriter;
import java.io.IOException;

public class WriteTxt {

    private static final String NEWLINE = "\r\n";
    private static final String EMPTY_LINE = "\r\n\r\n";

    public static void writeTxt(OutputTxtData txtData, String path){
        String fileName = path + "readme.txt";
        writeTxtData(txtData, fileName);
    }

    private static void writeTxtData(OutputTxtData txtData, String fileName){
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, false);

            // タイトル
            if(txtData.getTitle()!=null){
                fw.append(txtData.getTitle());
                fw.append(EMPTY_LINE);
            }

            // 試行回数
            if(txtData.getSimTime()!=Integer.MIN_VALUE){
                fw.append("SimTime: "+txtData.getSimTime());
                fw.append(EMPTY_LINE);
            }

            // delta myu
            if(txtData.getDelta()!=Double.MIN_VALUE) {
                fw.append("Delta: "+txtData.getDelta());
                fw.append(NEWLINE);
            }
            if(txtData.getMyu()!=Double.MIN_VALUE) {
                fw.append("Myu: "+txtData.getMyu());
                fw.append(EMPTY_LINE);
            }

            //

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
