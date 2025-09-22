package net.ofts.vecCalc.history;

import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.Main;

public class HistoryItem {
    public String operatorCode;
    public INumber[] operands;

    public void openHistory() {
        ICalculatorScreen calc = Main.openCalculatorPage(operatorCode, Main.menuItemMap.get(operatorCode));
        for (int i = 0; i < operands.length - 1; i++) {
            GenericPane pane = calc.getPaneByIndex(i);
            pane.setPanel(pane.getCurrent().cloneWithValue(operands[i]));
        }
    }
}