package main.evaluate;

public interface IEvaluate {
    // サイト数
    public int NUM_OF_MEDIA = 2;

    // 一人当たりの投稿機会数
    public int POST_TIME_PER_GAME = 4;

    // コメントが閲覧される確率
    public double PROP_SEE_COMMENT = 1.0;

    // 投稿コスト
    public double COST_POST = -1.0;

    // コメントコスト
    // public double COST_COMMENT = -2.0;

    // メタコメントコスト
    // public double COST_META = -2.0;

    // 閲覧報酬
    // public double REWARD_READ = 1.0;

    // コメント報酬
    // public double REWARD_COMMENT = 5.0;

    // メタコメント報酬
    // public double REWARD_META = 5.0;
}
