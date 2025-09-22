package net.ofts.vecCalc.history;

import com.google.gson.Gson;
import net.ofts.vecCalc.GenericPane;
import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.numberPane.AbstractNumberPane;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class HistoryItem {
    public String operatorCode;
    public SuperNumber[] operands;

    public static ArrayList<HistoryItem> histories = new ArrayList<>();

    public HistoryItem(String operatorCode, INumber[] operands){
        this.operands = new SuperNumber[operands.length];
        for (int i = 0; i < operands.length; i++) {
            this.operands[i] = new SuperNumber(operands[i]);
        }
        this.operatorCode = operatorCode;
    }

    public static void recordHistory(String operatorCode, AbstractNumberPane... panes){
        ArrayList<INumber> numbers = new ArrayList<>();
        for (AbstractNumberPane pane : panes) {
            INumber temp = pane.getNumber();
            if (temp != null) numbers.add(temp.clone());
        }
        HistoryItem item = new HistoryItem(operatorCode, numbers.toArray(new INumber[]{}));
        histories.add(item);
        if (histories.size() > 10) histories.remove(0);
        new HistoryPanel(item);

        if(!Main.recordFile) return;

        try {
            String userHome = System.getProperty("user.home");
            File file = new File(Paths.get(userHome, "vecCalc", "history.json").toUri());
            Files.createDirectories(Paths.get(userHome, "vecCalc"));
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            save(writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOperationName(){
        return Main.calculatorCodeMap.get(operatorCode.substring(0, 4)).getOperationName(operatorCode);
    }

    public void openHistory() {
        ICalculatorScreen calc = Main.openCalculatorPage(operatorCode, Main.menuItemMap.get(operatorCode));
        for (int i = 0; i < operands.length - 1; i++) {
            GenericPane pane = calc.getPaneByIndex(i);
            pane.setPanel(pane.getCurrent().cloneWithValue(operands[i].degrade().clone()));
        }
        calc.refreshResult(false);
    }

    public static void save(FileWriter writer) throws IOException {
        writer.write(new Gson().toJson(histories));
    }

    public static void read(FileReader reader){
        HistoryItem[] tmp = new Gson().fromJson(reader, HistoryItem[].class);
        histories = new ArrayList<>(Arrays.asList(tmp));
        for (HistoryItem history : histories) {
            new HistoryPanel(history);
        }
    }
}