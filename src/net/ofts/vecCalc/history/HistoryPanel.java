package net.ofts.vecCalc.history;

import net.ofts.vecCalc.INumber;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HistoryPanel extends JPanel {
    public HistoryItem item;
    public JButton button;

    public HistoryPanel(HistoryItem item){
        this.item = item;
        this.setBorder(new TitledBorder(item.getOperationName()));
        this.setLayout(new FlowLayout());
        this.setMaximumSize(new Dimension(285, 105));
        this.setPreferredSize(new Dimension(285, 105));

        button = new JButton("open");
        button.addActionListener(a -> item.openHistory());
        add(button);

        for (INumber number : item.operands){
            add(new HistoryNumberPanel(number));
        }

        HistoryFrame.addHistory(this);
    }
}
