package main.network;

import main.agent.Agent;
import main.library.Sfmt;

import java.util.ArrayList;
import java.util.List;

public class ConnectingNearestNeighbor extends Network{
    private static final int START_NUM = 2;
    private static final double U = 0.9;

    private List<Agent[]> potentialEdgeList = new ArrayList<>();

    public ConnectingNearestNeighbor() {
        super();
    }
    public ConnectingNearestNeighbor(int networkNumber) {
        super(networkNumber);
    }

    @Override
    public void setFollowerList(Sfmt sfmt) {
        // 2人からスタート
        this.bothSideConnect(agentList.get(0), agentList.get(1));

        // Agentの追加
        int agentNum = START_NUM;
        while(true){
            if(isAddAgent(sfmt)){
                // Agentを追加
                if(agentNum == agentList.size()) break;
                Agent connectAgent = agentList.get(sfmt.NextInt(agentNum));
                bothSideConnect(agentList.get(agentNum), connectAgent);
                addPotentialEdge(agentList.get(agentNum), connectAgent);
                agentNum++;
            }else{
                // ポテンシャルエッジの変換
                if(potentialEdgeList.isEmpty()) continue;
                int size = potentialEdgeList.size();
                Agent[] edge = potentialEdgeList.get(sfmt.NextInt(size));
                this.bothSideConnect(edge[0], edge[1]);
                potentialEdgeList.remove(edge);
            }
        }
    }

    private boolean isAddAgent(Sfmt sfmt){
        return U <= sfmt.NextUnif();
    }

    private void addPotentialEdge(Agent addAgent, Agent connectAgent){
        for(Agent agent : connectAgent.followList){
            if(agent == addAgent) continue;
            potentialEdgeList.add(new Agent[]{addAgent, agent});
        }
    }
}
