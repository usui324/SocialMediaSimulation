package main.network;

public interface INetwork {
    // Agent数
    public static int NUM_OF_AGENT = 80;

    // Network番号(MWGAを採用しない限りは0の固定値)
    public static int NETWORK_NUMBER = 0;

    // Networkの型の列挙
    public enum NetworkType{
        Network,
        CompleteGraph,
        BarabasiAlbert;

    }

}
