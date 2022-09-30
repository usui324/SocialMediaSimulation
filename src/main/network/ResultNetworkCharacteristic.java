package main.network;

public class ResultNetworkCharacteristic {
    double avgDistance=0;
    int[] degreeDistribution = new int[400];
    double cluster=0;
    static int simTime=1;
    public void addResult(ResultNetworkCharacteristic addValue){
        this.avgDistance += addValue.avgDistance;
        this.cluster += addValue.cluster;
        for(int index=0; index<degreeDistribution.length; index++){
            degreeDistribution[index] += addValue.degreeDistribution[index];
        }
        simTime++;
    }

    public void divideResult(){
        this.avgDistance /= simTime;
        this.cluster /= simTime;
        for(int index=0; index<degreeDistribution.length; index++){
            degreeDistribution[index] /= simTime;
        }
    }

    public void print(){
        System.out.println("AvgDist: "+avgDistance);
        System.out.println("AvgCluster: "+cluster);
        System.out.print("[ :");
        for(int degree : degreeDistribution){
            System.out.print(degree + ", ");
        }
        System.out.println("]");

    }
}
