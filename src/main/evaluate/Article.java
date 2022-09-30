package main.evaluate;

import java.util.ArrayList;
import java.util.List;

import main.agent.Agent;

public class Article implements IEvaluate{
    // 記事の持ち主のAgent
    protected Agent master;

    // 記事のクオリティ
    protected double parameterQ;

    // コメント欄
    private List<Comment> commentList = new ArrayList<>();

    // コスト値
    protected double cost;

    // 報酬値
    protected double reward;

    // 閲覧率
    private double propRead;

    // 閲覧数
    public int readTime=0;

    // 有効か否か
    public boolean enable=true;

    public Article(Agent master, double propRead, double cost, double reward, double parameterQ) {
        this.master = master;
        this.cost = calCost(cost, parameterQ);
        this.reward = calReward(reward, parameterQ);
        this.propRead = calPropRead(propRead);
        this.parameterQ = parameterQ;
    }

    public Agent getMaster() {
        return master;
    }
    public double getCost() {
        return cost;
    }
    public double getReward() {
        return reward;
    }
    public double getPropRead() {
        return propRead;
    }
    public double getParameterQ() {
        return parameterQ;
    }
    public List<Comment> getCommentList(){
        return commentList;
    }
    public void print() {
        System.out.println("Article master: " + master.getNumber() + ", comment: " + commentList.size());
    }

    protected double calCost(double cost, double parameterQ) {
        return cost * parameterQ;
    }
    protected double calReward(double reward, double parameterQ) {
        return reward * parameterQ;
    }
    protected double calPropRead(double propRead) {
        return propRead;
    }

    public void providePropRead(double time) {
        propRead /= time;
    }
}
