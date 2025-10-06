package net.ofts.vecCalc;

import javax.swing.*;
import java.awt.*;

public class ValueDisplayer {
    private static final JFrame valueFrame;
    private static final JLabel valueLabel;

    public static void displayValue(String val){
        valueLabel.setText(val);
        valueFrame.revalidate();
        valueFrame.setVisible(true);
    }

    static{
        valueLabel = new JLabel("");
        valueFrame = new JFrame("Value:");
        valueFrame.setSize(150, 100);
        valueFrame.setLayout(new FlowLayout());
        valueFrame.add(valueLabel);
        valueFrame.setAlwaysOnTop(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        valueFrame.setLocation(screenSize.width / 3, screenSize.height / 2);
        valueFrame.setVisible(false);
    }
}
