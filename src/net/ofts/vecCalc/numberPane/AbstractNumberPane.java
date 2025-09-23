package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.INumber;

import javax.swing.*;

public abstract class AbstractNumberPane extends JPanel {
    public abstract void resetPane();

    public abstract AbstractNumberPane cloneWithValue(INumber number);

    public abstract INumber getNumber();
}
