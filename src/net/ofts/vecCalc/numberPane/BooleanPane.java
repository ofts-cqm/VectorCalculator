package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.vector.Number;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BooleanPane extends AbstractNumberPane {
    public ICalculatorScreen parent;
    public boolean bool;
    public String name;
    public JLabel label;

    public BooleanPane(String name, ICalculatorScreen parent, boolean value){
        this.parent = parent;
        this.bool = value;
        this.name = name;
        this.setBorder(new TitledBorder(name));
        this.setPreferredSize(new Dimension(100, 300));
        this.setSize(100, 300);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label = new JLabel("Yes", SwingConstants.CENTER);
        label.setSize(100, 100);
        this.add(Box.createRigidArea(new Dimension(100, 125)));
        add(label);
        this.add(Box.createRigidArea(new Dimension(100, 125)));
        setValue(value);
    }

    public void setValue(boolean val){
        this.bool = val;
        label.setText(val ? "Yes" : "No");
        label.setForeground(val ? Color.GREEN : Color.red);
        repaint();
    }

    @Override
    public void resetPane() {
        setValue(false);
    }

    @Override
    public AbstractNumberPane cloneWithValue(INumber number) {
        assert number instanceof Number;
        return new BooleanPane(this.name, this.parent, ((Number)number).num != 0);
    }
}
