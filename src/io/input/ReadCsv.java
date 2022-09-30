package io.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ReadCsv {
    private BufferedReader br = null;
    private String fileName;

    public ReadCsv(String fileName) {
        this.fileName = fileName;
    }

    /**
     * -0 number
     * -1 paramB
     * -2 paramL
     * -3 paramQ
     * -4 paramBelong
     * -5 type money or not
     * -6 type post or read
     * @return inpurValues
     */
    public InputData readInput(int numOfAgent, int numOfInput, int numOfParameter) {
        InputData inputData  = new InputData(numOfAgent, numOfInput, numOfParameter);
        try {
            File file = new File(fileName);
            br = new BufferedReader(new FileReader(file));

            // 一行ずつの読み込み
            String line = br.readLine();
            String[] data = line.split(",");

            // digitDataの読み込み
            line = br.readLine();
            data = line.split(",");
            for(int index=0; index<data.length-1; index++) {
                inputData.inputDigits[index] = Integer.parseInt(data[index+1]);
            }

            // valueDataの読み込み
            for(int agentNumber=0; agentNumber<numOfAgent; agentNumber++) {
                line = br.readLine();
                data = line.split(",");
                for(int index=0; index<data.length; index++) {
                    inputData.inputValues[agentNumber][index] = Integer.parseInt(data[index]);
                }
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                br.close();
            }catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return inputData;
    }
}
