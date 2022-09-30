package main.evaluate;

import main.agent.Agent;
import main.library.Sfmt;
import main.network.Network;

public class SnsNormsGameWithQuality extends Game implements IEvaluate{
    // パラメータ
    double delta;
    double myu;

    // コストと報酬値
    double cost_post;
    double cost_comment;
    double cost_meta;
    double reward_post;
    double reward_comment;
    double reward_meta;
    double money_post;

    // 投稿したフラグリスト
    private boolean[] isPostedAgent;

    int threshold = 0;

    public SnsNormsGameWithQuality(Network network, Sfmt sfmt, double[] deltaMyu, double money) {
        super(network, sfmt, 1);
        this.delta = deltaMyu[0];
        this.myu = deltaMyu[1];
        this.money_post = money;
        isPostedAgent = new boolean[network.agentList.size()];
        setValue();
    }

    public SnsNormsGameWithQuality(Network network, Sfmt sfmt, double[] deltaMyu, double money, int threshold) {
        super(network, sfmt, 1);
        this.delta = deltaMyu[0];
        this.myu = deltaMyu[1];
        this.money_post = money;
        isPostedAgent = new boolean[network.agentList.size()];
        this.threshold = threshold;
        setValue();
    }

    private void setValue() {
        cost_post = COST_POST;
        cost_comment = cost_post * delta;
        cost_meta = cost_comment * delta;
        reward_post = cost_post * myu * -1;
        reward_comment = cost_comment * myu * -1;
        reward_meta = cost_meta * myu * -1;
        // System.out.println(cost_post+" "+cost_comment+" "+cost_meta+" "+reward_post+" "+reward_comment+" "+reward_meta+" "+money_post+" "+money_meta);
    }

    private void initIsPostedAgent(){
        for(int index=0; index<isPostedAgent.length; index++){
            isPostedAgent[index] = false;
        }
    }

    // ゲームの実行
    public void game() {
        for(SocialMedia media: socialMedias) {
            for(int postTime=0; postTime<IEvaluate.POST_TIME_PER_GAME; postTime++) {
                // 投稿
                initIsPostedAgent();
                for (Agent postAgent : media.agentList) {
                    if (postAgent.getTypeOfAct() != 0) continue;
                    Article article = postAgent.postArticle(sfmt, cost_post, reward_post, money_post);
                    if (article != null) {
                        media.articleList.add(article);
                        isPostedAgent[media.agentList.indexOf(postAgent)] = true;
                    }
                }


                // 閲覧・コメント
                for (Article article : media.articleList) {
                    // 記事が有効かのチェック
                    // if(!article.enable) continue;

                    // 閲覧・コメント処理
                    for (Agent commentAgent : article.getMaster().followerList) {
                        if(commentAgent == article.getMaster()) continue;
                        // 閲覧率の計算
                        double probRead = article.getParameterQ() / calArticleNumFromAgent(commentAgent);
                        if (!commentAgent.readArticle(article, sfmt, probRead, money_post)) continue;
                        Comment comment = commentAgent.replyComment(article, sfmt, cost_comment, reward_comment, money_post);
                        if (comment != null) {
                            article.getCommentList().add(comment);
                            article.getMaster().receiveComment(comment);
                        }
                    }
                }

                // メタコメント
                for (Article article : media.articleList) {
                    // 記事が有効かのチェック
                    // if(!article.enable) continue;
                    article.enable=false;
                    for (Comment comment : article.getCommentList()) {
                        MetaComment meta = article.getMaster().replyMetaComment(comment, sfmt, cost_meta, reward_meta, money_post);
                        if (meta != null) {
                            comment.getMetaList().add(meta);
                            comment.getMaster().receiveMetaComment(meta, money_post);
                        }
                    }
                }
                media.articleList.clear();
            }
            // 報酬計算
            for(Agent agent: media.agentList) {
                agent.calSumFitness(this.threshold);
            }
        }
    }

    private int calArticleNumFromAgent(Agent agent){
        // agentのフォローリストから見える記事数を算出
        int result=0;
        for(Agent follow : agent.followList){
            if(isPostedAgent[follow.getNumber()]) result++;
        }
        return result!=0 ? result : 1;
    }
}
