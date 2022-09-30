package main.simulation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import io.input.InputData;
import io.input.ReadCsv;
import io.output.csv.OutputData;
import main.agent.IAgent;
import main.evaluate.SnsNormsGameWithQuality;
import main.evolve.GeneticAlgorithmWithSameType;
import main.library.Seed;
import main.library.Sfmt;
import main.network.FacebookNetwork;
import main.network.INetwork;
import main.network.Network;
import main.network.ResultNetworkCharacteristic;
import main.simulation.SimulationOutputData.data;

public class Simulation implements ISimulation{

    private Network network;
    public Sfmt sfmt;
    private int number;
    SnsNormsGameWithQuality game;
    GeneticAlgorithmWithSameType ga;

    public int threshold = 0;

    // 報酬設計パラメータ
    double[] deltaMyu= new double[2];
    double moneyValue= 0;

    // inputデータ
    public InputData inputData;
    private ReadCsv readCsv = new ReadCsv(ISimulation.INPUT_FILE_NAME);

    // outputデータ
    public OutputData outputData;

    public Simulation(Sfmt sfmt, String networkClass, double[] deltaMyu, double moneyValue, OutputData outputData) {
        this.sfmt = sfmt;
        network = makeNetwork(ROOT_OF_NETWORK + networkClass);
        this.deltaMyu[0] = deltaMyu[0];
        this.deltaMyu[1] = deltaMyu[1];
        this.moneyValue = moneyValue;
        this.outputData = outputData;
    }

    public Simulation(Sfmt sfmt, String networkClass, double[] deltaMyu, double moneyValue, OutputData outputData, int threshold){
        this.sfmt = sfmt;
        network = makeNetwork(ROOT_OF_NETWORK + networkClass);
        this.deltaMyu[0] = deltaMyu[0];
        this.deltaMyu[1] = deltaMyu[1];
        this.moneyValue = moneyValue;
        this.outputData = outputData;
        this.threshold = threshold;
    }

    public Simulation(Sfmt sfmt, Network network, double[] deltaMyu, double moneyValue, OutputData outputData){
        this.sfmt = sfmt;
        this.network = network;
        this.deltaMyu[0] = deltaMyu[0];
        this.deltaMyu[1] = deltaMyu[1];
        this.moneyValue = moneyValue;
        this.outputData = outputData;
    }

    public Network getNetwork() {
        return network;
    }

    public int getNumber() {
        return number;
    }

    // 乱数調整
    private void spendRand(int time) {
        for(int t=0; t<time; t++) {
            sfmt.NextUnif();
        }
    }

    private Network makeNetwork(String clsName) {
        Object obj=null;
        try {
            Class<?> cls = Class.forName(clsName);
            obj = cls.getDeclaredConstructor().newInstance();
        }catch(ClassNotFoundException | IllegalAccessException | InstantiationException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (Network) obj;
    }

    // networkへAgentの挿入とAgentのパラメータセット
    public void setNetwork() {
        network.setAgentList();
        network.setFollowerList(sfmt);
        network.setParameters(inputData, sfmt);
    }

    // input配列へのinput(csv)
    public void setInputValue() {
        inputData = readCsv.readInput(INetwork.NUM_OF_AGENT, IAgent.NUM_OF_INPUT, IAgent.NUM_OF_PARAMETER);
    }

    // SNS規範ゲームの実行
    public void runGame() {
        game = new SnsNormsGameWithQuality(network, sfmt, deltaMyu, moneyValue, threshold);
        game.game();
    }

    public void printGameLog() {
        network.printGameLog();
        game.print();
    }

    // 遺伝の実行
    public void runEvolve() {
        ga = new GeneticAlgorithmWithSameType(network, sfmt);
        this.network = ga.startEvolve();
    }

    // シミュレーションの実行
    public SimulationOutputData runSimulation(boolean isFacebook) {
        if(!isFacebook) {
            setInputValue();
            setNetwork();
        }
        // network.print();//
        for(int generation=0; generation<GENERATION; generation++) {
            // System.out.println(generation + "start");
            runGame();
            runEvolve();
        }
        runGame();
        // printGameLog();//
        System.out.println("Finish simulation: m1=" + moneyValue);
        return writeOutput();
    }

    // グラフの特徴量を計算
    public ResultNetworkCharacteristic calculateGraphCharactor(){
        setInputValue();
        setNetwork();
        return network.calNetworkCharacteristic();
    }

    // output配列への結果出力
    private SimulationOutputData writeOutput() {
        SimulationOutputData output = new SimulationOutputData();

        output.dataList.add(new data("b_all", get_b_all()));
        output.dataList.add(new data("b_alp", get_b_alp()));
        output.dataList.add(new data("b_bet", get_b_bet()));
        output.dataList.add(new data("l_all", get_l_all()));
        output.dataList.add(new data("l_pos", get_l_pos()));
        output.dataList.add(new data("l_npo", get_l_npo()));
        output.dataList.add(new data("l_alp", get_l_alp()));
        output.dataList.add(new data("l_bet", get_l_bet()));
        output.dataList.add(new data("q_all", get_q_all()));
        output.dataList.add(new data("q_alp", get_q_alp()));
        output.dataList.add(new data("q_bet", get_q_bet()));
        output.dataList.add(new data("b_rec", get_b_rec()));
        output.dataList.add(new data("b_nre", get_b_nre()));
        output.dataList.add(new data("l_rec", get_l_rec()));
        output.dataList.add(new data("l_nre", get_l_nre()));
        output.dataList.add(new data("q_rec", get_q_rec()));
        output.dataList.add(new data("q_nre", get_q_nre()));
        output.dataList.add(new data("mp_all", get_mp_all()));
        output.dataList.add(new data("mp_all", get_mp_alp()));
        output.dataList.add(new data("mp_all", get_mp_bet()));
        output.dataList.add(new data("mp_all", get_mp_rec()));
        output.dataList.add(new data("mp_all", get_mp_nre()));
        output.dataList.add(new data("u_all", get_u_all()));
        output.dataList.add(new data("u_pos", get_u_pos()));
        output.dataList.add(new data("u_npo", get_u_npo()));
        output.dataList.add(new data("u_alp", get_u_alp()));
        output.dataList.add(new data("u_bet", get_u_bet()));
        output.dataList.add(new data("p_all", get_p_all()));
        output.dataList.add(new data("p_pos", get_p_pos()));
        output.dataList.add(new data("p_npo", get_p_npo()));
        output.dataList.add(new data("p_alp", get_p_alp()));
        output.dataList.add(new data("p_bet", get_p_bet()));
        output.dataList.add(new data("m_all", get_m_all()));
        output.dataList.add(new data("m_pos", get_m_pos()));
        output.dataList.add(new data("m_npo", get_m_npo()));
        output.dataList.add(new data("m_alp", get_m_alp()));
        output.dataList.add(new data("m_bet", get_m_bet()));
        output.dataList.add(new data("u_rec", get_u_rec()));
        output.dataList.add(new data("u_nre", get_u_nre()));
        output.dataList.add(new data("p_rec", get_p_rec()));
        output.dataList.add(new data("p_nre", get_p_nre()));
        output.dataList.add(new data("m_rec", get_m_rec()));
        output.dataList.add(new data("m_nre", get_m_nre()));
        output.dataList.add(new data("pt", get_pt()));
        output.dataList.add(new data("rt", get_rt()));
        output.dataList.add(new data("ct", get_ct()));
        output.dataList.add(new data("mt", get_mt()));

        return output;
    }

    private double calcData(List<Double> input){
        int length = input.size();
        double result = 0;
        for(double tmp : input){
            result += tmp;
        }
        return result / length;
    }

    private double get_b_all(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getParameterB().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_b_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getParameterB().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_b_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getParameterB().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_all(){
        List<Double> data = this.network.agentList.stream()
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_pos(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_npo(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 1)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_q_all(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getParameterQ().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_q_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getParameterQ().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_q_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getParameterQ().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_b_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getParameterB().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_b_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getParameterB().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_l_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getParameterL().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_q_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getParameterQ().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_q_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getParameterQ().getBodyToDouble()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mp_all(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getPropM()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mp_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getPropM()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mp_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getPropM()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mp_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getPropM()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mp_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getPropM()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_all(){
        List<Double> data = this.network.agentList.stream()
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_pos(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_npo(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 1)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_all(){
        List<Double> data = this.network.agentList.stream()
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_pos(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_npo(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 1)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_all(){
        List<Double> data = this.network.agentList.stream()
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_pos(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_npo(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 1)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_alp(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 0)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_bet(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .filter(agent -> agent.getTypeOfPrefer() == 1)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_u_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getSumFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_p_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getFitness()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_rec(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() > 0)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_m_nre(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getPoint() <= 0)
                .map(agent -> agent.getPoint()).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_pt(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> (double)agent.postTime).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_rt(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> (double)agent.readTime).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_ct(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> (double)agent.commentTime).collect(Collectors.toList());
        return calcData(data);
    }
    private double get_mt(){
        List<Double> data = this.network.agentList.stream()
                .filter(agent -> agent.getTypeOfAct() == 0)
                .map(agent -> (double)agent.metaTime).collect(Collectors.toList());
        return calcData(data);
    }

}