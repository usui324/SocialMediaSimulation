package main.network;

import main.agent.Agent;
import main.library.Sfmt;

public class CompleteGraph extends Network {

    public CompleteGraph() {
        super();
    }
    public CompleteGraph(int networkNumber) {
        super(networkNumber);
    }

    @Override
    public void setFollowerList(Sfmt sfmt) {
        for(Agent agent: agentList) {
            for(Agent bgent: agentList) {
                if(agent==bgent) continue;
                agent.follow(bgent);
            }
        }
    }
}
