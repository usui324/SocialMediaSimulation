package main.evaluate;

import main.agent.Agent;

public class MetaComment extends Comment {

    public MetaComment(Agent agent, double propSee, double cost, double reward, double parameterQ) {
        super(agent, propSee, cost, reward, parameterQ);
        this.cost = cost;
        this.reward = reward;
    }

    public void print() {
        System.out.println("Meta master: " + master.getNumber());
    }
}
