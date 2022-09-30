package main.evaluate;

import java.util.ArrayList;
import java.util.List;

import main.agent.Agent;

public class Comment extends Article {

    private List<MetaComment> metaList = new ArrayList<>();

    public Comment(Agent agent, double propSee, double cost, double reward, double parameterQ) {
        super(agent, propSee, cost, reward, parameterQ);
        this.cost = cost;
        this.reward = reward;
    }

    @Override
    protected double calCost(double cost, double parameterQ) {
        return cost;
    }
    @Override
    protected double calReward(double reward, double parameterQ) {
        return reward;
    }
    @Override
    protected double calPropRead(double propSee) {
        return 1.0;
    }

    public List<MetaComment> getMetaList(){
        return metaList;
    }

    public void print() {
        System.out.println("Comment master: " + master.getNumber() + ", meta: " + metaList.size());
    }
}
