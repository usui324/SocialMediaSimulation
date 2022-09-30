package main.network;

import main.agent.Agent;
import main.library.Sfmt;
import main.simulation.ISimulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static main.simulation.ISimulation.FACEBOOK_NETWORK_SIZE;

public class FacebookNetwork extends Network{
    public FacebookNetwork(){
        super();
    }

    public Network setNetwork(Sfmt sfmt){
        String inputFileName = ISimulation.INPUT_FACEBOOK_FILE_NAME;
        int size = FACEBOOK_NETWORK_SIZE;
        // エージェントの生成
        int[] inputValues = new int[6];
        for(int index=0; index<size; index++){
            inputValues[0] = index;
            inputValues[1] = -1;
            inputValues[2] = -1;
            inputValues[3] = -1;
            inputValues[4] = index % 4 == 2 ? 1 : 0;
            inputValues[5] = index % 2 == 1 ? 1 : 0;
            this.agentList.add(Agent.create().initParamAndType(inputValues, sfmt));
        }
        // エッジの生成
        List<int[]> edgeList = this.inputEdgeList(inputFileName);
        Agent agent1;
        Agent agent2;
        for(int[] edge : edgeList){
            agent1 = this.agentList.get(edge[0]% FACEBOOK_NETWORK_SIZE);
            agent2 = this.agentList.get(edge[1]% FACEBOOK_NETWORK_SIZE);
            this.bothSideConnect(agent1, agent2);
        }
        return this;
    }

    //
    private List<int[]> inputEdgeList(String inputFileName){
        BufferedReader br=null;
        List<int[]> result = new ArrayList<>();
        try {
            File file = new File(inputFileName);
            br = new BufferedReader(new FileReader(file));

            // 一行ずつの読み込み
            String line;
            while((line= br.readLine()) != null){
                result.add(Stream.of(line.split(" ")).mapToInt(Integer::parseInt).toArray());
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                br.close();
            }catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }


}
