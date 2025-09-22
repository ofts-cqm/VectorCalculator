package net.ofts.vecCalc.history;

import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.Main;

public class HistoryItem {
    public String operatorCode;
    public INumber[] operands;

    public void openHistory(){
        Main.openCalculatorPage(operatorCode, Main.menuItemMap.get(operatorCode));
    }
}
