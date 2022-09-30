package main.network;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.input.InputData;
import main.agent.Agent;
import main.library.Sfmt;

public class Network implements INetwork{
    // ネットワーク番号
    protected int networkNumber;

    // エージェントリスト
    public List<Agent> agentList;

    public Network() {
        networkNumber=0;
        agentList = new ArrayList<>();
    }
    public Network(int networkNumber) {
        this.networkNumber = networkNumber;
        agentList = new ArrayList<>();
    }

    public Network setNetwork(Sfmt sfmt) {
        System.out.println("this is not facebook class");
        return this;
    }

    public void print() {
        for(Agent agent: agentList) {
            agent.print();
        }
    }

    // ネットワーク生成メソッド
    public void setFollowerList(Sfmt sfmt) {
        System.out.println("This is NO Connecting Network.");
        System.exit(0);
    }

    public void setAgentList() {
        for(int agentNumber=0; agentNumber<NUM_OF_AGENT; agentNumber++) {
            agentList.add(Agent.create());
        }
    }
    public void setParameters(InputData inputData, Sfmt sfmt) {
        for(int agentNumber=0; agentNumber<NUM_OF_AGENT; agentNumber++) {
            agentList.get(agentNumber).initParamAndType(inputData, agentNumber, sfmt);
        }
    }

    // ネットワーク内の繋がりの複製
    public void copyConnect(Network basedNetwork) {
        for(Agent agent: this.agentList) {
            for(Agent aite: basedNetwork.agentList.get(agent.getNumber()).followList) {
                agent.follow(this.agentList.get(aite.getNumber()));
            }
        }
    }

    protected void bothSideConnect(Agent agent, Agent bgent) {
        agent.follow(bgent);
        bgent.follow(agent);
    }

    protected void oneSideConnect(Agent follow, Agent receive) {
        follow.follow(receive);
    }

    public void printGameLog() {
        for(Agent agent: agentList) {
            agent.printLog();
        }
    }

    public ResultNetworkCharacteristic calNetworkCharacteristic(){
        ResultNetworkCharacteristic result = new ResultNetworkCharacteristic();
        result.avgDistance = calAverageDist();
        result.degreeDistribution = calDegreeDistribution();
        result.cluster = calCluster2();
        return result;
    }

    private double calAverageDist(){
        double sumDist=0;
        for(Agent agent : agentList){
            for(Agent bgent : agentList){
                if(agent==bgent) continue;
                sumDist += calDist(agent, bgent);
            }
        }
        return sumDist / agentList.size() / (agentList.size()-1);
    }

    private double calDist(Agent agent, Agent bgent){
        // 距離リストの初期化
        int distList[] = new int[agentList.size()];
        for(int index=0; index<distList.length; index++){
            distList[index] = -1;
        }
        // agentを始点とする
        List<Agent> checkList = new ArrayList<>();
        checkList.add(agent);
        distList[agent.getNumber()] = 0;

        while(true){
            if(checkList.isEmpty()) break;
            Agent checkAgent = checkList.get(0);
            for(Agent follower : checkAgent.followerList){
                // distList=-1だったらcheckAgent+1を入れる
                if(!(distList[follower.getNumber()]==-1)) continue;
                distList[follower.getNumber()] = distList[checkAgent.getNumber()] + 1;
                checkList.add(follower);
            }
            checkList.remove(checkAgent);
        }
        return (double) distList[bgent.getNumber()];
    }

    private int[] calDegreeDistribution(){
        int[] degreeDist = new int[agentList.size()-1];
        for(Agent agent : agentList){
            degreeDist[agent.followerList.size()]++;
        }
        return degreeDist;
    }

    private double calCluster(){
        double clusterNum=0;
        for(Agent agent : agentList){
            for(Agent bgent : agentList){
                if(agent==bgent) continue;
                for(Agent cgent : agentList){
                    if(agent==cgent || bgent==cgent) continue;
                    if(isTriangle(agent, bgent, cgent)) clusterNum += 1.0;
                }
            }
        }
        return clusterNum/agentList.size()/(agentList.size()-1)/(agentList.size()-2);
    }

    private double calCluster2(){
        double clusterNum=0;
        double divNum=0;
        for(Agent agent : agentList){
            for(Agent friend1 : agent.followList){
                if(agent==friend1) continue;
                for (Agent friend2 : agent.followList){
                    if(agent==friend2) continue;
                    if(friend1==friend2) continue;
                    divNum+=1.0;
                    if(!friend1.followList.stream().filter(agent1 -> agent1==friend2).collect(Collectors.toList()).isEmpty()) clusterNum+=1.0;
                }
            }
        }
        return clusterNum / divNum;
    }

    private boolean isTriangle(Agent agent, Agent bgent, Agent cgent){
        boolean a_b=false;
        boolean b_c=false;
        boolean c_a=false;
        for(Agent a_follower : agent.followerList){
            if(a_follower==bgent) a_b = true;
            if(a_follower==cgent) c_a = true;
        }
        for(Agent b_follower : bgent.followerList){
            if(b_follower==cgent) b_c = true;
        }
        return (a_b && b_c && c_a);
    }

    // parameterBの平均計算
    public double calAvgB() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfAct()!=0) continue;
            result +=  agent.getParameterB().getBodyToDouble();
            devNum+=1.0;
        }
        return result/devNum;
    }

    public double calAvgBPref(int typeOfPrefer){
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfAct()!=0) continue;
            if(agent.getTypeOfPrefer()!=typeOfPrefer) continue;
            result +=  agent.getParameterB().getBodyToDouble();
            devNum+=1.0;
        }
        return result/devNum;
    }

    // parameterLの平均計算
    public double calAvgL() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            result +=  agent.getParameterL().getBodyToDouble();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    public double calAvgLPreferAct(int pref, int act) {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfPrefer()!=pref && pref!=-1) continue;
            if(agent.getTypeOfAct()!=act && act!=-1) continue;
            result +=  agent.getParameterL().getBodyToDouble();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // parameterQの平均計算
    public double calAvgQ() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfAct()!=0) continue;
            result +=  agent.getParameterQ().getBodyToDouble();
            devNum+=1.0;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // parameterQ(α)の平均計算
    public double calAvgQAlpha() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfAct()!=0) continue;
            if(agent.getTypeOfPrefer()!=0) continue;
            result +=  agent.getParameterQ().getBodyToDouble();
            devNum+=1.0;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // parameterQ(β)の平均計算
    public double calAvgQBeta() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfAct()!=0) continue;
            if(agent.getTypeOfPrefer()!=1) continue;
            result +=  agent.getParameterQ().getBodyToDouble();
            devNum+=1.0;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // parameterMの平均計算
    public double calAvgM() {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            result +=  agent.getPropM();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // fitnessの平均計算
    public double calAvgFitness(){
        double result=0;
        double devNum=0;
        for(Agent agent: agentList){
            result += agent.getFitness();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    public double calAvgFitnessPreferAct(int pref, int act) {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfPrefer()!=pref && pref!=-1) continue;
            if(agent.getTypeOfAct()!=act && act!=-1) continue;
            result +=  agent.getFitness();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // pointの平均計算
    public double calAvgPoint(){
        double result=0;
        double devNum=0;
        for(Agent agent: agentList){
            result += agent.getPoint();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    public double calAvgPointPreferAct(int pref, int act) {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfPrefer()!=pref && pref!=-1) continue;
            if(agent.getTypeOfAct()!=act && act!=-1) continue;
            result +=  agent.getPoint();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    // sunFitnessの平均計算
    public double calAvgSumFitness(){
        double result=0;
        double devNum=0;
        for(Agent agent: agentList){
            result += agent.getSumFitness();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    public double calAvgSumFitnessPreferAct(int pref, int act) {
        double result=0;
        double devNum=0;
        for(Agent agent: agentList) {
            if(agent.getTypeOfPrefer()!=pref && pref!=-1) continue;
            if(agent.getTypeOfAct()!=act && act!=-1) continue;
            result +=  agent.getSumFitness();
            devNum+=1;
        }
        return devNum!=0 ? result/devNum : 0;
    }

    public double calPostTime(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.postTime;
        }
        return  result;
    }

    public double calPostTimeHighQ(){
        double result=0;
        for(Agent agent : agentList){
            if(agent.getParameterQ().getBodyToInt() < 5) continue;
            result += (double)agent.postTime;
        }
        return result;
    }

    public double calReadTime(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.readTime;
        }
        return result;
    }

    public double calReadTimeHighQ(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.readHighQTime;
        }
        return result;
    }

    public double calCommTime(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.commentTime;
        }
        return  result;
    }

    public double calCommTimeHighQ(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.commentHighQTime;
        }
        return result;
    }

    public double calMetaTime(){
        double result=0;
        for(Agent agent : agentList){
            result += (double)agent.metaTime;
        }
        return result;
    }

    public double calMetaTimeHighQ(){
        double result=0;
        for(Agent agent : agentList){
            if(agent.getParameterQ().getBodyToInt() < 5) continue;
            result += (double)agent.metaTime;
        }
        return result;
    }

}
