package main.evaluate;

import java.util.ArrayList;
import java.util.List;

import main.agent.Agent;

public class SocialMedia {

    private int number;

    public List<Article> articleList = new ArrayList<>();
    public List<Agent> agentList = new ArrayList<>();

    public SocialMedia(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

}
