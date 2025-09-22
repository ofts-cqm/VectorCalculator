package net.ofts.vecCalc.history;

import com.google.gson.Gson;
import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.Main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryItem {
    public String operatorCode;
    public INumber[] operands;

    public static ArrayList<HistoryItem> histories;

    public HistoryItem(String operatorCode, INumber[] operands){
        this.operands = operands;
        this.operatorCode = operatorCode;
    }

    public static void recordHistory(String operatorCode, GenericPane... panes){

    }

    public String getOperationName(){
        return Main.calculatorCodeMap.get(operatorCode.substring(0, 4)).getOperationName(operatorCode);
    }

    public void openHistory() {
        ICalculatorScreen calc = Main.openCalculatorPage(operatorCode, Main.menuItemMap.get(operatorCode));
        for (int i = 0; i < operands.length - 1; i++) {
            GenericPane pane = calc.getPaneByIndex(i);
            pane.setPanel(pane.getCurrent().cloneWithValue(operands[i]));
        }
        calc.refreshResult();
    }

    public static void save(FileWriter writer) throws IOException {
        writer.write(new Gson().toJson(histories));
    }

    public static void read(FileReader reader){
        histories = new Gson().fromJson(reader, histories.getClass());
    }
}