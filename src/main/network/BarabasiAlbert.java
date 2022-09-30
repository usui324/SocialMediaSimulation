package main.network;

import main.agent.Agent;
import main.library.Sfmt;

import java.util.List;

public class BarabasiAlbert extends Network{
    private static final int INITIAL_NUM = 20;

    public BarabasiAlbert() {
        super();
    }
    public BarabasiAlbert(int networkNumber) {
        super(networkNumber);
    }

    @Override
    public void setFollowerList(Sfmt sfmt) {
        // 完全グラフの生成
        for(int num1=0; num1<INITIAL_NUM; num1++){
            for(int num2=0; num2<INITIAL_NUM; num2++){
                if(num1==num2) continue;
                Agent agent1 = agentList.get(num1);
                Agent agent2 = agentList.get(num2);
                agent1.follow(agent2);
            }
        }

        // Agentの追加
        int graph_size = agentList.size();
        for(int num=INITIAL_NUM; num<graph_size; num++){
            // 対象のエージェント
            Agent agent = agentList.get(num);

            // リンクの決定
            boolean[] linkList = new boolean[graph_size];
            for(int edge=0; edge<INITIAL_NUM; edge++){
                linkList = decideLink(linkList, sfmt);
            }

            // リンクの生成
            for(int index=0; index<linkList.length; index++){
                if(!linkList[index]) continue;
                bothSideConnect(agent, agentList.get(index));
            }
        }
    }

    private boolean[] decideLink(boolean[] flgList, Sfmt sfmt){
        // リンク数の集計
        int sumLink=0;
        for(int index=0; index<agentList.size(); index++){
            if(flgList[index]) continue;
            sumLink += agentList.get(index).followerList.size();
        }
        // リンク先の決定
        int randLink = sfmt.NextInt(sumLink);
        for(int index=0; index<agentList.size(); index++){
            if(flgList[index]) continue;
            randLink -= agentList.get(index).followerList.size();
            if(randLink<=0){
                flgList[index]=true;
                return flgList;
            }
        }
        return null;
    }
}
