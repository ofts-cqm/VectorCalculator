package net.ofts.vecCalc;

import javax.swing.*;
import java.awt.*;

public class ValueDisplayer {
    private static final JWindow valueFrame;
    private static final JLabel valueLabel;

    public static void displayValue(String val, Point location){
        valueLabel.setText(val);
        valueFrame.revalidate();
        location.y -= 35;
        valueFrame.setLocation(location);
        valueFrame.setVisible(true);
    }

    public static void closeDisplay(){
        valueFrame.setVisible(false);
    }

    static{
        valueLabel = new JLabel("");
        valueFrame = new JWindow();//JFrame("Value:");
        valueFrame.setSize(150, 30);
        valueFrame.setLayout(new FlowLayout());
        valueFrame.add(valueLabel);
        valueFrame.setAlwaysOnTop(true);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //valueFrame.setLocation(screenSize.width / 3, screenSize.height / 2);
        valueFrame.setVisible(false);
    }
}
