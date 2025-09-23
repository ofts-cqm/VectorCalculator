package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.INumber;

public class Number extends INumber {
    public double num;

    public Number(double num){
        super("numb");
        this.num = num;
    }

    @Override
    public INumber clone() {
        return new Number(num);
    }
}
