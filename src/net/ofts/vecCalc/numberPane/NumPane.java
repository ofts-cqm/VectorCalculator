package net.ofts.vecCalc.numberPane;

import net.ofts.vecCalc.ICalculatorScreen;
import net.ofts.vecCalc.INumber;
import net.ofts.vecCalc.calc.Calculator;
import net.ofts.vecCalc.vector.Number;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NumPane extends BlankPane {
    public ICalculatorScreen parent;
    public double num;
    public String name;
    public boolean editable;
    public TitledBorder title;
    public JTextField text = new JTextField("0");

    public NumPane(String name, ICalculatorScreen parent, boolean editable){
        title = new TitledBorder(name);
        this.name = name;
        this.editable = editable;
        this.setBorder(title);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parent = parent;

        if (!editable){
            text.setEditable(false);
            text.setFocusable(false);
        }else {
            text.addFocusListener(focusManager);
            text.addActionListener(e -> handleChange((JTextField) e.getSource(), true));
            text.getDocument().addDocumentListener(new TextFieldMonitor(text));
        }

        this.add(Box.createRigidArea(new Dimension(100, 125)));
        this.add(text);
        this.add(Box.createRigidArea(new Dimension(100, 125)));
    }

    public NumPane setNum(double num){
        this.num = num;
        text.setText(formatter.format(num));
        repaint();
        return this;
    }

    public void resetNum(){
        num = 0;
        text.setText("0");
        repaint();
    }

    @Override
    public void resetPane(){
        resetNum();
    }

    public void handleChange(JTextField field, boolean recordResult){
        double val;
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
         num = val;
         parent.refreshResult(recordResult);
    }

    public AbstractNumberPane cloneWithValue(INumber number){
        assert number instanceof Number;
        double num = ((Number) number).num;
        return new NumPane(this.name, this.parent, this.editable).setNum(num);
    }

    @Override
    public INumber getNumber() {
        return new Number(num);
    }
}
