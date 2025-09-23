package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.INumber;

public class TextNumber extends INumber {
    public String text;

    public TextNumber(String text){
        super("text");
        this.text = text;
    }

    @Override
    public INumber clone() {
        return new TextNumber(text);
    }
}
