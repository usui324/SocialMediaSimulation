package io.output.common;

import io.output.csv.OutputData;
import io.output.csv.WriteCsv;
import io.output.txt.OutputTxtData;
import io.output.txt.WriteTxt;

public class OutputFiles {
    private String path="";
    private OutputData data = null;
    private OutputTxtData txtData = null;

    public static OutputFiles create(){
        return new OutputFiles();
    }

    public OutputFiles setOutputData(OutputData data){
        this.data = data;
        return this;
    }

    public OutputFiles setOutputTxtData(OutputTxtData txtData){
        this.txtData = txtData;
        return this;
    }

    // 実行メソッド
    public void writeOutputFiles(){
        setDirectory();
        writeOutputCsv();
        writeOutputTxt();
    }

    private void setDirectory(){
        this.path = SetDirectory.setDirectory();
    }

    private void writeOutputCsv(){
        WriteCsv.write(data, path);
    }

    private void writeOutputTxt(){
        WriteTxt.writeTxt(txtData, path);
    }
}
