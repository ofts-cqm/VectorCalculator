package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.calc.Calculator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Vec3Pane extends BlankPane {
    public Vec3Screen parent;
    public Vec3 vector = new Vec3(0, 0, 0);
    public TitledBorder title;
    public JTextField x1 = new JTextField("0"), x2 = new JTextField("0"), x3 = new JTextField("0");

    public Vec3Pane(String name, Vec3Screen parent, boolean editable){
        super();
        title = new TitledBorder(name);
        this.setBorder(title);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parent = parent;

        if (!editable){
            x1.setEditable(false);
            x1.setFocusable(false);
            x2.setEditable(false);
            x2.setFocusable(false);
            x3.setEditable(false);
            x3.setFocusable(false);
        }else {
            x1.addFocusListener(focusManager);
            x1.getDocument().addDocumentListener(new TextFieldMonitor(x1));
            x2.addFocusListener(focusManager);
            x2.getDocument().addDocumentListener(new TextFieldMonitor(x2));
            x3.addFocusListener(focusManager);
            x3.getDocument().addDocumentListener(new TextFieldMonitor(x3));
        }

        this.add(Box.createRigidArea(new Dimension(100, 25)));
        this.add(x1);
        this.add(Box.createRigidArea(new Dimension(100, 50)));
        this.add(x2);
        this.add(Box.createRigidArea(new Dimension(100, 50)));
        this.add(x3);
        this.add(Box.createRigidArea(new Dimension(100, 25)));
    }

    public void setVector(Vec3 vector){
        this.vector = vector;
        x1.setText(formatter.format(vector.x1));
        x2.setText(formatter.format(vector.x2));
        x3.setText(formatter.format(vector.x3));
        repaint();
    }

    public void resetVector(){
        vector = new Vec3(0, 0, 0);
        x1.setText("0");
        x2.setText("0");
        x3.setText("0");
        repaint();
    }

    public void handleChange(JTextField field){
        double val;
        int index;

        if (field.equals(x1)) index = 1;
        else if (field.equals(x2)) index = 2;
        else index = 3;

        try{
            val = Double.parseDouble(field.getText());
            if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
        }catch (Exception ex){
            Calculator.logLevel = Calculator.LogLevel.WARNING;
            val = Calculator.evaluate(field.getText(), false);
            if (Calculator.getLog().isBlank()){
                if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
            }else{
                field.setBackground(Color.red);
                repaint();
                return;
            }
        }

        vector.setByIndex(index, val);
        parent.refreshResult();
    }
}
