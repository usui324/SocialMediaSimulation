package io.input;

public class InputData {

    // parameter, type値の二重配列
    public int[][] inputValues;

    // parameterのdigitの配列
    public int[] inputDigits;

    public InputData(int numOfAgent, int numOfInput, int numOfParameter) {
        this.inputValues = new int [numOfAgent][numOfInput];
        this.inputDigits = new int[numOfParameter];
    }

}
