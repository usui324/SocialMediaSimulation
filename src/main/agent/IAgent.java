package main.agent;

public interface IAgent {

    /**
     * inputValueの大きさの測定
     */
    // number
    public int IV_NUMBER = 1;

    // parameter
    public int NUM_OF_PARAMETER = 4;

    // type
    public int IV_TYPE = 2;

    // Agentが持つ性質の種類
    public int NUM_OF_INPUT = IV_NUMBER+NUM_OF_PARAMETER+IV_TYPE;

    // -----

    // Sfmt.NextNormal()の最大値
    public double MAX_NEXT_NORMAL = 6; // 仮の値
}
