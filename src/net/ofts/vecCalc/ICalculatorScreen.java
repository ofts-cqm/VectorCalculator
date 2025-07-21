package net.ofts.vecCalc;

import javax.swing.*;

public abstract class ICalculatorScreen extends JPanel{
    public abstract void refreshResult();

    public abstract void onPageOpened(JFrame frame);
}
