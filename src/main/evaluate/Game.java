package main.evaluate;

import java.util.ArrayList;
import java.util.List;

import main.agent.Agent;
import main.library.Sfmt;
import main.network.Network;

public abstract class Game {
    // SocialMediaのList
    public List<SocialMedia> socialMedias = new ArrayList<>();

    // 土台のNetwork
    protected Network network;

    // Sfmt
    protected Sfmt sfmt;

    public Game(Network network, Sfmt sfmt, int numOfSocialMedia) {
        this.network = network;
        this.sfmt = sfmt;
        setMedia(1);
        setAgentList();
    }

    // SocialMediaの生成
    protected void setMedia(int numOfSocialMedia) {
        for(int socialMedia=0; socialMedia<numOfSocialMedia; socialMedia++) {
            SocialMedia media = new SocialMedia(socialMedia);
            socialMedias.add(media);
        }
    }

    // Agentの分別
    protected void setAgentList() {
        for(Agent agent: network.agentList) {
            // int belong = agent.getParameterBelong().getBodyToInt();
            socialMedias.get(0).agentList.add(agent);
        }
    }

    // Gameの実行
    public abstract void game();

    // ログの出力
    public void print() {
        for(SocialMedia media: socialMedias) {
            System.out.println("<<SocialMedia: " + socialMedias.indexOf(media) + ", articleNum: " + media.articleList.size() + ">>");
            for(Article article: media.articleList) {
                article.print();
                for(Comment comment: article.getCommentList()) {
                    comment.print();
                    for(MetaComment meta: comment.getMetaList()) {
                        meta.print();
                    }
                }
            }
        }
    }


}
