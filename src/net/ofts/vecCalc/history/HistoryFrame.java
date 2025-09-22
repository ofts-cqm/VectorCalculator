package net.ofts.vecCalc.history;

import javax.swing.*;
import java.awt.*;

public class HistoryFrame extends JFrame {
    public static HistoryFrame instance;
    public static JScrollPane pane;
    public static JPanel contentPanel = new JPanel();

    public HistoryFrame(){
        super("History");
        pane = new JScrollPane(contentPanel);
        pane.setSize(275, 350);
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);
        setSize(300, 400);
        setVisible(true);
        instance = this;
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setSize(275, 350);
    }
}
