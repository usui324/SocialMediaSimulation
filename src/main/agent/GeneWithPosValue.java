package main.agent;

public class GeneWithPosValue extends Gene {

    public GeneWithPosValue(int digit, boolean active) {
        super(digit, active);
    }

    // digit=3の時に1/8 ... 8/8を示すよう
    @Override
    public int getBodyToInt() {
        int resultValue=1;
        for(int digitIndex=this.digit-1; digitIndex>=0; digitIndex--) {
            if(body[digitIndex]==1) {
                resultValue += (int) Math.pow(2, digitIndex);
            }
        }
        return resultValue;
    }

    @Override
    public double getBodyToDouble() {
        return (double)getBodyToInt() / Math.pow(2, digit);
    }

    @Override
    public int getMaxValue() {
        return (int)Math.pow(2, digit);
    }
}
