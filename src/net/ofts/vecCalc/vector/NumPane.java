package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.ICalculatorScreen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class NumPane extends BlankPane {
    public ICalculatorScreen parent;
    public double num;
    public TitledBorder title;
    public JTextField text = new JTextField("0");

    public NumPane(String name, ICalculatorScreen parent, boolean editable){
        title = new TitledBorder(name);
        this.setBorder(title);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.parent = parent;

        if (!editable){
            text.setEditable(false);
            text.setFocusable(false);
        }else {
            text.addFocusListener(focusManager);
            text.getDocument().addDocumentListener(new TextFieldMonitor(text));
        }

        this.add(Box.createRigidArea(new Dimension(100, 125)));
        this.add(text);
        this.add(Box.createRigidArea(new Dimension(100, 125)));
    }

    public void setNum(double num){
        this.num = num;
        text.setText(formatter.format(num));
        repaint();
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

    public void handleChange(JTextField field){
        double val;
         try{
             val = Double.parseDouble(field.getText());
             if (field.getBackground().equals(Color.red)) field.setBackground(Color.white);
         }catch (Exception ex){
             field.setBackground(Color.red);
             repaint();
             return;
         }
         num = val;
         parent.refreshResult();
    }
}
