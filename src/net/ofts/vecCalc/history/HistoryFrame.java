package net.ofts.vecCalc.history;

import net.ofts.vecCalc.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Queue;

public class HistoryFrame extends JFrame {
    public static HistoryFrame instance;
    public static JScrollPane pane;
    public static JButton clear;
    public static JPanel contentPanel = new JPanel();
    public static Queue<JPanel> addedItems = new LinkedList<>();

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
        //contentPanel.setSize(275, 114514);
        //contentPanel.setPreferredSize(new Dimension(275, 114514));
        clear = new JButton("Clear History");
        clear.addActionListener(a -> clearHistory());
        clear.setPreferredSize(new Dimension(100, 30));
        contentPanel.add(clear);
    }

    public static void addHistory(HistoryPanel panel){
        contentPanel.add(panel);
        addedItems.add(panel);

        if (addedItems.size() > 10){
            contentPanel.remove(addedItems.remove());
        }

        instance.revalidate();
        instance.repaint();
    }

    public static void clearHistory(){
        contentPanel.removeAll();
        contentPanel.add(clear);
        addedItems.clear();
        contentPanel.repaint();
        HistoryItem.histories.clear();
        HistoryItem.prepareHistoryWriteDown();
    }
}
