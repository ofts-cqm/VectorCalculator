package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.calc.TextNumber;

public class TextPane extends AbstractNumberPane{
    public TextNumber txt;

    public TextPane(String txt){
        this.txt = new TextNumber(txt);
    }

    @Override
    public void resetPane() {
        txt.text = "";
    }

    @Override
    public AbstractNumberPane cloneWithValue(INumber number) {
        return new TextPane(((TextNumber)number).text);
    }

    @Override
    public INumber getNumber() {
        return txt;
    }
}
