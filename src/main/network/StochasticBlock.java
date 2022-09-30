package main.network;

import main.agent.Agent;
import main.library.Sfmt;

public class StochasticBlock extends Network{

    private static final int NUM_OF_COMMUNITY = 3;
    private static final int[] NUM_OF_AGENT_PER_COMMUNITY = {20, 24, 36};
    private static final double[][] EDGE_PROB = {
            {0.5, 0.05, 0.05},
            {0.05, 0.5, 0.05},
            {0.05, 0.05, 0.5}
    };

    public StochasticBlock(){super();}
    public StochasticBlock(int networkNumber){ super(networkNumber);}

    @Override
    public void setFollowerList(Sfmt sfmt){
        // 各エージェントpair(i, j)に対して順番に判定を行う
        for(int i_index=0; i_index<this.agentList.size(); i_index++){
            for(int j_index=i_index+1; j_index<this.agentList.size(); j_index++){;
                setEdgeBetweenTwoAgents(i_index, j_index, sfmt);
            }
        }
    }

    // エージェントのインデックスからどのコミュニティに属するか判定する
    private int judgeBelongCommunity(int index){
        int maxIndexInCommunity = 0;
        for(int communityIndex=0; communityIndex<NUM_OF_COMMUNITY; communityIndex++){
            maxIndexInCommunity += NUM_OF_AGENT_PER_COMMUNITY[communityIndex];
            if(index < maxIndexInCommunity){
                return communityIndex;
            }
        }
        return -1;
    }

    // 二つのエージェント間に確率でエッジを張る
    private void setEdgeBetweenTwoAgents(int index1, int index2, Sfmt sfmt){
        int Agent1Community = judgeBelongCommunity(index1);
        int Agent2Community = judgeBelongCommunity(index2);
        double probEdge = EDGE_PROB[Agent1Community][Agent2Community];

        if(sfmt.NextUnif() < probEdge){
            this.bothSideConnect(agentList.get(index1), agentList.get(index2));
        }
    }
}
