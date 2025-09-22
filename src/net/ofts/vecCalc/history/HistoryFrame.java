package net.ofts.vecCalc.history;

import net.ofts.vecCalc.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Main.hist.setText("Open History");
            }
        };
        addWindowListener(exitListener);
        setVisible(false);
        instance = this;
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setSize(275, 350);
    }
}
