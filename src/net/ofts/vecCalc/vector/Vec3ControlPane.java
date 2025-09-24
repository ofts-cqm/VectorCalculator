package net.ofts.vecCalc.vector;

import net.ofts.vecCalc.numberPane.AbstractNumberPane;
import net.ofts.vecCalc.Main;
import net.ofts.vecCalc.numberPane.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Vec3ControlPane extends JPanel {
    public JButton currentOperation;
    public JButton move;
    public static final String[] operation = new String[]{"+", "-", "Scale", "Dot", "Cross", "Normalize", "Length", "Proj", "Perp"};
    @SuppressWarnings("unchecked")
    public static final Class<? extends AbstractNumberPane>[] operandForm = new Class[]{Vec3Pane.class, Vec3Pane.class, NumPane.class, Vec3Pane.class, Vec3Pane.class, BlankPane.class, BlankPane.class, Vec3Pane.class, Vec3Pane.class};
    @SuppressWarnings("unchecked")
    public static final Class<? extends AbstractNumberPane>[] resultForm = new Class[]{Vec3Pane.class, Vec3Pane.class, Vec3Pane.class, NumPane.class, Vec3Pane.class, Vec3Pane.class, NumPane.class, Vec3Pane.class, Vec3Pane.class};
    public int index = 0;
    public Vec3Screen parent;

    public Vec3ControlPane(Vec3Screen root){
        this.parent = root;
        setBorder(new TitledBorder("Operation"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(100, 120)));
        currentOperation = new JButton("+");
        currentOperation.setActionCommand("next");
        currentOperation.addActionListener(new ClickHandler());
        currentOperation.setFocusable(false);
        currentOperation.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentOperation);
        add(Box.createRigidArea(new Dimension(100, 60)));
        move = new JButton("<html>Move<br>Result</html>");
        move.setActionCommand("move");
        move.addActionListener(new ClickHandler());
        move.setFocusable(false);
        move.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(move);
        add(Box.createRigidArea(new Dimension(100, 30)));
    }

    public void move(){
        if (parent.operandC.getCurrent() instanceof NumPane number){
            double num = number.num;
            parent.operandA.getPanel(Vec3Pane.class).setVector(new Vec3(num, 0, 0));
        }
        else{
            parent.operandA.getPanel(Vec3Pane.class).setVector(parent.operandC.getPanel(Vec3Pane.class).vector);
        }
        parent.refreshResult(false);
    }

    public void setOperator(int operator){
        index = operator;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.operandC.displayPanel(resultForm[index]);
        parent.refreshResult(false);
    }

    public void nextOperator(){
        index++;
        if (index == operation.length) index = 0;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.operandC.displayPanel(resultForm[index]);
        parent.refreshResult(false);
        Main.updateSelectedMenuItem(Vec3Screen.items[index]);
    }

    @Deprecated
    public void previousOperator(){
        index--;
        if (index == -1) index = operation.length - 1;
        currentOperation.setText(operation[index]);
        parent.operandB.displayPanel(operandForm[index]);
        parent.operandC.displayPanel(resultForm[index]);
        parent.refreshResult(false);
        Main.updateSelectedMenuItem(Vec3Screen.items[index]);
    }

    public class ClickHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("next")){
                nextOperator();
            } else if(e.getActionCommand().equals("move")){
                move();
            }
        }
    }
}
