package main.entry;

import io.output.common.OutputFiles;
import io.output.csv.OutputData;
import io.output.csv.WriteCsv;
import io.output.txt.OutputTxtData;
import main.library.Seed;
import main.library.Sfmt;
import main.network.FacebookNetwork;
import main.network.Network;
import main.network.ResultNetworkCharacteristic;
import main.simulation.ISimulation;
import main.simulation.Simulation;

public class Launcher {

    public static void main(String[] args) {
        // debugSimulation();
        runSimulationPie1Pie2();
        // runSimulationThreshold();
        // runFacebookSimulation();
        // runSimulationDeltaMyu();
        // calGraphSimulation();
    }

    // 金銭報酬値を変化させるシミュレーション
    private static void runSimulationPie1Pie2(){
        double[] deltaAndMyu = {0.5, 8.0};
        int threshold = 0;
        double moneyValue = 0;
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("CompleteGraph Threshold=00 BeReadTime PostScheme")
                .setSimTime(100)
                .setDelta(deltaAndMyu[0])
                .setMyu(deltaAndMyu[1]);
        OutputData outputData = new OutputData(deltaAndMyu[0], deltaAndMyu[1], 51, 1);
        Sfmt sfmt = new Sfmt(Seed._seeds);
        for(int pie1=0; pie1<=50; pie1++) {
            double money = (double)pie1*0.2;
            outputData.pie1[pie1] = money;
            moneyValue = money;
            outputData.pie2[0] = 0;


            for(int simulationTime=1; simulationTime<=100; simulationTime++) {
                Simulation simulation = new Simulation(sfmt, "CompleteGraph",
                        deltaAndMyu, moneyValue, outputData, threshold);
                simulation.runSimulation(false);
            }


            System.out.println("---------- Simulation Pie " + pie1 + " Finished. ----------");
        }
        OutputFiles.create()
                .setOutputData(outputData)
                .setOutputTxtData(outputTxtData)
                .writeOutputFiles();
    }

    // 金銭報酬付与の閾値を変化させるシミュレーション
    private static void runSimulationThreshold(){
        double[] deltaAndMyu = {0.5,8.0};
        double moneyValue = 5.0;
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("CompleteGraph Threshold BeReadTime PostScheme")
                .setSimTime(100)
                .setDelta(deltaAndMyu[0])
                .setMyu(deltaAndMyu[1]);
        OutputData outputData = new OutputData(deltaAndMyu[0], deltaAndMyu[1], 31, 1);
        Sfmt sfmt = new Sfmt(Seed._seeds);
        for(int simulationTime=1; simulationTime<100; simulationTime++){
            for(int index=0; index<=30; index++){
                int threshold = index;
                outputData.pie1[index] = threshold;
                outputData.pie2[0] = 0;
                Simulation simulation = new Simulation(sfmt, "CompleteGraph",
                        deltaAndMyu, moneyValue, outputData, threshold);
                simulation.runSimulation(false);

                // System.out.println("debug log "+threshold+" "+outputData.avg_q[index][0]+" "+outputData.avg_readTime[index][0]);
            }
            System.out.println("----- Finish simulation Times: "+simulationTime);
        }
        OutputFiles.create()
                .setOutputData(outputData)
                .setOutputTxtData(outputTxtData)
                .writeOutputFiles();
    }

    // FacebookNetworkを用いた実験
    private static void runFacebookSimulation(){
        double[] deltaAndMyu = {0.5, 8.0};
        double moneyValue = 0;

        //OutputDataTxtData生成
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("Facebook_MoneyToPoster: Comment")
                .setSimTime(100)
                .setDelta(deltaAndMyu[0])
                .setMyu(deltaAndMyu[1]);
        // OutputCsvData生成
        OutputData outputData = new OutputData(deltaAndMyu[0], deltaAndMyu[1], 51, 1);
        // 乱数ジェネレータ生成
        Sfmt sfmt = new Sfmt(Seed._seeds);
        // ネットワーク生成
        Network network = new FacebookNetwork().setNetwork(sfmt);
        // シミュレーション実行
        for(int simulationTime=1; simulationTime<=25; simulationTime++) {
            for(int pie=0; pie<=50; pie++) {
                double money = (double) pie*0.2;
                outputData.pie1[pie] = money;
                moneyValue = money;
                outputData.pie2[0] = 0;
                Simulation simulation = new Simulation(sfmt, network,
                        deltaAndMyu, moneyValue, outputData);
                simulation.runSimulation(true);
            }
            System.out.println("---------- Simulation TIme " + simulationTime + " Finished. ----------");
        }
        OutputFiles.create()
                .setOutputData(outputData)
                .setOutputTxtData(outputTxtData)
                .writeOutputFiles();
    }




    // 金銭報酬無しでdeltaMyuを変化させるシミュレーション
    private static void runSimulationDeltaMyu(){
        double[] deltaAndMyu = new double[2];
        double moneyValue = 0;
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("Sim_DeltaMyu_defaultSimulation")
                .setSimTime(100);
        OutputData outputData = new OutputData(-1, -1, 10, 10);
        Sfmt sfmt = new Sfmt(Seed._seeds);
        for(int simulationTime=1; simulationTime<=100; simulationTime++) {
            for(int delta=1; delta<=10; delta++) {
                double deltaValue = (double)delta * 0.1;
                outputData.pie1[delta-1] = deltaValue;
                for(int myu=1; myu<=10; myu++) {
                    double myuValue = (double)myu;
                    outputData.pie2[myu-1] = myuValue;
                    deltaAndMyu[0] = deltaValue;
                    deltaAndMyu[1] = myuValue;
                    Simulation simulation = new Simulation(sfmt, "CompleteGraph", deltaAndMyu, moneyValue, outputData);
                    simulation.runSimulation(false);
                }
            }
        }
        OutputFiles.create()
                .setOutputData(outputData)
                .setOutputTxtData(outputTxtData)
                .writeOutputFiles();
    }

    // デバッグ用シミュレーション
    private static void debugSimulation(){
        double[] deltaAndMyu = {0.9, 9.0};
        double moneyValue = 5.0;
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("Debug_")
                .setSimTime(0)
                .setDelta(deltaAndMyu[0])
                .setMyu(deltaAndMyu[1]);
        OutputData outputData = new OutputData(deltaAndMyu[0], deltaAndMyu[1], 1, 1);
        outputData.pie1[0] = moneyValue;
        Sfmt sfmt = new Sfmt(Seed._seeds);
        Simulation simulation = new Simulation(sfmt, "StochasticBlock", deltaAndMyu, moneyValue, outputData);
        simulation.runSimulation(false);

        OutputFiles.create()
                .setOutputData(outputData)
                .setOutputTxtData(outputTxtData)
                .writeOutputFiles();
    }

    // ネットワークの性質計算
    private static void calGraphSimulation(){
        final  int SIMLATION_NUMBER = 0;
        OutputTxtData outputTxtData = OutputTxtData.create()
                .setTitle("Calculate_Graph_charactor");
        OutputData outputData = new OutputData(0, 0, 1, 1);
        Sfmt sfmt = new Sfmt(Seed._seeds);

        Simulation simulation = new Simulation(sfmt, "StochasticBlock", new double[]{0, 0}, 0, outputData);
        ResultNetworkCharacteristic result = simulation.calculateGraphCharactor();
        System.out.println("finish cal: 0");

        for(int sim=1; sim<=10; sim++) {
            Simulation addSimulation = new Simulation(sfmt, "StochasticBlock", new double[]{0, 0}, 0, outputData);
            result.addResult(addSimulation.calculateGraphCharactor());
            System.out.println("finish cal:"+sim);
        }
        result.divideResult();
        result.print();
    }
}
