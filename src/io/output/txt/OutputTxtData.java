package io.output.txt;

public class OutputTxtData {
    private int simTime=Integer.MIN_VALUE;
    private double delta=Double.MIN_VALUE;
    private double myu=Double.MIN_VALUE;
    private String title=null;

    public static OutputTxtData create(){
        return new OutputTxtData();
    }

    // set
    public OutputTxtData setSimTime(int simTime){
        this.simTime = simTime;
        return this;
    }

    public OutputTxtData setDelta(double delta){
        this.delta = delta;
        return this;
    }

    public OutputTxtData setMyu(double myu){
        this.myu = myu;
        return this;
    }

    public OutputTxtData setTitle(String title){
        this.title = title;
        return this;
    }

    // get
    public int getSimTime() {
        return simTime;
    }

    public double getDelta() {
        return delta;
    }

    public double getMyu() {
        return myu;
    }

    public String getTitle() {
        return title;
    }
}