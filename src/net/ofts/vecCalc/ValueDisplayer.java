package net.ofts.vecCalc;

import javax.swing.*;
import java.awt.*;

public class ValueDisplayer {
    private static final JWindow valueFrame;
    private static final JLabel valueLabel;

    public static void displayValue(String val, Dimension size, Point location){
        valueLabel.setText(val);
        valueFrame.setSize(size);
        location.y -= size.height + 5;
        valueFrame.setLocation(location);
        valueFrame.setVisible(true);
        valueFrame.revalidate();
    }

    public static void closeDisplay(){
        valueFrame.setVisible(false);
    }

    static{
        valueLabel = new JLabel("");
        valueFrame = new JWindow();
        valueFrame.setLayout(new FlowLayout());
        valueFrame.add(valueLabel);
        valueFrame.setAlwaysOnTop(true);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //valueFrame.setLocation(screenSize.width / 3, screenSize.height / 2);
        valueFrame.setVisible(false);
    }
}
