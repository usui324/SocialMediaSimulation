package io.output.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetDirectory {

    public static String setDirectory(){
        String dirName = "result/" + setDirName();
        String path = dirName + "/";
        makeDirectory(dirName);
        return path;
    }


    private static String setDirName() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(calendar.getTime()).toString();
    }

    private static void makeDirectory(String dirName) {
        File folder = new File(dirName);
        if(folder.mkdir()) {
            System.out.println("出力フォルダ生成に成功");
        }
        else {
            System.out.println("出力フォルダ生成に失敗");
            System.exit(0);
        }
    }
}
