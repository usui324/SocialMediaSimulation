package main.evolve;

public interface IEvolve {
    // 突然変異確率
    static double MUTATION_RATE = 0.01;

    // 優先選択 ゼロ回避のための値
    static double epsilon = 0.0001;
}
