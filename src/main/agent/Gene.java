package main.agent;

import main.library.Sfmt;

public class Gene {
    // 遺伝子桁数
    protected int digit;
    //遺伝子セット
    protected int[] body;

    // アクティブ()
    protected boolean active;

    public Gene(int digit, boolean active) {
        this.digit = digit;
        this.active = active;
        this.body = new int[digit];
    }

    public int getDigit() {
        return digit;
    }

    public int getMaxValue() {
        return (int)Math.pow(2, digit)-1;
    }

    public int getBodyToInt() {
        int resultValue=0;
        for(int digitIndex=0; digitIndex<this.digit; digitIndex++) {
            if(body[digitIndex]==1) {
                resultValue += (int) Math.pow(2, digitIndex);
            }
        }
        return resultValue;
    }

    public double getBodyToDouble() {
        return (double)getBodyToInt() / (Math.pow(2, digit)-1);
    }

    public int[] getBody() {
        return body;
    }

    public boolean getActive(){
        return active;
    }

    public void print() {
        System.out.print("[");
        for(int d=this.digit-1; d>=0; d--) {
            System.out.print(body[d]);
        }
        System.out.print("]");
    }

    public void setGene(int geneValue, Sfmt sfmt) {
        if(geneValue < 0) {
            this.body = valueToBody(nextNormalValue(sfmt));
        }
        else {
            this.body = valueToBody(geneValue);
        }
    }

    public void setGene(int[] geneValue) {
        this.body = geneValue;
    }

    protected int[] valueToBody(int geneValue) {
        int[] geneBody = new int[this.digit];

        // [0]に下の位の値, [digit-1]に上の位の値
        for(int digitIndex=0; digitIndex<this.digit; digitIndex++) {
            geneBody[digitIndex] = geneValue % 2 == 0 ? 0 : 1;
            geneValue /=2;
        }
        return geneBody;
    }

    // 標準正規分布の乱数生成
    protected int nextNormalValue(Sfmt sfmt) {
        int resultValue=-1;
        while(resultValue < 0 || resultValue > Math.pow(2, this.digit)-1) {
            resultValue = (int) (((sfmt.NextNormal() / IAgent.MAX_NEXT_NORMAL) + 0.5) * Math.pow(2,this.digit));
        }
        return resultValue;
    }
}
