package net.ofts.vecCalc.calc;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.numberPane.AbstractNumberPane;
import net.ofts.vecCalc.numberPane.TextPane;

public class HolderPane extends GenericPane {
    public CalculatorScreen item;

    public HolderPane(CalculatorScreen item){
        this.item = item;
    }

    @Override
    public AbstractNumberPane getCurrent(){
        return new TextPane(item.textField.getText());
    }

    @Override
    public void setPanel(AbstractNumberPane pane){
        assert pane instanceof TextPane;
        item.textField.setText(((TextPane)pane).txt.text.split("=")[0]);
    }
}
