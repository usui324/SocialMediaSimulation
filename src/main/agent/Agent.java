package main.agent;

import java.util.ArrayList;
import java.util.List;

import io.input.InputData;
import main.evaluate.Article;
import main.evaluate.Comment;
import main.evaluate.IEvaluate;
import main.evaluate.MetaComment;
import main.library.Sfmt;

public class Agent implements IAgent{
    // Agent番号
    private int number;

    // パラメータの配列
    public List<Gene> parameters = new ArrayList<>();
    // 記事投稿率
    private Gene parameterB;
    // コメント率
    private Gene parameterL;
    // クオリティ
    private GeneWithPosValue parameterQ;

    // 選好タイプ
    // 0:α(非金銭), 1:β(金銭)
    private int typeOfPrefer;
    // 金銭選好性
    private double propM;

    // 行動タイプ
    // 0:投稿者エージェント, 1:読者エージェント
    private int typeOfAct;

    // input配列のindex
    private int valuesIndex=0;
    private int digitsIndex=0;

    // フォロワーリスト(自分の投稿を見る人)
    public List<Agent> followerList = new ArrayList<>();
    // フォローリスト(自分が投稿を見る対象)
    public List<Agent> followList = new ArrayList<>();

    // 評価値
    private double sumFitness=0;
    private double fitness=0;
    private double point=0;
    private double cost=0;

    // ログ
    private List<String> logList = new ArrayList<>();
    public int postTime=0;
    public int readTime=0;
    public int readHighQTime=0;
    public int commentTime=0;
    public int commentHighQTime=0;
    public int metaTime=0;

    public int beReadTime=0;
    public int beCommentedTime=0;

    public static Agent create(){
        return new Agent();
    }

    public static Agent create(Agent base){
        return new Agent().copyAgentCharacter(base);
    }
    
    public Agent copyAgentCharacter(Agent base){
        this.number = base.getNumber();
        parameterB = new Gene(base.getParameterB().getDigit(), base.getParameterB().getActive());
        parameters.add(parameterB);
        parameterL = new Gene(base.getParameterL().getDigit(), base.getParameterL().getActive());
        parameters.add(parameterL);
        parameterQ = new GeneWithPosValue(base.getParameterQ().getDigit(), base.getParameterQ().getActive());
        parameters.add(parameterQ);
        this.typeOfPrefer = base.getTypeOfPrefer();
        this.propM = base.getPropM();
        this.typeOfAct = base.getTypeOfAct();

        return this;
    }

    // Geneクラスの初期化処理
    private void makeParameters(int[] digitData) {
        // parameterB
        parameterB = new Gene(digitData[digitsIndex], true);
        parameters.add(parameterB); digitsIndex++;
        // parameterL
        parameterL = new Gene(digitData[digitsIndex], true);
        parameters.add(parameterL); digitsIndex++;
        // parameterQ
        parameterQ = new GeneWithPosValue(digitData[digitsIndex], true);
        parameters.add(parameterQ); digitsIndex++;
    }

    public int getNumber() {
        return number;
    }
    public Gene getParameterB() {
        return parameterB;
    }
    public Gene getParameterL() {
        return parameterL;
    }
    public Gene getParameterQ() {
        return parameterQ;
    }
    public int getTypeOfPrefer() {
        return typeOfPrefer;
    }
    public double getPropM() {
        return propM;
    }
    public int getTypeOfAct() {
        return typeOfAct;
    }
    public double getFitness() {
        return fitness;
    }
    public double getPoint(){ return point; }
    public double getCost(){ return cost; }
    public double getSumFitness() {
        return sumFitness;
    }

    public void print() {
        System.out.print(this.number);
        for(Gene gene: parameters) {
            gene.print();
        }
        System.out.print("[Money:" + typeOfPrefer + "]");
        System.out.print("[Act:" + typeOfAct + "]");
        System.out.println("[M:" + getPropM() + "]");
    }

    public void printLog() {
        print();
        for(String str: logList) {
            System.out.println(str);
        }
        System.out.println(" ");
    }

    // parameter, typeの初期値セット
    public void initParamAndType(InputData inputData, int index, Sfmt sfmt) {
        setNumber(inputData.inputValues[index]);
        setParameters(inputData, index, sfmt);
        setTypeOfPrefer(inputData.inputValues[index]);
        setTypeOfAct(inputData.inputValues[index]);
        setPropM(sfmt);
    }

    /**
     * parameter, typeの初期値セット
     * @param inputValues
     *  0: number
     *  1: paramB
     *  2: paramL
     *  3: paramQ
     *  4: preferType
     *  5: actType
     * @param sfmt
     */
    public Agent initParamAndType(int[] inputValues, Sfmt sfmt){
        setNumber(inputValues);
        int[] digitData = {3, 3, 3};
        setParameters(inputValues, digitData, sfmt);
        setTypeOfPrefer(inputValues);
        setTypeOfAct(inputValues);
        setPropM(sfmt);
        return this;
    }

    // numberのセット
    private void setNumber(int[] values) {
        this.number = values[valuesIndex];
        valuesIndex++;
    }

    // Geneクラスのparameterのセット
    private void setParameters(InputData inputData, int index, Sfmt sfmt) {
        makeParameters(inputData.inputDigits);
        for(Gene parameter: parameters) {
            parameter.setGene(inputData.inputValues[index][valuesIndex], sfmt);
            valuesIndex++;
        }
    }
    private void setParameters(int[] inputValues, int[] digitData, Sfmt sfmt){
        makeParameters(digitData);
        for(Gene parameter: parameters){
            parameter.setGene(inputValues[valuesIndex], sfmt);
            valuesIndex++;
        }
    }

    // 選好タイプのセット
    private void setTypeOfPrefer(int[] values) {
        this.typeOfPrefer = values[valuesIndex];
        valuesIndex++;
    }
    // 金銭選好性のセット(一様分布)
    private void setPropM(Sfmt sfmt) {
        if(this.typeOfAct==0) {
            propM = sfmt.NextUnif() / 2.0 + (double) typeOfPrefer * 0.5;
        }else{
            propM = 0;
        }
    }

    // 行動タイプのセット(0:投稿者, 1:読者)
    private void setTypeOfAct(int[] values) {
        this.typeOfAct = values[valuesIndex];
        valuesIndex++;
    }

    // フォローする(接する)
    public void follow(Agent aite) {
        this.followList.add(aite);
        aite.followerList.add(this);
    }

    // sumFitnessの計算
    public void calSumFitness(int threshold) {
        this.point = this.beReadTime >= threshold ? this.point : 0;
        // this.point = this.beCommentedTime >= threshold ? this.point : 0;

        sumFitness = cost + fitness * (1 - propM) + point * propM;
    }

    // 投稿
    /**
     * - 投稿者であること
     * - 投稿率を下回ること
     */
    public Article postArticle(Sfmt sfmt, double cost, double reward, double money) {
        double propPost;
        // 投稿者判定
        if(this.getTypeOfAct()!=0) return null;
        // 投稿率判定
        if((propPost = sfmt.NextUnif()) >= parameterB.getBodyToDouble()/(double)parameterQ.getBodyToInt()) return null;

        Article article = new Article(this, propPost, cost, reward, this.parameterQ.getBodyToDouble());
        this.cost += article.getCost();

        // 投稿時の金銭報酬
        this.point += money;

        // ログ
        this.postTime++;
        // logList.add(makeLog("post", article.getCost()));

        // System.out.println("Post:"+this.number+" post money"+money);
        return article;
    }

    // 閲覧
    /**
     * - 閲覧率を下回ること
     */
    public boolean readArticle(Article article, Sfmt sfmt, double prob, double money) {
        // 閲覧率判定
        if(sfmt.NextUnif()>=prob) return false;

        this.fitness += article.getReward();

        // 閲覧時の金銭報酬
        // article.getMaster().point += money;

        // ログ
        article.readTime++;
        this.readTime++;
        if(article.getMaster().getParameterQ().getBodyToInt() > 4) this.readHighQTime++;
        // logList.add(makeLog("  read", article.getReward()));
        article.getMaster().beReadTime++;

        // System.out.println(" Read:"+this.number+" read"+article.getReward());
        return true;
    }

    // コメント
    /**
     * - コメント率を下回ること
     */
    public Comment replyComment(Article article, Sfmt sfmt, double cost, double reward, double money) {
        // コメント率判定
        if(sfmt.NextUnif() >= parameterL.getBodyToDouble() * article.getParameterQ()) return null;

        Comment comment = new Comment(this, IEvaluate.PROP_SEE_COMMENT, cost, reward, article.getParameterQ());
        this.cost+=comment.getCost();

        // コメント時の金銭報酬
        // article.getMaster().point += money;

        // ログ
        this.commentTime++;
        if(article.getMaster().getParameterQ().getBodyToInt() > 4) this.commentHighQTime++;
        // logList.add(makeLog("  comment", comment.getCost()));
        article.getMaster().beCommentedTime++;


        // System.out.println("  Comment:"+this.number+" comm reward"+comment.getReward());
        return comment;
    }

    // メタコメント
    /**
     * - 投稿者であること
     * - コメント率を下回ること
     */
    public MetaComment replyMetaComment(Comment comment, Sfmt sfmt, double cost, double reward, double money) {
        // コメント率判定
        if(sfmt.NextUnif() >= parameterL.getBodyToDouble() * comment.getParameterQ()) return null;

        MetaComment metaComment = new MetaComment(this, IEvaluate.PROP_SEE_COMMENT, cost, reward, comment.getParameterQ());
        this.cost+=metaComment.getCost();

        // メタコメント時の金銭報酬
        // this.point += money;

        // ログ
        this.metaTime++;
        // logList.add(makeLog("    meta", metaComment.getCost()));

        // System.out.println("   Meta:"+this.number+" meta money:"+money);
        return metaComment;
    }

    // 被コメント
    public void receiveComment(Comment comment) {
        this.fitness += comment.getReward();

        // ログ
        // (makeLog("get comment", comment.getReward()));

        // System.out.println("  GetComm:"+this.number+" get meta"+comment.getReward());
    }

    // 被メタコメント
    public void receiveMetaComment(MetaComment meta, double money) {
        this.fitness += meta.getReward();

        // 被メタコメント時の金銭報酬
        // this.point += money;

        // ログ
        // logList.add(makeLog("get meta", meta.getReward()));

        // System.out.println("   Get Meta"+this.number+" get meta money:"+money);
    }

    // ログの生成
    private String makeLog(String str, double value) {
        return str + ": " + value + ", fitness: " + this.fitness;
    }
}
