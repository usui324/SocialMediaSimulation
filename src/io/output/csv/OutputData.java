package io.output.csv;

public class OutputData {

    // simulation回数
    public int simulation_time;

    // simulationのdelta, myu(固定)
    public double delta;
    public double myu;

    // simulationのpie1, pie2の配列
    public double[] pie1;
    public double[] pie2;

    // simulation回数のカウント
    public int[][] simulationTime;

    // simulation毎のparameterB平均
    public double[][] avg_b;
    public double[][] avg_b_alpha;
    public double[][] avg_b_beta;

    // simulation毎のparameterL平均
    public double[][] avg_l;
    public double[][] avg_l_poster;
    public double[][] avg_l_reader;
    public double[][] avg_l_alpha_poster;
    public double[][] avg_l_beta_poster;

    // simulation毎のparameterQ平均
    public double[][] avg_q;
    public double[][] avg_q_alpha;
    public double[][] avg_q_beta;
    
    // simulation毎のpropM平均
    public double[][] avg_m;
    
    // simulation毎のfitness平均
    public double[][] avg_fitness;
    public double[][] avg_fitness_poster;
    public double[][] avg_fitness_reader;
    public double[][] avg_fitness_alpha_poster;
    public double[][] avg_fitness_beta_poster;
    
    // simulation毎のpoint平均
    public double[][] avg_point;
    public double[][] avg_point_poster;
    public double[][] avg_point_reader;
    public double[][] avg_point_alpha_poster;
    public double[][] avg_point_beta_poster;
    
    // simulation毎のsumFitness平均
    public double[][] avg_sumFitness;
    public double[][] avg_sumFitness_poster;
    public double[][] avg_sumFitness_reader;
    public double[][] avg_sumFitness_alpha_poster;
    public double[][] avg_sumFitness_beta_poster;

    // simulation毎 の最終ゲーム結果
    public double[][] avg_postTime;
    public double[][] avg_postTimeHighQ;
    public double[][] avg_readTime;
    public double[][] avg_readTimeHighQ;
    public double[][] avg_commTime;
    public double[][] avg_commTimeHighQ;
    public double[][] avg_metaTime;
    public double[][] avg_metaTimeHighQ;

    public OutputData(double delta, double myu, int num_pie1, int num_pie2) {
        this.delta=delta;
        this.myu=myu;
        pie1 = new double[num_pie1];
        pie2 = new double[num_pie2];
        simulationTime = new int[num_pie1][num_pie2];
        avg_b = new double[num_pie1][num_pie2];
        avg_b_alpha = new double[num_pie1][num_pie2];
        avg_b_beta = new double[num_pie1][num_pie2];
        avg_l = new double[num_pie1][num_pie2];
        avg_l_poster = new double[num_pie1][num_pie2];
        avg_l_reader = new double[num_pie1][num_pie2];
        avg_l_alpha_poster = new double[num_pie1][num_pie2];
        avg_l_beta_poster = new double[num_pie1][num_pie2];
        avg_q = new double[num_pie1][num_pie2];
        avg_q_alpha = new double[num_pie1][num_pie2];
        avg_q_beta = new double[num_pie1][num_pie2];
        avg_m = new double[num_pie1][num_pie2];
        avg_fitness = new double[num_pie1][num_pie2];
        avg_fitness_poster = new double[num_pie1][num_pie2];
        avg_fitness_reader = new double[num_pie1][num_pie2];
        avg_fitness_alpha_poster = new double[num_pie1][num_pie2];
        avg_fitness_beta_poster = new double[num_pie1][num_pie2];
        avg_point = new double[num_pie1][num_pie2];
        avg_point_poster = new double[num_pie1][num_pie2];
        avg_point_reader = new double[num_pie1][num_pie2];
        avg_point_alpha_poster = new double[num_pie1][num_pie2];
        avg_point_beta_poster = new double[num_pie1][num_pie2];
        avg_sumFitness = new double[num_pie1][num_pie2];
        avg_sumFitness_poster = new double[num_pie1][num_pie2];
        avg_sumFitness_reader = new double[num_pie1][num_pie2];
        avg_sumFitness_alpha_poster = new double[num_pie1][num_pie2];
        avg_sumFitness_beta_poster = new double[num_pie1][num_pie2];

        avg_postTime = new double[num_pie1][num_pie2];
        avg_postTimeHighQ = new double[num_pie1][num_pie2];
        avg_readTime = new double[num_pie1][num_pie2];
        avg_readTimeHighQ = new double[num_pie1][num_pie2];
        avg_commTime = new double[num_pie1][num_pie2];
        avg_commTimeHighQ = new double[num_pie1][num_pie2];
        avg_metaTime = new double[num_pie1][num_pie2];
        avg_metaTimeHighQ = new double[num_pie1][num_pie2];

        initSimulationTimes(num_pie1, num_pie2);
    }

    public void initSimulationTimes(int num_pie1, int num_pie2) {
        for(int index=0; index<num_pie1; index++) {
            for(int jndex=0; jndex<num_pie2; jndex++) {
                simulationTime[index][jndex] = 0;
            }
        }
    }

    public void writeOutputData(double avg_b, double avg_ba, double avg_bb,
                                double avg_l, double avg_la, double avg_lb, double avg_lp, double avg_lr,
                                double avg_lap, double avg_lar, double avg_lbp, double avg_lbr, 
                                double avg_q, double avg_q_a, double avg_q_b, double avg_m, 
                                double avg_fitness, double avg_fitnessa, double avg_fitnessb, double avg_fitnessp, double avg_fitnessr,
                                double avg_fitnessap, double avg_fitnessar, double avg_fitnessbp, double avg_fitnessbr,
                                double avg_point, double avg_pointa, double avg_pointb, double avg_pointp, double avg_pointr,
                                double avg_pointap, double avg_pointar, double avg_pointbp, double avg_pointbr,
                                double avg_sumFitness, double avg_sumFitnessa, double avg_sumFitnessb, double avg_sumFitnessp, double avg_sumFitnessr,
                                double avg_sumFitnessap, double avg_sumFitnessar, double avg_sumFitnessbp, double avg_sumFitnessbr,
                                double avg_postTime, double avg_postTimeHighQ, double avg_readTime, double avg_readTimeHighQ,
                                double avg_commTime, double avg_commTimeHighQ, double avg_metaTime, double avg_metaTimeHighQ,
                                double pieValue) {
        int index_pie1 = calIndexOfPie1(pieValue);
        int index_pie2 = calIndexOfPie2(0);
        
        this.avg_b[index_pie1][index_pie2] += avg_b;
        this.avg_b_alpha[index_pie1][index_pie2] += avg_ba;
        this.avg_b_beta[index_pie1][index_pie2] += avg_bb;

        this.avg_l[index_pie1][index_pie2] += avg_l;
        this.avg_l_poster[index_pie1][index_pie2] += avg_lp;
        this.avg_l_reader[index_pie1][index_pie2] += avg_lr;
        this.avg_l_alpha_poster[index_pie1][index_pie2] += avg_lap;
        this.avg_l_beta_poster[index_pie1][index_pie2] += avg_lbp;


        this.avg_q[index_pie1][index_pie2] += avg_q;
        this.avg_q_alpha[index_pie1][index_pie2] += avg_q_a;
        this.avg_q_beta[index_pie1][index_pie2] += avg_q_b;
        
        this.avg_m[index_pie1][index_pie2] += avg_m;

        this.avg_fitness[index_pie1][index_pie2] += avg_fitness;
        this.avg_fitness_poster[index_pie1][index_pie2] += avg_fitnessp;
        this.avg_fitness_reader[index_pie1][index_pie2] += avg_fitnessr;
        this.avg_fitness_alpha_poster[index_pie1][index_pie2] += avg_fitnessap;
        this.avg_fitness_beta_poster[index_pie1][index_pie2] += avg_fitnessbp;
        
        this.avg_point[index_pie1][index_pie2] += avg_point;
        this.avg_point_poster[index_pie1][index_pie2] += avg_pointp;
        this.avg_point_reader[index_pie1][index_pie2] += avg_pointr;
        this.avg_point_alpha_poster[index_pie1][index_pie2] += avg_pointap;
        this.avg_point_beta_poster[index_pie1][index_pie2] += avg_pointbp;

        this.avg_sumFitness[index_pie1][index_pie2] += avg_sumFitness;
        this.avg_sumFitness_poster[index_pie1][index_pie2] += avg_sumFitnessp;
        this.avg_sumFitness_reader[index_pie1][index_pie2] += avg_sumFitnessr;
        this.avg_sumFitness_alpha_poster[index_pie1][index_pie2] += avg_sumFitnessap;
        this.avg_sumFitness_beta_poster[index_pie1][index_pie2] += avg_sumFitnessbp;

        this.avg_postTime[index_pie1][index_pie2] += avg_postTime;
        this.avg_postTimeHighQ[index_pie1][index_pie2] += avg_postTimeHighQ;
        this.avg_readTime[index_pie1][index_pie2] += avg_readTime;
        this.avg_readTimeHighQ[index_pie1][index_pie2] += avg_readTimeHighQ;
        this.avg_commTime[index_pie1][index_pie2] += avg_commTime;
        this.avg_commTimeHighQ[index_pie1][index_pie2] += avg_commTimeHighQ;
        this.avg_metaTime[index_pie1][index_pie2] += avg_metaTime;
        this.avg_metaTimeHighQ[index_pie1][index_pie2] += avg_metaTimeHighQ;

        this.simulationTime[index_pie1][index_pie2]+=1;
    }

    private int calIndexOfPie1(double pie1Value) {
        for(int index=0; index<pie1.length; index++) {
            if(pie1[index] == pie1Value) return index;
        }
        return 0;
    }
    private int calIndexOfPie2(double pie2Value) {
        for(int index=0; index<pie2.length; index++) {
            if(pie2[index] == pie2Value) return index;
        }
        return 0;
    }

    public void devideOutputData() {
        for(int index=0; index<pie1.length; index++) {
            for(int jndex=0; jndex<pie2.length; jndex++) {
                avg_b[index][jndex] /= simulationTime[index][jndex];
                avg_b_alpha[index][jndex] /= simulationTime[index][jndex];
                avg_b_beta[index][jndex] /= simulationTime[index][jndex];

                avg_l[index][jndex] /= simulationTime[index][jndex];
                avg_l_poster[index][jndex] /= simulationTime[index][jndex];
                avg_l_reader[index][jndex] /= simulationTime[index][jndex];
                avg_l_alpha_poster[index][jndex] /= simulationTime[index][jndex];
                avg_l_beta_poster[index][jndex] /= simulationTime[index][jndex];

                avg_q[index][jndex] /= simulationTime[index][jndex];
                avg_q_alpha[index][jndex] /= simulationTime[index][jndex];
                avg_q_beta[index][jndex] /= simulationTime[index][jndex];
                
                avg_m[index][jndex] /= simulationTime[index][jndex];
                
                avg_fitness[index][jndex] /= simulationTime[index][jndex];
                avg_fitness_poster[index][jndex] /= simulationTime[index][jndex];
                avg_fitness_reader[index][jndex] /= simulationTime[index][jndex];
                avg_fitness_alpha_poster[index][jndex] /= simulationTime[index][jndex];
                avg_fitness_beta_poster[index][jndex] /= simulationTime[index][jndex];
                
                avg_point[index][jndex] /= simulationTime[index][jndex];
                avg_point_poster[index][jndex] /= simulationTime[index][jndex];
                avg_point_reader[index][jndex] /= simulationTime[index][jndex];
                avg_point_alpha_poster[index][jndex] /= simulationTime[index][jndex];
                avg_point_beta_poster[index][jndex] /= simulationTime[index][jndex];
                
                avg_sumFitness[index][jndex] /= simulationTime[index][jndex];
                avg_sumFitness_poster[index][jndex] /= simulationTime[index][jndex];
                avg_sumFitness_reader[index][jndex] /= simulationTime[index][jndex];
                avg_sumFitness_alpha_poster[index][jndex] /= simulationTime[index][jndex];
                avg_sumFitness_beta_poster[index][jndex] /= simulationTime[index][jndex];

                avg_postTime[index][jndex] /= simulationTime[index][jndex];
                avg_postTimeHighQ[index][jndex] /= simulationTime[index][jndex];
                avg_readTime[index][jndex] /= simulationTime[index][jndex];
                avg_readTimeHighQ[index][jndex] /= simulationTime[index][jndex];
                avg_commTime[index][jndex] /= simulationTime[index][jndex];
                avg_commTimeHighQ[index][jndex] /= simulationTime[index][jndex];
                avg_metaTime[index][jndex] /= simulationTime[index][jndex];
                avg_metaTimeHighQ[index][jndex] /= simulationTime[index][jndex];
            }
        }
    }
}
