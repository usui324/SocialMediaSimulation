package main.evolve;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.agent.Agent;
import main.agent.Gene;
import main.library.Sfmt;
import main.network.Network;

public class GeneticAlgorithmWithSameType implements IEvolve{
    // α-β, 投稿者-読者 それぞれを母体に

    // 遺伝ベースのネットワーク
    private Network basedNetwork;

    // 遺伝後の次世代ネットワーク
    private Network nextNetwork;

    // Sfmt
    Sfmt sfmt;

    // ログ
    List<String> logList = new ArrayList<>();

    public GeneticAlgorithmWithSameType(Network network, Sfmt sfmt) {
        this.basedNetwork = network;
        this.sfmt = sfmt;
    }

    // 実行メソッド
    public Network startEvolve() {
        setNextNetwork();
        // evolveSufficiently();
        evolve();
        // print();
        return nextNetwork;
    }

    // nextNetworkのセット
    private void setNextNetwork() {
        nextNetwork = new Network();

        // Agentの複製
        for(Agent agent: basedNetwork.agentList) {
            nextNetwork.agentList.add(Agent.create(agent));
        }

        // Agentの繋がりの複製
        nextNetwork.copyConnect(basedNetwork);
    }

    // 遺伝の実行
    private void evolve() {
        // 次世代ネットワークの各エージェントに対して
        for(Agent agent: nextNetwork.agentList) {
            // 親の決定
            Agent father = decideAgent(agent, null);
            Agent mother = decideAgent(agent, father);
            // 遺伝処理
            for(Gene parameter: agent.parameters) {
                if(!parameter.getActive()) continue;
                // 1parameterに対する遺伝処理
                int index = agent.parameters.indexOf(parameter);
                parameter.setGene(calParameter(father.parameters.get(index), mother.parameters.get(index)));
            }
            logList.add("Num: "+agent.getNumber()+", Act: "+agent.getTypeOfAct()+", Pre: "+agent.getTypeOfPrefer());
            logList.add("  Father: "+father.getNumber()+", B: "+father.getParameterB().getBodyToInt()+", L: "+father.getParameterL().getBodyToInt()
            +", Q: "+father.getParameterQ().getBodyToInt()+",  fitness: "+father.getFitness());
            logList.add("  Mother: "+mother.getNumber()+", B: "+mother.getParameterB().getBodyToInt()+", L: "+mother.getParameterL().getBodyToInt()
                    +", Q: "+mother.getParameterQ().getBodyToInt()+",  fitness: "+mother.getFitness());
            logList.add("   Child: "+agent.getNumber()+", B: "+agent.getParameterB().getBodyToInt()+", L: "+agent.getParameterL().getBodyToInt()
                    +", Q: "+agent.getParameterQ().getBodyToInt());
        }
    }

    // 遺伝の実行（効率的に）
    private void evolveSufficiently(){
        // fitnessSumとfitnessMinを先に計算する
        final int NUM_OF_TYPE_PREFER = 2;
        final int NUM_OF_TYPE_ACTION = 2;
        DecideParentsSet[][] decideParentsSet = new DecideParentsSet[NUM_OF_TYPE_ACTION][NUM_OF_TYPE_PREFER];
        for(int i=0; i<NUM_OF_TYPE_PREFER; i++){
            for(int j=0; j<NUM_OF_TYPE_ACTION; j++){
                 decideParentsSet[i][j] = this.calculateFitnessSumAndMin(i, j);
            }
        }

        // 次世代ネットワークの各エージェントに対して
        for(Agent agent: nextNetwork.agentList) {
            // タイプ診断
            int typePrefer = agent.getTypeOfPrefer();
            int typeAction = agent.getTypeOfAct();
            // 親の決定
            Agent father = decideAgent(agent, null, decideParentsSet[typePrefer][typeAction]);
            Agent mother = father;
            while(father==mother) {
                mother = decideAgent(agent, father, decideParentsSet[typePrefer][typeAction]);
            }
            // 遺伝処理
            for(Gene parameter: agent.parameters) {
                if (!parameter.getActive()) continue;
                // 1parameterに対する遺伝処理
                int index = agent.parameters.indexOf(parameter);
                parameter.setGene(calParameter(father.parameters.get(index), mother.parameters.get(index)));
            }
        }
    }

    private class DecideParentsSet{
        public double fitnessMin;
        public double fitnessSum;
        public List<Agent> parentList;
    }

    private DecideParentsSet calculateFitnessSumAndMin(int typePref, int typeAct){
        List<Agent> agentList = this.basedNetwork.agentList.stream()
                .filter(agent -> {
                    return agent.getTypeOfPrefer() == typePref && agent.getTypeOfAct() == typeAct;
                }).collect(Collectors.toList());
        double fitnessMin = Double.MAX_VALUE;
        for(Agent agent : agentList){
            fitnessMin = Math.min(fitnessMin, agent.getSumFitness());
        }
        double fitnessSum = epsilon;
        for(Agent agent: agentList) {
            fitnessSum += Math.pow(agent.getSumFitness() - fitnessMin, 2);
        }
        DecideParentsSet decideParentsSet = new DecideParentsSet();
        decideParentsSet.fitnessMin = fitnessMin;
        decideParentsSet.fitnessSum = fitnessSum;
        decideParentsSet.parentList = agentList;
        return decideParentsSet;
    }

    // 交叉相手の決定（fitnessSum 優先選択）
    private Agent decideAgent(Agent me, Agent father) {
        // 母体のリスト
        List<Agent> motherList = new ArrayList<>();
        for(Agent agent: basedNetwork.agentList) {
            if(me.getTypeOfPrefer()!=agent.getTypeOfPrefer()) continue;
            if(me.getTypeOfAct()!=agent.getTypeOfAct()) continue;
            motherList.add(agent);
        }
        if(father!=null)motherList.remove(father);

        // fitnessMinの決定
        double fitnessMin=Double.MAX_VALUE;
        for(Agent agent: motherList) {
            fitnessMin = Math.min(fitnessMin, agent.getSumFitness());
        }

        // fitnessSumの決定
        double fitnessSum=epsilon;
        for(Agent agent: motherList) {
            fitnessSum += Math.pow(agent.getSumFitness() - fitnessMin, 2);
        }

        // 乱数の生成
        double fitnessRand = sfmt.NextUnif() * fitnessSum;
        // Agentの決定
        for(Agent agent: motherList) {
            fitnessRand -= Math.pow(agent.getSumFitness() - fitnessMin, 2);
            if(fitnessRand<=epsilon) return agent;
        }
        System.out.println("select parents final return");
        return basedNetwork.agentList.get(sfmt.NextInt(motherList.size()));
    }

    // 交叉相手の決定（効率化）
    private Agent decideAgent(Agent me, Agent father, DecideParentsSet decideParentsSet) {
        // 母体のリスト
        List<Agent> motherList = decideParentsSet.parentList;
        // fitnessMinの決定
        double fitnessMin = decideParentsSet.fitnessMin;
        // fitnessSumの決定
        double fitnessSum = decideParentsSet.fitnessSum;
        // 乱数の生成
        double fitnessRand = sfmt.NextUnif() * fitnessSum;
        // Agentの決定
        for(Agent agent: motherList) {
            fitnessRand -= Math.pow(agent.getSumFitness() - fitnessMin, 2);
            if(fitnessRand<=epsilon) return agent;
        }
        return basedNetwork.agentList.get(sfmt.NextInt(motherList.size()));
    }

    // 一つのparameterに対する処理
    private int[] calParameter(Gene fatherGene, Gene motherGene) {
        int[] resultBody;
        // 一様交叉
        resultBody = crossover(fatherGene.getBody(), motherGene.getBody(), fatherGene.getDigit());

        // 突然変異
        mutation(resultBody);

        return resultBody;
    }

    private int[] crossover(int[] gene1, int[] gene2, int size) {
        int[] result = new int[size];
        for(int d=0; d<size; d++) {
            result[d] = sfmt.NextInt(2)==0 ? gene1[d] : gene2[d];
        }
        return result;
    }

    private void mutation(int[] gene) {
        for(int d=0; d<gene.length; d++) {
            if(sfmt.NextUnif()<MUTATION_RATE) {
                gene[d] = gene[d]==0 ? 1 : 0;
            }
        }
    }

    public void print(){
        for(String log : logList){
            System.out.println(log);
        }
    }
}
