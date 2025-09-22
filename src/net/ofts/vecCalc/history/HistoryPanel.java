package net.ofts.vecCalc.history;

import javax.swing.*;

public class HistoryPanel extends JScrollPane {
    public HistoryItem item;

    public HistoryPanel(HistoryItem item){
        this.item = item;
    }
}
